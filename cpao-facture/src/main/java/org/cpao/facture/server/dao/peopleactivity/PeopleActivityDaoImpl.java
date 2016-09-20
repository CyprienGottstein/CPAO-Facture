/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.peopleactivity;

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
import org.cpao.facture.server.model.PeopleActivity;

/**
 *
 * @author Cyprien
 */
public class PeopleActivityDaoImpl implements PeopleActivtyDao {

    @Override
    public int save(JsonObject peopleActivity) {
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final int result = s.executeUpdate("INSERT INTO CPAO.ACTIVITY_PERSON (ID, ID_ACTIVITY, ID_PERSON, ID_INSURANCE, TEACHER, OBSERVATOR, FAMILY, SEASON) VALUES ("
                    + "NEXT VALUE FOR CPAO.SEQ_ACTIVITY_PERSON, "
                    + peopleActivity.getInteger("idActivity") + ", "
                    + peopleActivity.getInteger("idPeople") + ", "
                    + peopleActivity.getInteger("idInsurance") + ", "
                    + peopleActivity.getBoolean("teacher") + ", "
                    + peopleActivity.getBoolean("observator") + ", "
                    + peopleActivity.getBoolean("family") + ", "
                    + peopleActivity.getInteger("season") +  " )");
            
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

            final int result = s.executeUpdate("DELETE FROM CPAO.ACTIVITY_PERSON WHERE ID = " + id);

            return result;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

    }

    @Override
    public int update(int id, JsonObject peopleActivity) {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();
            //ID_ACTIVITY INTEGER, ID_PERSON INTEGER, ID_INSURANCE INTEGER, TEACHER BOOLEAN, OBSERVATOR BOOLEAN, FAMILY BOOLEAN, SEASON INTEGER
            final int result = s.executeUpdate("UPDATE CPAO.ACTIVITY_PERSON SET "
                    + "ID_ACTIVITY = " + peopleActivity.getInteger("idActivity") + ","
                    + "ID_PERSON = " + peopleActivity.getInteger("idPeople") + ","
                    + "ID_INSURANCE = " + peopleActivity.getInteger("idInsurance") + ","
                    + "TEACHER = " + peopleActivity.getBoolean("teacher") + ","
                    + "OBSERVATOR = " + peopleActivity.getBoolean("observator") + ","
                    + "FAMILY = " + peopleActivity.getBoolean("family") + ","
                    + "SEASON = " + peopleActivity.getInteger("season")
                    + " WHERE ID = " + id );

            return result;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

    }
    
    @Override
    public JsonArray loadByPeople(int id) {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("SELECT * FROM CPAO.ACTIVITY_PERSON WHERE ID_PERSON = " + id);
            final JsonArray array = new JsonArray();

            while (result.next()) {
                final PeopleActivity peopleActivity = new PeopleActivity();
                peopleActivity.setId(result.getInt("ID"));
                peopleActivity.setIdActivity(result.getInt("ID_ACTIVITY"));
                peopleActivity.setIdInsurance(result.getInt("ID_INSURANCE"));
                peopleActivity.setIdPeople(result.getInt("ID_PERSON"));
                peopleActivity.setSeason(result.getInt("SEASON"));
                
                peopleActivity.setFamily(result.getBoolean("FAMILY"));
                peopleActivity.setObservator(result.getBoolean("OBSERVATOR"));
                peopleActivity.setTeacher(result.getBoolean("TEACHER"));
                
                array.add(peopleActivity);
            }

            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }

    }

}
