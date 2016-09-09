/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cpao.facture.server.JsonUtil;

/**
 *
 * @author Cyprien
 */
public class DatabaseScript {

    protected Vertx vertx;

    public DatabaseScript(Vertx vertx) {
        this.vertx = vertx;
    }

    public void generateDatabase() {

        purgeDatabase();
        generateUser();
        generateSchema();
    }

    public void purgeDatabase() {
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_ADMIN_USER, Database.HSQLDB_ADMIN_PASSWORD)) {

            final Statement s1 = c.createStatement();
            final int i1 = s1.executeUpdate("DROP SCHEMA CPAO CASCADE");
            System.out.println("Previous schema has been purged.");

            final Statement s2 = c.createStatement();
            final int i2 = s2.executeUpdate("DROP USER \"root\"");
            System.out.println("Previous user has been purged.");

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generateUser() {
        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_ADMIN_USER, Database.HSQLDB_ADMIN_PASSWORD)) {

            final Statement s = c.createStatement();
            final int i = s.executeUpdate("CREATE USER \"root\" PASSWORD \"root\" ");
            System.out.println("User root created.");

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generateSchema() {

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_ADMIN_USER, Database.HSQLDB_ADMIN_PASSWORD)) {

            Statement s = c.createStatement();

            final int i = s.executeUpdate("CREATE SCHEMA CPAO AUTHORIZATION \"root\"\n"
                    + "        CREATE TABLE ACTIVITY(ID INTEGER PRIMARY KEY, LABEL VARCHAR(300), LICENCE_COST DECIMAL, COTISATION_COST DECIMAL, SEASON INTEGER)\n"
                    + "        CREATE TABLE ADHERENT(ID INTEGER PRIMARY KEY, HOME INTEGER, FIRSTNAME VARCHAR(100), LASTNAME VARCHAR(100))\n"
                    + "        CREATE TABLE HOME(ID INTEGER PRIMARY KEY, LABEL VARCHAR(100))\n"
                    + "        CREATE TABLE HOME_ADHERENT(ID_HOME INTEGER, ID_ADHERENT INTEGER)\n"
                    + "        CREATE TABLE INSURANCE(ID INTEGER PRIMARY KEY, LABEL VARCHAR(300), INSURANCE_COST DECIMAL, SEASON INTEGER)\n"
                    + "        CREATE TABLE MAGAZINE(SEASON INTEGER PRIMARY KEY, MAGAZINE_COST DECIMAL)\n"
                    + "        CREATE TABLE ACTIVITY_ADHERENT(ID_ACTIVITY INTEGER, ID_ADHERENT INTEGER, ID_INSURANCE INTEGER, MAGAZINE BOOLEAN, TEACHER BOOLEAN, OBSERVATOR BOOLEAN, FAMILY BOOLEAN)\n"
                    + "        CREATE SEQUENCE SEQ_ACTIVITY AS INTEGER START WITH 0 INCREMENT BY 1\n"
                    + "        CREATE SEQUENCE SEQ_INSURANCE AS INTEGER START WITH 0 INCREMENT BY 1\n"
                    + "        GRANT ALL ON ACTIVITY TO \"root\"\n"
                    + "        GRANT ALL ON ADHERENT TO \"root\"\n"
                    + "        GRANT ALL ON HOME TO \"root\"\n"
                    + "        GRANT ALL ON INSURANCE TO \"root\"\n"
                    + "        GRANT ALL ON MAGAZINE TO \"root\"\n"
                    + "        GRANT ALL ON HOME_ADHERENT TO \"root\"\n"
                    + "        GRANT ALL ON ACTIVITY_ADHERENT TO \"root\"\n");
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void retrieveActivity() {
        final EventBus bus = vertx.eventBus();

        JsonUtil util = new JsonUtil();

        Path path = Paths.get("C:\\Users\\Cyprien\\Desktop\\Facture 2015-16 - VERSION CYPRIEN\\activite");
        try {
            DirectoryStream<Path> stream;
            stream = Files.newDirectoryStream(path);
            stream
                    .forEach(p -> {
                        if (p.toString().endsWith("_0.txt")) {
                            System.out.println("path : " + p.toString());
                            final JsonObject activity = util.parseActivityToJson(p.toString());
                            final String label = activity.getString("nom").split(" - ")[0];
                            activity.put("nom", label);
                            bus.send("org.cpao.facture.server.dao.activity.ActivityDaoVerticle-save", activity);
                        }
                    });
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void retrieveInsurance() {
        final EventBus bus = vertx.eventBus();

        JsonUtil util = new JsonUtil();

        Path path = Paths.get("C:\\Users\\Cyprien\\Desktop\\Facture 2015-16 - VERSION CYPRIEN\\activite");
        try {
            DirectoryStream<Path> stream;
            stream = Files.newDirectoryStream(path);
            stream
                    .forEach(p -> {
                        if (p.toString().contains("ESC1_")) {
                            System.out.println("path : " + p.toString());
                            final JsonObject insurance = util.parseInsuranceToJson(p.toString());
                            final String label = insurance.getString("label").split(" - ")[0];
                            insurance.put("label", label);
                            
                            System.out.println("insurance : " + insurance.encodePrettily());
                            bus.send("org.cpao.facture.server.dao.insurance.InsuranceDaoVerticle-save", insurance);
                        }
                    });
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}