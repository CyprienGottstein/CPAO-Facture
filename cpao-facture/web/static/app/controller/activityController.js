/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function ActivityController(root, seasonController) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = root;
    self.season = seasonController;

    self.activities = ko.observableArray();

    self.updateBySeason = function () {
        self.reload();
    };

    self.updateByMode = function () {
        self.reload();
    };

    self.root.controller.season.subscribeToSeason(self.updateBySeason);
    self.root.controller.season.subscribeToMode(self.updateByMode);

    self.modal = new NewActivityModal(root, self, self.season);
    self.removeModal = new RemoveActivityModal(root, self);

    self.toggleModal = function () {
        self.modal.toggle();
    };

    self.loadBySeason = function () {

        var callback = function (data) {
            var array = self.activities();
            array = [];
            data.forEach(function (activity) {
                array.push(new ActivityModel(root, self, activity));
            });
            self.activities(array);
        };

//        root.ajax.activity.loadBySeason(self.season.yearpicker.current(), callback);
        root.ajax.genericCrud.ajax(
                root.ajax.static.datatype.activity.id,
                root.ajax.static.crudOperation.loadBySeason.id,
                { season : self.season.yearpicker.current() },
                callback
                );
    };

    self.loadAll = function () {

        var callback = function (data) {
            var array = self.activities();
            array = [];
            data.forEach(function (activity) {
                array.push(new ActivityModel(root, self, activity));
            });
            self.activities(array);
        };

        root.ajax.activity.loadAll(callback);

    };

    self.reload = function () {
        if (self.season.currentMode() === 1) {
            self.loadBySeason();
        } else {
            self.loadAll();
        }
    };

    function ActivityModel($root, $parent, data) {
        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;

        self.id = data.id;
        self.season = data.season;
        self.label = data.label;
        self.licenceCost = data.licenceCost;
        self.cotisationCost = data.cotisationCost;

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

    function NewActivityModal($root, $parent, seasonController) {

        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;
        self.season = seasonController;

        self.editing = ko.observable(false);
        self.editingId = ko.observable(-1);

        self.show = ko.observable(false);

        $('#activityModal').on('hidden.bs.modal', function () {
            self.show(false);
            self.reset();
        });

        self.active = ko.observable(false);
        self.failure = ko.observable(false);

        self.yearpicker = new YearPicker(self.root, self.root.controller.season, self.season.yearpicker.current());

        self.selectedLabel = ko.observable("")
                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                .extend({minLength: {params: 5, message: "La description de l'activité doit faire au moins 5 caractères."}});
        self.selectedLicenceCost = ko.observable(0.0)
                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                .extend({number: {params: true, message: "Ce n'est pas un nombre"}})
                .extend({min: {params: 0, message: "Le coût ne peut pas être négatif"}});
        self.selectedCotisationCost = ko.observable(0.0)
                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                .extend({number: {params: true, message: "Ce n'est pas un nombre"}})
                .extend({min: {params: 0, message: "Le coût ne peut pas être négatif"}});

        self.activityValidator = ko.validatedObservable({
            label: self.selectedLabel,
            licenceCost: self.selectedLicenceCost,
            cotisationCost: self.selectedCotisationCost
        });

        self.toggle = function () {
            self.show(!self.show());
        };

        self.edit = function (activity) {
            self.editing(true);
            self.editingId(activity.id);
            self.yearpicker.current(activity.season);
            self.selectedLabel(activity.label);
            self.selectedLicenceCost(activity.licenceCost);
            self.selectedCotisationCost(activity.cotisationCost);
        };

        self.reset = function () {
            self.yearpicker.current(self.season.yearpicker.current());
            self.selectedLabel("");
            self.selectedLabel.isModified(false);
            self.selectedLicenceCost("");
            self.selectedLicenceCost.isModified(false);
            self.selectedCotisationCost("");
            self.selectedCotisationCost.isModified(false);
            self.active(false);
            self.failure(false);
            self.editing(false);
            self.editingId(-1);
        }

        self.save = function () {

            var activity = {};

            activity.season = _.clone(self.yearpicker.current());
            activity.label = _.clone(self.selectedLabel());
            activity.licenceCost = _.clone(self.selectedLicenceCost() + "");
            activity.cotisationCost = _.clone(self.selectedCotisationCost() + "");

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
                root.ajax.activity.update(self.editingId(), activity, callback);
            } else {
                root.ajax.activity.save(activity, callback);
            }
            self.active(true);

        };

    }
    ;

    function RemoveActivityModal($root, $parent) {

        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;

        self.show = ko.observable(false);

        $('#activityRemoveModal').on('hidden.bs.modal', function () {
            self.show(false);
        });

        self.active = ko.observable(false);

        self.static = {};
        self.static.msg = {};
        self.static.msg.announce = {};
        self.static.msg.announce.begin = "Vous êtes sur le point de supprimer l'activité : <label>";
        self.static.msg.announce.end = "</label>";
        self.static.msg.failure = {};
        self.static.msg.failure.begin = "La suppression de l'activité : <label>";
        self.static.msg.failure.end = "</label> a echoué.";


        self.activity = {};
        self.label = ko.observable("");

        self.toggle = function () {
            self.show(!self.show());
        };

        self.focus = function (activity) {
            self.activity = activity;
            self.label(self.static.msg.announce.begin + self.activity.label + self.static.msg.announce.end);
        };

        self.remove = function () {
            var callback = function (data) {

                if (typeof data !== "undefined") {
                    if (data.result === 1) {
                        var array = self.parent.activities();
                        _.remove(array, function (activity) {
                            if (activity.id === self.activity.id) {
                                return true;
                            }
                            return false;
                        });
                        self.parent.activities(array);
                        self.toggle();
                    } else {
                        self.label(self.static.msg.failure.begin + self.activity.label + self.static.msg.failure.end);
                    }
                } else {
                    self.label(self.static.msg.failure.begin + self.activity.label + self.static.msg.failure.end);
                }


                self.active(false);
            };

            root.ajax.activity.remove(self.activity, callback);
            self.active(true);
        };

    }
    ;

}
;