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
    
    self.controller = {};
    self.controller.season = new SeasonController(self);
    self.controller.inputRessourceController = new InputResourceController(self, true);

    self.model = {};
    self.model.datatypeRoot = new GenericDatatype(self);
    self.controller.overview = new GlobalOverviewController(self, self.model.datatypeRoot.datatypes);

    self.modalPrimaryDatatype = {};
    self.modalPrimaryDatatype.inputModal = new InputResourceModal(self, self.model.datatypeRoot.datatypes, self.controller.season,{
        idModal : "primaryInputResourceModal",
        large : true
    },
        true);
    self.modalPrimaryDatatype.inputModal.setInputController(self.controller.inputRessourceController);
    self.modalPrimaryDatatype.removeModal = new RemoveResourceModal(self, self.model.datatypeRoot.datatypes, "primaryRemoveResourceModal");
    
    self.modalSecondaryDatatype = {};
    self.modalSecondaryDatatype.inputModal = new InputResourceModal(self, self.model.datatypeRoot.datatypes, self.controller.season,{
        idModal : "secondaryInputResourceModal",
        large : false
    },
        false);
    self.modalSecondaryDatatype.removeModal = new RemoveResourceModal(self, self.model.datatypeRoot.datatypes, "secondaryRemoveResourceModal");
    
    self.modals = [
        self.modalPrimaryDatatype,
        self.modalSecondaryDatatype
    ];

    self.model.datatypeRoot.bindPrimaryModal(self.modalPrimaryDatatype.inputModal, self.modalPrimaryDatatype.removeModal);
    self.model.datatypeRoot.bindSecondaryModal(self.modalSecondaryDatatype.inputModal, self.modalSecondaryDatatype.removeModal);


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
        'template/datatable.html',
        'template/datatableRecursive.html',
        'template/homeOverview.html',
        'template/globalOverview.html'
    ];

    var n = 0;
    
    var template_holder = $('#template_holder');

    var call = null;

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
                appViewModel.controller.overview.load();
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
    
    var call = function (data) {
        callback(data);
    };
    
    $('#template_holder').load(templates[n], callback);

});