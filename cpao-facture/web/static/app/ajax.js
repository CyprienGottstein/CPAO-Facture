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
    self.static.defaultHeader.url = "http://localhost:10000";
    self.static.defaultHeader.method = "POST";
    self.static.defaultHeader.contentType = 'text/plain';
    self.static.defaultHeader.dataType = "text";
//    self.static.defaultHeader.crossDomain = true;

    self.getCurrentSeason = function (callback) {

        var header = _.cloneDeep(self.static.defaultHeader);
        header.url += "/season/current";

        $.ajax(header).done(function (data) {
            callback(JSON.parse(data));
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
