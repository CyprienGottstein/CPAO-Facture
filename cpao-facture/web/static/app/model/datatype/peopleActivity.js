/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function PeopleActivityDatatype($root, $parent, baseDatatype, msg) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;
    self.parnet = $parent;

    self.baseDatatype = baseDatatype;

    self.id = 4;
    self.primary = false;
    self.rest = "peopleActivity";
    self.label = "Activité par Adhérent";
    self.tab = "#peopleActivitySubTab";
    self.tabId = "peopleActivitySubTab";
    self.tabLabel = "Gestion des Activités par adhérents";
    self.modalLabel = "Saisie d'une activité pour un adhérent";
    self.loadableBySeason = false;
    self.loadableByPeople = true;
    self.msg = msg;

    self.field = {
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

    self.fields = [
        self.field.id,
        self.field.idPeople,
        self.field.idActivity,
        self.field.idInsurance,
        self.field.family,
        self.field.observator,
        self.field.teacher,
        self.field.season
    ];

    self.model = function ($root, $parent, data, datatype, pack) {
        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;
        self.datatype = datatype;
        self.datatypeId = datatype.id;
        self.msg = datatype.msg;
        self.pack = pack;

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

            if (self.family()) {
                str += " [ Famille ]";
            }
            if (self.teacher()) {
                str += " [ Encadrant ]";
            }
            if (self.observator()) {
                str += " [ Non-Pratiquant ]";
            }

            return str;
        };

        self.getPrice = function () {
            var str = "";

            var datatype = null;
            self.root.model.datatypeRoot.datatypes().forEach(function (dt) {
                if (dt().id === self.datatype.field.idActivity.subtype.id) {
                    datatype = dt;
                }
            });

            var price = 0;
            
            datatype().pack().resources().forEach(function (resource) {
                if (resource.id() === self.idActivity().id()) {
                    price += resource.licenceCost() + resource.cotisationCost();
                }
            });
            
            self.root.model.datatypeRoot.datatypes().forEach(function (dt) {
                if (dt().id === self.datatype.field.idInsurance.subtype.id) {
                    datatype = dt;
                }
            });

            datatype().pack().resources().forEach(function (resource) {
                if (resource.id() === self.idInsurance().id()) {
                    price += resource.insuranceCost();
                }
            });


            str += price + " euros";
            return str;
        };

    };

}