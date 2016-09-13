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
public class Insurance extends JsonObject {
    
    public void setId(final int id){
        put("id", id);
    }
    public int getId(){
        return getInteger("id", 0);
    }
    public void setSeason(final int season){
        put("season", season);
    }
    public int getSeason(){
        return getInteger("season", 2000);
    }
    public void setLabel(final String label){
        put("label", label);
    }
    public String getLabel(){
        return getString("label", "ASSURANCE NON RENSEIGNEE");
    }
    public void setInsuranceCost(final float insuranceCost){
        put("insuranceCost", insuranceCost);
    }
    public float getInsuranceCost(){
        return getFloat("insuranceCost", 0f);
    }
}
