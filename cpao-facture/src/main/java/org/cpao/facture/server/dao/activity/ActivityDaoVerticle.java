/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.activity;

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
public class ActivityDaoVerticle extends AbstractVerticle {
    
    protected ActivityDao dao = new ActivityDaoImpl();

    @Override
    public void start() {

        final EventBus bus = vertx.eventBus();
        bus.consumer("org.cpao.facture.server.dao.activity.ActivityDaoVerticle-save", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final JsonObject activity = message.body();
                final int result = dao.save(activity);
                
                message.reply(result);
                
            }
        });
        
        bus.consumer("org.cpao.facture.server.dao.activity.ActivityDaoVerticle-load-bySeason", new Handler<Message<JsonObject>>() {
            @Override
            public void handle(Message<JsonObject> message){
                
                final JsonObject data = message.body();
                final int season = data.getInteger("season");
                final JsonArray array = dao.loadBySeason(season);
                
                message.reply(array);
                
            }
        });

    }

}
