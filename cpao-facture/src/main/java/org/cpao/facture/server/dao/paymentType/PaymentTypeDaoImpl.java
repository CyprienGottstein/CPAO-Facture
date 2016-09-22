/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.paymentType;

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
import org.cpao.facture.server.model.PaymentType;

/**
 *
 * @author Cyprien
 */
public class PaymentTypeDaoImpl implements PaymentTypeDao {

    @Override
    public int save(JsonObject payment) {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            System.out.println("Saving : " + payment.encodePrettily());

            final Statement s = c.createStatement();

            final int result = s.executeUpdate("INSERT INTO CPAO.PAYMENT_TYPE (ID, NAME) VALUES ( "
                    + "NEXT VALUE FOR CPAO.SEQ_PAYMENT_TYPE, "
                    + "'" + payment.getString("name") + "')");

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

            final int result = s.executeUpdate("DELETE FROM CPAO.PAYMENT_TYPE WHERE ID = " + id);

            return result;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

    }

    @Override
    public int update(int id, JsonObject paymentType) {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();
            //ID, ID_HOME, SEASON, TYPE, AMOUNT, METADATA, SOLDED
            
            final int result = s.executeUpdate("UPDATE CPAO.PAYMENT_TYPE SET NAME = " + paymentType.getString("name")
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

            final ResultSet result = s.executeQuery("SELECT * FROM CPAO.PAYMENT_TYPE ORDER BY ID");
            final JsonArray array = new JsonArray();

            while (result.next()) {
                final PaymentType paymentType = new PaymentType();
                paymentType.setId(result.getInt("ID"));
                paymentType.setName(result.getString("NAME"));
                array.add(paymentType);
            }

            return array;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return new JsonArray();
        }

    }

}
