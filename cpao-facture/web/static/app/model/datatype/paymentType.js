/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function PaymentTypeDatatype($root, $parent, baseDatatype, msg) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;
    self.parent = $parent;

    self.baseDatatype = baseDatatype;
    
    self.id = 6;
    self.primary = false;
    self.static = true;
    self.rest = "paymentType";
    self.label = "Mode de paiement";
    self.tab = "#paymentTypeSubTab";
    self.tabId = "paymentTypeSubTab";
    self.tabLabel = "Gestion des modes de paiements";
    self.modalLabel = "Saisie d'un mode de paiement";
    self.loadableBySeason = false;
    self.loadableByHome = false;
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
        }
    };

    self.fields = [
        self.field.id,
        self.field.name
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

        self.rebind = function () {
        };

        self.getLabel = function () {
            return self.msg.announce.start + " le type de paiement " + self.msg.announce.middle + self.name() + self.msg.announce.end;
        };
        
        self.getPrice = function() {
            var str = "";
            return str;
        };

    };

}