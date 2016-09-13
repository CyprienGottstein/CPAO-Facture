/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function YearPicker($root, $parent, year) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;
    self.parent = $parent;

    self.show = ko.computed(function () {
        switch (self.parent.currentMode()) {
            case 0:
                return false;
            case 1:
                return true;
            default:
                return false;
        }
    });

    self.current = ko.observable(year);

    self.increment = function () {
        self.current(self.current() + 1);
    };

    self.decrement = function () {
        self.current(self.current() - 1);
    };

}
;

