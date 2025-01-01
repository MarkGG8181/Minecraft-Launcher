package dev.lexi.launcher.init;

import dev.lexi.launcher.data.version.VersionList;

public class Initializer {

    public static Initializer instance = new Initializer();

    public VersionList versionList = new VersionList();

    public void init() {
        versionList.populate();
    }

}
