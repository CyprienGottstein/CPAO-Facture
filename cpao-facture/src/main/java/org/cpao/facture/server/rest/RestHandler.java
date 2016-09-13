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
                if (result.failed()) {
                    response.setStatusCode(500);
                    response.end();
                } else {
                    response.end(result.result().body().encode());
                }
            }
        });
    }
    
    public void activityLoadAll(RoutingContext routingContext) {

        System.out.println("Loading all activities !");

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-activity-load-all", null, new Handler<AsyncResult<Message<JsonArray>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonArray>> result) {
                HttpServerResponse response = routingContext.response();
                if (result.failed()) {
                    response.setStatusCode(500);
                    response.end();
                } else {
                    response.end(result.result().body().encode());
                }
            }
        });
    }

    public void activitySave(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final JsonObject activity = body.getJsonObject("activity");

        System.out.println("Saving activity : " + activity.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-activity-save", activity, new Handler<AsyncResult<Message<JsonObject>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonObject>> result) {
                HttpServerResponse response = routingContext.response();
                if (result.failed()) {
                    response.setStatusCode(500);
                    response.end();
                } else {
                    System.out.println(result.result().body().encode());
                    response.end("" + result.result().body().encode());
                }
            }
        });

    }

    public void activityRemove(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");

        System.out.println("Removing activity with id : " + id);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-activity-remove", id, new Handler<AsyncResult<Message<JsonObject>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonObject>> result) {
                HttpServerResponse response = routingContext.response();
                if (result.failed()) {
                    response.setStatusCode(500);
                    response.end();
                } else {
                    System.out.println(result.result().body().encode());
                    response.end("" + result.result().body().encode());
                }
            }
        });

    }
    
    public void activityUpdate(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");
        final JsonObject data = body.getJsonObject("activity");

        System.out.println("Updating activity with id : " + id + " with following data : " + data.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-activity-update", body, new Handler<AsyncResult<Message<JsonObject>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonObject>> result) {
                HttpServerResponse response = routingContext.response();
                if (result.failed()) {
                    response.setStatusCode(500);
                    response.end();
                } else {
                    System.out.println(result.result().body().encode());
                    response.end("" + result.result().body().encode());
                }
            }
        });

    }
    
    public void insuranceLoadBySeason(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int season = body.getInteger("season");

        System.out.println("Loading all insurance for season : " + season);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-insurance-load-bySeason", body, new Handler<AsyncResult<Message<JsonArray>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonArray>> result) {
                HttpServerResponse response = routingContext.response();
                if (result.failed()) {
                    response.setStatusCode(500);
                    response.end();
                } else {
                    response.end(result.result().body().encode());
                }
            }
        });
    }
    
    public void insuranceLoadAll(RoutingContext routingContext) {

        System.out.println("Loading all insurances !");

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-insurance-load-all", null, new Handler<AsyncResult<Message<JsonArray>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonArray>> result) {
                HttpServerResponse response = routingContext.response();
                if (result.failed()) {
                    response.setStatusCode(500);
                    response.end();
                } else {
                    response.end(result.result().body().encode());
                }
            }
        });
    }

    public void insuranceSave(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final JsonObject insurance = body.getJsonObject("insurance");

        System.out.println("Saving insurance : " + insurance.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-insurance-save", insurance, new Handler<AsyncResult<Message<JsonObject>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonObject>> result) {
                HttpServerResponse response = routingContext.response();
                if (result.failed()) {
                    response.setStatusCode(500);
                    response.end();
                } else {
                    System.out.println(result.result().body().encode());
                    response.end("" + result.result().body().encode());
                }
            }
        });

    }

    public void insuranceRemove(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");

        System.out.println("Removing insurance with id : " + id);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-insurance-remove", id, new Handler<AsyncResult<Message<JsonObject>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonObject>> result) {
                HttpServerResponse response = routingContext.response();
                if (result.failed()) {
                    response.setStatusCode(500);
                    response.end();
                } else {
                    response.end("" + result.result().body().encode());
                }
            }
        });

    }
    
    public void insuranceUpdate(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");
        final JsonObject data = body.getJsonObject("insurance");

        System.out.println("Updating insurance with id : " + id + " with following data : " + data.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-insurance-update", body, new Handler<AsyncResult<Message<JsonObject>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonObject>> result) {
                HttpServerResponse response = routingContext.response();
                if (result.failed()) {
                    response.setStatusCode(500);
                    response.end();
                } else {
                    System.out.println(result.result().body().encode());
                    response.end("" + result.result().body().encode());
                }
            }
        });

    }

}
