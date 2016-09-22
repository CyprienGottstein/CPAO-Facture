function InputResourceModal($root, datatypes, seasonController, data, primary) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;

    self.datatypes = datatypes;
    self.season = seasonController;

    self.idModal = data.idModal;
    self.large = data.large;

    self.editing = ko.observable(false);
    self.editingId = ko.observable(-1);
    self.editingResource = ko.observable({});

    self.active = ko.observable(false);
    self.failure = ko.observable(false);
    self.show = ko.observable(false);

    self.inputResourceController = new InputResourceController(self.root, primary);

    self.setInputController = function (controller) {
        self.inputResourceController = controller;
    };

    self.subscriber = ko.observable();
    self.subscribe = function (controller) {
        self.subscriber(controller);
    };

    self.close = function () {
        self.show(false);
        self.inputResourceController.reset();
        self.active(false);
        self.failure(false);
        self.editing(false);
        self.editingId(-1);
    };

    self.triggerCloseListener = function () {
        $('#' + self.idModal).on('hidden.bs.modal', function () {
            self.close();
        });
    };

    self.edit = function (resource, datatype) {
        self.editing(true);
        self.editingId(resource.id());
        self.inputResourceController.edit(resource, datatype);
        self.show(!self.show());
    };

    self.toggle = function (datatype) {
        self.show(!self.show());
        if (typeof datatype !== "undefined") {
            self.inputResourceController.title(datatype.modalLabel);
            self.inputResourceController.datatype(datatype);
        } else {
            console.log("DATATYPE UNDEFINED");
        }

    };

    self.isValid = ko.computed(function () {
        if (!self.inputResourceController) {
            return false;
        }
        return self.inputResourceController.resourceValidator.isValid();
    });

    self.save = function () {

        var resource = {};
        var datatype = self.inputResourceController.datatype();

        var array = self.inputResourceController.resourceFields();


        var composite = null;
        var compositeId = null;

        array.forEach(function (fieldObs) {
            if (fieldObs.metadata.type === "composite") {
                compositeId = fieldObs.metadata.id;
                composite = {};
            }
        });


        array.forEach(function (fieldObs) {
            switch (fieldObs.metadata.type) {
                case "id":
                    break;
                case "datatype":
                    break;
                case "composite":
                    break;
                case "float":
                    resource[fieldObs.metadata.id] = fieldObs.data() + "";
                    break;
                case "date":
                    var timestamp = new Date(fieldObs.data()).getTime();
                    if (fieldObs.metadata.composite) {
                        composite[fieldObs.metadata.id] = timestamp;
                    } else {
                        resource[fieldObs.metadata.id] = timestamp;
                    }
                    break;
                case "season":
                    resource[fieldObs.metadata.id] = fieldObs.data.current();
                    break;
                case "select":
                    resource[fieldObs.metadata.id] = fieldObs.data()[0].id();
                    break;
                case "short-string":
                    if (fieldObs.metadata.composite) {
                        composite[fieldObs.metadata.id] = fieldObs.data();
                    } else {
                        resource[fieldObs.metadata.id] = fieldObs.data();
                    }
                    break;
                default:
                    resource[fieldObs.metadata.id] = fieldObs.data();
                    break;
            }
        });

        if (composite) {
            resource[compositeId] = JSON.stringify(composite);
        }

        if (!self.inputResourceController.primary) {
            var field = self.inputResourceController.forceField();
            var id = self.inputResourceController.forceId();
            var pack = self.inputResourceController.forcePack();
            resource[field] = id;
        }

        var callback = function (data) {

            if (typeof data !== "undefined") {
                if (data.result === 1) {
                    if (!self.inputResourceController.primary) {
                        console.log(pack);
                        if (datatype.loadableByPeople) {
                            datatype.reload.people(id, pack, function (array) {
                                pack.resources(array);
                            });
                        }
                        if (datatype.loadableByHome) {
                            datatype.reload.home(id, pack, function (array) {
                                pack.resources(array);
                            });
                        }
                    }
                    self.toggle();
                } else {
                    self.failure(true);
                }
            } else {
                self.failure(true);
            }
            
            if(self.subscriber()){
                self.subscriber().reload();
            }

            self.active(false);
        };

        if (self.editing()) {
            var params = {};
            params.id = self.editingId();
            params[datatype.rest] = resource;

            self.root.ajax.genericCrud.ajax(
                    datatype.rest,
                    self.root.ajax.static.crudOperation.update.id,
                    params,
                    callback
                    );
        } else {
            var params = {};
            params[datatype.rest] = resource;

            self.root.ajax.genericCrud.ajax(
                    datatype.rest,
                    self.root.ajax.static.crudOperation.save.id,
                    params,
                    callback
                    );
        }
        self.active(true);

    };

}
;