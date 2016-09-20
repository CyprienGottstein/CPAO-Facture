/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function InputResourceController($root, $parent) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;
    self.parent = $parent;

    self.title = ko.observable("No title");

    self.datatype = ko.observable();
    self.pointers = ko.observableArray();

    self.resourceButtons = ko.observableArray();
    self.resourceFields = ko.observableArray();
    self.resourceValidator = ko.validatedObservable();

    self.prepareNew = function () {

        var datatype = self.datatype();

        var array = [];

        datatype.fields.forEach(function (field) {
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
                    self.root.controller.resource.datatypes().forEach(function (resourceDatatype) {
                        if (resourceDatatype.id === fieldObs.metadata.subtype.id) {
                            fieldObs.options = resourceDatatype.resources;
                        }
                    });

                    fieldObs.data = ko.observable([fieldObs.options()[0]])
                            .extend({required: {params: true, message: "Ce champ est obligatoire"}});
                    fieldObs.data.isModified(false);
                    break;
                case "datatype":
                    fieldObs.data = new InputResourceController(self.root, self.root.modalSecondaryDatatype.inputModal);
                    fieldObs.inputModal = self.root.modalSecondaryDatatype.inputModal;
                    fieldObs.removeModal = self.root.modalSecondaryDatatype.removeModal;
                    fieldObs.metadata.pointerToDatatype.enhance(fieldObs.inputModal, fieldObs.removeModal);

//                    fieldObs.data.datatype(fieldObs.metadata.pointerToDatatype);
//                    fieldObs.metadata.pointerToDatatype.inputResourceController(fieldObs.data);
                    break;
            }

            array.push(fieldObs);

        });

        self.resourceFields(array);

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

        datatype.fields.forEach(function (field) {
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
                            fieldObs.metadata.pointerToDatatype.setId(resource.id());
                            fieldObs.metadata.pointerToDatatype.reload();
                            break;
                        default:
                            fieldObs.data(resource[field.id]());
                            break;
                    }
                }
            });

            self.resourceFields(array);
        });

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

    self.reset = function () {
        self.prepareNew();
    };

}