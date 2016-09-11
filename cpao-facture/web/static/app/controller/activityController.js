/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function ActivityController(root) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = root;

    self.activities = ko.observableArray();
    self.modal = new NewActivityModal(root, self);
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

        root.ajax.activity.loadBySeason(root.model.season(), callback);
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

    function NewActivityModal($root, $parent) {

        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;

        self.editing = ko.observable(false);
        self.editingId = ko.observable(-1);

        self.show = ko.observable(false);

        $('#activityModal').on('hidden.bs.modal', function () {
            self.show(false);
        });

        self.active = ko.observable(false);
        self.failure = ko.observable(false);

        self.selectedSeason = ko.observable(2016)
                .extend({required: {params: true, message: "Ce champ est obligatoire"}})
                .extend({number: {params: true, message: "La saison doit être une année."}});
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
            season: self.selectedSeason,
            label: self.selectedLabel,
            licenceCost: self.selectedLicenceCost,
            cotisationCost: self.selectedCotisationCost
        });

        self.toggle = function () {
            self.show(!self.show());
            if (!self.show()) {
                self.reset();
            }
        };

        self.edit = function (activity) {
            self.editing(true);
            self.editingId(activity.id);
            self.selectedSeason(activity.season);
            self.selectedLabel(activity.label);
            self.selectedLicenceCost(activity.licenceCost);
            self.selectedCotisationCost(activity.cotisationCost);
        };

        self.reset = function () {
            self.selectedSeason(2016);
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

            activity.season = _.clone(self.selectedSeason());
            activity.label = _.clone(self.selectedLabel());
            activity.licenceCost = _.clone(self.selectedLicenceCost() + "");
            activity.cotisationCost = _.clone(self.selectedCotisationCost() + "");

            var callback = function (data) {

                if (typeof data !== "undefined") {
                    if (data.result === 1) {
                        $parent.loadBySeason();
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