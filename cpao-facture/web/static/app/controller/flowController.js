/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function FlowController($root, datatype) {
    
    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;
    
    self.datatype = datatype;
    
    self.subscriber = ko.observableArray();
    
    self.subscribe = function(callback) {
        var array = self.subscriber();
        array.push(callback);
        self.subscriber(array);
    };
    
    self.trigger = function(operation, params) {
        self.subscriber().forEach(function(callback){
            callback(operation, params);
        });
    };
    
};
