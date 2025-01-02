package dev.lexi.launcher.utils;

public class SystemUtil {

    public static String fetchOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) return "windows";
        if (osName.contains("mac")) return "osx";
        if (osName.contains("nux") || osName.contains("nix")) return "linux";
        throw new UnsupportedOperationException("Unsupported operating system: " + osName);
    }

}
