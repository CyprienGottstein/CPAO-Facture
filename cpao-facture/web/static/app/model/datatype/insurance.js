/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function InsuranceDatatype($root, $parent, baseDatatype, msg) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;
    self.parent = $parent;

    self.baseDatatype = baseDatatype;

    self.id = 1;
    self.primary = true;
    self.rest = "insurance";
    self.label = "Assurances";
    self.tab = "#insuranceSubTab";
    self.tabId = "insuranceSubTab";
    self.tabLabel = "Gestion des Assurances";
    self.modalLabel = "Saisie d'un mode d'assurance";
    self.loadableBySeason = true;
    self.msg = msg;

    self.field = {
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

    self.fields = [
        self.field.id,
        self.field.season,
        self.field.label,
        self.field.insuranceCost
    ];

    self.model = function ($root, $parent, data, datatype, pack) {
        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;

        self.id = ko.observable(data.id);
        self.season = ko.observable(data.season);
        self.label = ko.observable(data.label);
        self.insuranceCost = ko.observable(data.insuranceCost);

        self.msg = datatype.msg;
        self.datatype = datatype;
        self.datatypeId = datatype.id;
        self.pack = pack;

        self.reload = function (data) {
            self.season(data.season);
            self.label(data.label);
            self.insuranceCost(data.insuranceCost);
        };

        self.rebind = function () {
        };

        self.getLabel = function () {
            return self.msg.announce.start + " l'assurance " + self.msg.announce.middle + self.label() + self.msg.announce.end;
        };

        self.getPrice = function () {
            var str = "";
            return str;
        };

    };

}