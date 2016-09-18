/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function GenericResource(root) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = root;
    
    self.static = {};
    self.static.msg = {};
    self.static.msg.announce = {};
    self.static.msg.announce.start = "Vous êtes sur le point de supprimer ";
    self.static.msg.announce.middle = " : <label>";
    self.static.msg.announce.end = "</label>";
    self.static.msg.failure = {};
    self.static.msg.failure.start = "La suppression ";
    self.static.msg.failure.middle = " : <label>";
    self.static.msg.failure.end = "</label> a echoué";

    self.datatype = {};
    self.datatype.activity = {
        id: 0,
        rest: "activity",
        label: "Activités",
        tab: "#activitySubTab",
        tabId: "activitySubTab",
        tabLabel: "Gestion des Activités",
        modalLabel: "Saisie d'une activité",
        loadableBySeason: true,
        msg : self.static.msg
    };
    self.datatype.activity.field = {
        id: {
            id: "id",
            label: "Id",
            type: "id"
        },
        season: {
            id: "season",
            label: "Saison",
            type: "season"
        },
        label: {
            id: "label",
            label: "Description",
            type: "long-string"
        },
        licenceCost: {
            id: "licenceCost",
            label: "Licence",
            type: "float"
        },
        cotisationCost: {
            id: "cotisationCost",
            label: "Cotisation",
            type: "float"
        }
    };

    self.datatype.activity.fields = [
        self.datatype.activity.field.id,
        self.datatype.activity.field.season,
        self.datatype.activity.field.label,
        self.datatype.activity.field.licenceCost,
        self.datatype.activity.field.cotisationCost
    ];

    self.datatype.activity.inputFields = [
        self.datatype.activity.field.season,
        self.datatype.activity.field.label,
        self.datatype.activity.field.licenceCost,
        self.datatype.activity.field.cotisationCost
    ];

    self.datatype.activity.model = function ($root, $parent, data, datatype) {
        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;

        self.id = ko.observable(data.id);
        self.season = ko.observable(data.season);
        self.label = ko.observable(data.label)
                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                .extend({minLength: {params: 5, message: "La description de l'activité doit faire au moins 5 caractères."}});
        self.licenceCost = ko.observable(data.licenceCost)
                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                .extend({number: {params: true, message: "Ce n'est pas un nombre"}})
                .extend({min: {params: 0, message: "Le coût ne peut pas être négatif"}});
        self.cotisationCost = ko.observable(data.cotisationCost)
                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                .extend({number: {params: true, message: "Ce n'est pas un nombre"}})
                .extend({min: {params: 0, message: "Le coût ne peut pas être négatif"}});

        self.msg = datatype.msg;
        self.datatypeId = datatype.id;
        
        self.rebind = function () {
        };
        
        self.getLabel = function() {
            return self.msg.announce.start + " l'activité " + self.msg.announce.middle + self.label() + self.msg.announce.end;
        };

    };

    self.datatype.activity.resources = ko.observableArray();



    self.datatype.insurance = {
        id: 1,
        rest: "insurance",
        label: "Assurances",
        tab: "#insuranceSubTab",
        tabId: "insuranceSubTab",
        tabLabel: "Gestion des Assurances",
        modalLabel: "Saisie d'un mode d'assurance",
        loadableBySeason: true,
        msg : self.static.msg
    };

    self.datatype.insurance.field = {
        id: {
            id: "id",
            label: "Id",
            type: "id"
        },
        season: {
            id: "season",
            label: "Saison",
            type: "season"
        },
        label: {
            id: "label",
            label: "Description",
            type: "long-string"
        },
        insuranceCost: {
            id: "insuranceCost",
            label: "Assurance",
            type: "float"
        }
    };

    self.datatype.insurance.fields = [
        self.datatype.insurance.field.id,
        self.datatype.insurance.field.season,
        self.datatype.insurance.field.label,
        self.datatype.insurance.field.insuranceCost
    ];

    self.datatype.insurance.inputFields = [
        self.datatype.insurance.field.season,
        self.datatype.insurance.field.label,
        self.datatype.insurance.field.insuranceCost
    ];

    self.datatype.insurance.model = function ($root, $parent, data, datatype) {
        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;

        self.id = ko.observable(data.id);
        self.season = ko.observable(data.season);
        self.label = ko.observable(data.label)
                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                .extend({minLength: {params: 5, message: "La description de l'activité doit faire au moins 5 caractères."}});
        self.insuranceCost = ko.observable(data.insuranceCost)
                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                .extend({number: {params: true, message: "Ce n'est pas un nombre"}})
                .extend({min: {params: 0, message: "Le coût ne peut pas être négatif"}});

        self.msg = datatype.msg;
        self.datatypeId = datatype.id;
        
        self.rebind = function () {
        };
        
        self.getLabel = function() {
            return self.msg.announce.start + " l'assurance " + self.msg.announce.middle + self.label() + self.msg.announce.end;
        };

    };
    self.datatype.insurance.resources = ko.observableArray();




    self.datatype.home = {
        id: 2,
        rest: "home",
        label: "Foyers",
        tab: "#homeSubTab",
        tabId: "homeSubTab",
        tabLabel: "Gestion des Foyers",
        modalLabel: "Saisie d'un foyer",
        loadableBySeason: false,
        msg : self.static.msg
    };
    self.datatype.home.field = {
        id: {
            id: "id",
            label: "Id",
            type: "id"
        },
        name: {
            id: "name",
            label: "Nom",
            type: "long-string"
        }
    };

    self.datatype.home.fields = [
        self.datatype.home.field.id,
        self.datatype.home.field.name
    ];

    self.datatype.home.inputFields = [
        self.datatype.home.field.name
    ];

    self.datatype.home.model = function ($root, $parent, data, datatype) {
        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;

        self.id = ko.observable(data.id);
        self.name = ko.observable(data.name)
                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                .extend({minLength: {params: 2, message: "Le prénom ne peut pas faire moins de deux caractères."}});

        self.msg = datatype.msg;
        self.datatypeId = datatype.id;
        
        self.rebind = function () {
        };
        
        self.getLabel = function() {
            return self.msg.announce.start + " le foyer " + self.msg.announce.middle + self.name() + self.msg.announce.end;
        };

    };

    self.datatype.home.resources = ko.observableArray();




    self.datatype.people = {
        id: 3,
        rest: "people",
        label: "Adhérents",
        tab: "#peopleSubTab",
        tabId: "peopleSubTab",
        tabLabel: "Gestion des Adhérents",
        modalLabel: "Saisie d'un adhérent",
        loadableBySeason: false,
        msg : self.static.msg
    };
    self.datatype.people.field = {
        id: {
            id: "id",
            label: "Id",
            type: "id"
        },
        firstname: {
            id: "firstname",
            label: "Prénom",
            type: "short-string"
        },
        lastname: {
            id: "lastname",
            label: "Nom",
            type: "short-string"
        },
        birthday: {
            id: "birthday",
            label: "Date de naissance",
            type: "date"
        },
        age: {
            id: "age",
            label: "Age",
            type: "id"
        },
        home: {
            id: "home",
            label: "Foyer",
            type: "select",
            subtype: 2,
            subfield: "name"
        }
    };

    self.datatype.people.fields = [
        self.datatype.people.field.id,
        self.datatype.people.field.firstname,
        self.datatype.people.field.lastname,
        self.datatype.people.field.birthday,
        self.datatype.people.field.age,
        self.datatype.people.field.home
    ];

    self.datatype.people.inputFields = [
        self.datatype.people.field.firstname,
        self.datatype.people.field.lastname,
        self.datatype.people.field.birthday,
        self.datatype.people.field.home
    ];

    self.datatype.people.model = function ($root, $parent, data, datatype) {
        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;
        self.msg = datatype.msg;
        self.datatypeId = datatype.id;

        self.id = ko.observable(data.id);
        self.firstname = ko.observable(data.firstname)
                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                .extend({minLength: {params: 2, message: "Le prénom ne peut pas faire moins de deux caractères."}});
        self.lastname = ko.observable(data.lastname)
                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                .extend({minLength: {params: 2, message: "Le nom ne peut pas faire moins de deux caractères."}});

        var formattedDate = moment(new Date(data.birthday)).format('DD/MM/YYYY');
        self.birthday = ko.observable(formattedDate)
                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                .extend({date: {params: true, message: "La date de naissance doit être une date !"}});

        var now = Date.now();
        var bday = new Date(data.birthday);
        var diff = now - bday;
        var age = Math.floor(new Date(diff).getTime() / (1000 * 60 * 60 * 24 * 365.25));

        self.age = ko.observable(age);
        self.home = ko.observable(new HomeAdherent(self.root, self, data.home, datatype));

        self.rebind = function () {
            self.home().rebind();
        };
        
        self.getLabel = function() {
            return self.msg.announce.start + " l'adhérent " + self.msg.announce.middle + self.firstname() + " " + self.lastname() + self.msg.announce.end;
        };

        function HomeAdherent($root, $parent, data, datatype) {

            // Safe pointer on self for model
            var self = this;
            // Safe pointer on the root of the application to access other controlers
            self.root = $root;
            self.parent = $parent;
            self.datatypeId = datatype.id;
            self.subDatatypeId = datatype.field.home.subtype;

            self.id = ko.observable(data);
            self.label = ko.observable("NOT-FOUND");

            self.rebind = function () {
                
                var datatype = null;
                self.parent.parent.datatypes().forEach(function (dt) {
                    if (dt.id === self.subDatatypeId) {
                        datatype = dt;
                    }
                });

                datatype.resources().forEach(function (resource) {
                    if (resource.id() === self.id()) {
                        self.label(resource.name());
                    }
                });
            };

        }
        ;



    };

    self.datatype.people.resources = ko.observableArray();

    self.datatypes = ko.observableArray([
        self.datatype.activity,
        self.datatype.insurance,
        self.datatype.people,
        self.datatype.home
    ]);

}