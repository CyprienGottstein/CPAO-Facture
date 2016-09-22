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
    
    self.datatype.activity = ko.observable({});
    self.datatype.insurance = ko.observable({});
    self.datatype.home = ko.observable({});
    self.datatype.peopleActivity = ko.observable({});
    self.datatype.people = ko.observable({});
    self.datatype.payment = ko.observable({});
    
    self.datatype.activity({
        id: 0,
        primary: true,
        rest: "activity",
        label: "Activités",
        tab: "#activitySubTab",
        tabId: "activitySubTab",
        tabLabel: "Gestion des Activités",
        modalLabel: "Saisie d'une activité",
        loadableBySeason: true,
        msg: self.static.msg
    });
    self.datatype.activity().field = {
        id: {
            id: "id",
            label: "Id",
            type: "id",
            input: false,
            show: true
        },
        season: {
            id: "season",
            label: "Saison",
            type: "season",
            input: true,
            show: true
        },
        label: {
            id: "label",
            label: "Description",
            type: "long-string",
            input: true,
            show: true
        },
        licenceCost: {
            id: "licenceCost",
            label: "Licence",
            type: "float",
            input: true,
            show: true
        },
        cotisationCost: {
            id: "cotisationCost",
            label: "Cotisation",
            type: "float",
            input: true,
            show: true
        }
    };

    self.datatype.activity().fields = [
        self.datatype.activity().field.id,
        self.datatype.activity().field.season,
        self.datatype.activity().field.label,
        self.datatype.activity().field.licenceCost,
        self.datatype.activity().field.cotisationCost
    ];

    self.datatype.activity().model = function ($root, $parent, data, datatype) {
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
        self.datatype = datatype;
        self.datatypeId = datatype.id;

        self.rebind = function () {
        };

        self.getLabel = function () {
            return self.msg.announce.start + " l'activité " + self.msg.announce.middle + self.label() + self.msg.announce.end;
        };

    };


    self.datatype.insurance({
        id: 1,
        primary: true,
        rest: "insurance",
        label: "Assurances",
        tab: "#insuranceSubTab",
        tabId: "insuranceSubTab",
        tabLabel: "Gestion des Assurances",
        modalLabel: "Saisie d'un mode d'assurance",
        loadableBySeason: true,
        msg: self.static.msg
    });

    self.datatype.insurance().field = {
        id: {
            id: "id",
            label: "Id",
            type: "id",
            input: false,
            show: true
        },
        season: {
            id: "season",
            label: "Saison",
            type: "season",
            input: true,
            show: true
        },
        label: {
            id: "label",
            label: "Description",
            type: "long-string",
            input: true,
            show: true
        },
        insuranceCost: {
            id: "insuranceCost",
            label: "Assurance",
            type: "float",
            input: true,
            show: true
        }
    };

    self.datatype.insurance().fields = [
        self.datatype.insurance().field.id,
        self.datatype.insurance().field.season,
        self.datatype.insurance().field.label,
        self.datatype.insurance().field.insuranceCost
    ];

    self.datatype.insurance().model = function ($root, $parent, data, datatype) {
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
        self.datatype = datatype;
        self.datatypeId = datatype.id;

        self.rebind = function () {
        };

        self.getLabel = function () {
            return self.msg.announce.start + " l'assurance " + self.msg.announce.middle + self.label() + self.msg.announce.end;
        };

    };




    self.datatype.home({
        id: 2,
        primary: true,
        rest: "home",
        label: "Foyers",
        tab: "#homeSubTab",
        tabId: "homeSubTab",
        tabLabel: "Gestion des Foyers",
        modalLabel: "Saisie d'un foyer",
        loadableBySeason: false,
        msg: self.static.msg
    });
    self.datatype.home().field = {
        id: {
            id: "id",
            label: "Id",
            type: "id",
            input: false,
            show: true
        },
        name: {
            id: "name",
            label: "Nom",
            type: "long-string",
            input: true,
            show: true
        },
        peoples: {
            id: "peoples",
            label: "Adhérents",
            type: "recursive",
            pointerToDatatype: self.datatype.people,
            input: false,
            show: false,
            restrictBy: "idHome",
            reloadBy: "home"
        },
        payments: {
            id: "payments",
            label: "Paiements",
            type: "datatype",
            pointerToDatatype: self.datatype.payment,
            list: true,
            input: true,
            show: false,
            restrictBy: "idHome",
            reloadBy: "home"
        }
    };

    self.datatype.home().fields = [
        self.datatype.home().field.id,
        self.datatype.home().field.name,
        self.datatype.home().field.peoples,
        self.datatype.home().field.payments
    ];

    self.datatype.home().model = function ($root, $parent, data, datatype) {
        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;

        self.id = ko.observable(data.id);
        self.name = ko.observable(data.name)
                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                .extend({minLength: {params: 2, message: "Le prénom ne peut pas faire moins de deux caractères."}});
        
//        self.idPeople = ko.observable(new ResourceBinder(self.root, self, data.idPeople, datatype, datatype.field.idPeople.subtype));

        self.msg = datatype.msg;
        self.datatype = datatype;
        self.datatypeId = datatype.id;

        self.rebind = function () {
        };

        self.getLabel = function () {
            return self.msg.announce.start + " le foyer " + self.msg.announce.middle + self.name() + self.msg.announce.end;
        };

    };





    self.datatype.peopleActivity({
        id: 4,
        primary: false,
        rest: "peopleActivity",
        label: "Activité par Adhérent",
        tab: "#peopleActivitySubTab",
        tabId: "peopleActivitySubTab",
        tabLabel: "Gestion des Activités par adhérents",
        modalLabel: "Saisie d'une activité pour un adhérent",
        loadableBySeason: false,
        loadableByPeople: true,
        msg: self.static.msg
    });
    self.datatype.peopleActivity().field = {
        id: {
            id: "id",
            label: "Id",
            type: "id",
            input: false,
            show: true
        },
        idPeople: {
            id: "idPeople",
            label: "Adhérent",
            type: "select",
            subtype: {id: 3, field: "lastname"},
            input: false,
            show: true
        },
        idActivity: {
            id: "idActivity",
            label: "Activité",
            type: "select",
            subtype: {id: 0, field: "label"},
            input: true,
            show: true
        },
        idInsurance: {
            id: "idInsurance",
            label: "Assurance",
            type: "select",
            subtype: {id: 1, field: "label"},
            input: true,
            show: true
        },
        family: {
            id: "family",
            label: "Famille",
            type: "boolean",
            input: true,
            show: true
        },
        teacher: {
            id: "teacher",
            label: "Encadrant",
            type: "boolean",
            input: true,
            show: true
        },
        observator: {
            id: "observator",
            label: "Non-Pratiquant",
            type: "boolean",
            input: true,
            show: true
        },
        season: {
            id: "season",
            label: "saison",
            type: "season",
            input: true,
            show: true
        }
    };

    self.datatype.peopleActivity().fields = [
        self.datatype.peopleActivity().field.id,
        self.datatype.peopleActivity().field.idPeople,
        self.datatype.peopleActivity().field.idActivity,
        self.datatype.peopleActivity().field.idInsurance,
        self.datatype.peopleActivity().field.family,
        self.datatype.peopleActivity().field.observator,
        self.datatype.peopleActivity().field.teacher,
        self.datatype.peopleActivity().field.season
    ];

    self.datatype.peopleActivity().model = function ($root, $parent, data, datatype) {
        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;
        self.datatype = datatype;
        self.datatypeId = datatype.id;
        self.msg = datatype.msg;

        self.id = ko.observable(data.id);
        self.family = ko.observable(data.family);
        self.observator = ko.observable(data.observator);
        self.teacher = ko.observable(data.teacher);
        self.season = ko.observable(data.season);
        
        self.idPeople = ko.observable(new ResourceBinder(self.root, self, data.idPeople, datatype, datatype.field.idPeople.subtype));
        self.idInsurance = ko.observable(new ResourceBinder(self.root, self, data.idInsurance, datatype, datatype.field.idInsurance.subtype));
        self.idActivity = ko.observable(new ResourceBinder(self.root, self, data.idActivity, datatype, datatype.field.idActivity.subtype));

        self.rebind = function () {
            self.idPeople().rebind();
            self.idInsurance().rebind();
            self.idActivity().rebind();
        };

        self.rebind();

        self.getLabel = function () {
            return self.msg.announce.start + " l'activité <label>" + self.idActivity().label() + "</label> de l'adhérent " + self.msg.announce.middle + self.idPeople().label() + " " + self.msg.announce.end;
        };
        
        self.getDescription = function () {
            var str = "";
            str += self.idActivity().label();
            str += " - "
            str += self.idInsurance().label();
            
            if (self.family()){
                str += " [ Famille ]";
            }
            if (self.teacher()){
                str += " [ Encadrant ]";
            }
            if (self.observator()){
                str += " [ Non-Pratiquant ]";
            }
            
            return str;
        };

    };



    self.datatype.people({
        id: 3,
        primary: true,
        rest: "people",
        label: "Adhérents",
        tab: "#peopleSubTab",
        tabId: "peopleSubTab",
        tabLabel: "Gestion des Adhérents",
        modalLabel: "Saisie d'un adhérent",
        loadableBySeason: false,
        msg: self.static.msg
    });
    self.datatype.people().field = {
        id: {
            id: "id",
            label: "Id",
            type: "id",
            input: false,
            show: true
        },
        firstname: {
            id: "firstname",
            label: "Prénom",
            type: "short-string",
            input: true,
            show: true
        },
        lastname: {
            id: "lastname",
            label: "Nom",
            type: "short-string",
            input: true,
            show: true
        },
        birthday: {
            id: "birthday",
            label: "Date de naissance",
            type: "date",
            input: true,
            show: true
        },
        age: {
            id: "age",
            label: "Age",
            type: "id",
            input: false,
            show: true
        },
        idHome: {
            id: "idHome",
            label: "Foyer",
            type: "select",
            subtype: {id: 2, field: "name"},
            input: true,
            show: true
        },
        activities: {
            id: "activities",
            label: "Activités",
            type: "datatype",
            pointerToDatatype: self.datatype.peopleActivity,
            list: true,
            input: true,
            show: false,
            restrictBy: "idPeople",
            reloadBy: "people"
        }
    };

    self.datatype.people().fields = [
        self.datatype.people().field.id,
        self.datatype.people().field.firstname,
        self.datatype.people().field.lastname,
        self.datatype.people().field.birthday,
        self.datatype.people().field.age,
        self.datatype.people().field.idHome,
        self.datatype.people().field.activities
    ];

    self.datatype.people().model = function ($root, $parent, data, datatype) {
        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;

        self.msg = datatype.msg;
        self.datatype = datatype;
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
        self.idHome = ko.observable(new ResourceBinder(self.root, self, data.idHome, datatype, datatype.field.idHome.subtype));

        self.rebind = function () {
            self.idHome().rebind();
        };

        self.getLabel = function () {
            return self.msg.announce.start + " l'adhérent " + self.msg.announce.middle + self.firstname() + " " + self.lastname() + self.msg.announce.end;
        };
        
        self.getDescription = function () {
            var str = "";
            str += self.firstname();
            str += " - ";
            str += self.lastname();
            str += " - ";
            str += self.age();
            str += " ans";
            
            return str;
        };

    };
    
    
    
    
    
    
    
    
    
    
    
    
    
    self.datatype.payment({
        id: 5,
        primary: false,
        rest: "payment",
        label: "Paiements",
        tab: "#paymentSubTab",
        tabId: "paymentSubTab",
        tabLabel: "Gestion des Paiements",
        modalLabel: "Saisie d'un paiements",
        loadableBySeason: false,
        loadableByHome: true,
        msg: self.static.msg
    });
    self.datatype.payment().field = {
        id: {
            id: "id",
            label: "Id",
            type: "id",
            input: false,
            show: true
        },
        amount: {
            id: "amount",
            label: "Montant",
            type: "float",
            input: true,
            show: true
        },
        idHome: {
            id: "idHome",
            label: "Foyer",
            type: "select",
            subtype: {id: 2, field: "name"},
            input: false,
            show: true
        },
        season: {
            id: "season",
            label: "Saison",
            type: "season",
            input: true,
            show: true
        },
        bankLabel: {
            id: "bankLabel",
            label: "Banque",
            type: "short-string",
            input: true,
            show: true
        },
        solded: {
            id: "solded",
            label: "Soldé",
            type: "boolean",
            input: true,
            show: true
        },
        cashed: {
            id: "cashed",
            label: "Encaissé",
            type: "boolean",
            input: true,
            show: true
        }
    };

    self.datatype.payment().fields = [
        self.datatype.payment().field.id,
        self.datatype.payment().field.idHome,
        self.datatype.payment().field.amount,
        self.datatype.payment().field.season,
        self.datatype.payment().field.bankLabel,
        self.datatype.payment().field.solded,
        self.datatype.payment().field.cashed
    ];

    self.datatype.payment().model = function ($root, $parent, data, datatype) {
        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;

        self.msg = datatype.msg;
        self.datatype = datatype;
        self.datatypeId = datatype.id;

        self.id = ko.observable(data.id);
        self.idHome = ko.observable(new ResourceBinder(self.root, self, data.idHome, datatype, datatype.field.idHome.subtype));
        self.amount = ko.observable(data.amount);
        self.season = ko.observable(data.season);
        self.bankLabel = ko.observable(data.bankLabel);
        self.solded = ko.observable(data.solded);
        self.cashed = ko.observable(data.cashed);

        

        self.rebind = function () {
            self.idHome().rebind();
        };
        
        self.rebind();

        self.getLabel = function () {
            return self.msg.announce.start + " le paiement numéro " + self.msg.announce.middle + self.id() + " d'un montant de : <label>" + self.amount()
                    + "</label> addressé par le foyer " + self.idHome().label() + self.msg.announce.end;
        };
        
        self.getDescription = function () {
            var str = "";
            str += self.idHome().label();
            str += " - ";
            str += self.bankLabel();
            str += " - ";
            str += self.amount();
            str += " ans";
            
            return str;
        };

    };
    
    self.datatype.payment().getDatapack = function () {
        var datapack = {};
        datapack.resources = ko.observableArray();
        datapack.fields = self.datatype.payment().fields;
        return datapack;
    };
    
    self.datatypes = ko.observableArray([
        self.datatype.activity,
        self.datatype.insurance,
        self.datatype.people,
        self.datatype.peopleActivity,
        self.datatype.home,
        self.datatype.payment
    ]);

}