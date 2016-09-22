/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function GenericDatatype(root) {

    // Safe pointer on self for model
    var self = this;
    // Safe pointer on the root of the application to access other controlers
    self.root = root;

    self.static = {};
    self.static.msg = {};
    self.static.msg.announce = {};
    self.static.msg.announce.start = "Vous êtes sur le point de supprimer ";
    self.static.msg.announce.middle = " : <label>";
    self.static.msg.announce.end = "</label>";
    self.static.msg.failure = {};
    self.static.msg.failure.start = "La suppression ";
    self.static.msg.failure.middle = " : <label>";
    self.static.msg.failure.end = "</label> a echoué";

    function BaseDatatype() {
        var self = this;
        self.getDatapack = function (datatype) {
            var datapack = {};
            datapack.resources = ko.observableArray();
            datapack.fields = datatype.fields;
            datapack.toggleModal = datatype.toggleModal;
            return datapack;
        };

    }
    ;

    self.datatype = {};

    self.datatype.activity = ko.observable({});
    self.datatype.insurance = ko.observable({});
    self.datatype.home = ko.observable({});
    self.datatype.peopleActivity = ko.observable({});
    self.datatype.people = ko.observable({});
    self.datatype.payment = ko.observable({});
    self.datatype.paymentType = ko.observable({});

    var base = new BaseDatatype();

    self.datatype.activity(new ActivityDatatype(self.root, self, base, self.static.msg));
    self.datatype.insurance(new InsuranceDatatype(self.root, self, base, self.static.msg));
    self.datatype.home(new HomeDatatype(self.root, self, base, self.static.msg));
    self.datatype.peopleActivity(new PeopleActivityDatatype(self.root, self, base, self.static.msg));
    self.datatype.people(new PeopleDatatype(self.root, self, base, self.static.msg));
    self.datatype.payment(new PaymentDatatype(self.root, self, base, self.static.msg));
    self.datatype.paymentType(new PaymentTypeDatatype(self.root, self, base, self.static.msg));

    self.datatypes = ko.observable([
        self.datatype.activity,
        self.datatype.insurance,
        self.datatype.home,
        self.datatype.people,
        self.datatype.peopleActivity,
        self.datatype.payment,
        self.datatype.paymentType
    ]);

    self.rebindAll = function () {
        self.datatypes().forEach(function (datatype) {
            datatype().pack().resources().forEach(function (resource) {
                resource.rebind();
            });
        });
    };

    self.enhance = function (datatypePointer) {

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

        datatype.toggle = function (data) {
            self.root.controller.season.show(data.loadableBySeason);
        };
        
        datatype.pack = ko.observable();
        datatype.getDatapack = function () {
            var datapack = {};
            datapack.resources = ko.observableArray();
            datapack.fields = datatype.fields;
            datapack.toggleModal = datatype.toggleModal;
            return datapack;
        };
        datatype.pack(datatype.getDatapack());

        datatype.pack().resources.subscribe(function () {
            self.rebindAll();
        });

        datatype.reload = {};
        datatype.reload.callback = function (data) {
            var array = datatype.pack().resources();
            array = [];
            data.forEach(function (resource) {
                var resourceObject = new datatype.model(root, self, resource, datatype, datatype.pack());
                resourceObject.toggleEditModal = function () {
                    if (datatype.modal.inputModal) {
                        datatype.modal.inputModal.edit(resourceObject, resourceObject.datatype);
                    }
                };

                resourceObject.toggleRemoveModal = function () {
                    if (datatype.modal.removeModal) {
                        datatype.modal.removeModal.focus(resourceObject, resourceObject.datatype, resourceObject.pack);
                        datatype.modal.removeModal.toggle(resourceObject.datatype);
                    }
                };
                array.push(resourceObject);
            });
            datatype.pack().resources(array);
        };

        if (datatype.primary || datatype.static) {

            datatype.reload.all = function () {
                root.ajax.genericCrud.ajax(
                        datatype.rest,
                        root.ajax.static.crudOperation.loadAll.id,
                        null,
                        datatype.reload.callback
                        );
            };
            
            datatype.reload.all();

        }

        if (datatype.loadableBySeason) {

            datatype.reload.season = function () {
                root.ajax.genericCrud.ajax(
                        datatype.rest,
                        root.ajax.static.crudOperation.loadBySeason.id,
                        {id: _.clone(self.root.controller.season.yearpicker.current())},
                datatype.reload.callback
                        );
            };

            datatype.updateBySeason = function () {
                datatype.reload.season();
            };

            datatype.updateByMode = function () {
                datatype.reload.season();
            };

            self.root.controller.season.subscribeToSeason(datatype.updateBySeason);
            self.root.controller.season.subscribeToMode(datatype.updateByMode);
        }

        datatype.reload.partialCallback = function (data, pack, followup) {
            var array = pack.resources();
            array = [];
            data.forEach(function (resource) {
                var resourceObject = new datatype.model(root, self, resource, datatype, pack);
                resourceObject.toggleEditModal = function () {
                    if (datatype.modal.inputModal) {
                        datatype.modal.inputModal.edit(resourceObject, resourceObject.datatype);
                    }
                };

                resourceObject.toggleRemoveModal = function () {
                    if (datatype.modal.removeModal) {
                        datatype.modal.removeModal.focus(resourceObject, resourceObject.datatype, resourceObject.pack);
                        datatype.modal.removeModal.toggle(resourceObject.datatype);
                    }
                };
                array.push(resourceObject);
            });
//            arrayObs(array);

            if (typeof followup !== "undefined") {
                followup(array);
            }
        };

        if (datatype.loadableByPeople) {
            datatype.reload.people = function (id, pack, followup) {

                root.ajax.genericCrud.ajaxSecondary(
                        datatype.rest,
                        root.ajax.static.crudOperation.loadByPeople.id,
                        {id: id},
                        datatype.reload.partialCallback,
                        pack,
                        followup
                        );
            };
        }

        if (datatype.loadableByHome) {
            datatype.reload.home = function (id, pack, followup) {

                root.ajax.genericCrud.ajaxSecondary(
                        datatype.rest,
                        root.ajax.static.crudOperation.loadByHome.id,
                        {id: id},
                        datatype.reload.partialCallback,
                        pack,
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
        self.enhance(datatype);
    });

    self.bindPrimaryModal = function(inputModal, removeModal) {
        self.datatypes().forEach(function(datatype){
            if (datatype().primary) {
                datatype().setInputModal(inputModal);
                datatype().setRemoveModal(removeModal);
            }
        });
    };
    
    self.bindSecondaryModal = function(inputModal, removeModal) {
        self.datatypes().forEach(function(datatype){
            if (!datatype().primary) {
                datatype().setInputModal(inputModal);
                datatype().setRemoveModal(removeModal);
            }
        });
    };

}