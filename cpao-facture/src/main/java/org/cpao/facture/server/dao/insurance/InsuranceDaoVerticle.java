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
import io.vertx.core.json.JsonArray;
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
                
                final JsonObject insurance = message.body();
                final int result = dao.save(insurance);
                
                message.reply(result);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.activity.InsuranceDaoVerticle-remove", new Handler<Message<Integer>>() {
            @Override
            public void handle(Message<Integer> message){
                
                final int id = message.body();
                final int result = dao.remove(id);
                
                final JsonObject o = new JsonObject().put("result", result);
                message.reply(o);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.activity.InsuranceDaoVerticle-update", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final int id = message.body().getInteger("id");
                final JsonObject insuranceData = message.body().getJsonObject("insurance");
                final int result = dao.update(id, insuranceData);
                
                final JsonObject o = new JsonObject().put("result", result);
                message.reply(o);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.activity.InsuranceDaoVerticle-load-bySeason", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final JsonObject data = message.body();
                final int season = data.getInteger("season");
                final JsonArray array = dao.loadBySeason(season);
                
                message.reply(array);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.activity.InsuranceDaoVerticle-load-all", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final JsonArray array = dao.loadAll();
                
                message.reply(array);
                
            }
        });
    }
    
}
