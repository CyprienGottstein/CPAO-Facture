/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function FinancialOverview($root) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;

    self.id = ko.observable(0);
    self.home = ko.observable("");
    self.total = ko.observable(0);
    self.totalDeposit = ko.observable(0);
    self.totalSolded = ko.observable(0);
    self.totalMissing = ko.observable(0);
    
    // 0: No deposit
    // 1: Partial deposit
    // 2: Full deposit
    // 3: Full deposit + Full solded
    self.printingMode = ko.observable(0);

    self.reload = function (data) {
        self.id(data.id);
        self.home(data.home);
        self.total(data.totalCost);
        self.totalDeposit(data.totalDeposit);
        self.totalSolded(data.totalSolded);
        self.totalMissing(data.totalMissing);
        
        if (self.totalSolded() === self.total()){
            self.printingMode(3);
        } else {
            if (self.totalMissing() === 0){
                self.printingMode(2);
            } else {
                if (self.totalDeposit() !== 0) {
                    self.printingMode(1);
                } else {
                    self.printingMode(0);
                }
            }
        }

    };
    
}

