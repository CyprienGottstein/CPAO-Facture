/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server.dao;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
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
                    + "        CREATE TABLE ACTIVITY(ID INTEGER IDENTITY PRIMARY KEY, LABEL VARCHAR(300), LICENCE_COST DOUBLE, COTISATION_COST DOUBLE, SEASON INTEGER)\n"
                    + "        CREATE TABLE HOME(ID INTEGER IDENTITY PRIMARY KEY, NAME VARCHAR(100))\n"
                    + "        CREATE TABLE PERSON(ID INTEGER IDENTITY PRIMARY KEY, ID_HOME INTEGER, FIRSTNAME VARCHAR(100), LASTNAME VARCHAR(100), BIRTHDAY BIGINT"
                    + "        , CONSTRAINT HOME_PERSON FOREIGN KEY (ID_HOME) REFERENCES CPAO.HOME (ID) ON DELETE CASCADE ON UPDATE CASCADE)"
                    + "        CREATE TABLE INSURANCE(ID INTEGER IDENTITY PRIMARY KEY, LABEL VARCHAR(300), INSURANCE_COST DOUBLE, SEASON INTEGER)\n"
                    + "        CREATE TABLE ACTIVITY_PERSON(ID INTEGER IDENTITY PRIMARY KEY, ID_ACTIVITY INTEGER, ID_PERSON INTEGER, ID_INSURANCE INTEGER, TEACHER BOOLEAN, OBSERVATOR BOOLEAN, FAMILY BOOLEAN, SEASON INTEGER"
                    + "        , CONSTRAINT SAME_ACTIVITY_EVERYWHERE FOREIGN KEY (ID_ACTIVITY) REFERENCES CPAO.ACTIVITY (ID) ON DELETE CASCADE ON UPDATE CASCADE"
                    + "        , CONSTRAINT SAME_INSURANCE_EVERYWHERE FOREIGN KEY (ID_INSURANCE) REFERENCES CPAO.INSURANCE (ID) ON DELETE CASCADE ON UPDATE CASCADE"
                    + "        , CONSTRAINT SAME_PERSON_EVERYWHERE FOREIGN KEY (ID_PERSON) REFERENCES CPAO.PERSON (ID) ON DELETE CASCADE ON UPDATE CASCADE)"
                    + "        CREATE TABLE PAYMENT(ID INTEGER IDENTITY PRIMARY KEY, ID_HOME INTEGER, SEASON INTEGER, ID_TYPE INTEGER, AMOUNT DOUBLE, METADATA VARCHAR(1000), SOLDED BOOLEAN"
                    + "        , CONSTRAINT HOME_PAYMENT FOREIGN KEY (ID_HOME) REFERENCES CPAO.HOME (ID) ON DELETE CASCADE ON UPDATE CASCADE)\n"
                    + "        CREATE TABLE PAYMENT_TYPE(ID INTEGER IDENTITY PRIMARY KEY, NAME VARCHAR(100))\n"
                    + "        CREATE SEQUENCE SEQ_ACTIVITY AS INTEGER START WITH 0 INCREMENT BY 1\n"
                    + "        CREATE SEQUENCE SEQ_INSURANCE AS INTEGER START WITH 0 INCREMENT BY 1\n"
                    + "        CREATE SEQUENCE SEQ_PERSON AS INTEGER START WITH 0 INCREMENT BY 1\n"
                    + "        CREATE SEQUENCE SEQ_HOME AS INTEGER START WITH 0 INCREMENT BY 1\n"
                    + "        CREATE SEQUENCE SEQ_ACTIVITY_PERSON AS INTEGER START WITH 0 INCREMENT BY 1\n"
                    + "        CREATE SEQUENCE SEQ_PAYMENT AS INTEGER START WITH 0 INCREMENT BY 1\n"
                    + "        CREATE SEQUENCE SEQ_PAYMENT_TYPE AS INTEGER START WITH 0 INCREMENT BY 1\n"
                    + "        GRANT ALL ON ACTIVITY TO \"root\"\n"
                    + "        GRANT ALL ON PERSON TO \"root\"\n"
                    + "        GRANT ALL ON HOME TO \"root\"\n"
                    + "        GRANT ALL ON INSURANCE TO \"root\"\n"
                    + "        GRANT ALL ON PAYMENT TO \"root\"\n"
                    + "        GRANT ALL ON PAYMENT_TYPE TO \"root\"\n"
                    + "        GRANT ALL ON ACTIVITY_PERSON TO \"root\"\n");

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
                            final String label = activity.getString("label").split(" - ")[0];
                            activity.put("label", label);
                            activity.put("season", 2016);
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
    
    public void addPaymentTypeBatch() {
        
        final JsonArray paymentTypes = new JsonArray();
        paymentTypes.add("Chèque bancaire");
        paymentTypes.add("Chèque vacance");
        paymentTypes.add("Coupon sport");
        paymentTypes.add("Cart@too");
        paymentTypes.add("Espèce");

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final StringBuilder query = new StringBuilder();

            for (int i = 0; i < paymentTypes.size(); i++) {
                final String paymentType = paymentTypes.getString(i);
                query
                        .append("INSERT INTO CPAO.PAYMENT_TYPE (ID, NAME) VALUES (")
                        .append("NEXT VALUE FOR CPAO.SEQ_PAYMENT_TYPE, ")
                        .append("'")
                        .append(paymentType)
                        .append("' );");
            }

            final int result = s.executeUpdate(query.toString());

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void retrieveHomeBatch() {
        final EventBus bus = vertx.eventBus();
        final JsonArray homes = new JsonArray();

        JsonUtil util = new JsonUtil();

        Path path = Paths.get("C:\\Users\\Cyprien\\Desktop\\Facture 2015-16 - VERSION CYPRIEN\\familleMySave");
        try {
            DirectoryStream<Path> stream;
            stream = Files.newDirectoryStream(path);
            stream
                    .forEach(p -> {
                        if (!p.toString().contains("DS_Store")) {
                            final JsonObject home = util.parseHomeToJson(p.toString());
                            homes.add(home);
                        }
                    });
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("homes : " + homes.encodePrettily());

        try (Connection c = DriverManager.getConnection(Database.HSQLDB_URL, Database.HSQLDB_CPAO_USER, Database.HSQLDB_CPAO_PASSWORD)) {

            final Statement s = c.createStatement();

            final StringBuilder query = new StringBuilder();

            for (int i = 0; i < homes.size(); i++) {
                final JsonObject home = homes.getJsonObject(i);
                query
                        .append("INSERT INTO CPAO.HOME (ID, NAME) VALUES (")
                        .append("NEXT VALUE FOR CPAO.SEQ_HOME, ")
                        .append("'")
                        .append(home.getString("name"))
                        .append("' );");
            }

            final int result = s.executeUpdate(query.toString());

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseScript.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void retrieveHome() {
        final EventBus bus = vertx.eventBus();

        JsonUtil util = new JsonUtil();

        Path path = Paths.get("C:\\Users\\Cyprien\\Desktop\\Facture 2015-16 - VERSION CYPRIEN\\familleMySave");
        try {
            DirectoryStream<Path> stream;
            stream = Files.newDirectoryStream(path);
            stream
                    .forEach(p -> {
                        if (!p.toString().contains("DS_Store")) {
                            final JsonObject home = util.parseHomeToJson(p.toString());
                            bus.send("org.cpao.facture.server.dao.home.HomeDaoVerticle-save", home);
                        }
                    });
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void retrievePeople() {
        final EventBus bus = vertx.eventBus();

        JsonUtil util = new JsonUtil();

        bus.send("org.cpao.facture.server.dao.home.HomeDaoVerticle-load-all", null, (AsyncResult<Message<JsonArray>> result) -> {
            final JsonArray array = result.result().body();
            System.out.println("homes : " + array.encodePrettily());
            Path path = Paths.get("C:\\Users\\Cyprien\\Desktop\\Facture 2015-16 - VERSION CYPRIEN\\familleMySave");
            try {
                DirectoryStream<Path> stream;
                stream = Files.newDirectoryStream(path);
                stream
                        .forEach(p -> {
                            if (!p.toString().contains("DS_Store")) {
                                System.out.println("path : " + p.toString());
                                final JsonArray peoples = util.parsePeopleToJson(p.toString());
                                for (int i = 0; i < peoples.size(); i++) {
                                    final JsonObject people = peoples.getJsonObject(i);

                                    int n = 0;
                                    boolean flag = true;
                                    while (n < array.size() && flag) {
                                        final JsonObject home = array.getJsonObject(n);
                                        if (home.getString("name").equals(people.getString("lastname"))) {
                                            people.put("idHome", home.getInteger("id"));
                                            flag = true;
                                        }
                                        n++;
                                    }

                                    System.out.println("people : " + people.encodePrettily());
                                    bus.send("org.cpao.facture.server.dao.people.PeopleDaoVerticle-save", people);

                                }
                            }
                        });

                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
