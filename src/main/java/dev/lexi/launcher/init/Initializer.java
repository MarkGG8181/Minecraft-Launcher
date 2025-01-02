package dev.lexi.launcher.init;

import dev.lexi.launcher.data.version.VersionList;
import dev.lexi.launcher.utils.SystemUtil;

public class Initializer {

    public static Initializer instance = new Initializer();

    public VersionList versionList = new VersionList();

    public static final String os = SystemUtil.fetchOS();

    public void init() {
        versionList.populate();
    }

}
