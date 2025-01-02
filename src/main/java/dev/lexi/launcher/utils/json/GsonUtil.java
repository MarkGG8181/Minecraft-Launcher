package dev.lexi.launcher.utils.json;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;

public class GsonUtil {

    public static JsonObject parseJsonFromFile(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (Exception e) {
            throw new IOException("Failed to parse JSON from file: " + filePath, e);
        }
    }

    public static JsonObject getNestedObject(JsonObject jsonObject, String... keys) {
        JsonObject current = jsonObject;
        for (String key : keys) {
            if (current.has(key) && current.get(key).isJsonObject()) {
                current = current.getAsJsonObject(key);
            } else {
                return null; // Key is missing or not a JsonObject
            }
        }
        return current;
    }

    public static String getString(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && jsonObject.get(key).isJsonPrimitive()) {
            return jsonObject.get(key).getAsString();
        }
        return null;
    }

    public static Long getLong(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && jsonObject.get(key).isJsonPrimitive()) {
            return jsonObject.get(key).getAsLong();
        }
        return null;
    }

    public static Boolean getBoolean(JsonObject jsonObject, String key) {
        if (jsonObject.has(key) && jsonObject.get(key).isJsonPrimitive()) {
            return jsonObject.get(key).getAsBoolean();
        }
        return null;
    }

}
