package com.horstmann.places;

/**

javac -p mods/jakarta.json.bind-api-2.0.0.jar \
    ch15.sec06/module-info.java \
    ch15.sec06/com/horstmann/places/Demo.java \
    ch15.sec06/com/horstmann/places/Country.java

java -p mods/jakarta.json-api-2.0.1.jar:\
mods/jakarta.json.bind-api-2.0.0.jar:\
mods/jakarta.json-2.0.1-module.jar:\
mods/yasson-2.0.3.jar:v2ch09.openpkg2:\
ch15.sec06 \
    -m ch15.sec06/com.horstmann.places.Demo

*/

import jakarta.json.bind.*;
import jakarta.json.bind.config.*;
import java.lang.reflect.*;

public class Demo {
    public static void main(String[] args) {
        var belgium = new Country("Belgium", 30510);
        var config = new JsonbConfig()
            .withPropertyVisibilityStrategy(
                new PropertyVisibilityStrategy() {
                    public boolean isVisible(Field field) { return true; }
                    public boolean isVisible(Method method) { return false; }
                });
        Jsonb jsonb = JsonbBuilder.create(config);
        String json = jsonb.toJson(belgium);
        System.out.println(json);
    }
}