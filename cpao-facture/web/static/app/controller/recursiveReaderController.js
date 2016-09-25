/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function RecursiveReaderController($root, $parent, datatype, pack, resource, field) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;
    self.parent = $parent;

    self.datatype = datatype;
    self.pack = ko.observable(pack);
    self.resource = resource;
    self.field = field;

    self.resources = self.pack().resources;
    self.fields = self.pack().fields;

    self.load = function () {

        var callback = function (array) {
            self.fields.forEach(function (field) {
                if (field.type === 'datatype') {
                    array.forEach(function (resource) {
                        resource[field.id] = new RecursiveReaderController(self.root, self, field.pointerToDatatype(), field.pointerToDatatype().getDatapack(), resource, field);
                    });
                }
            });
            
            self.pack().resources(array);
        };

        switch (self.field.reloadBy) {
            case "people":
                self.field.pointerToDatatype().reload.people(resource.id(), self.pack(), function (array) {
                    callback(array);
                });
                break;
            case "home":
                console.log(self.field.pointerToDatatype());
                console.log(resource.id());
                self.field.pointerToDatatype().reload.home(resource.id(), self.pack(), function (array) {
                    console.log(array);
                    callback(array);
                });
                break;
        }
    };
    self.load();

}