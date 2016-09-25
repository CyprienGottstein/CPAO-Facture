/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.service.billGenerator;

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

/**
 *
 * @author Cyprien
 */
public class BillGeneratorDaoImpl implements BillGeneratorDao {

    @Override
    public JsonArray retrieveHomeActivities(int home, int season) {
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("SELECT CPAO.ACTIVITY_PERSON.TEACHER AS TEACHER,"
                    + "CPAO.ACTIVITY_PERSON.OBSERVATOR AS OBSERVATOR, CPAO.ACTIVITY_PERSON.FAMILY AS FAMILY, CPAO.INSURANCE.INSURANCE_COST AS INSURANCE_COST, CPAO.ACTIVITY.LICENCE_COST AS LICENCE_COST, CPAO.ACTIVITY.COTISATION_COST AS COTISATION_COST "
                    + "FROM CPAO.PERSON, CPAO.ACTIVITY_PERSON, CPAO.ACTIVITY, CPAO.INSURANCE "
                    + "WHERE CPAO.PERSON.ID_HOME = " + home + " "
                    + "AND CPAO.ACTIVITY_PERSON.SEASON = " + season + " "
                    + "AND CPAO.ACTIVITY_PERSON.ID_PERSON = CPAO.PERSON.ID "
                    + "AND CPAO.ACTIVITY_PERSON.ID_INSURANCE = CPAO.INSURANCE.ID "
                    + "AND CPAO.ACTIVITY_PERSON.ID_ACTIVITY = CPAO.ACTIVITY.ID");

            final JsonArray array = new JsonArray();

            while (result.next()) {
                final JsonObject line = new JsonObject();
                line.put("insuranceCost", result.getFloat("INSURANCE_COST"));
                line.put("licenceCost", result.getFloat("LICENCE_COST"));
                line.put("cotisationCost", result.getFloat("COTISATION_COST"));
                line.put("teacher", result.getFloat("TEACHER"));
                line.put("observator", result.getFloat("OBSERVATOR"));
                line.put("family", result.getFloat("FAMILY"));

                array.add(line);
            }

            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }
    }

    @Override
    public JsonArray retrieveHomePayments(int home, int season) {
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("SELECT CPAO.PAYMENT.AMOUNT AS AMOUNT, CPAO.PAYMENT.SOLDED AS SOLDED\n"
                    + "FROM CPAO.HOME, CPAO.PAYMENT\n"
                    + "WHERE CPAO.HOME.ID = " + home + " "
                    + "AND CPAO.PAYMENT.SEASON = " + season + " "
                    + "AND CPAO.HOME.ID = CPAO.PAYMENT.ID_HOME");

            final JsonArray array = new JsonArray();

            while (result.next()) {
                final JsonObject line = new JsonObject();
                line.put("amount", result.getFloat("AMOUNT"));
                line.put("solded", result.getBoolean("SOLDED"));

                array.add(line);
            }

            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }
    }

    @Override
    public JsonArray retrieveAllActivities(int season) {
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("SELECT CPAO.PERSON.ID_HOME AS ID_HOME, CPAO.HOME.NAME AS NAME_HOME, CPAO.ACTIVITY_PERSON.TEACHER AS TEACHER,"
                    + "CPAO.ACTIVITY_PERSON.OBSERVATOR AS OBSERVATOR, CPAO.ACTIVITY_PERSON.FAMILY AS FAMILY, CPAO.INSURANCE.INSURANCE_COST AS INSURANCE_COST, CPAO.ACTIVITY.LICENCE_COST AS LICENCE_COST, CPAO.ACTIVITY.COTISATION_COST AS COTISATION_COST "
                    + "FROM CPAO.PERSON, CPAO.HOME, CPAO.ACTIVITY_PERSON, CPAO.ACTIVITY, CPAO.INSURANCE "
                    + "WHERE CPAO.ACTIVITY_PERSON.SEASON = " + season + " "
                    + "AND CPAO.PERSON.ID_HOME = CPAO.HOME.ID "
                    + "AND CPAO.ACTIVITY_PERSON.ID_PERSON = CPAO.PERSON.ID "
                    + "AND CPAO.ACTIVITY_PERSON.ID_INSURANCE = CPAO.INSURANCE.ID "
                    + "AND CPAO.ACTIVITY_PERSON.ID_ACTIVITY = CPAO.ACTIVITY.ID "
                    + "ORDER BY CPAO.HOME.ID");

            final JsonArray array = new JsonArray();

            while (result.next()) {
                final JsonObject line = new JsonObject();
                line.put("idHome", result.getInt("ID_HOME"));
                line.put("nameHome", result.getString("NAME_HOME"));
                line.put("insuranceCost", result.getFloat("INSURANCE_COST"));
                line.put("licenceCost", result.getFloat("LICENCE_COST"));
                line.put("cotisationCost", result.getFloat("COTISATION_COST"));
                line.put("teacher", result.getFloat("TEACHER"));
                line.put("observator", result.getFloat("OBSERVATOR"));
                line.put("family", result.getFloat("FAMILY"));

                array.add(line);
            }

            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }
    }

    @Override
    public JsonArray retrieveAllPayments(int season) {
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("SELECT CPAO.HOME.ID AS ID_HOME, CPAO.PAYMENT.AMOUNT AS AMOUNT, CPAO.PAYMENT.SOLDED AS SOLDED\n"
                    + "FROM CPAO.HOME, CPAO.PAYMENT\n"
                    + "WHERE CPAO.PAYMENT.SEASON = " + season + " "
                    + "AND CPAO.HOME.ID = CPAO.PAYMENT.ID_HOME "
                    + "ORDER BY CPAO.HOME.ID");

            final JsonArray array = new JsonArray();

            while (result.next()) {
                final JsonObject line = new JsonObject();
                line.put("idHome", result.getInt("ID_HOME"));
                line.put("amount", result.getFloat("AMOUNT"));
                line.put("solded", result.getBoolean("SOLDED"));

                array.add(line);
            }

            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }
    }

    @Override
    public JsonObject retrieveHome(final int home) {
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("SELECT CPAO.HOME.ID AS ID, CPAO.HOME.NAME AS NAME\n"
                    + "FROM CPAO.HOME\n"
                    + "WHERE CPAO.HOME.ID = " + home);

            final JsonObject o = new JsonObject();

            while (result.next()) {
                o.put("id", result.getInt("ID"));
                o.put("name", result.getString("NAME"));
            }

            return o;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonObject();
        }
    }

    @Override
    public JsonArray retrieveAllHomesWithInputInSeason(final int season) {
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("SELECT CPAO.HOME.ID AS ID, CPAO.HOME.NAME\n"
                    + "FROM CPAO.HOME\n"
                    + "WHERE ( CPAO.HOME.ID IN ( SELECT CPAO.PERSON.ID_HOME\n"
                    + "	FROM CPAO.PERSON, CPAO.ACTIVITY_PERSON\n"
                    + "	WHERE CPAO.PERSON.ID = CPAO.ACTIVITY_PERSON.ID_PERSON\n"
                    + "	AND CPAO.ACTIVITY_PERSON.SEASON = " + season + " )\n"
                    + "	OR CPAO.HOME.ID IN ( SELECT CPAO.PAYMENT.ID_HOME FROM CPAO.PAYMENT WHERE CPAO.PAYMENT.SEASON = " + season + " ) )");

            final JsonArray array = new JsonArray();

            while (result.next()) {
                final JsonObject o = new JsonObject();
                o.put("id", result.getInt("ID"));
                o.put("name", result.getString("NAME"));
                array.add(o);
            }

            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }
    }

}
