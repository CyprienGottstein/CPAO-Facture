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
    
    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = root;
    self.season = ko.observable(2016);
    
    self.setSeason = function (data) {
        self.season(data.season);
    };
    
};
