/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.paymentType;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author Cyprien
 */
public interface PaymentTypeDao {
    
    public int save(JsonObject paymentType);
    public int remove(int id);
    public int update(int id, JsonObject paymentType);
    public JsonArray loadAll();
    
}
