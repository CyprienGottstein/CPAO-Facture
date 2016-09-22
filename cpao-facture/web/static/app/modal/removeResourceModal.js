function RemoveResourceModal($root, datatypes, idModal) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;
    
    self.idModal = idModal;
    
    self.datatypes = datatypes;
    
    self.resource = {};
    self.datatype = null;
    self.pack = null;
    
    self.label = ko.observable("");

    self.show = ko.observable(false);
    self.active = ko.observable(false);
    
    self.toggle = function () {
        self.show(!self.show());
    };

    self.triggerCloseListener = function () {
        $('#' + self.idModal).on('hidden.bs.modal', function () {
            self.show(false);
        });
    };

    self.focus = function (resource, datatype, pack) {
        self.datatype = datatype;
        self.pack = pack;
        self.resource = resource;
        self.label(self.resource.getLabel());
    };

    self.remove = function () {
        
//        var datatype = self.inputResourceController.datatype();
        
        var callback = function (data) {

            if (typeof data !== "undefined") {
                if (data.result === 1) {
                    var array = self.pack.resources();
                    _.remove(array, function (resource) {
                        if (resource.id() === self.resource.id()) {
                            return true;
                        }
                        return false;
                    });
                    self.pack.resources(array);
                    self.toggle();
                } else {
                    self.label(self.resource.getLabel());
                }
            } else {
//                self.label(self.static.msg.failure.begin + self.resource.getLabel() + self.static.msg.failure.end);
            }


            self.active(false);
        };

        var params = {id: self.resource.id()};

        self.root.ajax.genericCrud.ajax(
                self.datatype.rest,
                self.root.ajax.static.crudOperation.remove.id,
                params,
                callback
                );
        self.active(true);
    };

}
;