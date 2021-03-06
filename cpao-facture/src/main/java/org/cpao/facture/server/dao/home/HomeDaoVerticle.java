/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.home;

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
public class HomeDaoVerticle extends AbstractVerticle {
    
    protected HomeDao dao = new HomeDaoImpl();
    
    @Override
    public void start(){
        
        final EventBus bus = vertx.eventBus();
        bus.consumer("org.cpao.facture.server.dao.home.HomeDaoVerticle-save", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final JsonObject people = message.body();
                final JsonObject result = dao.save(people);
                
//                final JsonObject reply = new JsonObject();
//                reply.put("result", result);
                
                message.reply(result);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.home.HomeDaoVerticle-remove", new Handler<Message<Integer>>() {
            @Override
            public void handle(Message<Integer> message){
                
                final int id = message.body();
                final int result = dao.remove(id);
                
                final JsonObject o = new JsonObject().put("result", result);
                message.reply(o);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.home.HomeDaoVerticle-update", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final int id = message.body().getInteger("id");
                final JsonObject peopleData = message.body().getJsonObject("home");
                final int result = dao.update(id, peopleData);
                
                final JsonObject o = new JsonObject().put("result", result);
                message.reply(o);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.home.HomeDaoVerticle-load-all", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final JsonArray array = dao.loadAll();
                
                message.reply(array);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.home.HomeDaoVerticle-load-activity", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final int activity = message.body().getInteger("id");
                final JsonObject o = dao.loadByActivity(activity);
                
                message.reply(o);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.home.HomeDaoVerticle-load-people", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final int people = message.body().getInteger("id");
                final JsonObject o = dao.loadByPeople(people);
                
                message.reply(o);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.home.HomeDaoVerticle-load-single", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final int id = message.body().getInteger("id");
                final JsonObject o = dao.loadSingle(id);
                
                message.reply(o);
                
            }
        });
    }
    
}
