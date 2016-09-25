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
public interface BillGeneratorHandler {
    
    public JsonObject generateSingle(int home, int season);
    public JsonArray generateAll(int season);
    public JsonObject generateGlobal(int season);
    
}
