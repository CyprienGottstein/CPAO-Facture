/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function ActivityController(root) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = root;

    self.activities = ko.observableArray();

    self.load = function (data) {
        var array = self.activities();
        data.forEach(function(activity){
            array.push(new ActivityModel(root, self, activity));
        });
        self.activities(array);
    };

    function ActivityModel($root, $parent, data) {
        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;

        self.id = data.id;
        self.season = data.season;
        self.label = data.label;
        self.licenceCost = data.licenceCost;
        self.cotisationCost = data.cotisationCost;

    };

};