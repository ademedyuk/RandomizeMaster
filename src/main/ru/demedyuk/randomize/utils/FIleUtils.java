package ru.demedyuk.randomize.utils;

import ru.demedyuk.randomize.AppLaunch;

import java.io.File;

public class FIleUtils {

    public static File findInitialDirectory(String path, String defaultPath) {
        File parentFile = new File(path).getParentFile();

        if (parentFile.exists())
            return parentFile;

        return makeDirsIfNotExists(defaultPath);
    }

    public static File makeDirsIfNotExists(String path) {
        File directory = new File("." + path);

        if (!directory.exists())
            directory.mkdirs();

        return directory;
    }
}
