/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.payment;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author Cyprien
 */
public interface PaymentDao {
    
    public int save(JsonObject payment);
    public int remove(int id);
    public int update(int id, JsonObject payment);
    public JsonArray loadByHome(int home);
    public JsonArray loadAll();
    
}
