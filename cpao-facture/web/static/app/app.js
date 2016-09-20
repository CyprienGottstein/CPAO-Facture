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

    self.modalPrimaryDatatype = {};
    self.modalPrimaryDatatype.inputModal = new InputResourceModal(self, self.model.genericResource.datatypes, self.controller.season, {
        idModal : "primaryInputResourceModal",
        large : true
    });
    self.modalPrimaryDatatype.removeModal = new RemoveResourceModal(self, self.model.genericResource.datatypes, "primaryRemoveResourceModal");
    
    self.modalSecondaryDatatype = {};
    self.modalSecondaryDatatype.inputModal = new InputResourceModal(self, self.model.genericResource.datatypes, self.controller.season,{
        idModal : "secondaryInputResourceModal",
        large : false
    });
    self.modalSecondaryDatatype.removeModal = new RemoveResourceModal(self, self.model.genericResource.datatypes, "secondaryRemoveResourceModal");
    
    self.modals = [
        self.modalPrimaryDatatype,
        self.modalSecondaryDatatype
    ];

    var allDatatypes = [
        self.model.genericResource.datatype.activity,
        self.model.genericResource.datatype.insurance,
        self.model.genericResource.datatype.home,
        self.model.genericResource.datatype.people,
        self.model.genericResource.datatype.peopleActivity
    ];
    
    self.controller.resource = new ResourceController(self,
            self.controller.season,
            allDatatypes,
            self.modalPrimaryDatatype.inputModal,
            self.modalPrimaryDatatype.removeModal);


}
;

$(window).on('load', function () {
// Activates knockout.js
    var appViewModel = new AppViewModel();

    var templates = [
        'template/resource.html',
        'template/input.html',
        'template/inputModal.html',
        'template/removeModal.html',
        'template/datatable.html'
    ];

    var n = 0;
    
    var template_holder = $('#template_holder');
    
    var call = function () {
        $.get(templates[n], callback);
    }

    var callback = function callback (content) {
        
        template_holder.append(content);
        
        n++;
        if (n < templates.length) {
            $.get(templates[n], call);
        } else {
            //knockout binding goes here
            ko.applyBindingsWithValidation(appViewModel, {messageTemplate: "validationTemplate"});

            var callback = function (data) {
                appViewModel.controller.season.setSeason(data.season);
            };
            appViewModel.ajax.getCurrentSeason(callback);
            
            appViewModel.modals.forEach(function(group){
                group.inputModal.triggerCloseListener();
                group.removeModal.triggerCloseListener();
            });

            $('.navbar-lower').affix({
                offset: {top: 50}
            });
        }
    };
    
    $('#template_holder').load(templates[n], callback);

});