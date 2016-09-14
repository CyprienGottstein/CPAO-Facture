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

    self.static.datatype = {};
    self.static.datatype.activity = {id: 0, rest: "activity"};
    self.static.datatype.insurance = {id: 1, rest: "insurance"};
    self.static.datatypes = [
        self.static.datatype.activity,
        self.static.datatype.insurance
    ];

    self.static.crudOperation = {};
    self.static.crudOperation.loadAll = {id: 0, rest: "load/all"};
    self.static.crudOperation.loadBySeason = {id: 1, rest: "load/season"};
    self.static.crudOperation.save = {id: 2, rest: "save"};
    self.static.crudOperation.update = {id: 3, rest: "update"};
    self.static.crudOperation.remove = {id: 4, rest: "remove"};
    self.static.operations = [
        self.static.crudOperation.loadAll,
        self.static.crudOperation.loadBySeason,
        self.static.crudOperation.save,
        self.static.crudOperation.update,
        self.static.crudOperation.remove
    ];

    self.getCurrentSeason = function (callback) {

        var header = _.cloneDeep(self.static.defaultHeader);
        header.url += "/season/current";

        $.ajax(header).done(function (data) {
            callback(JSON.parse(data));
        });
    };

    self.genericCrud = {};

    self.genericCrud.ajax = function (datatype, operation, params, callback) {

        var header = _.cloneDeep(self.static.defaultHeader);
        
        self.static.datatypes.forEach(function(staticDatatype){
            if (staticDatatype.id === datatype) {
                header.url += "/" + staticDatatype.rest;
            }
        });
        
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

    self.activity = {};

    self.activity.loadBySeason = function (season, callback) {

        var data = {season: season};

        var header = _.cloneDeep(self.static.defaultHeader);
        header.url += "/activity/load/season";
        header.data = JSON.stringify(data);

        $.ajax(header).done(function (data) {
            if (typeof callback !== "undefined") {
                callback(JSON.parse(data));
            }
        });

    };

    self.activity.loadAll = function (callback) {

        var header = _.cloneDeep(self.static.defaultHeader);
        header.url += "/activity/load/all";

        $.ajax(header).done(function (data) {
            if (typeof callback !== "undefined") {
                callback(JSON.parse(data));
            }
        });

    };

    self.activity.save = function (activity, callback) {

        var data = {activity: activity};

        var header = _.cloneDeep(self.static.defaultHeader);
        header.url += "/activity/save";
        header.data = JSON.stringify(data);

        $.ajax(header).always(function (data) {
            if (typeof callback !== "undefined") {
                callback(JSON.parse(data));
            }
        });

    };

    self.activity.remove = function (activity, callback) {

        var data = {id: activity.id};

        var header = _.cloneDeep(self.static.defaultHeader);
        header.url += "/activity/remove";
        header.data = JSON.stringify(data);

        $.ajax(header).always(function (data) {
            if (typeof callback !== "undefined") {
                callback(JSON.parse(data));
            }
        });

    };

    self.activity.update = function (id, activity, callback) {
        var data = {
            id: id,
            activity: activity
        };

        var header = _.cloneDeep(self.static.defaultHeader);
        header.url += "/activity/update";
        header.data = JSON.stringify(data);

        $.ajax(header).always(function (data) {
            if (typeof callback !== "undefined") {
                callback(JSON.parse(data));
            }
        });

    }


    self.insurance = {};

    self.insurance.loadBySeason = function (season, callback) {

        var data = {season: season};

        var header = _.cloneDeep(self.static.defaultHeader);
        header.url += "/insurance/load/season";
        header.data = JSON.stringify(data);

        $.ajax(header).done(function (data) {
            if (typeof callback !== "undefined") {
                callback(JSON.parse(data));
            }
        });

    };

    self.insurance.loadAll = function (callback) {

        var header = _.cloneDeep(self.static.defaultHeader);
        header.url += "/insurance/load/all";

        $.ajax(header).done(function (data) {
            if (typeof callback !== "undefined") {
                callback(JSON.parse(data));
            }
        });

    };

    self.insurance.save = function (insurance, callback) {

        var data = {insurance: insurance};

        var header = _.cloneDeep(self.static.defaultHeader);
        header.url += "/insurance/save";
        header.data = JSON.stringify(data);

        $.ajax(header).always(function (data) {
            if (typeof callback !== "undefined") {
                callback(JSON.parse(data));
            }
        });

    };

    self.insurance.remove = function (insurance, callback) {

        var data = {id: insurance.id};

        var header = _.cloneDeep(self.static.defaultHeader);
        header.url += "/insurance/remove";
        header.data = JSON.stringify(data);

        $.ajax(header).always(function (data) {
            if (typeof callback !== "undefined") {
                callback(JSON.parse(data));
            }
        });

    };

    self.insurance.update = function (id, insurance, callback) {
        var data = {
            id: id,
            insurance: insurance
        };

        var header = _.cloneDeep(self.static.defaultHeader);
        header.url += "/insurance/update";
        header.data = JSON.stringify(data);

        $.ajax(header).always(function (data) {
            if (typeof callback !== "undefined") {
                callback(JSON.parse(data));
            }
        });
    }

}
;
