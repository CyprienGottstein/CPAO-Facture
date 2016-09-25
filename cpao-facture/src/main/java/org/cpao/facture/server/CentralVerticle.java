/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.cpao.facture.server.model.People;

/**
 *
 * @author Cyprien
 */
public class CentralVerticle extends AbstractVerticle {

    @Override
    public void start() {

        final EventBus bus = vertx.eventBus();
        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-activity-load-bySeason", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {

                bus.send("org.cpao.facture.server.dao.activity.ActivityDaoVerticle-load-bySeason", message.body(), new Handler<AsyncResult<Message<JsonArray>>>() {

                    @Override
                    public void handle(AsyncResult<Message<JsonArray>> result) {
                        message.reply(result.result().body());
                    }
                });

            }
        });
        
        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-activity-load-single", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {

                bus.send("org.cpao.facture.server.dao.activity.ActivityDaoVerticle-load-single", message.body(), new Handler<AsyncResult<Message<JsonObject>>>() {

                    @Override
                    public void handle(AsyncResult<Message<JsonObject>> result) {
                        message.reply(result.result().body());
                    }
                });

            }
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-activity-load-all", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {

                bus.send("org.cpao.facture.server.dao.activity.ActivityDaoVerticle-load-all", null, new Handler<AsyncResult<Message<JsonArray>>>() {

                    @Override
                    public void handle(AsyncResult<Message<JsonArray>> result) {
                        message.reply(result.result().body());
                    }
                });

            }
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-activity-save", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {

                bus.send("org.cpao.facture.server.dao.activity.ActivityDaoVerticle-save", message.body(), new Handler<AsyncResult<Message<JsonObject>>>() {

                    @Override
                    public void handle(AsyncResult<Message<JsonObject>> result) {
                        message.reply(result.result().body());
                    }
                });

            }
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-activity-remove", new Handler<Message<Integer>>() {
            @Override
            public void handle(Message<Integer> message) {

                bus.send("org.cpao.facture.server.dao.activity.ActivityDaoVerticle-remove", message.body(), new Handler<AsyncResult<Message<JsonObject>>>() {

                    @Override
                    public void handle(AsyncResult<Message<JsonObject>> result) {
                        message.reply(result.result().body());
                    }
                });

            }
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-activity-update", new Handler<Message<Integer>>() {
            @Override
            public void handle(Message<Integer> message) {

                bus.send("org.cpao.facture.server.dao.activity.ActivityDaoVerticle-update", message.body(), new Handler<AsyncResult<Message<JsonObject>>>() {

                    @Override
                    public void handle(AsyncResult<Message<JsonObject>> result) {
                        message.reply(result.result().body());
                    }
                });

            }
        });

        /**
         * Insurance Redirection
         */
        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-insurance-load-bySeason", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.insurance.InsuranceDaoVerticle-load-bySeason", message.body(), (AsyncResult<Message<JsonArray>> result) -> {
                message.reply(result.result().body());
            });
        });
        
        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-insurance-load-single", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.insurance.InsuranceDaoVerticle-load-single", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-insurance-load-all", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.insurance.InsuranceDaoVerticle-load-all", null, (AsyncResult<Message<JsonArray>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-insurance-save", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.insurance.InsuranceDaoVerticle-save", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-insurance-remove", (Message<Integer> message) -> {
            bus.send("org.cpao.facture.server.dao.insurance.InsuranceDaoVerticle-remove", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-insurance-update", (Message<Integer> message) -> {
            bus.send("org.cpao.facture.server.dao.insurance.InsuranceDaoVerticle-update", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        /**
         * People Redirection
         */
        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-people-load-all", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.people.PeopleDaoVerticle-load-all", null, (AsyncResult<Message<JsonArray>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-people-load-home", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.people.PeopleDaoVerticle-load-home", message.body(), (AsyncResult<Message<JsonArray>> result) -> {
                message.reply(result.result().body());
            });
        });
        
        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-people-load-single", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.people.PeopleDaoVerticle-load-single", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-people-save", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.people.PeopleDaoVerticle-save", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-people-remove", (Message<Integer> message) -> {
            bus.send("org.cpao.facture.server.dao.people.PeopleDaoVerticle-remove", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-people-update", (Message<Integer> message) -> {
            bus.send("org.cpao.facture.server.dao.people.PeopleDaoVerticle-update", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        /**
         * Home Redirection
         */
        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-home-load-all", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.home.HomeDaoVerticle-load-all", null, (AsyncResult<Message<JsonArray>> result) -> {
                message.reply(result.result().body());
            });
        });
        
        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-home-load-activity", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.home.HomeDaoVerticle-load-activity", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });
        
        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-home-load-people", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.home.HomeDaoVerticle-load-people", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });
        
        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-home-load-single", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.home.HomeDaoVerticle-load-single", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-home-save", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.home.HomeDaoVerticle-save", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-home-remove", (Message<Integer> message) -> {
            bus.send("org.cpao.facture.server.dao.home.HomeDaoVerticle-remove", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-home-update", (Message<Integer> message) -> {
            bus.send("org.cpao.facture.server.dao.home.HomeDaoVerticle-update", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        /**
         * People Activity Redirection
         */
        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-peopleActivity-load-people", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.peopleactivity.PeopleActivityDaoVerticle-load-people", message.body(), (AsyncResult<Message<JsonArray>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-peopleActivity-save", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.peopleactivity.PeopleActivityDaoVerticle-save", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-peopleActivity-remove", (Message<Integer> message) -> {
            bus.send("org.cpao.facture.server.dao.peopleactivity.PeopleActivityDaoVerticle-remove", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-peopleActivity-update", (Message<Integer> message) -> {
            bus.send("org.cpao.facture.server.dao.peopleactivity.PeopleActivityDaoVerticle-update", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        /**
         * Payment Redirection
         */
        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-payment-load-home", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.payment.PaymentDaoVerticle-load-home", message.body(), (AsyncResult<Message<JsonArray>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-payment-load-all", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.payment.PaymentDaoVerticle-load-all", message.body(), (AsyncResult<Message<JsonArray>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-payment-save", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.payment.PaymentDaoVerticle-save", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-payment-remove", (Message<Integer> message) -> {
            bus.send("org.cpao.facture.server.dao.payment.PaymentDaoVerticle-remove", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-payment-update", (Message<Integer> message) -> {
            bus.send("org.cpao.facture.server.dao.payment.PaymentDaoVerticle-update", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });
        
        
        
        /**
         * Payment Type Redirection
         */
        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-paymentType-load-all", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.paymenttype.PaymentTypeDaoVerticle-load-all", message.body(), (AsyncResult<Message<JsonArray>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-paymentType-save", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.paymenttype.PaymentTypeDaoVerticle-save", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-paymentType-remove", (Message<Integer> message) -> {
            bus.send("org.cpao.facture.server.dao.paymenttype.PaymentTypeDaoVerticle-remove", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-paymentType-update", (Message<Integer> message) -> {
            bus.send("org.cpao.facture.server.dao.paymenttype.PaymentTypeDaoVerticle-update", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

        
        
        /**
         * Bill Type Redirection
         */
        bus.consumer("org.cpao.facture.server.CentralVerticle-service-billGenerator-generate-single", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.service.BillGeneratorVerticle-generate-single", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });
        
        bus.consumer("org.cpao.facture.server.CentralVerticle-service-billGenerator-generate-all", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.service.BillGeneratorVerticle-generate-all", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });
        
        bus.consumer("org.cpao.facture.server.CentralVerticle-service-billGenerator-generate-global", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.service.BillGeneratorVerticle-generate-global", message.body(), (AsyncResult<Message<JsonObject>> result) -> {
                message.reply(result.result().body());
            });
        });

    }

}
