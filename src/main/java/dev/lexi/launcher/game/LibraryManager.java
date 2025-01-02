package dev.lexi.launcher.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.lexi.launcher.init.Initializer;
import dev.lexi.launcher.utils.file.FileUtil;
import dev.lexi.launcher.utils.file.hash.ShaUtil;
import dev.lexi.launcher.utils.json.GsonUtil;
import dev.lexi.launcher.utils.web.WebUtil;

import javax.swing.*;
import java.io.File;

public class LibraryManager {

    public static LibraryManager instance = new LibraryManager();

    public void fetchLibraries(String jsonPath, String mainLibraryFolder) {
        try {
            JsonObject json = GsonUtil.parseJsonFromFile(jsonPath);
            JsonArray libraries = json.getAsJsonArray("libraries");

            if (libraries == null || libraries.isEmpty()) {
                System.out.println("[!] No libraries found in JSON.");
                return;
            }

            for (int i = 0; i < libraries.size(); i++) {
                JsonObject library = libraries.get(i).getAsJsonObject();
                JsonObject downloads = library.getAsJsonObject("downloads");

                // Process regular artifact
                JsonObject artifact = downloads.getAsJsonObject("artifact");
                if (artifact != null) {
                    processLibrary(artifact, mainLibraryFolder);
                }

                // Process natives if available
                JsonObject classifiers = downloads.getAsJsonObject("classifiers");
                JsonObject natives = library.getAsJsonObject("natives");

                if (classifiers != null && natives != null) {
                    String nativeKey = natives.get(Initializer.os).getAsString();
                    JsonObject nativeArtifact = classifiers.getAsJsonObject(nativeKey);

                    if (nativeArtifact != null) {
                        processLibrary(nativeArtifact, mainLibraryFolder);
                    }
                }
            }

            System.out.println("[!] All libraries processed successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to process libraries: " + e.getMessage());
        }
    }

    private void processLibrary(JsonObject libraryData, String mainLibraryFolder) {
        try {
            String path = libraryData.get("path").getAsString();
            String sha1 = libraryData.get("sha1").getAsString();
            String url = libraryData.get("url").getAsString();

            File libraryFile = new File(mainLibraryFolder, path);
            String libraryFilePath = libraryFile.getAbsolutePath();

            if (!libraryFile.getParentFile().exists()) {
                libraryFile.getParentFile().mkdirs();
            }

            if (!FileUtil.fileExists(libraryFilePath)) {
                System.out.println("[!] Downloading library: " + url);
                WebUtil.download(url, libraryFilePath);
            } else {
                System.out.println("[!] Library already exists, validating: " + libraryFilePath);
                if (!ShaUtil.checkFileSHA1(libraryFilePath, sha1)) {
                    System.out.println("[!] Invalid hash for library: " + libraryFilePath);
                    JOptionPane.showMessageDialog(null, "Hash mismatch for library: " + path);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to process library: " + e.getMessage());
        }
    }

}
