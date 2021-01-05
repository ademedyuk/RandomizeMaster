package ru.demedyuk.randomize.configuration.properties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class ActionProperties {

    public static String saveProperties(Properties props, File selectedFile) {
        if (selectedFile != null) {
            String pathToConfig = selectedFile.getPath();
            try (OutputStream output = new FileOutputStream(selectedFile)) {
                props.store(output, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return pathToConfig;
        }

        return "";
    }

    public static void saveProperties(Properties props, String path) {
        try (OutputStream output = new FileOutputStream(path)) {
            props.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
