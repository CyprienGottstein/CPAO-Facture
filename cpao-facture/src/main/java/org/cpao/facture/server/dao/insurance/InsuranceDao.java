/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.insurance;

import io.vertx.core.json.JsonObject;

/**
 *
 * @author Cyprien
 */
public interface InsuranceDao {
    
    public int save(JsonObject insurance);
    
}
