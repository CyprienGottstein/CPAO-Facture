/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.home;

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
import org.cpao.facture.server.model.Home;

/**
 *
 * @author Cyprien
 */
public class HomeDaoImpl implements HomeDao {

    @Override
    public int save(JsonObject home) {
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final int result = s.executeUpdate("INSERT INTO CPAO.HOME (ID, NAME) VALUES ("
                    + "NEXT VALUE FOR CPAO.SEQ_HOME, "
                    + "'" + home.getString("name") + "' )");

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

            final int result = s.executeUpdate("DELETE FROM CPAO.HOME WHERE ID = " + id);

            return result;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

    }

    @Override
    public int update(int id, JsonObject home) {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();
            // LABEL, INSURANCE_COST, SEASON
            final int result = s.executeUpdate("UPDATE CPAO.HOME SET "
                    + "NAME = '" + home.getString("name") + "'"
                    + " WHERE ID = " + id);

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

            final ResultSet result = s.executeQuery("SELECT * FROM CPAO.HOME ORDER BY NAME");
            final JsonArray array = new JsonArray();

            while (result.next()) {
                final Home home = new Home();
                home.setId(result.getInt("id"));
                home.setName(result.getString("NAME"));

                array.add(home);
            }

            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }

    }

}
