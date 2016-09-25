/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.insurance;

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
import org.cpao.facture.server.model.Insurance;

/**
 *
 * @author Cyprien
 */
public class InsuranceDaoImpl implements InsuranceDao {

    @Override
    public JsonObject save(JsonObject insurance) {
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("INSERT INTO CPAO.INSURANCE (ID, LABEL, INSURANCE_COST, SEASON) VALUES ("
                    + "NEXT VALUE FOR CPAO.SEQ_INSURANCE, "
                    + "'" + insurance.getString("label") + "', "
                    + insurance.getFloat("insurance_cost") + ", "
                    + " 2016); CALL IDENTITY(); ");

            final JsonObject o = new JsonObject();

            while (result.next()) {
                o.put("id", result.getInt(1));
            }

            o.put("status", 1);
            return o;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonObject().put("status", -1);
        }
    }

    @Override
    public int remove(int id) {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final int result = s.executeUpdate("DELETE FROM CPAO.INSURANCE WHERE ID = " + id);

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
            // LABEL, INSURANCE_COST, SEASON
            final int result = s.executeUpdate("UPDATE CPAO.INSURANCE SET SEASON = " + activity.getInteger("season") + ","
                    + "LABEL = '" + activity.getString("label") + "',"
                    + "INSURANCE_COST = " + Float.parseFloat(activity.getString("insuranceCost"))
                    + " WHERE ID = " + id);

            return result;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

    }

    @Override
    public JsonArray loadBySeason(int season) {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("SELECT * FROM CPAO.INSURANCE WHERE SEASON = " + season);
            final JsonArray array = new JsonArray();

            while (result.next()) {
                final Insurance insurance = new Insurance();
                insurance.setId(result.getInt("id"));
                insurance.setSeason(result.getInt("SEASON"));
                insurance.setLabel(result.getString("LABEL"));
                insurance.setInsuranceCost(result.getFloat("INSURANCE_COST"));

                array.add(insurance);
            }

            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }

    }

    @Override
    public JsonObject loadSingle(int id) {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("SELECT * FROM CPAO.INSURANCE WHERE ID = " + id);
            final Insurance insurance = new Insurance();

            if (result.next()) {
                insurance.setId(result.getInt("id"));
                insurance.setSeason(result.getInt("SEASON"));
                insurance.setLabel(result.getString("LABEL"));
                insurance.setInsuranceCost(result.getFloat("INSURANCE_COST"));
            }

            return insurance;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonObject();
        }

    }

    @Override
    public JsonArray loadAll() {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("SELECT * FROM CPAO.INSURANCE ORDER BY SEASON");
            final JsonArray array = new JsonArray();

            while (result.next()) {
                final Insurance insurance = new Insurance();
                insurance.setId(result.getInt("id"));
                insurance.setSeason(result.getInt("SEASON"));
                insurance.setLabel(result.getString("LABEL"));
                insurance.setInsuranceCost(result.getFloat("INSURANCE_COST"));

                array.add(insurance);
            }

            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }

    }

}
