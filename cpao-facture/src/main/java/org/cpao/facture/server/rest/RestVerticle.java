/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.rest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

/**
 *
 * @author Cyprien
 */
public class RestVerticle extends AbstractVerticle {

    protected RestHandler handler = new RestHandler();

    @Override
    public void start() {

        handler.setVertx(vertx);

        HttpServer restServer = vertx.createHttpServer();
        Router dynamicRouter = Router.router(vertx);

        dynamicRouter.route().handler(BodyHandler.create());
//        dynamicRouter.route().handler(CorsHandler.create("vertx\\.io")
//                .allowedHeader("Access-Control-Allow-Origin")
//                .allowedMethod(HttpMethod.GET)
//                .allowedMethod(HttpMethod.POST));
        dynamicRouter.route().handler(routingContext -> {
                    routingContext.response().headers().add("Access-Control-Allow-Origin", "*");
                    routingContext.response().headers().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
                    routingContext.response().headers().add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type");
                    routingContext.next();
                });

        dynamicRouter.route("/season/current").handler(handler::getCurrentSeason);
        
        dynamicRouter.route("/activity/load/season").handler(handler::activityLoadBySeason);
        dynamicRouter.route("/activity/load/all").handler(handler::activityLoadAll);
        dynamicRouter.route("/activity/save").handler(handler::activitySave);
        dynamicRouter.route("/activity/remove").handler(handler::activityRemove);
        dynamicRouter.route("/activity/update").handler(handler::activityUpdate);
        
        dynamicRouter.route("/insurance/load/season").handler(handler::insuranceLoadBySeason);
        dynamicRouter.route("/insurance/load/all").handler(handler::insuranceLoadAll);
        dynamicRouter.route("/insurance/save").handler(handler::insuranceSave);
        dynamicRouter.route("/insurance/remove").handler(handler::insuranceRemove);
        dynamicRouter.route("/insurance/update").handler(handler::insuranceUpdate);
        
        dynamicRouter.route("/people/load/all").handler(handler::peopleLoadAll);
        dynamicRouter.route("/people/save").handler(handler::peopleSave);
        dynamicRouter.route("/people/remove").handler(handler::peopleRemove);
        dynamicRouter.route("/people/update").handler(handler::peopleUpdate);
        
        dynamicRouter.route("/home/load/all").handler(handler::homeLoadAll);
        dynamicRouter.route("/home/save").handler(handler::homeSave);
        dynamicRouter.route("/home/remove").handler(handler::homeRemove);
        dynamicRouter.route("/home/update").handler(handler::homeUpdate);
        
        dynamicRouter.route("/peopleActivity/load/people").handler(handler::peopleActivityLoadByPeople);
        dynamicRouter.route("/peopleActivity/save").handler(handler::peopleActivitySave);
        dynamicRouter.route("/peopleActivity/remove").handler(handler::peopleActivityRemove);
        dynamicRouter.route("/peopleActivity/update").handler(handler::peopleActivityUpdate);
        
        restServer.requestHandler(dynamicRouter::accept).listen(10000);
        
        

        HttpServer staticServer = vertx.createHttpServer();
        Router staticRouter = Router.router(vertx);

        StaticHandler staticHandler = StaticHandler.create();
        staticHandler.setWebRoot("web/static/");
        staticHandler.setCachingEnabled(false);

        staticRouter.route().handler(routingContext -> {
            System.out.println("Request received on : " + routingContext.normalisedPath());
            routingContext.next();
        });
        staticRouter.route("/*").handler(staticHandler::handle);

        staticServer.requestHandler(staticRouter::accept).listen(9999);

    }

}
