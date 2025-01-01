package dev.lexi.launcher.utils;

import java.io.File;

public class FileUtil {

    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile(); // Returns true if the file exists and it's a regular file
    }

    public static void createFolder(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public static boolean deleteFolder(String path) {
        File folder = new File(path);
        if (!folder.exists() || !folder.isDirectory()) {
            return false; // The folder doesn't exist or it's not a directory
        }

        // Recursively delete contents
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file.getAbsolutePath()); // Recursively delete subdirectories
                } else {
                    file.delete(); // Delete files
                }
            }
        }
        return folder.delete(); // Delete the empty folder
    }

}
