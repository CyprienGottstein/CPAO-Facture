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
public class Payment extends JsonObject {
    
    public void setId(final int id){
        put("id", id);
    }
    public int getId(){
        return getInteger("id", 0);
    }
    public void setIdHome(final int id){
        put("idHome", id);
    }
    public int getIdHome(){
        return getInteger("idHome", 0);
    }
    public void setSeason(final int season){
        put("season", season);
    }
    public int getSeason(){
        return getInteger("season", 2000);
    }
    public void setType(final int type){
        put("idType", type);
    }
    public int getType(){
        return getInteger("idType", 0);
    }
    public void setAmount(final float amount){
        put("amount", amount);
    }
    public float getAmount(){
        return getFloat("amount", 0f);
    }
    public void setMetadata(final String metadata){
        put("metadata", metadata);
    }
    public String getMetadata(){
        return getString("metadata", "");
    }
    public void setSolded(final boolean solded) {
        put("solded", solded);
    }
    public boolean getSolded() {
        return getBoolean("solded");
    }
    
}
