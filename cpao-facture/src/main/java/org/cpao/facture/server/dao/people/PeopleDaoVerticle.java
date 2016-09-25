/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.people;

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
public class PeopleDaoVerticle extends AbstractVerticle {
    
    protected PeopleDao dao = new PeopleDaoImpl();
    
    @Override
    public void start(){
        
        final EventBus bus = vertx.eventBus();
        bus.consumer("org.cpao.facture.server.dao.people.PeopleDaoVerticle-save", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final JsonObject people = message.body();
                final JsonObject result = dao.save(people);
                
                message.reply(result);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.people.PeopleDaoVerticle-remove", new Handler<Message<Integer>>() {
            @Override
            public void handle(Message<Integer> message){
                
                final int id = message.body();
                final int result = dao.remove(id);
                
                final JsonObject o = new JsonObject().put("result", result);
                message.reply(o);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.people.PeopleDaoVerticle-update", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final int id = message.body().getInteger("id");
                final JsonObject peopleData = message.body().getJsonObject("people");
                final int result = dao.update(id, peopleData);
                
                final JsonObject o = new JsonObject().put("result", result);
                message.reply(o);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.people.PeopleDaoVerticle-load-all", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final JsonArray array = dao.loadAll();
                
                message.reply(array);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.people.PeopleDaoVerticle-load-home", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final JsonObject data = message.body();
                final int season = data.getInteger("id");
                final JsonArray array = dao.loadByHome(season);
                
                message.reply(array);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.people.PeopleDaoVerticle-load-single", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final JsonObject data = message.body();
                final int id = data.getInteger("id");
                final JsonObject o = dao.loadSingle(id);
                
                message.reply(o);
                
            }
        });
    }
    
}
