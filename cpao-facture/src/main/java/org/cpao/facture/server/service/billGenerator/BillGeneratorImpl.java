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
import org.cpao.facture.server.model.People;

/**
 *
 * @author Cyprien
 */
public class BillGeneratorImpl implements BillGenerator {

    @Override
    public JsonArray retrieveHomeData(int home, int season) {
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final ResultSet result = s.executeQuery("SELECT CPAO.ACTIVITY_PERSON.TEACHER AS TEACHER,"
                    + "CPAO.ACTIVITY_PERSON.OBSERVATOR AS OBSERVATOR, CPAO.ACTIVITY_PERSON.FAMILY AS FAMILY, CPAO.INSURANCE.INSURANCE_COST AS INSURANCE_COST, CPAO.ACTIVITY.LICENCE_COST AS LICENCE_COST, CPAO.ACTIVITY.COTISATION_COST AS COTISATION_COST "
                    + "FROM CPAO.HOME_PERSON, CPAO.ACTIVITY_PERSON, CPAO.ACTIVITY, CPAO.INSURANCE "
                    + "WHERE CPAO.HOME_PERSON.ID_HOME = " + home + " "
                    + "AND CPAO.ACTIVITY_PERSON.SEASON = " + season + " "
                    + "AND CPAO.ACTIVITY_PERSON.ID_PERSON = CPAO.HOME_PERSON.ID_PERSON "
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

}
