package dev.lexi.launcher;

import dev.lexi.launcher.data.Urls;
import dev.lexi.launcher.utils.FileUtil;
import dev.lexi.launcher.utils.ShaUtil;
import dev.lexi.launcher.utils.WebUtils;
import dev.lexi.launcher.windows.LauncherWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Create the launcher's main folder.
        FileUtil.createFolder("launcher_data");

        // Downloads the 'version_manifest_v2.json' if missing.
        if (!FileUtil.fileExists("launcher_data/version_manifest_v2.json")) {
            WebUtils.download(Urls.VERSION_MANIFEST, "launcher_data/version_manifest_v2.json");
        }

        // Checks the sha-1 hash of 'version_manifest_v2.json'.
        if (!ShaUtil.checkFileSHA1("launcher_data/version_manifest_v2.json", "2ca408a6367f28107a1a0cdcfb5a87c2fcc95c0c")) {
            JOptionPane.showMessageDialog(null, "Hash of 'version_manifest_v2.json' is invalid.");
        }

        // Spawns in the main launcher's window.
        LauncherWindow.init();
    }
}
