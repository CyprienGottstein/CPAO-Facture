/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Model class
 * 
 * @param AppViewModel root, the root of the client application
 * @returns void
 */
function Model(root) {
    
    console.log(root);
    
    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = root;
    
    self.dummy = ko.observable({ test : 'AAA'});
    self.activities = ko.observableArray();
    
    console.log(self.activities());
    
    var array = self.activities();
    console.log(array);
    array.push({ test : 'AAA'});
    array.push(self.dummy());
    array.push(self.dummy());
    self.activities(array);
    
    console.log(self.activities());
    
};
