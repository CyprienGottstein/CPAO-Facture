/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function InsuranceController(root, seasonController) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = root;
    self.season = seasonController;

    self.insurances = ko.observableArray();
    
    self.updateBySeason = function () {
        self.reload();
    };
    
    self.updateByMode = function() {
        self.reload();
    };
    
    self.root.controller.season.subscribeToSeason(self.updateBySeason);
    self.root.controller.season.subscribeToMode(self.updateByMode);
    
    self.modal = new NewInsuranceModal(root, self, self.season);
    self.removeModal = new RemoveInsuranceModal(root, self);

    self.toggleModal = function () {
        self.modal.toggle();
    };

    self.loadBySeason = function () {

        var callback = function (data) {
            var array = self.insurances();
            array = [];
            data.forEach(function (insurance) {
                array.push(new InsuranceModel(root, self, insurance));
            });
            self.insurances(array);
        };

        root.ajax.insurance.loadBySeason(self.season.yearpicker.current(), callback);
    };
    
    self.loadAll = function () {
        
        var callback = function (data) {
            var array = self.insurances();
            array = [];
            data.forEach(function (insurance) {
                array.push(new InsuranceModel(root, self, insurance));
            });
            self.insurances(array);
        };

        root.ajax.insurance.loadAll(callback);
        
    };
    
    self.reload = function () {
        if (self.season.currentMode() === 1){
            self.loadBySeason();
        } else {
            self.loadAll();
        }
    };

    function InsuranceModel($root, $parent, data) {
        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;

        self.id = data.id;
        self.season = data.season;
        self.label = data.label;
        self.insuranceCost = data.insuranceCost;

        self.toggleRemoveModal = function () {
            $parent.removeModal.focus(self);
            $parent.removeModal.toggle();
        };

        self.toggleEditModal = function () {
            $parent.modal.edit(self);
            $parent.modal.toggle();
        };

    }
    ;

    function NewInsuranceModal($root, $parent, seasonController) {

        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;
        self.season = seasonController;

        self.editing = ko.observable(false);
        self.editingId = ko.observable(-1);

        self.show = ko.observable(false);

        $('#insuranceInputModal').on('hidden.bs.modal', function () {
            self.show(false);
            self.reset();
        });

        self.active = ko.observable(false);
        self.failure = ko.observable(false);

        self.yearpicker = new YearPicker(self.root, self.root.controller.season, self.season.yearpicker.current());
        
        self.selectedLabel = ko.observable("")
                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                .extend({minLength: {params: 5, message: "La description de l'activité doit faire au moins 5 caractères."}});
        self.selectedInsuranceCost = ko.observable(0.0)
                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                .extend({number: {params: true, message: "Ce n'est pas un nombre"}})
                .extend({min: {params: 0, message: "Le coût ne peut pas être négatif"}});

        self.insuranceValidator = ko.validatedObservable({
            label: self.selectedLabel,
            insuranceCost: self.selectedInsuranceCost
        });

        self.toggle = function () {
            self.show(!self.show());
        };

        self.edit = function (insurance) {
            self.editing(true);
            self.editingId(insurance.id);
            self.yearpicker.current(insurance.season);
            self.selectedLabel(insurance.label);
            self.selectedInsuranceCost(insurance.insuranceCost);
        };

        self.reset = function () {
            self.yearpicker.current(self.season.yearpicker.current());
            self.selectedLabel("");
            self.selectedLabel.isModified(false);
            self.selectedInsuranceCost("");
            self.selectedInsuranceCost.isModified(false);
            self.active(false);
            self.failure(false);
            self.editing(false);
            self.editingId(-1);
        }

        self.save = function () {

            var insurance = {};

            insurance.season = _.clone(self.yearpicker.current());
            insurance.label = _.clone(self.selectedLabel());
            insurance.insuranceCost = _.clone(self.selectedInsuranceCost() + "");

            var callback = function (data) {

                if (typeof data !== "undefined") {
                    if (data.result === 1) {
                        $parent.reload();
                        self.toggle();
                    } else {
                        self.failure(true);
                    }
                } else {
                    self.failure(true);
                }

                self.active(false);
            };

            if (self.editing()) {
                root.ajax.insurance.update(self.editingId(), insurance, callback);
            } else {
                root.ajax.insurance.save(insurance, callback);
            }
            self.active(true);

        };

    }
    ;

    function RemoveInsuranceModal($root, $parent) {

        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;

        self.show = ko.observable(false);

        $('#insuranceRemoveModal').on('hidden.bs.modal', function () {
            self.show(false);
        });

        self.active = ko.observable(false);

        self.static = {};
        self.static.msg = {};
        self.static.msg.announce = {};
        self.static.msg.announce.begin = "Vous êtes sur le point de supprimer l'assurance : <label>";
        self.static.msg.announce.end = "</label>";
        self.static.msg.failure = {};
        self.static.msg.failure.begin = "La suppression de l'assurance : <label>";
        self.static.msg.failure.end = "</label> a echoué.";


        self.insurance = {};
        self.label = ko.observable("");

        self.toggle = function () {
            self.show(!self.show());
        };

        self.focus = function (insurance) {
            self.insurance = insurance;
            self.label(self.static.msg.announce.begin + self.insurance.label + self.static.msg.announce.end);
        };

        self.remove = function () {
            var callback = function (data) {

                if (typeof data !== "undefined") {
                    if (data.result === 1) {
                        var array = self.parent.insurances();
                        _.remove(array, function (insurance) {
                            if (insurance.id === self.insurance.id) {
                                return true;
                            }
                            return false;
                        });
                        self.parent.insurances(array);
                        self.toggle();
                    } else {
                        self.label(self.static.msg.failure.begin + self.insurance.label + self.static.msg.failure.end);
                    }
                } else {
                    self.label(self.static.msg.failure.begin + self.insurance.label + self.static.msg.failure.end);
                }


                self.active(false);
            };

            root.ajax.insurance.remove(self.insurance, callback);
            self.active(true);
        };

    }
    ;

}
;