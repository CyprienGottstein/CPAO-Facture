/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function MultiSelectController($root, $parent, data) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;
    self.parent = $parent;

    self.data = data;

    self.currentResourceFields = ko.observableArray();
    var array = [];

    data.subtype.forEach(function (subtype) {
        var fieldObs = {};
        fieldObs.metadata = subtype;

        switch (subtype.type) {
            case "id":
                break;
            case "season":
                fieldObs.data = new YearPicker(self.root, self.root.controller.season, _.clone(self.root.controller.season.yearpicker.current()));
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
                fieldObs.data = ko.observable(fieldObs.options()[0])
                        .extend({required: {params: true, message: "Ce champ est obligatoire"}});
                fieldObs.data.isModified(false);
                break;
        }

        array.push(fieldObs);
    });

    self.currentResourceFields(array);

    self.edit = function (resource) {

        var array = self.currentResourceFields();

        self.data.subtype.forEach(function (field) {
            if (field.input) {
                array.forEach(function (fieldObs) {
                    if (field.id === fieldObs.metadata.id) {
                        switch (fieldObs.metadata.type) {
                            case "season":
                                fieldObs.data.current(resource[field.id]());
                                break;
                            case "select":
                                self.root.controller.resource.datatypes().forEach(function (resourceDatatype) {
                                    if (resourceDatatype.id === fieldObs.metadata.subtype.id) {
                                        fieldObs.options = resourceDatatype.resources;
                                    }
                                });
                                fieldObs.data = ko.observable(fieldObs.options()[0])
                                        .extend({required: {params: true, message: "Ce champ est obligatoire"}});
                                fieldObs.data.isModified(false);
                                break;
                            default:
                                fieldObs.data(resource[field.id]());
                                break;
                        }
                    }
                });
            }
        });

        self.currentResourceFields(array);
    };

}