/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function ResourceController(root, seasonController) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = root;
    self.season = seasonController;

    self.datatypes = ko.observableArray(root.model.genericResource.datatypes);
    self.datatypes().forEach(function (datatype) {
        datatype.updateBySeason = function () {
            datatype.reload();
        };

        datatype.updateByMode = function () {
            datatype.reload();
        };

        datatype.loadBySeason = function () {

            var callback = function (data) {
                var array = datatype.resources();
                array = [];
                data.forEach(function (resource) {
                    array.push(new datatype.model(root, self, resource, datatype.id));
                });
                datatype.resources(array);
            };

            root.ajax.genericCrud.ajax(
                    datatype.id,
                    root.ajax.static.crudOperation.loadBySeason.id,
                    {season: _.clone(self.season.yearpicker.current())},
            callback
                    );
        };

        datatype.loadAll = function () {

            var callback = function (data) {
                var array = datatype.resources();
                array = [];
                data.forEach(function (resource) {
                    array.push(new datatype.model(root, self, resource, datatype.id));
                });
                datatype.resources(array);
            };

            root.ajax.genericCrud.ajax(
                    datatype.id,
                    root.ajax.static.crudOperation.loadAll.id,
                    null,
                    callback
                    );

        };

        datatype.reload = function () {
            console.log("Triggered");
            if (self.season.currentMode() === 1) {
                datatype.loadBySeason();
            } else {
                datatype.loadAll();
            }
        };

        self.root.controller.season.subscribeToSeason(datatype.updateBySeason);
        self.root.controller.season.subscribeToMode(datatype.updateByMode);

        datatype.toggleModal = function (datatype) {
            self.modal.toggle(datatype.id);
        };


    });

    self.modal = new InputResourceModal(root, self, self.season);
    self.removeModal = new RemoveResourceModal(root, self);

    self.toggleModal = function () {
        self.modal.toggle();
    };

    function InputResourceModal($root, $parent, seasonController) {

        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;
        self.season = seasonController;

        self.editing = ko.observable(false);
        self.editingId = ko.observable(-1);
        self.editingResource = ko.observable({});

        self.active = ko.observable(false);
        self.failure = ko.observable(false);

        self.show = ko.observable(false);
        self.currentDatatype = ko.observable();
        self.resourceValidator = ko.validatedObservable({});

        self.currentResourceFields = ko.observableArray();

        $('#inputResourceModal').on('hidden.bs.modal', function () {
            self.show(false);
            self.reset(self.currentDatatype());
        });

        self.toggle = function (datatypeId) {
            self.show(!self.show());
            console.log(datatypeId);
            if (typeof datatypeId !== "undefined"){
                self.currentDatatype(datatypeId);
            }
            
        };

        self.yearpicker = new YearPicker(self.root, self.root.controller.season, _.clone(self.season.yearpicker.current()));

        self.prepareNew = function (datatypeId) {

            var datatype = null;
            self.parent.datatypes().forEach(function (dt) {
                if (dt.id === datatypeId) {
                    datatype = dt;
                }
            });

            var array = [];

            datatype.fields.forEach(function (field) {
                switch (field.type) {
                    case "id":
                        break;
                    case "season":
                        break;
                    case "string":
                        var fieldObs = {};
                        fieldObs.metadata = field;
                        fieldObs.data = ko.observable("")
                                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                                .extend({minLength: {params: 5, message: "La description de l'activité doit faire au moins 5 caractères."}});
                        fieldObs.data.isModified(false);
                        array.push(fieldObs);
                        break;
                    case "float":
                        var fieldObs = {};
                        fieldObs.metadata = field;
                        fieldObs.data = ko.observable("")
                                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                                .extend({number: {params: true, message: "Ce n'est pas un nombre"}})
                                .extend({min: {params: 0, message: "Le coût ne peut pas être négatif"}});
                        fieldObs.data.isModified(false);
                        array.push(fieldObs);
                        break;
                }

            });

            self.currentResourceFields(array);
        };

        self.edit = function (resource, datatypeId) {

            self.prepareNew(datatypeId);

            self.editing(true);
            self.editingId(resource.id());

            var datatype = null;
            self.parent.datatypes().forEach(function (dt) {
                if (dt.id === datatypeId) {
                    datatype = dt;
                }
            });

            datatype.inputFields.forEach(function (field) {
                self.currentResourceFields().forEach(function (fieldObs) {
                    if (field.id === fieldObs.metadata.id) {
                        fieldObs.data(resource[field.id]());
                    }
                });
            });

            var group = {};
            var n = 0;

            datatype.resources().forEach(function (fieldObs) {
                var label = "v" + n;
                group[label] = fieldObs.data;
                n += 1;
            });

            self.resourceValidator(group);

            self.yearpicker.current(resource.season());
        };

        self.reset = function (datatypeId) {
            self.yearpicker.current(self.season.yearpicker.current());

            self.prepareNew(datatypeId);

            self.active(false);
            self.failure(false);
            self.editing(false);
            self.editingId(-1);
        };

        self.save = function () {

            var resource = {};
            var datatypeId = self.currentDatatype();
//            console.log(datatypeId);
            var datatype = null;
            self.parent.datatypes().forEach(function (dt) {
                if (dt.id === datatypeId) {
                    datatype = dt;
                }
            });

            var array = self.currentResourceFields();
            array.forEach(function (fieldObs) {
                if (fieldObs.metadata.type === "float") {
                    resource[fieldObs.metadata.id] = fieldObs.data() + "";
                } else {
                    resource[fieldObs.metadata.id] = fieldObs.data();
                }
            });

            resource.season = _.clone(self.yearpicker.current());

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

                root.ajax.genericCrud.ajax(
                        datatype.id,
                        root.ajax.static.crudOperation.update.id,
                        params,
                        callback
                        );
            } else {
                var params = {};
                params[datatype.rest] = resource;

                root.ajax.genericCrud.ajax(
                        datatype.id,
                        root.ajax.static.crudOperation.save.id,
                        params,
                        callback
                        );
            }
            self.active(true);

        };

    }
    ;

    function RemoveResourceModal($root, $parent) {

        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;

        self.show = ko.observable(false);

        $('#removeResourceModal').on('hidden.bs.modal', function () {
            self.show(false);
        });

        self.active = ko.observable(false);

        self.static = {};
        self.static.msg = {};
        self.static.msg.announce = {};
        self.static.msg.announce.begin = "Vous êtes sur le point de supprimer l'activité : <label>";
        self.static.msg.announce.end = "</label>";
        self.static.msg.failure = {};
        self.static.msg.failure.begin = "La suppression de l'activité : <label>";
        self.static.msg.failure.end = "</label> a echoué.";


        self.resource = {};
        self.label = ko.observable("");

        self.toggle = function () {
            self.show(!self.show());
        };

        self.focus = function (resource) {
            self.resource = resource;
            self.label(self.static.msg.announce.begin + self.resource.label() + self.static.msg.announce.end);
        };

        self.remove = function () {
            var callback = function (data) {

                if (typeof data !== "undefined") {
                    if (data.result === 1) {
                        var array = self.parent.resources();
                        _.remove(array, function (resource) {
                            if (resource.id() === self.resource.id()) {
                                return true;
                            }
                            return false;
                        });
                        self.parent.resources(array);
                        self.toggle();
                    } else {
                        self.label(self.static.msg.failure.begin + self.resource.label() + self.static.msg.failure.end);
                    }
                } else {
                    self.label(self.static.msg.failure.begin + self.resource.label() + self.static.msg.failure.end);
                }


                self.active(false);
            };

            var datatype = self.parent.currentDatatype();
            var params = {id: self.resource.id()};

            root.ajax.genericCrud.ajax(
                    datatype.id,
                    root.ajax.static.crudOperation.remove.id,
                    params,
                    callback
                    );
            self.active(true);
        };

    }
    ;

}
;