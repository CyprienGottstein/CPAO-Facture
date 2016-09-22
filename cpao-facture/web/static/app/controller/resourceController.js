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
            datatype().resources().forEach(function (resource) {
                resource.rebind();
            });
        });
    };

    self.showSeasonPicker = ko.observable(false);

    self.enhancePrimaryDatatype = function (datatypePointer) {

        var datatype = datatypePointer();
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
                        {id: _.clone(self.season.yearpicker.current())},
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

        datatypePointer(datatype);
    };

//    self.enhanceSecondaryDatatype = function (datatypePointer) {
//
//        var datatype = datatypePointer();
//        datatype.enhance = function (inputResourceModal, removeResourceModal) {
//            datatype.toggleModal = function () {
//                inputResourceModal.toggle(datatype);
//            };
//
//            if (datatype.loadableByPeople) {
//                datatype.loadByPeople = function (followup) {
//
//                    var callback = function (data) {
//                        var array = datatype.resources();
//                        array = [];
//                        data.forEach(function (resource) {
//                            var resourceObject = new datatype.model(root, self, resource, datatype);
//                            resourceObject.toggleEditModal = function () {
//                                inputResourceModal.edit(resourceObject, resourceObject.datatype);
//                            };
//
//                            resourceObject.toggleRemoveModal = function () {
//                                removeResourceModal.focus(resourceObject, resourceObject.datatype);
//                                removeResourceModal.toggle(resourceObject.datatype);
//                            };
//                            array.push(resourceObject);
//                        });
//                        datatype.resources(array);
//
//                        if (typeof followup !== "undefined") {
//                            followup();
//                        }
//                    };
//
//                    root.ajax.genericCrud.ajax(
//                            datatype.rest,
//                            root.ajax.static.crudOperation.loadByPeople.id,
//                            datatype.params,
//                            callback
//                            );
//                };
//
//                datatype.reloadingFunction = datatype.loadByPeople;
//            }
//
//            if (datatype.loadableByHome) {
//                datatype.loadByHome = function (followup) {
//
//                    var callback = function (data) {
//                        var array = datatype.resources();
//                        array = [];
//                        data.forEach(function (resource) {
//                            var resourceObject = new datatype.model(root, self, resource, datatype);
//                            resourceObject.toggleEditModal = function () {
//                                inputResourceModal.edit(resourceObject, resourceObject.datatype);
//                            };
//
//                            resourceObject.toggleRemoveModal = function () {
//                                removeResourceModal.focus(resourceObject, resourceObject.datatype);
//                                removeResourceModal.toggle(resourceObject.datatype);
//                            };
//                            array.push(resourceObject);
//                        });
//                        datatype.resources(array);
//
//                        if (typeof followup !== "undefined") {
//                            followup();
//                        }
//                    };
//
//                    root.ajax.genericCrud.ajax(
//                            datatype.rest,
//                            root.ajax.static.crudOperation.loadByHome.id,
//                            datatype.params,
//                            callback
//                            );
//                };
//
//                datatype.reloadingFunction = datatype.loadByHome;
//            }
//        };
//
//        datatype.toggleModal = function () {
//
//        };
//
//        datatype.setId = function (id) {
//            datatype.params = {};
//            datatype.params.id = id;
//        };
//
//        datatype.reloadingFunction = function () {
//            console.log("CALLED BUT USELESS");
//        };
//
//        datatype.reload = function (followup) {
//            if (typeof followup !== "undefined") {
//                datatype.reloadingFunction(followup);
//            }
//            datatype.reloadingFunction();
//        };
//
//        datatypePointer(datatype);
//
//    };

    self.enhanceSecondaryDatatype = function (datatypePointer) {

        var datatype = datatypePointer();

        datatype.modal = {};
        datatype.setInputModal = function (modal) {
            datatype.modal.inputModal = modal;
        };
        datatype.setRemoveModal = function (modal) {
            datatype.modal.removeModal = modal;
        };

        datatype.toggleModal = function () {
            if (datatype.modal.inputModal) {
                datatype.modal.inputModal.toggle(datatype);
            }
        };

        datatype.callback = function (data, arrayObs, followup) {
            var array = arrayObs();
            array = [];
            data.forEach(function (resource) {
                var resourceObject = new datatype.model(root, self, resource, datatype);
                resourceObject.toggleEditModal = function () {
                    if (datatype.modal.inputModal) {
                        datatype.modal.inputModal.edit(resourceObject, resourceObject.datatype);
                    }
                };

                resourceObject.toggleRemoveModal = function () {
                    if (datatype.modal.removeModal) {
                        datatype.modal.removeModal.focus(resourceObject, resourceObject.datatype);
                        datatype.modal.removeModal.toggle(resourceObject.datatype);
                    }
                };
                array.push(resourceObject);
            });
            arrayObs(array);

            if (typeof followup !== "undefined") {
                followup();
            }
        };
        
        if (datatype.loadableByPeople) {
            datatype.loadByPeople = function (arrayObs, followup) {

                root.ajax.genericCrud.ajaxSecondary(
                        datatype.rest,
                        root.ajax.static.crudOperation.loadByPeople.id,
                        datatype.params,
                        callback,
                        arrayObs,
                        followup
                        );
            };
        }

        if (datatype.loadableByHome) {
            datatype.loadByHome = function (arrayObs, followup) {

                root.ajax.genericCrud.ajaxSecondary(
                        datatype.rest,
                        root.ajax.static.crudOperation.loadByHome.id,
                        datatype.params,
                        callback,
                        arrayObs,
                        followup
                        );
            };
        }

        datatype.setId = function (id) {
            datatype.params = {};
            datatype.params.id = id;
        };

        datatypePointer(datatype);

    };

    self.datatypes().forEach(function (datatype) {

        if (datatype().primary) {
            self.enhancePrimaryDatatype(datatype);
        } else {
            self.enhanceSecondaryDatatype(datatype);
        }

    });

}
;