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

    self.datatype = {};
    self.datatype.activity = {
        id: 0,
        rest: "activity",
        label: "Activités",
        tab: "#activitySubTab",
        tabId: "activitySubTab",
        tabLabel: "Gestion des Activités"
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
            type: "string"
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
        self.datatype.activity.field.label,
        self.datatype.activity.field.licenceCost,
        self.datatype.activity.field.cotisationCost
    ];
    
    self.datatype.activity.model = function ($root, $parent, data, datatypeId) {
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
        
        self.datatypeId = datatypeId;

        self.toggleRemoveModal = function () {
            self.parent.removeModal.focus(self, self.datatypeId);
            self.parent.removeModal.toggle(self.datatypeId);
        };

        self.toggleEditModal = function () {
            self.parent.modal.edit(self, self.datatypeId);
            self.parent.modal.toggle(self.datatypeId);
        };

    };
    
    self.datatype.activity.resources = ko.observableArray();
    
    
    
    
    
    
    


    self.datatype.insurance = {
        id: 1,
        rest: "insurance",
        label: "Assurances",
        tab: "#insuranceSubTab",
        tabId: "insuranceSubTab",
        tabLabel: "Gestion des Assurances"
        
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
            type: "string"
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
        self.datatype.insurance.field.label,
        self.datatype.insurance.field.insuranceCost
    ];
    
    self.datatype.insurance.model = function ($root, $parent, data, datatypeId) {
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
        
        self.datatypeId = datatypeId;

        self.toggleRemoveModal = function () {
            console.log(self.datatypeId);
            $parent.removeModal.focus(self, self.datatypeId);
            $parent.removeModal.toggle(self.datatypeId);
        };

        self.toggleEditModal = function () {
            $parent.modal.edit(self, self.datatypeId);
            $parent.modal.toggle(self.datatypeId);
        };

    };
    self.datatype.insurance.resources = ko.observableArray();

    self.datatypes = [
        self.datatype.activity,
        self.datatype.insurance
    ];

}