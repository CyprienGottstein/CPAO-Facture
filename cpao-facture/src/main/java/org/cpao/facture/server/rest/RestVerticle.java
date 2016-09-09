/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.rest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
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

//
//        dynamicRouter.route().handler(routingContext -> {
//
////            // This handler will be called for every request
////            HttpServerResponse response = routingContext.response();
////            response.putHeader("content-type", "text/plain");
////
////            // Write to the response and end it
////            response.end("Hello World from Vert.x-Web!");
//        });
//        
        dynamicRouter.route("/activity/load/season").handler(handler::activityLoadBySeason);
        restServer.requestHandler(dynamicRouter::accept).listen(10000);

        HttpServer staticServer = vertx.createHttpServer();
        Router staticRouter = Router.router(vertx);

        StaticHandler staticHandler = StaticHandler.create();
        staticHandler.setWebRoot("web/static/");

        staticRouter.route().handler(routingContext -> {
            System.out.println("Request received on : " + routingContext.normalisedPath());
            routingContext.next();
        });
        staticRouter.route("/*").handler(staticHandler::handle);

        staticServer.requestHandler(staticRouter::accept).listen(9999);

    }

}