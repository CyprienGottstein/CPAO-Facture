/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function PeopleDatatype($root, $parent, baseDatatype, msg) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;
    self.parent = $parent;

    self.baseDatatype = baseDatatype;

    self.id = 3;
    self.primary = true;
    self.rest = "people";
    self.label = "Adhérents";
    self.tab = "#peopleSubTab";
    self.tabId = "peopleSubTab";
    self.tabLabel = "Gestion des Adhérents";
    self.modalLabel = "Saisie d'un adhérent";
    self.loadableBySeason = false;
    self.loadableByHome = true;
    self.msg = msg;
    
    self.field = {
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
            pointerToDatatype: self.parent.datatype.peopleActivity,
            list: true,
            input: true,
            show: false,
            restrictBy: "idPeople",
            reloadBy: "people"
        }
    };

    self.fields = [
        self.field.id,
        self.field.firstname,
        self.field.lastname,
        self.field.birthday,
        self.field.age,
        self.field.idHome,
        self.field.activities
    ];

    self.model = function ($root, $parent, data, datatype, pack) {
        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;

        self.msg = datatype.msg;
        self.datatype = datatype;
        self.datatypeId = datatype.id;
        self.pack = pack;

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
        
        self.getPrice = function() {
            var str = "";
            return str;
        };

    };

}