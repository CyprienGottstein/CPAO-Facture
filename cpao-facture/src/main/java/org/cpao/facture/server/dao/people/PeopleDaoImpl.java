/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.people;

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
import org.cpao.facture.server.model.People;

/**
 *
 * @author Cyprien
 */
public class PeopleDaoImpl implements PeopleDao {

    @Override
    public JsonObject save(JsonObject people) {
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final JsonObject o = new JsonObject()
                    .put("result", 1);
            
            final ResultSet result = s.executeQuery("INSERT INTO CPAO.PERSON (ID, ID_HOME, FIRSTNAME, LASTNAME, BIRTHDAY) VALUES ("
                    + "NEXT VALUE FOR CPAO.SEQ_PERSON, "
                    + people.getInteger("idHome") + ", "
                    + "'" + people.getString("firstname") + "', "
                    + "'" + people.getString("lastname") + "', "
                    + people.getLong("birthday") + " ); CALL IDENTITY() ;");
            
            while(result.next()){
                o.put("id", result.getInt(1));
            }
            
            return o;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonObject()
                    .put("result", -1);
        }
    }

    @Override
    public int remove(int id) {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final int result = s.executeUpdate("DELETE FROM CPAO.PERSON WHERE ID = " + id);

            return result;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

    }

    @Override
    public int update(int id, JsonObject people) {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();
            // LABEL, INSURANCE_COST, SEASON
            final int result = s.executeUpdate("UPDATE CPAO.PERSON SET "
                    + "ID_HOME = " + people.getInteger("idHome") + ", "
                    + "FIRSTNAME = '" + people.getString("firstname") + "',"
                    + "LASTNAME = '" + people.getString("lastname") + "',"
                    + "BIRTHDAY = " + people.getLong("birthday")
                    + " WHERE ID = " + id + ";");

            return result;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

    }

    @Override
    public JsonArray loadAll() {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("SELECT CPAO.PERSON.*"
                    + "FROM CPAO.PERSON "
                    + "ORDER BY ID");
            final JsonArray array = new JsonArray();

            while (result.next()) {
                final People people = new People();
                people.setId(result.getInt("id"));
                people.setHome(result.getInt("ID_HOME"));
                people.setFirstname(result.getString("FIRSTNAME"));
                people.setLastname(result.getString("LASTNAME"));
                people.setBirthDate(result.getLong("BIRTHDAY"));
                people.addActivities(new JsonArray());
                array.add(people);
            }

            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }

    }
    
    @Override
    public JsonArray loadByHome(final int home) {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("SELECT CPAO.PERSON.*"
                    + "FROM CPAO.PERSON "
                    + "WHERE CPAO.PERSON.ID_HOME = " + home
                    + " ORDER BY ID");
            final JsonArray array = new JsonArray();

            while (result.next()) {
                final People people = new People();
                people.setId(result.getInt("ID"));
                people.setFirstname(result.getString("FIRSTNAME"));
                people.setLastname(result.getString("LASTNAME"));
                people.setBirthDate(result.getLong("BIRTHDAY"));
                people.setHome(result.getInt("ID_HOME"));
                people.addActivities(new JsonArray());
                array.add(people);
            }

            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }

    }
    
    @Override
    public JsonObject loadSingle(final int id) {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("SELECT CPAO.PERSON.*, CPAO.HOME_PERSON.ID_HOME AS HOME "
                    + "FROM CPAO.PERSON, JOIN CPAO.HOME "
                    + "WHERE CPAO.PERSON.ID_HOME = CPAO.HOME.ID "
                    + "AND CPAO.PERSON.ID = " + id);
            final People people = new People();

            while (result.next()) {
                people.setId(result.getInt("ID"));
                people.setFirstname(result.getString("FIRSTNAME"));
                people.setLastname(result.getString("LASTNAME"));
                people.setBirthDate(result.getLong("BIRTHDAY"));
                people.setHome(result.getInt("ID_HOME"));
                people.addActivities(new JsonArray());
            }

            return people;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonObject();
        }

    }

}
