package dev.lexi.launcher.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.lexi.launcher.data.Urls;
import dev.lexi.launcher.data.version.Version;
import dev.lexi.launcher.init.Initializer;
import dev.lexi.launcher.utils.file.FileUtil;
import dev.lexi.launcher.utils.file.ZipUtil;
import dev.lexi.launcher.utils.file.hash.ShaUtil;
import dev.lexi.launcher.utils.json.GsonUtil;
import dev.lexi.launcher.utils.web.WebUtil;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class VersionManager {

    public static VersionManager instance = new VersionManager();

    public void init() {
        FileUtil.createFolder("launcher_data/versions");
    }

    public void launch(Version version) {
        try {
            System.out.println("[!] Launching " + version.versionId);
            String id = version.versionId;

            FileUtil.createFolder("launcher_data/versions/" + id);

            String jsonPath = "launcher_data/versions/" + id + "/" + id + ".json";
            String jsonHash = version.versionJsonSha1;

            if (!FileUtil.fileExists(jsonPath)) {
                WebUtil.download(version.versionJsonUrl, jsonPath);
            } else {
                if (!ShaUtil.checkFileSHA1(jsonPath, jsonHash)) {
                    JOptionPane.showMessageDialog(null, "Hash of '" + id + ".json' is invalid.");
                    return;
                }
            }

            String jarPath = "launcher_data/versions/" + id + "/" + id + ".jar";
            if (!FileUtil.fileExists(jarPath)) {
                try {
                    JsonObject json = GsonUtil.parseJsonFromFile(jsonPath);
                    JsonObject downloads = json.getAsJsonObject("downloads");
                    JsonObject client = downloads.getAsJsonObject("client");

                    String clientUrl = client.get("url").getAsString();
                    String clientSha1 = client.get("sha1").getAsString();

                    WebUtil.download(clientUrl, jarPath);

                    if (!ShaUtil.checkFileSHA1(jarPath, clientSha1)) {
                        JOptionPane.showMessageDialog(null, "Hash of '" + id + ".jar' is invalid after download.");
                        return;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Failed to parse version JSON or download client JAR.");
                    return;
                }
            } else {
                try {
                    JsonObject json = GsonUtil.parseJsonFromFile(jsonPath);
                    JsonObject downloads = json.getAsJsonObject("downloads");
                    JsonObject client = downloads.getAsJsonObject("client");

                    String clientSha1 = client.get("sha1").getAsString();

                    if (!ShaUtil.checkFileSHA1(jarPath, clientSha1)) {
                        JOptionPane.showMessageDialog(null, "Hash of '" + id + ".jar' is invalid.");
                        return;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Failed to parse version JSON or validate client JAR.");
                    return;
                }
            }

            System.out.println("[!] Fetching libraries...");
            LibraryManager.instance.fetchLibraries(jsonPath, "launcher_data/libraries");

            System.out.println("[!] Fetching natives...");
            FileUtil.createFolder("launcher_data/versions/" + id + "/natives");
            WebUtil.download(Urls.NATIVES_REPOSITORY + id + "-natives.zip", "launcher_data/versions/" + id + "/natives/" + id + "-natives.zip");
            try {
                ZipUtil.unzip("launcher_data/versions/" + id + "/natives/" + id + "-natives.zip");
                FileUtil.deleteFolder("launcher_data/versions/" + id + "/natives/" + id + "-natives.zip");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            JsonObject json = GsonUtil.parseJsonFromFile(jsonPath);

            // Get main class
            String mainClass = json.get("mainClass").getAsString();
            System.out.println("[!] Main class: " + mainClass);

            // Get arguments
            String arguments = getArguments(json, id);
            System.out.println("[!] Arguments: " + arguments);

            // Build classpath
            JsonArray libraries = json.getAsJsonArray("libraries");
            String classpath = buildClasspath(jarPath, libraries);
            System.out.println("[!] Classpath: " + classpath);

            // Launch Minecraft
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "java",
                    "-Djava.library.path=launcher_data/versions/" + id + "/natives",
                    "-cp", classpath,
                    mainClass,
                    arguments
            );

            processBuilder.redirectErrorStream(true);
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            processBuilder.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to parse JSON or launch the game: " + e.getMessage());
        }
    }

    public void launch(String id) {
        launch(Initializer.instance.versionList.getVersionById(id));
    }

    private String buildClasspath(String versionJarPath, JsonArray libraries) {
        StringBuilder classpath = new StringBuilder(versionJarPath);

        File librariesFolder = new File("launcher_data/libraries");
        if (librariesFolder.exists() && librariesFolder.isDirectory()) {
            // Iterate through all libraries listed in the version JSON
            for (int i = 0; i < libraries.size(); i++) {
                JsonObject library = libraries.get(i).getAsJsonObject();
                JsonObject downloads = library.getAsJsonObject("downloads");
                JsonObject artifact = downloads.getAsJsonObject("artifact");

                if (artifact != null) {
                    String path = artifact.get("path").getAsString();
                    File jarFile = new File(librariesFolder, path);

                    if (jarFile.exists() && jarFile.isFile()) {
                        classpath.append(File.pathSeparator).append(jarFile.getAbsolutePath());
                    } else {
                        System.out.println("[!] Library JAR not found: " + jarFile.getAbsolutePath());
                    }
                }
            }
        }

        return classpath.toString();
    }

    private String getArguments(JsonObject json, String versionId) {
        StringBuilder arguments = new StringBuilder();

        // Handle 'game' arguments
        if (json.has("minecraftArguments")) {
            String minecraftArgs = json.getAsJsonPrimitive("minecraftArguments").getAsString();
            arguments.append(replacePlaceholders(minecraftArgs, versionId));
        } else {
            if (json.has("arguments") && json.getAsJsonObject("arguments").has("game")) {
                JsonArray gameArgs = json.getAsJsonObject("arguments").getAsJsonArray("game");

                for (int i = 0; i < gameArgs.size(); i++) {
                    JsonElement arg = gameArgs.get(i);

                    if (arg.isJsonPrimitive()) {
                        String argValue = arg.getAsString();
                        arguments.append(replacePlaceholders(argValue, versionId));
                    } else if (arg.isJsonObject()) {
                        JsonObject rule = arg.getAsJsonObject();
                        if (rule.has("value")) {
                            JsonElement value = rule.get("value");
                            if (value.isJsonArray()) {
                                for (JsonElement valueElement : value.getAsJsonArray()) {
                                    arguments.append(replacePlaceholders(valueElement.getAsString(), versionId));
                                }
                            } else {
                                arguments.append(replacePlaceholders(value.getAsString(), versionId));
                            }
                        }
                    }
                    arguments.append(" ");
                }
            }

            // Handle 'jvm' arguments
            if (json.has("arguments") && json.getAsJsonObject("arguments").has("jvm")) {
                JsonArray jvmArgs = json.getAsJsonObject("arguments").getAsJsonArray("jvm");

                for (int i = 0; i < jvmArgs.size(); i++) {
                    JsonElement arg = jvmArgs.get(i);

                    if (arg.isJsonPrimitive()) {
                        String argValue = arg.getAsString();
                        arguments.append(replacePlaceholders(argValue, versionId));
                    } else if (arg.isJsonObject()) {
                        JsonObject rule = arg.getAsJsonObject();
                        if (rule.has("value")) {
                            JsonElement value = rule.get("value");
                            if (value.isJsonArray()) {
                                for (JsonElement valueElement : value.getAsJsonArray()) {
                                    arguments.append(replacePlaceholders(valueElement.getAsString(), versionId));
                                }
                            } else {
                                arguments.append(replacePlaceholders(value.getAsString(), versionId));
                            }
                        }
                    }
                    arguments.append(" ");
                }
            }
        }

        return arguments.toString().trim();
    }

    private String replacePlaceholders(String value, String versionId) {
        return value
                .replace("${auth_player_name}", "Steve")
                .replace("${version_name}", versionId)
                .replace("${game_directory}", "launcher_data")
                .replace("${assets_root}", "launcher_data/assets")
                .replace("${assets_index_name}", "none")
                .replace("${auth_access_token}", "0")
                .replace("${user_properties}", "{}")
                .replace("${auth_uuid}", "00000000-0000-0000-0000-000000000000")
                .replace("${clientid}", "0")
                .replace("${auth_xuid}", "0")
                .replace("${user_type}", "full")
                .replace("${version_type}", "release")
                .replace("${quickPlayPath}", "")
                .replace("${quickPlaySingleplayer}", "false")
                .replace("${quickPlayMultiplayer}", "false")
                .replace("${quickPlayRealms}", "false")
                .replace("${resolution_width}", "1280")
                .replace("${resolution_height}", "720");
    }

}
