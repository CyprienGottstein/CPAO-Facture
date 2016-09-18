function InputResourceModal($root, datatypes, seasonController) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;

    self.datatypes = datatypes;

    self.season = seasonController;

    self.editing = ko.observable(false);
    self.editingId = ko.observable(-1);
    self.editingResource = ko.observable({});

    self.active = ko.observable(false);
    self.failure = ko.observable(false);

    self.show = ko.observable(false);
    self.currentDatatype = ko.observable();
    self.resourceValidator = ko.validatedObservable({});

    self.title = ko.observable("No title");
    self.currentResourceFields = ko.observableArray();

    self.close = function () {
        self.show(false);
        self.reset();
    };

    $('#inputResourceModal').on('hidden.bs.modal', function () {
        self.close();
    });

    self.toggle = function (datatypeId) {
        self.show(!self.show());
        if (typeof datatypeId !== "undefined") {
            self.currentDatatype(datatypeId);
        }

    };

    self.yearpicker = new YearPicker(self.root, self.root.controller.season, _.clone(self.season.yearpicker.current()));

    self.prepareNew = function () {

        var datatypeId = self.currentDatatype();
        if (typeof datatypeId !== "undefined") {
            var datatype = null;
            self.datatypes().forEach(function (dt) {
                if (dt.id === datatypeId) {
                    datatype = dt;
                }
            });
            
            self.title(datatype.modalLabel);

            var array = [];

            datatype.fields.forEach(function (field) {
                var fieldObs = {};
                fieldObs.metadata = field;
                
                switch (field.type) {
                    case "id":
                        break;
                    case "season":
                        fieldObs.data = new YearPicker(self.root, self.root.controller.season, _.clone(self.season.yearpicker.current()));
                        array.push(fieldObs);
                        break;
                    case "short-string":
                        fieldObs.data = ko.observable("")
                                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                                .extend({minLength: {params: 2, message: "Ce champ doit faire au moins 2 caractères."}});
                        fieldObs.data.isModified(false);
                        array.push(fieldObs);
                        break;
                    case "long-string":
                        fieldObs.data = ko.observable("")
                                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                                .extend({minLength: {params: 2, message: "Ce champ doit faire au moins 2 caractères."}});
                        fieldObs.data.isModified(false);
                        array.push(fieldObs);
                        break;
                    case "float":
                        fieldObs.data = ko.observable("")
                                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                                .extend({number: {params: true, message: "Ce n'est pas un nombre"}})
                                .extend({min: {params: 0, message: "Le coût ne peut pas être négatif"}});
                        fieldObs.data.isModified(false);
                        array.push(fieldObs);
                        break;
                    case "date":
                        fieldObs.data = ko.observable("")
                                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                                .extend({date: {params: true, message: "Ce n'est pas une date"}});
                        fieldObs.data.isModified(false);
                        array.push(fieldObs);
                        break;
                    case "select":
                        self.root.controller.resource.datatypes().forEach(function(resourceDatatype){
                            if (resourceDatatype.id === fieldObs.metadata.subtype){
                                fieldObs.options = resourceDatatype.resources;
                            }
                        });

                        fieldObs.data = ko.observable(fieldObs.options()[0])
                                .extend({required: {params: true, message: "Ce champ est obligatoire"}});
                        fieldObs.data.isModified(false);
                        array.push(fieldObs);
                        break;
                }

            });

            self.currentResourceFields(array);

        }


    };

    self.currentDatatype.subscribe(self.prepareNew);

    self.edit = function (resource, datatypeId) {
        
        self.currentDatatype(datatypeId);
        self.editing(true);
        self.editingId(resource.id());

        var datatype = null;
        self.datatypes().forEach(function (dt) {
            if (dt.id === datatypeId) {
                datatype = dt;
            }
        });

        datatype.inputFields.forEach(function (field) {
            self.currentResourceFields().forEach(function (fieldObs) {
                if (field.id === fieldObs.metadata.id) {

                    switch (fieldObs.metadata.type) {
                        case "season":
                            self.yearpicker.current(resource[field.id]());
                            break;
                        case "date":
                            fieldObs.data(resource[field.id]());
                            break;
                        case "select":
                            fieldObs.options().forEach(function(option){
                                if (option.id() === resource[field.id]().id()){
                                    fieldObs.data([option]);
                                }
                            });
                            break;
                        default:
                            fieldObs.data(resource[field.id]());
                            break;
                    }
                }
            });
        });

        var group = {};
        var n = 0;

        self.currentResourceFields().forEach(function (fieldObs) {
            if (fieldObs.metadata.type !== "season") {
                var label = "v" + n;
                group[label] = fieldObs.data;
                n += 1;
            }
        });

        self.resourceValidator(group);
    };

    self.reset = function () {
        self.yearpicker.current(self.season.yearpicker.current());
        self.prepareNew();
        self.active(false);
        self.failure(false);
        self.editing(false);
        self.editingId(-1);
    };

    self.save = function () {

        var resource = {};
        var datatypeId = self.currentDatatype();

        var datatype = null;
        self.datatypes().forEach(function (dt) {
            if (dt.id === datatypeId) {
                datatype = dt;
            }
        });

        var array = self.currentResourceFields();
        console.log(array);
        array.forEach(function (fieldObs) {
            switch (fieldObs.metadata.type) {
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
                    resource[fieldObs.metadata.id] = fieldObs.data()[0].id();
                    break;
                default:
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