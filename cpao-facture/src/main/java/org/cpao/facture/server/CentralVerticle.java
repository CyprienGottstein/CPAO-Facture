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
            bus.send("org.cpao.facture.server.dao.activity.InsuranceDaoVerticle-load-bySeason", message.body(), (AsyncResult<Message<JsonArray>> result) -> {
                message.reply(result.result().body());
            });
        });
        
        bus.consumer("org.cpao.facture.server.CentralVerticle-dao-insurance-load-all", (Message<JsonObject> message) -> {
            bus.send("org.cpao.facture.server.dao.activity.InsuranceDaoVerticle-load-all", null, (AsyncResult<Message<JsonArray>> result) -> {
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
        
    }
    
}
