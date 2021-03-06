/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.activity;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author Cyprien
 */
public interface ActivityDao {
    
    public JsonObject save(JsonObject activity);
    public int remove(int id);
    public int update(int id, JsonObject activity);
    public JsonArray loadBySeason(int season);
    public JsonObject loadSingle(int activity);
    public JsonArray loadAll();
    
}
