function RemoveResourceModal($root, datatypes) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;
    
    self.datatypes = datatypes;

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

    self.currentDatatype = ko.observable();

    self.show = ko.observable(false);
    self.active = ko.observable(false);

    self.toggle = function () {
        self.show(!self.show());
    };

    $('#removeResourceModal').on('hidden.bs.modal', function () {
        self.show(false);
    });



    self.focus = function (resource, datatypeId) {
        self.currentDatatype(datatypeId);
        self.resource = resource;
        self.label(self.resource.getLabel());
    };

    self.remove = function () {

        var datatypeId = self.currentDatatype();
        var datatype = null;
        self.datatypes().forEach(function (dt) {
            if (dt.id === datatypeId) {
                datatype = dt;
            }
        });

        var callback = function (data) {

            if (typeof data !== "undefined") {
                if (data.result === 1) {
                    var array = datatype.resources();
                    _.remove(array, function (resource) {
                        if (resource.id() === self.resource.id()) {
                            return true;
                        }
                        return false;
                    });
                    datatype.resources(array);
                    self.toggle();
                } else {
                    self.label(self.static.msg.failure.begin + self.resource.label() + self.static.msg.failure.end);
                }
            } else {
                self.label(self.static.msg.failure.begin + self.resource.label() + self.static.msg.failure.end);
            }


            self.active(false);
        };

        var params = {id: self.resource.id()};

        self.root.ajax.genericCrud.ajax(
                datatype.rest,
                self.root.ajax.static.crudOperation.remove.id,
                params,
                callback
                );
        self.active(true);
    };

}
;