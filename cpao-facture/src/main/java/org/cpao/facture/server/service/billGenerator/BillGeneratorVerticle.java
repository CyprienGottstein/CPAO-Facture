/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.service.billGenerator;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author Cyprien
 */
public class BillGeneratorVerticle extends AbstractVerticle {
    
    protected BillGeneratorHandler handler = new BillGeneratorHandlerImpl();
    
    @Override
    public void start() {
        
        final EventBus bus = vertx.eventBus();
        bus.consumer("org.cpao.facture.server.service.BillGeneratorVerticle-generate-single", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                
                final int id = message.body().getInteger("id");
                final int season = message.body().getInteger("season");
                
                final JsonObject o = handler.generateSingle(id, season);
                
                message.reply(o);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.service.BillGeneratorVerticle-generate-all", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                
                final int season = message.body().getInteger("season");
                
                final JsonArray array = handler.generateAll(season);
                
                message.reply(array);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.service.BillGeneratorVerticle-generate-global", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                
                final int season = message.body().getInteger("season");
                
                final JsonObject o = handler.generateGlobal(season);
                
                message.reply(o);
                
            }
        });
    }
    
}
