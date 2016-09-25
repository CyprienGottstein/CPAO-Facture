/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function GlobalOverviewController($root, datatypes) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = $root;

    self.globalOverview = ko.observable(new FinancialOverview(self.root));

    self.homeOverview = ko.observableArray();

    
    
    self.callbackGlobal = function (data) {
        self.globalOverview().reload(data);
    };
    
    self.loadGlobal = function() {
        console.log("reloadGlobal");
        self.root.ajax.genericCrud.ajax(
                "bill",
                self.root.ajax.static.crudOperation.generateGlobal.id,
                {season: self.root.controller.season.yearpicker.current()},
        self.callbackGlobal
                );
    };
    
    self.callbackAll = function (data) {
        var array = self.homeOverview();
        data.forEach(function (overview) {
            var ov = new FinancialOverview(self.root)
            ov.reload(overview);
            array.push(ov);
        });
        self.homeOverview(array);
    };
    
    self.loadall = function() {
        self.root.ajax.genericCrud.ajax(
                "bill",
                self.root.ajax.static.crudOperation.generateAll.id,
                {season: self.root.controller.season.yearpicker.current()},
        self.callbackAll
                );
    };
    
    self.callbackSingle = function (data) {
        var array = self.homeOverview();
        var flag = false;
        array.forEach(function(overview){
            if (overview.id() === data.id) {
                overview.reload(data);
                flag = true;
            }
        });
        
        if (!flag) {
            var ov = new FinancialOverview(self.root);
            ov.reload(data);
            array.push(ov);
        }
        self.homeOverview(array);
    };
    
    self.loadSingle = function(idHome) {
        self.root.ajax.genericCrud.ajax(
                "bill",
                self.root.ajax.static.crudOperation.generate.id,
                {id:idHome, season: self.root.controller.season.yearpicker.current()},
                self.callbackSingle
                );
    };
    
    self.callbackLoadHome = function(data) {
        console.log(data);
        self.loadSingle(data.id);
    };
    
    self.loadHomeByActivity = function(idActivity) {
        self.root.ajax.genericCrud.ajax(
                "home",
                self.root.ajax.static.crudOperation.loadByActivity.id,
                {id:idActivity},
                self.callbackLoadHome
                );
    };
    
    self.loadHomeByPeople = function(idPeople) {
        self.root.ajax.genericCrud.ajax(
                "home",
                self.root.ajax.static.crudOperation.loadByPeople.id,
                {id:idPeople},
                self.callbackLoadHome
                );
    };

    self.load = function () {
        self.loadGlobal();
        self.loadall();
    };

    datatypes().forEach(function (datatypePointer) {

        var datatype = datatypePointer();
        switch (datatype.id) {
            case 0:
                // Activity -- Update all
                datatype.flow.subscribe(function (operation, params) {
                    self.loadall();
                    self.loadGlobal();
                });
                break;
            case 1:
                // Insurance -- Update all
                datatype.flow.subscribe(function (operation, params) {
                    self.loadall();
                    self.loadGlobal();
                });
                break;
            case 2:
                // Home - In case of deletion
                datatype.flow.subscribe(function (operation, params) {
                    if (operation === self.root.ajax.static.crudOperation.remove.id) {
                        var array = self.homeOverview();
                        _.remove(array, function (overview) {
                            if (overview.id() === params.id) { return true; }
                            return false;
                        });
                        self.homeOverview(array);
                        self.loadGlobal();
                    }
                });
                break;
            case 3:
                // People
                // Remove -> reload home + global
                // Update (switching people to an other home) -> reload previous and current home  (previous ?)
                datatype.flow.subscribe(function (operation, params) {
                    if (operation === self.root.ajax.static.crudOperation.remove.id) {
                        var array = self.homeOverview();
                        _.remove(array, function (overview) {
                            if (overview.id() === params.id) { return true; }
                            return false;
                        });
                        self.homeOverview(array);
                        self.loadGlobal();
                    }
                    if (operation === self.root.ajax.static.crudOperation.update.id) {
                        self.loadSingle(params[datatype.rest].previousidHome);
                        self.loadSingle(params[datatype.rest].idHome);
                    }
                });
                break;
            case 4:
                // People Activity - Update home overview -> Reload data from server
                datatype.flow.subscribe(function (operation, params) {
                    if (operation === self.root.ajax.static.crudOperation.remove.id) {
                        self.loadHomeByPeople(params.idPeople);
                    } else {
                        self.loadHomeByPeople(params[datatype.rest].idPeople);
                    }
                    self.loadGlobal();
                });
                break;
            case 5:
                // Payment
                datatype.flow.subscribe(function (operation, params) {
                    if (operation === self.root.ajax.static.crudOperation.remove.id) {
                        self.loadSingle(params.idHome);
                    } else {
                        self.loadSingle(params[datatype.rest].idHome);
                    }
                    self.loadGlobal();
                });
                break;
            case 6:
                // PaymentType - Nothing to do
                break;
        }

    });

//    self.reload();

}