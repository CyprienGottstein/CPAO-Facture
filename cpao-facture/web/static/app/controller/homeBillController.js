/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function HomeBillController($root, dependencies) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;

    self.total = ko.observable(0);
    self.totalDeposit = ko.observable(0);
    self.totalSolded = ko.observable(0);
    self.totalMissing = ko.observable(0);
    
    self.reload = function () {
        
        var callback = function (data) {
            self.total(data.totalCost);
            self.totalDeposit(data.totalDeposit);
            self.totalSolded(data.totalSolded);
            self.totalSolded.valueHasMutated();
            self.totalMissing(data.totalMissing);
            
        };

        self.root.ajax.genericCrud.ajax(
                "bill",
                self.root.ajax.static.crudOperation.generate.id,
                {id: 0, season: 2016},
        callback
                );
    };
    
    self.root.modalPrimaryDatatype.inputModal.subscribe(self);
    self.root.modalPrimaryDatatype.removeModal.subscribe(self);
    self.root.modalSecondaryDatatype.inputModal.subscribe(self);
    self.root.modalSecondaryDatatype.removeModal.subscribe(self);
    
//    dependencies.forEach(function(dependency){
//        dependency().subscribe(self);
//    });

    self.reload();

}