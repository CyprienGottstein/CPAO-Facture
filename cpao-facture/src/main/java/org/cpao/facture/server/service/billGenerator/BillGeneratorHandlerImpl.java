/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.service.billGenerator;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author Cyprien
 */
public class BillGeneratorHandlerImpl implements BillGeneratorHandler {

    protected BillGeneratorDao dao = new BillGeneratorDaoImpl();

    @Override
    public JsonObject generateSingle(int home, int season) {

        final JsonObject homeData = dao.retrieveHome(home);
        final JsonArray activities = dao.retrieveHomeActivities(home, season);
        final JsonArray payments = dao.retrieveHomePayments(home, season);

        return processHomeBill(homeData, activities, payments);

    }

    @Override
    public JsonArray generateAll(int season) {

        final JsonArray homes = dao.retrieveAllHomesWithInputInSeason(season);
        final JsonArray activities = dao.retrieveAllActivities(season);
        final JsonArray payments = dao.retrieveAllPayments(season);
        final JsonArray bills = new JsonArray();
        
        System.out.println("homes : " + homes.encodePrettily());
        System.out.println("activities : " + activities.encodePrettily());
        System.out.println("payments : " + payments.encodePrettily());
        
        int indexActivities = 0;
        int indexPayments = 0;
        
        for (int i = 0; i < homes.size(); i++){
            final JsonObject home = homes.getJsonObject(i);
            
            final int currentHome = home.getInteger("id");
            
            boolean flagActivity = true;
            boolean flagPayment = true;
            
            final JsonArray currentFamilyActivities = new JsonArray();
            final JsonArray currentFamilyPayments = new JsonArray();
            
            while(indexActivities < activities.size() && flagActivity) {
                final JsonObject activity = activities.getJsonObject(indexActivities);
                if (activity.getInteger("idHome") == currentHome) {
                    currentFamilyActivities.add(activity);
                    indexActivities++;
                } else {
                    flagActivity = false;
                }
            }
            
            while(indexPayments < payments.size() && flagPayment) {
                final JsonObject payment = payments.getJsonObject(indexPayments);
                if (payment.getInteger("idHome") == currentHome) {
                    currentFamilyPayments.add(payment);
                    indexPayments++;
                } else {
                    flagPayment = false;
                }
            }
            
            bills.add(processHomeBill(home, currentFamilyActivities, currentFamilyPayments));
            
        }

        return bills;
    }

    @Override
    public JsonObject generateGlobal(int season) {

        final JsonArray bills = generateAll(season);
        System.out.println("bills : " + bills.encodePrettily());
        return processClubBill(bills);
    }

    public JsonObject processHomeBill(JsonObject home, JsonArray activities, JsonArray payments) {
        
        System.out.println("activities : " + activities.encodePrettily());
        System.out.println("payments : " + payments.encodePrettily());

        float totalCost = 0;
        float totalDeposit = 0;
        float totalSolded = 0;
        float totalMissing = 0;

        for (int i = 0; i < activities.size(); i++) {
            final JsonObject line = activities.getJsonObject(i);
            totalCost += line.getFloat("insuranceCost");
            totalCost += line.getFloat("cotisationCost");
            totalCost += line.getFloat("licenceCost");
        }

        for (int i = 0; i < payments.size(); i++) {
            final JsonObject line = payments.getJsonObject(i);
            totalDeposit += line.getFloat("amount");
            if (line.getBoolean("solded")) {
                totalSolded += line.getFloat("amount");
            }
        }

        // <-- Strategy around here
        totalMissing = totalCost - totalDeposit;

        final JsonObject o = new JsonObject()
                .put("id", home.getInteger("id"))
                .put("home", home.getString("name"))
                .put("totalCost", Math.round(totalCost * 100.0) / 100.0)
                .put("totalDeposit", Math.round(totalDeposit * 100.0) / 100.0)
                .put("totalSolded", Math.round(totalSolded * 100.0) / 100.0)
                .put("totalMissing", Math.round(totalMissing * 100.0) / 100.0);

        return o;

    }

    public JsonObject processClubBill(JsonArray bills) {

        float totalCost = 0;
        float totalDeposit = 0;
        float totalSolded = 0;
        float totalMissing = 0;

        for (int i = 0; i < bills.size(); i++) {
            final JsonObject bill = bills.getJsonObject(i);
            totalCost += bill.getFloat("totalCost");
            totalDeposit += bill.getFloat("totalDeposit");
            totalSolded += bill.getFloat("totalSolded");
            totalMissing += bill.getFloat("totalMissing");
        }

        final JsonObject clubBill = new JsonObject()
                .put("totalCost", Math.round(totalCost * 100.0) / 100.0)
                .put("totalDeposit", Math.round(totalDeposit * 100.0) / 100.0)
                .put("totalSolded", Math.round(totalSolded * 100.0) / 100.0)
                .put("totalMissing", Math.round(totalMissing * 100.0) / 100.0);

        return clubBill;

    }

}
