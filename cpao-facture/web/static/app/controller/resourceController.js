/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function ResourceController(root, seasonController, datatypes, inputModal, removeModal) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = root;
    self.season = seasonController;

    self.modal = inputModal;
    self.removeModal = removeModal;

    self.datatypes = ko.observableArray(datatypes);
    self.rebindAll = function () {
        self.datatypes().forEach(function (datatype) {
            datatype.resources().forEach(function (resource) {
                resource.rebind();
            });
        });
    };

    self.showSeasonPicker = ko.observable(false);

    self.enhancePrimaryDatatype = function (datatype) {
        datatype.loadAll = function () {

            var callback = function (data) {
                var array = datatype.resources();
                array = [];
                data.forEach(function (resource) {
                    var resourceObject = new datatype.model(root, self, resource, datatype);
                    resourceObject.toggleEditModal = function () {
                        self.modal.edit(resourceObject, resourceObject.datatype);
                    };

                    resourceObject.toggleRemoveModal = function () {
                        self.removeModal.focus(resourceObject, resourceObject.datatype);
                        self.removeModal.toggle(resourceObject.datatype);
                    };
                    array.push(resourceObject);
                });
                datatype.resources(array);
            };

            root.ajax.genericCrud.ajax(
                    datatype.rest,
                    root.ajax.static.crudOperation.loadAll.id,
                    null,
                    callback
                    );

        };

        datatype.toggleModal = function (datatype) {
            self.modal.toggle(datatype);
        };

        datatype.toggle = function (data) {
            self.season.show(data.loadableBySeason);
        };

        datatype.resources.subscribe(function () {
            self.rebindAll();
        });

        if (datatype.loadableBySeason) {

            datatype.updateBySeason = function () {
                datatype.reload();
            };

            datatype.updateByMode = function () {
                datatype.reload();
            };

            datatype.loadBySeason = function () {

                var callback = function (data) {
                    var array = datatype.resources();
                    array = [];
                    data.forEach(function (resource) {
                        var resourceObject = new datatype.model(root, self, resource, datatype);
                        resourceObject.toggleEditModal = function () {
                            self.modal.edit(resourceObject, resourceObject.datatype);
//                            self.modal.toggle(resourceObject.datatype);
                        };

                        resourceObject.toggleRemoveModal = function () {
                            self.removeModal.focus(resourceObject, resourceObject.datatype);
                            self.removeModal.toggle(resourceObject.datatype);
                        };
                        array.push(resourceObject);
                    });
                    datatype.resources(array);
                };

                root.ajax.genericCrud.ajax(
                        datatype.rest,
                        root.ajax.static.crudOperation.loadBySeason.id,
                        {season: _.clone(self.season.yearpicker.current())},
                callback
                        );
            };

            datatype.reload = function () {
                if (self.season.currentMode() === 1) {
                    datatype.loadBySeason();
                } else {
                    datatype.loadAll();
                }
            };

            self.root.controller.season.subscribeToSeason(datatype.updateBySeason);
            self.root.controller.season.subscribeToMode(datatype.updateByMode);
        } else {

            datatype.reload = function () {
                datatype.loadAll();
            };

            datatype.reload();
        }
    };

    self.enhanceSecondaryDatatype = function (datatype) {

        datatype.enhance = function (inputResourceModal, removeResourceModal) {
            datatype.toggleModal = function () {
                inputResourceModal.toggle(datatype);
            };
            
            datatype.setId = function(id) {
                datatype.params = {};
                datatype.params.people = id;
            };

            if (datatype.loadableByPeople) {
                datatype.loadByPeople = function () {

                    var callback = function (data) {
                        var array = datatype.resources();
                        array = [];
                        data.forEach(function (resource) {
                            var resourceObject = new datatype.model(root, self, resource, datatype);
                            resourceObject.toggleEditModal = function () {
                                inputResourceModal.edit(resourceObject, resourceObject.datatype);
                            };

                            resourceObject.toggleRemoveModal = function () {
                                removeResourceModal.focus(resourceObject, resourceObject.datatype);
                                removeResourceModal.toggle(resourceObject.datatype);
                            };
                            array.push(resourceObject);
                        });
                        datatype.resources(array);
                    };

//                    var params = {};
//                    params.people = people.id();

                    root.ajax.genericCrud.ajax(
                            datatype.rest,
                            root.ajax.static.crudOperation.loadByPeople.id,
                            datatype.params,
                            callback
                            );
                };

                datatype.reloadingFunction = datatype.loadByPeople;
            }
        };

        datatype.toggleModal = function () {

        };

        datatype.reloadingFunction = function () {

        };

        datatype.reload = function () {
            datatype.reloadingFunction();
        };

    };

    self.datatypes().forEach(function (datatype) {

        if (datatype.primary) {
            self.enhancePrimaryDatatype(datatype);
        } else {
            self.enhanceSecondaryDatatype(datatype);
        }

    });

}
;