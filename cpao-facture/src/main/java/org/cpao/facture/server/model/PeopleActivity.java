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
public class PeopleActivity extends JsonObject {
    
    public void setId(final int id){
        put("id", id);
    }
    public int getId(){
        return getInteger("id", 0);
    }
    public void setIdActivity(final int id){
        put("idActivity", id);
    }
    public int getIdActivity(){
        return getInteger("idActivity", 0);
    }
    public void setIdInsurance(final int id){
        put("idInsurance", id);
    }
    public int getIdInsurance(){
        return getInteger("idInsurance", 0);
    }
    public void setIdPeople(final int id){
        put("idPeople", id);
    }
    public int getIdPeople(){
        return getInteger("idPeople", 0);
    }
    public void setTeacher(final boolean teacher){
        put("teacher", teacher);
    }
    public boolean getTeacher(){
        return getBoolean("teacher", false);
    }
    public void setObservator(final boolean observator){
        put("observator", observator);
    }
    public boolean getObservator(){
        return getBoolean("observator", false);
    }
    public void setFamily(final boolean familiy){
        put("family", familiy);
    }
    public boolean getFamily(){
        return getBoolean("family", false);
    }
    public void setSeason(final int season){
        put("season", season);
    }
    public int getSeason(){
        return getInteger("season", 2000);
    }
}
