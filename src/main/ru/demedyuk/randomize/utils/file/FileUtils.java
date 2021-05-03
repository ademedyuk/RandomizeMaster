package ru.demedyuk.randomize.utils.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileUtils {

    public static File findInitialDirectory(String path, String defaultPath) {
        File parentFile = new File(path).getParentFile();

        if (parentFile != null && parentFile.exists())
            return parentFile;

        return makeDirsIfNotExists(defaultPath);
    }

    public static File makeDirsIfNotExists(String path) {
        File directory = new File("." + path);

        if (!directory.exists())
            directory.mkdirs();

        return directory;
    }

    public static List<String> getTextFromFile(String path) throws IOException {
        Path file = Paths.get(path);

        if (!file.toFile().exists())
            throw new IllegalArgumentException("Файл " + file.getFileName() + " не существует");

       return Files.readAllLines(file);
    }
}
