/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function ActivityDatatype($root, $parent, baseDatatype, msg) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;
    self.parent = $parent;

    self.baseDatatype = baseDatatype;

    self.id = 0;
    self.primary = true;
    self.rest = "activity";
    self.label = "Activités";
    self.tab = "#activitySubTab";
    self.tabId = "activitySubTab";
    self.tabLabel = "Gestion des Activités";
    self.modalLabel = "Saisie d'une activité";
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

    self.fields = [
        self.field.id,
        self.field.season,
        self.field.label,
        self.field.licenceCost,
        self.field.cotisationCost
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
        self.licenceCost = ko.observable(data.licenceCost);
        self.cotisationCost = ko.observable(data.cotisationCost);

        self.datatype = datatype;
        self.datatypeId = datatype.id;
        self.msg = datatype.msg;
        self.pack = pack;

        self.reload = function (data) {
            self.season(data.season);
            self.label(data.label);
            self.licenceCost(data.licenceCost);
            self.cotisationCost(data.cotisationCost);
        };

        self.rebind = function () {
        };

        self.getLabel = function () {
            return self.msg.announce.start + " l'activité " + self.msg.announce.middle + self.label() + self.msg.announce.end;
        };

        self.getPrice = function () {
            var str = "";
            return str;
        };

    };

}