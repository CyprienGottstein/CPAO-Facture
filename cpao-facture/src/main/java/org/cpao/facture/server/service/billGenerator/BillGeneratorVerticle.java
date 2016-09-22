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
    
    protected BillGenerator billGenerator = new BillGeneratorImpl();
    
    @Override
    public void start() {
        
        final EventBus bus = vertx.eventBus();
        bus.consumer("org.cpao.facture.server.service.BillGeneratorVerticle-generate", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message) {
                
                final int id = message.body().getInteger("id");
                final int season = message.body().getInteger("season");
                
                final JsonArray array = billGenerator.retrieveHomeData(id, season);
                
                float totalCost = 0;
                for (int i = 0; i < array.size(); i++){
                    final JsonObject line = array.getJsonObject(i);
                    totalCost += line.getFloat("insuranceCost");
                    totalCost += line.getFloat("cotisationCost");
                    totalCost += line.getFloat("licenceCost");
                }
                
                final JsonObject o = new JsonObject().put("totalCost", totalCost);
                message.reply(o);
                
            }
        });
    }
    
}
