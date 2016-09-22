/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.payment;

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
public class PaymentDaoVerticle extends AbstractVerticle {
    
    protected PaymentDao dao = new PaymentDaoImpl();

    @Override
    public void start() {

        final EventBus bus = vertx.eventBus();
        bus.consumer("org.cpao.facture.server.dao.payment.PaymentDaoVerticle-save", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final JsonObject activity = message.body();
                final int result = dao.save(activity);
                
                final JsonObject o = new JsonObject().put("result", result);
                message.reply(o);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.payment.PaymentDaoVerticle-remove", new Handler<Message<Integer>>() {
            @Override
            public void handle(Message<Integer> message){
                
                final int id = message.body();
                final int result = dao.remove(id);
                
                final JsonObject o = new JsonObject().put("result", result);
                message.reply(o);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.payment.PaymentDaoVerticle-update", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final int id = message.body().getInteger("id");
                final JsonObject paymentData = message.body().getJsonObject("payment");
                final int result = dao.update(id, paymentData);
                
                final JsonObject o = new JsonObject().put("result", result);
                message.reply(o);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.payment.PaymentDaoVerticle-load-home", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final JsonObject data = message.body();
                final int season = data.getInteger("id");
                final JsonArray array = dao.loadByHome(season);
                
                message.reply(array);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.payment.PaymentDaoVerticle-load-all", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final JsonArray array = dao.loadAll();
                
                message.reply(array);
                
            }
        });

    }

}
