package dev.lexi.launcher.data.version;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VersionList {

    public List<Version> available_releases = new ArrayList<>();

    public void populate() {
        available_releases = getReleases();
    }

    public Version getVersionById(String id) {
        return available_releases.stream()
                .filter(v -> v.versionId.equals(id))
                .findFirst()
                .orElse(new FallbackVersion());
    }

    private List<Version> getReleases() {
        List<Version> releaseVersions = new ArrayList<>();
        try {
            // Reading the JSON file into a String
            FileReader reader = new FileReader("launcher_data/data/version_manifest_v2.json");
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            // Extracting the "versions" array from the JSON
            JsonArray versions = jsonObject.getAsJsonArray("versions");

            // Iterating over the versions and filtering for "release" type
            for (JsonElement versionElement : versions) {
                JsonObject versionObject = versionElement.getAsJsonObject();
                String type = versionObject.get("type").getAsString();
                if ("release".equals(type)) {
                    String id = versionObject.get("id").getAsString();
                    String url = versionObject.get("url").getAsString();
                    String sha1 = versionObject.get("sha1").getAsString();

                    releaseVersions.add(new Version(id, url, sha1));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return releaseVersions;
    }

}
