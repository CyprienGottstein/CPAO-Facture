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
        
    }
    
}
