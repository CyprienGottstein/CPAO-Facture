/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function SeasonController(root) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = root;

    self.static = {};
    self.static.mode = {};
    self.static.mode.allSeason = {
        id: 0,
        label: "Toutes les saisons"
    };
    self.static.mode.oneSeason = {
        id: 1,
        label: "Saison unique"
    };

    self.modes = ko.observableArray([
        new SeasonModeModel(self.root, self, self.static.mode.allSeason),
        new SeasonModeModel(self.root, self, self.static.mode.oneSeason)
    ]);

    // 0 = All seasons / 1 = One season at a time
    self.currentMode = ko.observable(1);
    self.currentModeLabel = ko.computed(function () {
        switch (self.currentMode()) {
            case 0:
                return self.static.mode.allSeason.label + ' <span class="caret"></span>';
            case 1:
                return self.static.mode.oneSeason.label + ' <span class="caret"></span>';
            default:
                return 'Saison <span class="caret"></span>';
        }
    });

    self.yearpicker = new YearPicker(self.root, self, 2015);
    
    self.subscribeToSeason = function (callback){
        self.yearpicker.current.subscribe(callback);
    };
    
    self.subscribeToMode = function (callback){
        self.currentMode.subscribe(callback);
    };
    
    self.setSeason = function(season) {
        self.yearpicker.current(season);
    }

    function SeasonModeModel($root, $parent, data) {

        // Safe pointer on self for model
        var self = this;
        // Safe pointer on the root of the application to access other controlers
        self.root = $root;
        self.parent = $parent;
        self.id = data.id;
        self.label = data.label;

        self.select = function () {
            self.parent.currentMode(self.id);
        };

    }
    ;

}
;
