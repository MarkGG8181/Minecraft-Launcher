package dev.lexi.launcher.game;

import dev.lexi.launcher.data.version.Version;
import dev.lexi.launcher.init.Initializer;
import dev.lexi.launcher.utils.file.FileUtil;
import dev.lexi.launcher.utils.web.WebUtil;

public class VersionManager {

    public static VersionManager instance = new VersionManager();

    public void init() {
        FileUtil.createFolder("launcher_data/versions");
    }

    public void launch(Version version) {
        String id = version.versionId;

        FileUtil.createFolder("launcher_data/versions/" + id);

        if (!FileUtil.fileExists("launcher_data/versions/" + id + "/" + id + ".json")) {
            WebUtil.download(version.versionJsonUrl, "launcher_data/versions/" + id + "/" + id + ".json");
        } else {

        }

        if (!FileUtil.fileExists("launcher_data/versions/" + id + ".jar")) {

        }
    }

    public void launch(String id) {
        launch(Initializer.instance.versionList.getVersionById(id));
    }

}
