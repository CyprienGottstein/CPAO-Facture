/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function HomeDatatype($root, $parent, baseDatatype, msg) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;
    self.parent = $parent;

    self.baseDatatype = baseDatatype;
    
    self.id = 2;
    self.primary = true;
    self.rest = "home";
    self.label = "Foyers";
    self.tab = "#homeSubTab";
    self.tabId = "homeSubTab";
    self.tabLabel = "Gestion des Foyers";
    self.modalLabel = "Saisie d'un foyer";
    self.loadableBySeason = false;
    self.msg = msg;

    self.field = {
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
            label: "Fiche de foyer",
            type: "recursive",
            pointerToDatatype: self.parent.datatype.people,
            input: false,
            show: false,
            restrictBy: "idHome",
            reloadBy: "home"
        },
        payments: {
            id: "payments",
            label: "Paiements",
            type: "datatype",
            pointerToDatatype: self.parent.datatype.payment,
            list: true,
            input: true,
            show: false,
            restrictBy: "idHome",
            reloadBy: "home"
        },
        situation: {
            id: "situation",
            label: "Récapitulatif financier",
            type: "template",
            pointerToTemplate: self.parent.datatype.payment,
            list: false,
            input: false,
            show: false
        }
    };

    self.fields = [
        self.field.id,
        self.field.name,
        self.field.peoples,
        self.field.payments,
        self.field.situation
    ];
    
    self.field.situation.dependencies = [
        self.field.peoples.pointerToDatatype,
        self.field.payments.pointerToDatatype
    ];

    self.model = function ($root, $parent, data, datatype, pack) {
        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;

        self.id = ko.observable(data.id);
        self.name = ko.observable(data.name);

        self.msg = datatype.msg;
        self.datatype = datatype;
        self.datatypeId = datatype.id;
        self.pack = pack;
        
        self.reload = function(data) {
            self.name(data.name);
        };

        self.rebind = function () {
        };

        self.getLabel = function () {
            return self.msg.announce.start + " le foyer " + self.msg.announce.middle + self.name() + self.msg.announce.end;
        };
        
        self.getPrice = function() {
            var str = "";
            return str;
        };

    };

}