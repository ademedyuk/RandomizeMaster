package ru.demedyuk.randomize.utils.file.reader;

import ru.demedyuk.randomize.utils.file.FileUtils;

import java.io.IOException;

public class TXTInputFileReader extends BaseInputFileReader implements IInputFileReader {

    public TXTInputFileReader(String fileName) {
        this.fileName = fileName;
    }

    public void readFile() throws IOException {
        this.lines = FileUtils.getTextFromFile(this.fileName);
    }
}
