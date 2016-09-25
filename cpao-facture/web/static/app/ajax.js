/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function Ajax(root) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = root;

    self.static = {};
    self.static.defaultHeader = {};
//    self.static.defaultHeader.url = "http://localhost:10000";
    self.static.defaultHeader.url = "http://109.8.187.63:10000";
    
    self.static.defaultHeader.method = "POST";
    self.static.defaultHeader.contentType = 'text/plain';
    self.static.defaultHeader.dataType = "text";
//    self.static.defaultHeader.crossDomain = true;

    self.static.crudOperation = {};
    self.static.crudOperation.loadAll = {id: 0, rest: "load/all"};
    self.static.crudOperation.loadSingle = {id: 11, rest: "load/single"};
    self.static.crudOperation.loadBySeason = {id: 1, rest: "load/season"};
    self.static.crudOperation.loadByPeople = {id: 5, rest: "load/people"};
    self.static.crudOperation.loadByHome = {id: 6, rest: "load/home"};
    self.static.crudOperation.loadByActivity = {id: 10, rest: "load/activity"};
    self.static.crudOperation.save = {id: 2, rest: "save"};
    self.static.crudOperation.update = {id: 3, rest: "update"};
    self.static.crudOperation.remove = {id: 4, rest: "remove"};
    self.static.crudOperation.generate = {id: 7, rest: "generate/single"};
    self.static.crudOperation.generateAll = {id: 8, rest: "generate/all"};
    self.static.crudOperation.generateGlobal = {id: 9, rest: "generate/global"};
    
    self.static.operations = [
        self.static.crudOperation.loadAll,
        self.static.crudOperation.loadSingle,
        self.static.crudOperation.loadBySeason,
        self.static.crudOperation.loadByPeople,
        self.static.crudOperation.loadByHome,
        self.static.crudOperation.loadByActivity,
        self.static.crudOperation.save,
        self.static.crudOperation.update,
        self.static.crudOperation.remove,
        self.static.crudOperation.generate,
        self.static.crudOperation.generateAll,
        self.static.crudOperation.generateGlobal
    ];
    
    self.flow = [
        
    ];

    self.getCurrentSeason = function (callback) {

        var header = _.cloneDeep(self.static.defaultHeader);
        header.url += "/season/current";

        $.ajax(header).done(function (data) {
            callback(JSON.parse(data));
        });
    };

    self.genericCrud = {};

    self.genericCrud.ajax = function (rest, operation, params, callback) {

        var header = _.cloneDeep(self.static.defaultHeader);
        
        header.url += "/" + _.clone(rest);
        
        self.static.operations.forEach(function(staticOperation){
            if (staticOperation.id === operation) {
                header.url += "/" + staticOperation.rest;
            }
        });
        
        if (typeof params !== "undefined"){
            header.data = JSON.stringify(params);
        }

        $.ajax(header).done(function (data) {
            if (typeof callback !== "undefined") {
                callback(JSON.parse(data));
            }
        });

    };
    
    self.genericCrud.ajaxSecondary = function (rest, operation, params, callback, arrayObs, followup) {

        var header = _.cloneDeep(self.static.defaultHeader);
        
        header.url += "/" + _.clone(rest);
        
        self.static.operations.forEach(function(staticOperation){
            if (staticOperation.id === operation) {
                header.url += "/" + staticOperation.rest;
            }
        });
        
        if (typeof params !== "undefined"){
            header.data = JSON.stringify(params);
        }

        $.ajax(header).done(function (data) {
            if (typeof callback !== "undefined") {
                callback(JSON.parse(data), arrayObs, followup);
            }
        });

    };

}
;
