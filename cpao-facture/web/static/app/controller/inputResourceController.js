/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function InputResourceController($root, primary) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;
    self.primary = primary;

    self.title = ko.observable("No title");

    self.datatype = ko.observable();
    self.pointers = ko.observableArray();

    self.resourceButtons = ko.observableArray();
    self.resourceFields = ko.observableArray();
    self.resourceValidator = ko.validatedObservable();

    self.forceField = ko.observable();
    self.forceId = ko.observable();
    self.forcePack = ko.observable();

    self.setForceField = function (field) {
        self.forceField(field);
    };
    self.setForceId = function (id) {
        self.forceId(id);
    };
    self.setForcePack = function (pack) {
        self.forcePack(pack);
    };

    self.prepareNew = function () {

        var datatype = self.datatype();
        var array = [];
        self.prepareNewField(datatype, datatype.fields, array);
        self.resourceFields(array);

    };

    self.prepareNewField = function (datatype, fields, array) {

//        var array = [];

        fields.forEach(function (field) {
            var fieldObs = {};
            fieldObs.metadata = field;

            switch (field.type) {
                case "id":
                    break;
                case "season":
                    fieldObs.data = new YearPicker(self.root, self.root.controller.season, _.clone(self.root.controller.season.yearpicker.current()));
                    break;
                case "short-string":
                    fieldObs.data = ko.observable("")
                            .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                            .extend({minLength: {params: 2, message: "Ce champ doit faire au moins 2 caractères."}});
                    fieldObs.data.isModified(false);
                    break;
                case "long-string":
                    fieldObs.data = ko.observable("")
                            .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                            .extend({minLength: {params: 2, message: "Ce champ doit faire au moins 2 caractères."}});
                    fieldObs.data.isModified(false);
                    break;
                case "float":
                    fieldObs.data = ko.observable("")
                            .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                            .extend({number: {params: true, message: "Ce n'est pas un nombre"}})
                            .extend({min: {params: 0, message: "Le coût ne peut pas être négatif"}});
                    fieldObs.data.isModified(false);
                    break;
                case "date":
                    fieldObs.data = ko.observable("")
                            .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                            .extend({date: {params: true, message: "Ce n'est pas une date"}});
                    fieldObs.data.isModified(false);
                    break;
                case "boolean":
                    fieldObs.data = ko.observable(false);
                    break;
                case "select":
                    self.root.model.datatypeRoot.datatypes().forEach(function (datatype) {
                        if (datatype().id === fieldObs.metadata.subtype.id) {
                            fieldObs.options = datatype().pack().resources;
                        }
                    });

                    fieldObs.data = ko.observable([fieldObs.options()[0]])
                            .extend({required: {params: true, message: "Ce champ est obligatoire"}});
                    fieldObs.data.isModified(false);
                    break;
                case "datatype":
                    fieldObs.pack = ko.observable(fieldObs.metadata.pointerToDatatype().getDatapack());
                    fieldObs.inputModal = self.root.modalSecondaryDatatype.inputModal;
                    fieldObs.removeModal = self.root.modalSecondaryDatatype.removeModal;
                    break;
                case "recursive":
                    fieldObs.recursive = ko.observable({
                        resources: []
                    });
                    break;
                case "composite":
                    // add all observables
                    self.prepareNewField(datatype, field.compositeFields, array);

                    var phase1 = [];
                    var phase2 = [];

                    array.forEach(function (resourceField2) {
                        field.compositeFields.forEach(function (compositeField) {
                            if (compositeField.id === resourceField2.metadata.id) {
                                resourceField2.metadata.input = false;
                            }
                        });
                        phase1.push(resourceField2);
                    });

                    phase1.forEach(function (resourceField2) {
                        field.compositeFieldsList[0].forEach(function (compositeField) {
                            if (compositeField.id === resourceField2.metadata.id) {
                                resourceField2.metadata.input = true;
                            }
                        });
                        phase2.push(resourceField2);
                    });

                    self.resourceFields([]);
                    self.resourceFields(phase2);

                    array.forEach(function (resourceField) {
                        if (resourceField.metadata.id === field.dependency) {
                            // show inputs according to currently type selected (dependency)
                            resourceField.data.subscribe(function (resourceBinder) {

                                var resourceFieldsArray = self.resourceFields();
                                var phase1 = [];
                                var phase2 = [];

                                resourceFieldsArray.forEach(function (resourceField2) {
                                    field.compositeFields.forEach(function (compositeField) {
                                        if (compositeField.id === resourceField2.metadata.id) {
                                            resourceField2.metadata.input = false;
                                        }
                                    });
                                    phase1.push(resourceField2);
                                });

                                phase1.forEach(function (resourceField2) {
                                    field.compositeFieldsList[resourceBinder[0].id()].forEach(function (compositeField) {
                                        if (compositeField.id === resourceField2.metadata.id) {
                                            resourceField2.metadata.input = true;
                                        }
                                    });
                                    phase2.push(resourceField2);
                                });

                                self.resourceFields([]);
                                self.resourceFields(phase2);

                            });
                        }
                    });
                    break;
                case "template":
                    fieldObs.template = ko.observable(new HomeBillController(self.root, fieldObs.metadata.dependencies));
                    break;
            }

            array.push(fieldObs);

        });

        return array;

    };

    self.purge = function () {
        self.pointers().forEach(function (pointer) {
            pointer([]);
        });
    };

    self.datatype.subscribe(function () {
        self.prepareNew();
        self.title(self.datatype().modalLabel);
        self.purge();
    });

    self.edit = function (resource, datatype) {

        self.datatype(datatype);

        self.editField(datatype.fields, resource);

        var group = {};
        var n = 0;

        self.resourceFields().forEach(function (fieldObs) {
            if (fieldObs.metadata.type !== "season") {
                var label = "v" + n;
                group[label] = fieldObs.data;
                n += 1;
            }
        });

//        self.resourceValidator(group);
    };

    self.editField = function (fields, resource) {

        fields.forEach(function (field) {
            var array = self.resourceFields();
            array.forEach(function (fieldObs) {
                if (field.id === fieldObs.metadata.id) {
                    switch (fieldObs.metadata.type) {
                        case "id":
                            break
                        case "season":
                            fieldObs.data.current(resource[field.id]());
                            break;
                        case "select":
                            fieldObs.options().forEach(function (option) {
                                if (option.id() === resource[field.id]().id()) {
                                    fieldObs.data([option]);
                                }
                            });
                            break;
                        case "datatype":
                            fieldObs.pack(fieldObs.metadata.pointerToDatatype().getDatapack());
                            fieldObs.inputModal = self.root.modalSecondaryDatatype.inputModal;
                            fieldObs.removeModal = self.root.modalSecondaryDatatype.removeModal;

                            fieldObs.inputModal.inputResourceController.setForceField(fieldObs.metadata.restrictBy);
                            fieldObs.inputModal.inputResourceController.setForceId(resource.id());
                            fieldObs.inputModal.inputResourceController.setForcePack(fieldObs.pack());

                            switch (fieldObs.metadata.reloadBy) {
                                case "people":
                                    fieldObs.metadata.pointerToDatatype().reload.people(resource.id(), fieldObs.pack(), function (array) {
                                        fieldObs.pack().resources(array);
                                    });
                                    break;
                                case "home":
                                    fieldObs.metadata.pointerToDatatype().reload.home(resource.id(), fieldObs.pack(), function (array) {
                                        fieldObs.pack().resources(array);
                                    });
                                    break;
                            }

                            break;
                        case "recursive":
                            fieldObs.recursive(new RecursiveReaderController(self.root, self, fieldObs.metadata.pointerToDatatype(), fieldObs.metadata.pointerToDatatype().getDatapack(), resource, field));
                            break;
                        case "composite":
                            var id = null;
                            array.forEach(function (resourceField) {
                                if (resourceField.metadata.id === field.dependency) {
                                    id = resourceField.data()[0].id()
                                }
                            });

                            self.editField(field.compositeFieldsList[id], resource);
                            break;
                        case "template":
//                            fieldObs.template = ko.observable(new HomeBillController(self.root, fieldObs.metadata.dependencies));
                            break;
                        default:
                            fieldObs.data(resource[field.id]());
                            break;
                    }
                }
            });

            self.resourceFields(array);
        });
    };

    self.reset = function () {
        self.prepareNew();
    };

}