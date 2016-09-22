/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function HomeBillTemplate($root) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;

    self.total = ko.observable(120.0);
    self.totalCashed = ko.observable(80.0);
    self.totalSolded = ko.observable(80.0);
    self.totalMissing = ko.observable(40.0);

    var callback = function(data){
        console.log(data);
        self.total(data.totalCost);
//        self.total(data)
        
    };

    self.root.ajax.genericCrud.ajax(
            "bill",
            self.root.ajax.static.crudOperation.generate.id,
            {id: 0, season: 2016},
            callback
            );

}