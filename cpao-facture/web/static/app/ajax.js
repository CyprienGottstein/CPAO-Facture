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

    self.loadActivitiesBySeason = function (season) {
        
        var data = { season : season };
        
        var header = _.cloneDeep(self.static.defaultHeader);
        header.url += "/activity/load/season";
        header.data = JSON.stringify(data);
//        header.data = data;

        $.ajax(header).done(function (data) {
            root.controller.activity.load(JSON.parse(data));
        });

    }


}
;
