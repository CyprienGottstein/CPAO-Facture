/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function HomeOverviewController($root) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;

    self.overview = ko.observable(new FinancialOverview(self.root));
    
    self.homeId = ko.observable();
    self.setHomeId = function(id) {
        self.homeId(id);
        self.reload();
    };
    
    self.reload = function () {
        
        var callback = function (data) {
            self.overview().reload(data);
        };
        
        if (typeof self.homeId() !== "undefined"){
            self.root.ajax.genericCrud.ajax(
                "bill",
                self.root.ajax.static.crudOperation.generate.id,
                {id: self.homeId(), season: self.root.controller.season.yearpicker.current()},
                callback
                );
        }
    };
    
    self.root.modalPrimaryDatatype.inputModal.subscribe(self);
    self.root.modalPrimaryDatatype.removeModal.subscribe(self);
    self.root.modalSecondaryDatatype.inputModal.subscribe(self);
    self.root.modalSecondaryDatatype.removeModal.subscribe(self);

    self.reload();

}