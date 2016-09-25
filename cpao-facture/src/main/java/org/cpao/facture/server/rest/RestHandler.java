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
        final int season = body.getInteger("id");

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
    
    public void activityLoadSingle(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");

        System.out.println("Loading activity with id : " + id);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-activity-load-single", body, new Handler<AsyncResult<Message<JsonObject>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonObject>> result) {
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
        final int season = body.getInteger("id");

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
    
    public void insuranceLoadSingle(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");

        System.out.println("Loading insurance with id :  : " + id);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-insurance-load-single", body, new Handler<AsyncResult<Message<JsonObject>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonObject>> result) {
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

    public void peopleLoadAll(RoutingContext routingContext) {

        System.out.println("Loading all peoples !");

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-people-load-all", null, new Handler<AsyncResult<Message<JsonArray>>>() {
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
    
    public void peopleLoadByHome(RoutingContext routingContext) {
        
        final JsonObject body = routingContext.getBodyAsJson();
        final int home = body.getInteger("id");

        System.out.println("Loading all people with home id : " + home);
        System.out.println("body : " + body.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-people-load-home", body, new Handler<AsyncResult<Message<JsonArray>>>() {
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
    
    public void peopleLoadSingle(RoutingContext routingContext) {
        
        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");

        System.out.println("Loading people with home id : " + id);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-people-load-single", body, new Handler<AsyncResult<Message<JsonObject>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonObject>> result) {
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

    public void peopleSave(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final JsonObject people = body.getJsonObject("people");

        System.out.println("Saving people : " + people.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-people-save", people, new Handler<AsyncResult<Message<JsonObject>>>() {
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

    public void peopleRemove(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");

        System.out.println("Removing people with id : " + id);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-people-remove", id, new Handler<AsyncResult<Message<JsonObject>>>() {
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

    public void peopleUpdate(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");
        final JsonObject data = body.getJsonObject("people");

        System.out.println("Updating people with id : " + id + " with following data : " + data.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-people-update", body, new Handler<AsyncResult<Message<JsonObject>>>() {
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
    
    public void homeLoadAll(RoutingContext routingContext) {

        System.out.println("Loading all homes !");

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-home-load-all", null, new Handler<AsyncResult<Message<JsonArray>>>() {
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
    
    public void homeLoadByActivity(RoutingContext routingContext) {
        
        final JsonObject body = routingContext.getBodyAsJson();
        final int activity = body.getInteger("id");

        System.out.println("Loading home with id activity : " + activity);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-home-load-activity", body, new Handler<AsyncResult<Message<JsonObject>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonObject>> result) {
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
    
    public void homeLoadByPeople(RoutingContext routingContext) {
        
        final JsonObject body = routingContext.getBodyAsJson();
        final int people = body.getInteger("id");

        System.out.println("Loading home with id people : " + people);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-home-load-people", body, new Handler<AsyncResult<Message<JsonObject>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonObject>> result) {
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
    
    public void homeLoadSingle(RoutingContext routingContext) {
        
        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");

        System.out.println("Loading home with id : " + id);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-home-load-single", body, new Handler<AsyncResult<Message<JsonObject>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonObject>> result) {
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

    public void homeSave(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final JsonObject home = body.getJsonObject("home");

        System.out.println("Saving home : " + home.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-home-save", home, new Handler<AsyncResult<Message<JsonObject>>>() {
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

    public void homeRemove(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");

        System.out.println("Removing home with id : " + id);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-home-remove", id, new Handler<AsyncResult<Message<JsonObject>>>() {
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

    public void homeUpdate(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");
        final JsonObject data = body.getJsonObject("home");

        System.out.println("Updating home with id : " + id + " with following data : " + data.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-home-update", body, new Handler<AsyncResult<Message<JsonObject>>>() {
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
    
    public void peopleActivityLoadByPeople(RoutingContext routingContext) {
        
        final JsonObject body = routingContext.getBodyAsJson();
        final int people = body.getInteger("id");

        System.out.println("Loading all activities for people with id : " + people);
        System.out.println("body : " + body.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-peopleActivity-load-people", body, new Handler<AsyncResult<Message<JsonArray>>>() {
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

    public void peopleActivitySave(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final JsonObject peopleActivity = body.getJsonObject("peopleActivity");

        System.out.println("Saving people activity : " + peopleActivity.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-peopleActivity-save", peopleActivity, new Handler<AsyncResult<Message<JsonObject>>>() {
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

    public void peopleActivityRemove(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");

        System.out.println("Removing peopleActivity with id : " + id);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-peopleActivity-remove", id, new Handler<AsyncResult<Message<JsonObject>>>() {
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

    public void peopleActivityUpdate(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");
        final JsonObject data = body.getJsonObject("peopleActivity");

        System.out.println("Updating peopleActivity with id : " + id + " with following data : " + data.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-peopleActivity-update", body, new Handler<AsyncResult<Message<JsonObject>>>() {
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
    
    public void paymentLoadAll(RoutingContext routingContext) {

        System.out.println("Loading all payments !");

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-payment-load-all", null, new Handler<AsyncResult<Message<JsonArray>>>() {
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
    
    public void paymentLoadByHome(RoutingContext routingContext) {
        
        final JsonObject body = routingContext.getBodyAsJson();
        final int people = body.getInteger("id");

        System.out.println("Loading all payments for people with id : " + people);
        System.out.println("body : " + body.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-payment-load-home", body, new Handler<AsyncResult<Message<JsonArray>>>() {
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

    public void paymentSave(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final JsonObject peopleActivity = body.getJsonObject("payment");

        System.out.println("Saving payment : " + peopleActivity.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-payment-save", peopleActivity, new Handler<AsyncResult<Message<JsonObject>>>() {
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

    public void paymentRemove(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");

        System.out.println("Removing payment with id : " + id);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-payment-remove", id, new Handler<AsyncResult<Message<JsonObject>>>() {
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

    public void paymentUpdate(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");
        final JsonObject data = body.getJsonObject("payment");

        System.out.println("Updating payment with id : " + id + " with following data : " + data.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-payment-update", body, new Handler<AsyncResult<Message<JsonObject>>>() {
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
    
    public void paymentTypeLoadAll(RoutingContext routingContext) {

        System.out.println("Loading all payments types !");

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-paymentType-load-all", null, new Handler<AsyncResult<Message<JsonArray>>>() {
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

    public void paymentTypeSave(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final JsonObject paymentType = body.getJsonObject("paymentType");

        System.out.println("Saving paymentType : " + paymentType.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-paymentType-save", paymentType, new Handler<AsyncResult<Message<JsonObject>>>() {
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

    public void paymentTypeRemove(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");

        System.out.println("Removing payment type with id : " + id);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-paymentType-remove", id, new Handler<AsyncResult<Message<JsonObject>>>() {
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

    public void paymentTypeUpdate(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");
        final JsonObject data = body.getJsonObject("paymentType");

        System.out.println("Updating payment type with id : " + id + " with following data : " + data.encodePrettily());

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-paymentType-update", body, new Handler<AsyncResult<Message<JsonObject>>>() {
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
    
    public void billGenerate(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int id = body.getInteger("id");
        final int season = body.getInteger("season");

        System.out.println("Generating bill for home with id : " + id + " for season : " + season);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-service-billGenerator-generate-single", body, new Handler<AsyncResult<Message<JsonObject>>>() {
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
    
    public void billGenerateAll(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int season = body.getInteger("season");

        System.out.println("Generating global bill of the club for season : " + season);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-service-billGenerator-generate-all", body, new Handler<AsyncResult<Message<JsonArray>>>() {
            @Override
            public void handle(AsyncResult<Message<JsonArray>> result) {
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
    
    public void billGenerateGlobal(RoutingContext routingContext) {

        final JsonObject body = routingContext.getBodyAsJson();
        final int season = body.getInteger("season");

        System.out.println("Generating global bill of the club for season : " + season);

        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-service-billGenerator-generate-global", body, new Handler<AsyncResult<Message<JsonObject>>>() {
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
