package ru.demedyuk.randomize.utils;

import ru.demedyuk.randomize.AppLaunch;

import java.io.File;

public class FIleUtils {

    public static File makeDirsIfNotExists(String path) {
        File directory = new File("." + path);

        if (!directory.exists())
            directory.mkdirs();

        return directory;
    }
}
