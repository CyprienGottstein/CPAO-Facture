package org.cpao.facture.server;

import io.vertx.core.Vertx;
import org.cpao.facture.server.dao.DatabaseScript;

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
        
        VerticleDeployer deployer = new VerticleDeployer(vertx);
        deployer.deploy();
        
        DatabaseScript dbscript = new DatabaseScript(vertx);
//        dbscript.generateDatabase();
//        dbscript.retrieveActivity();
//        dbscript.retrieveInsurance();
//        dbscript.retrieveHomeBatch();  
//        dbscript.retrievePeople();
//        dbscript.addPaymentTypeBatch();
        

    }

}
