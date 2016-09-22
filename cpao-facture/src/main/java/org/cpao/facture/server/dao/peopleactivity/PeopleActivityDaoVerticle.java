/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.peopleactivity;

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
public class PeopleActivityDaoVerticle extends AbstractVerticle {
    
    protected PeopleActivtyDao dao = new PeopleActivityDaoImpl();
    
    @Override
    public void start(){
        
        final EventBus bus = vertx.eventBus();
        bus.consumer("org.cpao.facture.server.dao.peopleactivity.PeopleActivityDaoVerticle-save", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                System.out.println("proc");
                final JsonObject people = message.body();
                System.out.println(people.encodePrettily());
                final int result = dao.save(people);
                
                final JsonObject reply = new JsonObject();
                reply.put("result", result);
                
                message.reply(reply);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.peopleactivity.PeopleActivityDaoVerticle-remove", new Handler<Message<Integer>>() {
            @Override
            public void handle(Message<Integer> message){
                
                final int id = message.body();
                final int result = dao.remove(id);
                
                final JsonObject o = new JsonObject().put("result", result);
                message.reply(o);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.peopleactivity.PeopleActivityDaoVerticle-update", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final int id = message.body().getInteger("id");
                final JsonObject peopleData = message.body().getJsonObject("peopleActivity");
                final int result = dao.update(id, peopleData);
                
                final JsonObject o = new JsonObject().put("result", result);
                message.reply(o);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.peopleactivity.PeopleActivityDaoVerticle-load-people", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final JsonObject data = message.body();
                System.out.println("data:  " + data.encodePrettily());
                final int people = data.getInteger("id");
                final JsonArray array = dao.loadByPeople(people);
                
                message.reply(array);
                
            }
        });
    }
    
}
