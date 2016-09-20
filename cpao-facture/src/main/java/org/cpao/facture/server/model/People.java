/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.model;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author Cyprien
 */
public class People extends JsonObject {
    
    public void setId(final int id){
        put("id", id);
    }
    public int getId(){
        return getInteger("id", 0);
    }
    public void setFirstname(final String firstname){
        put("firstname", firstname);
    }
    public String getFirstname(){
        return getString("firstname", "PRENOM NON RENSEIGNEE");
    }
    public void setLastname(final String lastname){
        put("lastname", lastname);
    }
    public String getLastname(){
        return getString("lastname", "NOM NON RENSEIGNEE");
    }
    public void setBirthDate(final long birthday) {
        put("birthday", birthday);
    }
    public long getBirthday(){
        return getLong("birthday", Long.MIN_VALUE);
    }
    public void setHome(final int home) {
        put("home", home);
    }
    public int getHome(){
        return getInteger("home", Integer.MIN_VALUE);
    }
    public void addActivities(final JsonObject activity) {
        getJsonArray("activites", new JsonArray()).add(activity);
    }
    public void addActivities(final JsonArray activities) {
        getJsonArray("activites", new JsonArray()).addAll(activities);
    }
    public JsonArray getActivities() {
        return getJsonArray("activities", new JsonArray());
    }
    
}
