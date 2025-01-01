package dev.lexi.launcher;

import dev.lexi.launcher.data.Urls;
import dev.lexi.launcher.game.VersionManager;
import dev.lexi.launcher.init.Initializer;
import dev.lexi.launcher.utils.file.FileUtil;
import dev.lexi.launcher.utils.file.hash.ShaUtil;
import dev.lexi.launcher.utils.web.WebUtil;
import dev.lexi.launcher.windows.LauncherWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Create the launcher's main folder & subfolders.
        FileUtil.createFolder("launcher_data");
        FileUtil.createFolder("launcher_data/data");
        FileUtil.createFolder("launcher_data/libraries");
        FileUtil.createFolder("launcher_data/assets");

        VersionManager.instance.init();

        // Downloads the 'version_manifest_v2.json' if missing.
        if (!FileUtil.fileExists("launcher_data/data/version_manifest_v2.json")) {
            WebUtil.download(Urls.VERSION_MANIFEST, "launcher_data/data/version_manifest_v2.json");
        }

        // Checks the sha-1 hash of 'version_manifest_v2.json'.
        if (!ShaUtil.checkFileSHA1("launcher_data/data/version_manifest_v2.json", "2ca408a6367f28107a1a0cdcfb5a87c2fcc95c0c")) {
            JOptionPane.showMessageDialog(null, "Hash of 'version_manifest_v2.json' is invalid.");
        }

        Initializer.instance.init();

        // Spawns in the main launcher's window.
        LauncherWindow.init();
    }
}
