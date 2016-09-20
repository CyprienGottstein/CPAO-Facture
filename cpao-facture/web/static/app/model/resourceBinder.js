/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function ResourceBinder($root, $parent, data, datatype, subDatatype) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;
    self.parent = $parent;
    self.datatype = datatype;
    self.subDatatype = subDatatype;
    self.targetDatatype = subDatatype.id;

    self.id = ko.observable(data);
    self.label = ko.observable("NOT-FOUND");

    self.rebind = function () {

        var datatype = null;
        self.root.model.genericResource.datatypes().forEach(function (dt) {
            if (dt.id === self.targetDatatype) {
                datatype = dt;
            }
        });

        datatype.resources().forEach(function (resource) {
            if (resource.id() === self.id()) {
                self.label(resource[self.subDatatype.field]());
            }
        });
    };

}
;