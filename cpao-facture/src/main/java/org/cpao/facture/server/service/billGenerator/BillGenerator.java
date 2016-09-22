/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.service.billGenerator;

import io.vertx.core.json.JsonArray;

/**
 *
 * @author Cyprien
 */
public interface BillGenerator {
    
    public JsonArray retrieveHomeActivities(final int home, final int season);
    public JsonArray retrieveHomePayments(final int home, final int season);
    
}
