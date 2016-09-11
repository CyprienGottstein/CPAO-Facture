/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


// This is a simple *viewmodel* - JavaScript that defines the data and behavior of your UI

function AppViewModel() {

    var self = this;
    self.controller = {};
    self.controller.activity = new ActivityController(self);

    self.ajax = new Ajax(self);
    self.model = new Model(self);

}
;

$(window).on('load', function () {
// Activates knockout.js
    var appViewModel = new AppViewModel();

    ko.applyBindingsWithValidation(appViewModel, { messageTemplate : "validationTemplate"});

    var callback = function () {
        appViewModel.controller.activity.loadBySeason();
    };
    appViewModel.ajax.getCurrentSeason(callback);


});