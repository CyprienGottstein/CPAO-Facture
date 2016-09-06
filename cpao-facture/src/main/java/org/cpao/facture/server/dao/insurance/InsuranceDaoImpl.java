/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao.insurance;

import io.vertx.core.json.JsonObject;
import java.sql.Connection;
import java.sql.DriverManager;
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
public class InsuranceDaoImpl implements InsuranceDao {

    @Override
    public int save(JsonObject insurance) {
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();
            
            final int result = s.executeUpdate("INSERT INTO CPAO.INSURANCE (ID, LABEL, INSURANCE_COST, SEASON) VALUES ("
                    + "NEXT VALUE FOR CPAO.SEQ_INSURANCE, "
                    + "'" + insurance.getString("label") + "', "
                    + insurance.getFloat("insurance_cost") + ", "
                    + " 2016) ");
            
             return result;

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }
    
}
