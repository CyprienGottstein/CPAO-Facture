/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cpao.facture.server.dao.activity.ActivityDaoVerticle;
import org.cpao.facture.server.dao.home.HomeDaoVerticle;
import org.cpao.facture.server.dao.insurance.InsuranceDaoVerticle;
import org.cpao.facture.server.dao.people.PeopleDaoVerticle;
import org.cpao.facture.server.dao.peopleactivity.PeopleActivityDaoVerticle;
import org.cpao.facture.server.rest.RestVerticle;

/**
 *
 * @author Cyprien
 */
public class VerticleDeployer {

    protected Vertx vertx;

    public VerticleDeployer(Vertx vertx) {
        this.vertx = vertx;
    }

    public void deploy() {

        List<String> verticlesToDeploy = new ArrayList<>();
        verticlesToDeploy.add(ActivityDaoVerticle.class.getCanonicalName());
        verticlesToDeploy.add(InsuranceDaoVerticle.class.getCanonicalName());
        verticlesToDeploy.add(PeopleDaoVerticle.class.getCanonicalName());
        verticlesToDeploy.add(HomeDaoVerticle.class.getCanonicalName());
        verticlesToDeploy.add(PeopleActivityDaoVerticle.class.getCanonicalName());
        verticlesToDeploy.add(CentralVerticle.class.getName());
        verticlesToDeploy.add(RestVerticle.class.getName());

        final CountDownLatch latch = new CountDownLatch(7);
        
        verticlesToDeploy.forEach(verticle -> {
            vertx.deployVerticle(verticle, new Handler<AsyncResult<String>>() {

                @Override
                public void handle(AsyncResult<String> e) {
                    if (e.succeeded()) {
                        System.out.println("Verticle : " + e.result().toLowerCase() + " has been deployed.");
                        latch.countDown();
                    } else {
                        throw new RuntimeException("Could not deploy verticle.");
                    }

                }
            });
        });

        try {
            latch.await();
        } catch (InterruptedException ex) {
            Logger.getLogger(VerticleDeployer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
