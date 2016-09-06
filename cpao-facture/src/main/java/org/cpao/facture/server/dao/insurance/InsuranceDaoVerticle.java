/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.insurance;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author Cyprien
 */
public class InsuranceDaoVerticle extends AbstractVerticle {
    
    protected InsuranceDao dao = new InsuranceDaoImpl();
    
    @Override
    public void start(){
        
        final EventBus bus = vertx.eventBus();
        bus.consumer("org.cpao.facture.server.dao.insurance.InsuranceDaoVerticle-save", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final JsonObject activity = message.body();
                final int result = dao.save(activity);
                
                message.reply(result);
                
            }
        });
    }
    
}
