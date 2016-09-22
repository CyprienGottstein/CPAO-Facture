/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function PaymentDatatype($root, $parent, baseDatatype, msg) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;
    self.parnet = $parent;

    self.baseDatatype = baseDatatype;

    self.id = 5;
    self.primary = false;
    self.rest = "payment";
    self.label = "Paiements";
    self.tab = "#paymentSubTab";
    self.tabId = "paymentSubTab";
    self.tabLabel = "Gestion des Paiements";
    self.modalLabel = "Saisie d'un paiement";
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
        idType: {
            id: "idType",
            label: "Type",
            type: "select",
            subtype: {id: 6, field: "name"},
            input: true,
            show: true
        },
        amount: {
            id: "amount",
            label: "Montant",
            type: "float",
            input: true,
            show: true
        },
        metadata: {
            id: "metadata",
            label: "Label",
            type: "composite",
            compositeField: field = {
                owner: {
                    id: "owner",
                    label: "Titulaire",
                    type: "short-string",
                    input: true,
                    show: true,
                    composite : true
                },
                bankLabel: {
                    id: "bankLabel",
                    label: "Banque",
                    type: "short-string",
                    input: true,
                    show: true,
                    composite : true
                },
                paycheck: {
                    id: "paycheck",
                    label: "Numéro de chèque",
                    type: "short-string",
                    input: true,
                    show: true,
                    composite : true
                },
                validity: {
                    id: "validity",
                    label: "Validité",
                    type: "date",
                    input: true,
                    show: true,
                    composite : true
                },
                firstname: {
                    id: "firstname",
                    label: "Prénom",
                    type: "short-string",
                    input: true,
                    show: true,
                    composite : true
                },
                lastname: {
                    id: "lastname",
                    label: "Nom",
                    type: "short-string",
                    input: true,
                    show: true,
                    composite : true
                },
                birthday: {
                    id: "birthday",
                    label: "Date de naissance",
                    type: "date",
                    input: true,
                    show: true,
                    composite : true
                }
            },
            dependency: "idType",
            input: false,
            show: false
        },
        solded: {
            id: "solded",
            label: "Soldé",
            type: "boolean",
            input: true,
            show: true
        }
    };

    self.field.metadata.compositeFields = [
        self.field.metadata.compositeField.owner,
        self.field.metadata.compositeField.bankLabel,
        self.field.metadata.compositeField.paycheck,
        self.field.metadata.compositeField.validity,
        self.field.metadata.compositeField.firstname,
        self.field.metadata.compositeField.lastname,
        self.field.metadata.compositeField.birthday
    ];

    self.field.metadata.compositeFieldsList = [
        [
            self.field.metadata.compositeField.owner,
            self.field.metadata.compositeField.bankLabel,
            self.field.metadata.compositeField.paycheck
        ],
        [
            self.field.metadata.compositeField.owner,
            self.field.metadata.compositeField.paycheck,
            self.field.metadata.compositeField.validity
        ],
        [
            self.field.metadata.compositeField.owner,
            self.field.metadata.compositeField.paycheck,
            self.field.metadata.compositeField.validity
        ],
        [
            self.field.metadata.compositeField.paycheck,
            self.field.metadata.compositeField.firstname,
            self.field.metadata.compositeField.lastname,
            self.field.metadata.compositeField.birthday
        ],
        []
    ];

    self.fields = [
        self.field.id,
        self.field.idHome,
        self.field.season,
        self.field.idType,
        self.field.amount,
        self.field.metadata,
        self.field.solded
    ];

    self.fieldsSafeCopy = [
        self.field.id,
        self.field.idHome,
        self.field.season,
        self.field.idType,
        self.field.amount,
        self.field.metadata,
        self.field.solded
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
        self.idHome = ko.observable(new ResourceBinder(self.root, self, data.idHome, datatype, datatype.field.idHome.subtype));
        self.season = ko.observable(data.season);
        self.idType = ko.observable(new ResourceBinder(self.root, self, data.idType, datatype, datatype.field.idType.subtype));
        self.amount = ko.observable(data.amount);
        self.metadata = ko.observable(data.metadata);
        self.solded = ko.observable(data.solded);
        
        var metadata = JSON.parse(self.metadata());
        
        self.owner = ko.observable(metadata.owner);
        self.bankLabel = ko.observable(metadata.bankLabel);
        self.paycheck = ko.observable(metadata.paycheck);
        self.validity = ko.observable();
        self.firstname = ko.observable(metadata.firstname);
        self.lastname = ko.observable(metadata.lastname);
        self.birthday = ko.observable();
        
        if (metadata.validity){
            self.validity(moment(new Date(metadata.validity)).format('DD/MM/YYYY'));
        }
        
        if (metadata.birthday){
            self.birthday(moment(new Date(metadata.birthday)).format('DD/MM/YYYY'));
        }


        self.rebind = function () {
            self.idHome().rebind();
            self.idType().rebind();
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

        self.getPrice = function () {
            var str = "";
            return str;
        };

    };

}