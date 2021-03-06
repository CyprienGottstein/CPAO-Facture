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
        dynamicRouter.route("/activity/load/single").handler(handler::activityLoadSingle);
        dynamicRouter.route("/activity/save").handler(handler::activitySave);
        dynamicRouter.route("/activity/remove").handler(handler::activityRemove);
        dynamicRouter.route("/activity/update").handler(handler::activityUpdate);
        
        dynamicRouter.route("/insurance/load/season").handler(handler::insuranceLoadBySeason);
        dynamicRouter.route("/insurance/load/all").handler(handler::insuranceLoadAll);
        dynamicRouter.route("/insurance/load/single").handler(handler::insuranceLoadSingle);
        dynamicRouter.route("/insurance/save").handler(handler::insuranceSave);
        dynamicRouter.route("/insurance/remove").handler(handler::insuranceRemove);
        dynamicRouter.route("/insurance/update").handler(handler::insuranceUpdate);
        
        dynamicRouter.route("/people/load/all").handler(handler::peopleLoadAll);
        dynamicRouter.route("/people/load/home").handler(handler::peopleLoadByHome);
        dynamicRouter.route("/people/load/single").handler(handler::peopleLoadSingle);
        dynamicRouter.route("/people/save").handler(handler::peopleSave);
        dynamicRouter.route("/people/remove").handler(handler::peopleRemove);
        dynamicRouter.route("/people/update").handler(handler::peopleUpdate);
        
        dynamicRouter.route("/home/load/all").handler(handler::homeLoadAll);
        dynamicRouter.route("/home/load/activity").handler(handler::homeLoadByActivity);
        dynamicRouter.route("/home/load/people").handler(handler::homeLoadByPeople);
        dynamicRouter.route("/home/load/single").handler(handler::homeLoadSingle);
        dynamicRouter.route("/home/save").handler(handler::homeSave);
        dynamicRouter.route("/home/remove").handler(handler::homeRemove);
        dynamicRouter.route("/home/update").handler(handler::homeUpdate);
        
        dynamicRouter.route("/peopleActivity/load/people").handler(handler::peopleActivityLoadByPeople);
        dynamicRouter.route("/peopleActivity/save").handler(handler::peopleActivitySave);
        dynamicRouter.route("/peopleActivity/remove").handler(handler::peopleActivityRemove);
        dynamicRouter.route("/peopleActivity/update").handler(handler::peopleActivityUpdate);
        
        dynamicRouter.route("/payment/load/all").handler(handler::paymentLoadAll);
        dynamicRouter.route("/payment/load/home").handler(handler::paymentLoadByHome);
        dynamicRouter.route("/payment/save").handler(handler::paymentSave);
        dynamicRouter.route("/payment/remove").handler(handler::paymentRemove);
        dynamicRouter.route("/payment/update").handler(handler::paymentUpdate);
        
        dynamicRouter.route("/paymentType/load/all").handler(handler::paymentTypeLoadAll);
        dynamicRouter.route("/paymentType/save").handler(handler::paymentTypeSave);
        dynamicRouter.route("/paymentType/remove").handler(handler::paymentTypeRemove);
        dynamicRouter.route("/paymentType/update").handler(handler::paymentTypeUpdate);
        
        dynamicRouter.route("/bill/generate/single").handler(handler::billGenerate);
        dynamicRouter.route("/bill/generate/all").handler(handler::billGenerateAll);
        dynamicRouter.route("/bill/generate/global").handler(handler::billGenerateGlobal);
        
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
