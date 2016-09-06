/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cpao.facture.server;

import io.vertx.core.json.JsonObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
                        o.put(key, data[1]);
                        break;
                    case "prix":
                        break;
                    default:
                        o.put(data[1], Float.parseFloat(data[2]));
                        break;
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println(o.encodePrettily());
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
