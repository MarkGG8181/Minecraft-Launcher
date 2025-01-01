package dev.lexi.launcher.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaUtil {

    public static boolean checkFileSHA1(String filePath, String expectedSHA1) {
        String fileSHA1 = getFileSHA1(filePath);
        return fileSHA1 != null && fileSHA1.equals(expectedSHA1); // Return true if the hashes match
    }

    public static String getFileSHA1(String filePath) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            File file = new File(filePath);
            if (!file.exists()) {
                return null; // File doesn't exist
            }

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }

            byte[] hashBytes = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b)); // Convert each byte to hex format
            }
            return hexString.toString();

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Return null in case of an error
        }
    }

}
