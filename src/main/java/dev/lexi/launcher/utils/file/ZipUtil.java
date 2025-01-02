package dev.lexi.launcher.utils.file;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil {

    public static void unzip(String zipFilePath) throws IOException {
        // Get the parent directory of the zip file
        Path zipPath = Paths.get(zipFilePath);
        Path destinationDir = zipPath.getParent();

        // Create a zip input stream
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;

            // Iterate through each entry in the zip file
            while ((entry = zipInputStream.getNextEntry()) != null) {
                // Resolve the destination file (extract to the same folder)
                Path destinationFile = destinationDir.resolve(entry.getName());

                // Ensure the parent directories exist
                if (entry.isDirectory()) {
                    Files.createDirectories(destinationFile);
                } else {
                    // Create the necessary parent directories
                    Files.createDirectories(destinationFile.getParent());

                    // Write the file contents
                    try (BufferedOutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(destinationFile))) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                }
                zipInputStream.closeEntry();
            }
        }
    }

}
