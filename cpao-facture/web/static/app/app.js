/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


// This is a simple *viewmodel* - JavaScript that defines the data and behavior of your UI

function AppViewModel() {

    var self = this;
    self.model = new Model(self);
    
    self.model = {};
    self.model.genericResource = new GenericResource(self);

    self.controller = {};
    self.controller.season = new SeasonController(self);
    self.controller.activity = new ActivityController(self, self.controller.season);
    self.controller.insurance = new InsuranceController(self, self.controller.season);
    self.controller.resource = new ResourceController(self, self.controller.season);
    
    self.ajax = new Ajax(self);


}
;

$(window).on('load', function () {
// Activates knockout.js
    var appViewModel = new AppViewModel();

    ko.applyBindingsWithValidation(appViewModel, {messageTemplate: "validationTemplate"});

    var callback = function (data) {
        appViewModel.controller.season.setSeason(data.season);
    };
    appViewModel.ajax.getCurrentSeason(callback);

    $('.navbar-lower').affix({
        offset: {top: 50}
    });

});