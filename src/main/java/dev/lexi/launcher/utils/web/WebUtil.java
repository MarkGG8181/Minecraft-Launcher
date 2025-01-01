package dev.lexi.launcher.utils.web;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class WebUtil {

    public static void download(String url, String filename) {
        System.out.println("[!] Downloading " + url + " to " + filename);
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            System.out.println("[!] Finished downloading " + filename + "!");
        } catch (IOException e) {
            // handle exception
        }
    }

}
