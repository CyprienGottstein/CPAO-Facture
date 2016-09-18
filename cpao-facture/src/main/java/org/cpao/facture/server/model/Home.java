/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.model;

import io.vertx.core.json.JsonObject;

/**
 *
 * @author Cyprien
 */
public class Home extends JsonObject {
    
    public void setId(final int id){
        put("id", id);
    }
    public int getId(){
        return getInteger("id", 0);
    }
    public void setName(final String firstname){
        put("name", firstname);
    }
    public String getName(){
        return getString("name", "NOM NON RENSEIGNEE");
    }
    
}
