/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.payment;

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
import org.cpao.facture.server.model.Payment;

/**
 *
 * @author Cyprien
 */
public class PaymentDaoImpl implements PaymentDao {

    @Override
    public int save(JsonObject payment) {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            System.out.println("Saving : " + payment.encodePrettily());

            final Statement s = c.createStatement();

            final int result = s.executeUpdate("INSERT INTO CPAO.PAYMENT (ID, ID_HOME, SEASON, ID_TYPE, AMOUNT, METADATA, SOLDED) VALUES ( "
                    + "NEXT VALUE FOR CPAO.SEQ_PAYMENT, "
                    + payment.getInteger("idHome") + ", "
                    + payment.getInteger("season") + ", "
                    + payment.getInteger("idType") + ", "
                    + Float.parseFloat(payment.getString("amount")) + ", "
                    + "'" + payment.getString("metadata") + "', "
                    + payment.getBoolean("solded") + ")");

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

            final int result = s.executeUpdate("DELETE FROM CPAO.PAYMENT WHERE ID = " + id);

            return result;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

    }

    @Override
    public int update(int id, JsonObject payment) {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();
            //ID, ID_HOME, SEASON, TYPE, AMOUNT, METADATA, SOLDED
            System.out.println(payment.encodePrettily());
            
            final int result = s.executeUpdate("UPDATE CPAO.PAYMENT SET ID_HOME = " + payment.getInteger("idHome") + ","
                    + "SEASON = " + payment.getInteger("season") + ","
                    + "ID_TYPE = " + payment.getInteger("idType") + ","
                    + "AMOUNT = " + Float.parseFloat(payment.getString("amount")) + ","
                    + "METADATA = '" + payment.getString("metadata") + "',"
                    + "SOLDED = " + payment.getBoolean("solded")
                    + " WHERE ID = " + id);

            return result;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

    }

    @Override
    public JsonArray loadByHome(int home) {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("SELECT * FROM CPAO.PAYMENT WHERE ID_HOME = " + home);
            final JsonArray array = new JsonArray();

            while (result.next()) {
                final Payment payment = new Payment();
                payment.setId(result.getInt("ID"));
                payment.setIdHome(result.getInt("ID_HOME"));
                payment.setSeason(result.getInt("SEASON"));
                payment.setType(result.getInt("ID_TYPE"));
                payment.setAmount(result.getFloat("AMOUNT"));
                payment.setMetadata(result.getString("METADATA"));
                payment.setSolded(result.getBoolean("SOLDED"));
                array.add(payment);
            }

            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }

    }

    @Override
    public JsonArray loadAll() {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("SELECT * FROM CPAO.PAYMENT ORDER BY ID");
            final JsonArray array = new JsonArray();

            while (result.next()) {
                final Payment payment = new Payment();
                payment.setId(result.getInt("ID"));
                payment.setIdHome(result.getInt("ID_HOME"));
                payment.setSeason(result.getInt("SEASON"));
                payment.setType(result.getInt("ID_TYPE"));
                payment.setAmount(result.getFloat("AMOUNT"));
                payment.setMetadata(result.getString("METADATA"));
                payment.setSolded(result.getBoolean("SOLDED"));
                array.add(payment);
            }

            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }

    }

}
