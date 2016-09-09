/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.rest;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.time.LocalDateTime;

/**
 *
 * @author Cyprien
 */
public class RestHandler {

    private Vertx vertx;
    private int currentSeason = 2016;
    
    public RestHandler() {
        
        LocalDateTime current = LocalDateTime.now();
        
        final int year = current.getYear();
        final int month = current.getMonthValue();
        
        if (month < 9) {
            currentSeason = year - 1;
        } else {
            currentSeason = year;
        }
        
    }

    public Vertx getVertx() {
        return vertx;
    }

    public void setVertx(Vertx vertx) {
        this.vertx = vertx;
    }
    
    public void getCurrentSeason(RoutingContext routingContext) {
        
        HttpServerResponse response = routingContext.response();
        response.end(new JsonObject().put("season", currentSeason).encode());
    }
            

    public void activityLoadBySeason(RoutingContext routingContext) {

        System.out.println("body raw : " + routingContext.getBody());
        final JsonObject body = routingContext.getBodyAsJson();
        System.out.println("body : " + body.encodePrettily());
        final int season = body.getInteger("season");

        System.out.println("Loading all activity for season : " + season);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-activity-load-bySeason", body, new Handler<AsyncResult<Message<JsonArray>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonArray>> result) {
                HttpServerResponse response = routingContext.response();
                if (result.failed()){
                    response.setStatusCode(500);
                    response.end();
                } else {
                    response.end(result.result().body().encode());
                }
            }
        });
    }

}
