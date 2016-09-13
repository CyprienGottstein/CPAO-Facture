/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.activity;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cpao.facture.server.dao.Database;
import org.cpao.facture.server.dao.DatabaseScript;
import org.cpao.facture.server.model.Activity;

/**
 *
 * @author Cyprien
 */
public class ActivityDaoImpl implements ActivityDao {

    @Override
    public int save(JsonObject activity) {
        
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {
            
            System.out.println("Saving : " + activity.encodePrettily());

            final Statement s = c.createStatement();
            
            final int result = s.executeUpdate("INSERT INTO CPAO.ACTIVITY (ID, LABEL, LICENCE_COST, COTISATION_COST, SEASON) VALUES ( "
                    + "NEXT VALUE FOR CPAO.SEQ_ACTIVITY, "
                    + "'" + activity.getString("label") + "', "
                    + Float.parseFloat(activity.getString("licenceCost")) + ", "
                    + Float.parseFloat(activity.getString("cotisationCost")) + ","
                    + activity.getInteger("season") + ")");
            
             return result;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        
    }
    
    @Override
    public int remove(int id) {
        
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();
            
            final int result = s.executeUpdate("DELETE FROM CPAO.ACTIVITY WHERE ID = " + id);
            
            return result;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        
    }
    
    @Override
    public int update(int id, JsonObject activity) {
        
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();
            
            final int result = s.executeUpdate("UPDATE CPAO.ACTIVITY SET SEASON = " + activity.getInteger("season") + ","
            + "LABEL = '" + activity.getString("label") + "',"
            + "LICENCE_COST = " + Float.parseFloat(activity.getString("licenceCost")) + ","
            + "COTISATION_COST = " + Float.parseFloat(activity.getString("cotisationCost"))
            + " WHERE ID = " + id);
            
            return result;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        
    }
    
    @Override
    public JsonArray loadBySeason(int season){
        
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();
            
            final ResultSet result = s.executeQuery("SELECT * FROM CPAO.ACTIVITY WHERE SEASON = " + season);
            final JsonArray array = new JsonArray();
            
            while (result.next()){
                final Activity activity = new Activity();
                activity.setId(result.getInt("id"));
                activity.setSeason(result.getInt("SEASON"));
                activity.setLabel(result.getString("LABEL"));
                activity.setLicenceCost(result.getFloat("LICENCE_COST"));
                activity.setCotisationCost(result.getFloat("COTISATION_COST"));
                
                array.add(activity);
            }
            
            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }
        
    }
    
    @Override
    public JsonArray loadAll(){
        
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();
            
            final ResultSet result = s.executeQuery("SELECT * FROM CPAO.ACTIVITY ORDER BY SEASON");
            final JsonArray array = new JsonArray();
            
            while (result.next()){
                final Activity activity = new Activity();
                activity.setId(result.getInt("id"));
                activity.setSeason(result.getInt("SEASON"));
                activity.setLabel(result.getString("LABEL"));
                activity.setLicenceCost(result.getFloat("LICENCE_COST"));
                activity.setCotisationCost(result.getFloat("COTISATION_COST"));
                
                array.add(activity);
            }
            
            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }
        
    }
    
}
