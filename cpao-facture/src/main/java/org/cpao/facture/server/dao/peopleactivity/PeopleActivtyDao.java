/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.peopleactivity;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author Cyprien
 */
public interface PeopleActivtyDao {
    
    public int save(JsonObject people);
    public int remove(int id);
    public int update(int id, JsonObject peopleActivity);
    public JsonArray loadByPeople(int id);
    
}
