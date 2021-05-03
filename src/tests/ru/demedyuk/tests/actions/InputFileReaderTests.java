package ru.demedyuk.tests.actions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.demedyuk.randomize.utils.file.reader.TXTInputFileReader;
import ru.demedyuk.randomize.models.files.InputFileStates;

import java.io.IOException;

public class InputFileReaderTests {

    @Test
    public void inputFileReader_fileNotExists() throws IOException {
        String path = "C://Folder/not_exists.players";

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            readFileAndValidate(path);
        });

        Assertions.assertEquals("Файл not_exists.players не существует", exception.getMessage());
    }

    @Test
    public void inputFileReader_NameValid() throws IOException {
        String path = getResourcePath("players/valid/name.players");
        TXTInputFileReader inputFile = readFileAndValidate(path);

        Assertions.assertEquals(InputFileStates.NAME, inputFile.getState());
    }

    @Test
    public void inputFileReader_firstNameLastnameValid() throws IOException {
        String path = getResourcePath("players/valid/nameWithLastname.players");
        TXTInputFileReader inputFile = readFileAndValidate(path);

        Assertions.assertEquals(InputFileStates.NAME, inputFile.getState());
    }

    @Test
    public void inputFileReader_numberNameValid() throws IOException {
        String path = getResourcePath("players/valid/numberName.players");
        TXTInputFileReader inputFile = readFileAndValidate(path);

        Assertions.assertEquals(InputFileStates.NUMBER_NAME, inputFile.getState());
    }

    @Test
    public void inputFileReader_nameGenderValid() throws IOException {
        String path = getResourcePath("players/valid/nameGender.players");
        TXTInputFileReader inputFile = readFileAndValidate(path);

        Assertions.assertEquals(InputFileStates.NAME_GENDER, inputFile.getState());
    }

    @Test
    public void inputFileReader_numberNameGenderValid() throws IOException {
        String path = getResourcePath("players/valid/numberNameGender.players");
        TXTInputFileReader inputFile = readFileAndValidate(path);

        Assertions.assertEquals(InputFileStates.NUMBER_NAME_GENDER, inputFile.getState());
    }

    @Test
    public void inputFileReader_emptyFile() {
        String path = getResourcePath("players/invalid/empty.players");

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TXTInputFileReader inputFile = readFileAndValidate(path);
        });

        Assertions.assertEquals( "Файл *.players пустой", exception.getMessage());
    }

    @Test
    public void inputFileReader_wrongGender() throws IOException {
        String path = getResourcePath("players/invalid/wrongGender.players");

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TXTInputFileReader inputFile = readFileAndValidate(path);
        });

        Assertions.assertEquals("Проверьте корректность файла с участниками", exception.getMessage());
    }

    @Test
    public void inputFileReader_wrongNumber() throws IOException {
        String path = getResourcePath("players/invalid/wrongNumber.players");

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TXTInputFileReader inputFile = readFileAndValidate(path);
        });

        Assertions.assertEquals( "Проверьте корректность файла с участниками", exception.getMessage());
    }

    @Test
    public void inputFileReader_wrongNumberArguments_1() throws IOException {
        String path = getResourcePath("players/invalid/wrongNumberArguments_1.players");

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TXTInputFileReader inputFile = readFileAndValidate(path);
        });

        Assertions.assertEquals( "Проверьте корректность файла с участниками", exception.getMessage());
    }

    @Test
    public void inputFileReader_wrongOrderArguments_1() throws IOException {
        String path = getResourcePath("players/invalid/wrongOrderArguments_1.players");

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            TXTInputFileReader inputFile = readFileAndValidate(path);
        });



        Assertions.assertEquals("Проверьте корректность файла с участниками", exception.getMessage());
    }

    private TXTInputFileReader readFileAndValidate(String path) throws IOException {
        TXTInputFileReader inputFileReader = new TXTInputFileReader(path);
        inputFileReader.readFile();
        inputFileReader.validateDocument();

        return inputFileReader;
    }

    private String getResourcePath(String path) {
        return getClass().getClassLoader().getResource(path).toString().replace("file:/", "");
    }


}
