package org.cpao.facture.server;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Cyprien
 */
public class App {

    public static void main(String[] args) {
        
        final Vertx vertx = Vertx.vertx();

//        DatabaseScript dbscript = new DatabaseScript(verstx);
//        dbscript.generateDatabase();
        
        VerticleDeployer deployer = new VerticleDeployer(vertx);
        deployer.deploy();
        
//        vertx.eventBus().send("org.cpao.facture.server.CentralVerticle-dao-activity-load-bySeason", new JsonObject().put("season", 2016), new Handler<AsyncResult<Message<JsonArray>>>() {
//                    
//                    @Override
//                    public void handle(AsyncResult<Message<JsonArray>> result) {
//                        System.out.println("array : " + result.result().body().encodePrettily());
//                    }
//                });
        
//        dbscript.retrieveActivity();
//        dbscript.retrieveInsurance();
        

    }

}
