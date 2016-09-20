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
    public int save(JsonObject people) {
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final int result = s.executeUpdate("INSERT INTO CPAO.PERSON (ID, FIRSTNAME, LASTNAME, BIRTHDAY) VALUES ("
                    + "NEXT VALUE FOR CPAO.SEQ_PERSON, "
                    + "'" + people.getString("firstname") + "', "
                    + "'" + people.getString("lastname") + "', "
                    + people.getLong("birthday") + " ); INSERT INTO CPAO.HOME_PERSON (ID_HOME, ID_PERSON) VALUES ("
                    + people.getInteger("home")+ ", IDENTITY() )");
            
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
                    + "FIRSTNAME = '" + people.getString("firstname") + "',"
                    + "LASTNAME = '" + people.getString("lastname") + "',"
                    + "BIRTHDAY = " + people.getLong("birthday")
                    + " WHERE ID = " + id + "; UPDATE CPAO.HOME_PERSON SET "
                    + "ID_HOME = " + people.getInteger("home") + " WHERE ID_PERSON = " + id);

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

            final ResultSet result = s.executeQuery("SELECT CPAO.PERSON.*, CPAO.HOME_PERSON.ID_HOME AS HOME "
                    + "FROM CPAO.PERSON LEFT JOIN CPAO.HOME_PERSON "
                    + "ON CPAO.PERSON.ID = CPAO.HOME_PERSON.ID_PERSON "
                    + "ORDER BY ID");
            final JsonArray array = new JsonArray();

            while (result.next()) {
                final People people = new People();
                people.setId(result.getInt("id"));
                people.setFirstname(result.getString("FIRSTNAME"));
                people.setLastname(result.getString("LASTNAME"));
                people.setBirthDate(result.getLong("BIRTHDAY"));
                people.setHome(result.getInt("HOME"));
                people.addActivities(new JsonArray());
                array.add(people);
            }

            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }

    }

}
