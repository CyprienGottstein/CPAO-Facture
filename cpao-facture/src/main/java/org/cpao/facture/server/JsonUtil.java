/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 *
 * @author Cyprien
 */
public class JsonUtil {

    public JsonObject parseActivityToJson(String filename) {

        final JsonObject o = new JsonObject();

        try (Stream<String> stream = Files.lines(Paths.get(filename), Charset.forName("UTF-8"))) {

            stream.forEach(line -> {
                final String[] data = line.replace(":", "").split("\t");

                final String key = data[0];
                switch (key) {
                    case "ref":
                        o.put(key, data[1]);
                        break;
                    case "nom":
                        o.put("label", data[1]);
                        break;
                    case "prix":
                        break;
                    default:
                        switch (data[1]) {
                            case "licence":
                                o.put("licenceCost", data[2]);
                                break;
                            case "cotisation":
                                o.put("cotisationCost", data[2]);
                                break;
                            default:
                                o.put(data[1], data[2]);
                                break;
                        }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return o;
    }

    public JsonObject parseInsuranceToJson(String filename) {

        final JsonObject o = new JsonObject();

        try (Stream<String> stream = Files.lines(Paths.get(filename), Charset.forName("UTF-8"))) {

            stream.forEach(line -> {
                System.out.println("line : " + line);
                final String[] data = line.replace(":", "").split("\t");

                if (data[0].equals("nom")) {
                    o.put("label", data[1].split(" - ")[1]);
                }

                if (data[0].equals("")) {
                    final String key = data[1];
                    if (key.equals("garantie_de_personne")) {
                        o.put("insurance_cost", Float.parseFloat(data[2]));
                    }
                }

            });

        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println(o.encodePrettily());
        return o;
    }

    public JsonArray parsePeopleToJson(String filename) {

        final JsonArray array = new JsonArray();

        try (Stream<String> stream = Files.lines(Paths.get(filename), Charset.forName("UTF-8"))) {

            stream.forEach(new Consumer<String>() {

                boolean nameFlag = false;
                String lastname = "NOM";

                @Override
                public void accept(String line) {
                    final String[] data = line.replace(":", "").split("\t");

                    if (nameFlag) {
                        if (data.length == 1) {
                            nameFlag = false;
                        } else {
                            if (data.length == 2) {
                                final JsonObject o = new JsonObject();
                                o.put("lastname", lastname);
                                o.put("firstname", data[1]);
                                o.put("birthday", Instant.now().toEpochMilli());
                                array.add(o);
                            }
                        }

                    } else {
                        System.out.println(data[0]);
                        if (data[0].equals("Nom")) {
                            System.out.println("PROC : " + data[1]);
                            lastname = data[1];
                        }
                        if (data[0].equals("Adherent")) {
                            nameFlag = true;
                        }
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return array;
    }

    public JsonObject parseHomeToJson(String filename) {

        final JsonObject o = new JsonObject();

        try (Stream<String> stream = Files.lines(Paths.get(filename), Charset.forName("UTF-8"))) {

            stream.forEach(line -> {
                final String[] data = line.replace(":", "").split("\t");

                if (data[0].equals("Nom")) {
                    o.put("name", data[1]);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return o;
    }

    public JsonObject parseFileToJson(String filename) {

        StringBuilder content = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filename), Charset.forName("UTF-8"))) {

            stream.forEach(line -> {
                content.append(line);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JsonObject(content.toString());
    }

    public void writeJsonToFile(String filename, JsonObject object) {

        Path path = Paths.get(filename);
        try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"), StandardOpenOption.WRITE)) {
            writer.write(object.encode());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
