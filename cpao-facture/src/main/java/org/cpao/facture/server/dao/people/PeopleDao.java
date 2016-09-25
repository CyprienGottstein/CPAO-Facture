/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.people;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author Cyprien
 */
public interface PeopleDao {
    
    public JsonObject save(JsonObject people);
    public int remove(int id);
    public int update(int id, JsonObject people);
    public JsonArray loadAll();
    public JsonArray loadByHome(final int home);
    public JsonObject loadSingle(final int id);
    
}
