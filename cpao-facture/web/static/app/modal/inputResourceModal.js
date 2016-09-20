function InputResourceModal($root, datatypes, seasonController, data) {

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

    self.inputResourceController = new InputResourceController(self.root, self);


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
    }

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
        }

    };

    self.isValid = ko.computed(function () {
        return self.inputResourceController.resourceValidator.isValid();
    });

    self.save = function () {

        var resource = {};
        var datatype = self.inputResourceController.datatype();

        var array = self.inputResourceController.resourceFields();
        array.forEach(function (fieldObs) {
            switch (fieldObs.metadata.type) {
                case "id":
                    break;
                case "datatype":
                    break;
                case "float":
                    resource[fieldObs.metadata.id] = fieldObs.data() + "";
                    break;
                case "date":
                    var timestamp = new Date(fieldObs.data()).getTime();
                    resource[fieldObs.metadata.id] = timestamp;
                    break;
                case "season":
                    resource[fieldObs.metadata.id] = fieldObs.data.current();
                    break;
                case "select":
                    console.log(fieldObs.data());
                    console.log(fieldObs.metadata.id);
                    resource[fieldObs.metadata.id] = fieldObs.data()[0].id();
                    break;
                default:
                    console.log(fieldObs.metadata.id);
                    resource[fieldObs.metadata.id] = fieldObs.data();
                    break;
            }
        });

        var callback = function (data) {

            if (typeof data !== "undefined") {
                if (data.result === 1) {
                    datatype.reload();
                    self.toggle();
                } else {
                    self.failure(true);
                }
            } else {
                self.failure(true);
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