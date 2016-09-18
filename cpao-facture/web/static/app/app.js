/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


// This is a simple *viewmodel* - JavaScript that defines the data and behavior of your UI

function AppViewModel() {

    var self = this;
    self.model = new Model(self);

    self.ajax = new Ajax(self);

    self.model = {};
    self.model.genericResource = new GenericResource(self);

    self.controller = {};
    self.controller.season = new SeasonController(self);

    self.modal = {};
    self.modal.inputResource = new InputResourceModal(self, self.model.genericResource.datatypes, self.controller.season);
    self.modal.removeResource = new RemoveResourceModal(self, self.model.genericResource.datatypes);

//    var activityControllerDatatypes = [
//        self.model.genericResource.datatype.activity,
//        self.model.genericResource.datatype.insurance
//    ];
//    self.controller.activity = new ResourceController(self,
//            self.controller.season,
//            activityControllerDatatypes,
//            self.modal.inputResource,
//            self.modal.removeResource);
//
//    var adherentControllerDatatypes = [
//        self.model.genericResource.datatype.home,
//        self.model.genericResource.datatype.people
//    ];
//    self.controller.adherent = new ResourceController(self,
//            self.controller.season,
//            adherentControllerDatatypes,
//            self.modal.inputResource,
//            self.modal.removeResource);

    var allDatatypes = [
        self.model.genericResource.datatype.activity,
        self.model.genericResource.datatype.insurance,
        self.model.genericResource.datatype.home,
        self.model.genericResource.datatype.people
    ];
    self.controller.resource = new ResourceController(self,
            self.controller.season,
            allDatatypes,
            self.modal.inputResource,
            self.modal.removeResource);


}
;

$(window).on('load', function () {
// Activates knockout.js
    var appViewModel = new AppViewModel();

    var templates = [
        'template/activity.html',
        'template/adherent.html'
    ];

    var n = 0;
    
    var template_holder = $('#template_holder');
    
    var call = function () {
        $.get(templates[n], callback);
    }

    var callback = function callback (content) {
        
        template_holder.append(content);
        
        n++;
        console.log("n : " + n);
        if (n < templates.length) {
            $.get(templates[n], call);
        } else {
            //knockout binding goes here
            ko.applyBindingsWithValidation(appViewModel, {messageTemplate: "validationTemplate"});

            var callback = function (data) {
                appViewModel.controller.season.setSeason(data.season);
            };
            appViewModel.ajax.getCurrentSeason(callback);

            $('.navbar-lower').affix({
                offset: {top: 50}
            });
        }
    };
    
    $('#template_holder').load(templates[n], callback);

});