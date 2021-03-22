package ru.demedyuk.tests.actions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.demedyuk.randomize.models.files.InputFileReader;
import ru.demedyuk.randomize.models.files.InputFileStates;

import java.io.IOException;

public class InputFileReaderTests {

    @Test
    public void inputFileReader_fileNotExists() throws IOException {
        InputFileReader inputFileReader = new InputFileReader("C://Folder/not_exists.players");

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            inputFileReader.validateDocument();
        });

        Assertions.assertEquals("Файл not_exists.players не существует", exception.getMessage());
    }

    @Test
    public void inputFileReader_firstNameValid() throws IOException {
        String path = getResourcePath("players/valid/firstname.players");
        InputFileReader inputFile = validateDocument(path);

        Assertions.assertEquals(InputFileStates.FIRTSNAME, inputFile.getState());
    }

    @Test
    public void inputFileReader_firstNameLastnameValid() throws IOException {
        String path = getResourcePath("players/valid/firstnameLastname.players");
        InputFileReader inputFile = validateDocument(path);

        Assertions.assertEquals(InputFileStates.FIRTSNAME_LASTNAME, inputFile.getState());
    }

    @Test
    public void inputFileReader_numberFirstNameLastnameValid() throws IOException {
        String path = getResourcePath("players/valid/numberFirstnameLastname.players");
        InputFileReader inputFile = validateDocument(path);

        Assertions.assertEquals(InputFileStates.NUMBER_FIRTSNAME_LASTNAME, inputFile.getState());
    }

    @Test
    public void inputFileReader_firstNameLastnameGenderValid() throws IOException {
        String path = getResourcePath("players/valid/firstnameLastnameGender.players");
        InputFileReader inputFile = validateDocument(path);

        Assertions.assertEquals(InputFileStates.FIRTSNAME_LASTNAME_GENDER, inputFile.getState());
    }

    @Test
    public void inputFileReader_numberFirstNameLastnameGenderValid() throws IOException {
        String path = getResourcePath("players/valid/numberFirstnameLastnameGender.players");
        InputFileReader inputFile = validateDocument(path);

        Assertions.assertEquals(InputFileStates.NUMBER_FIRTSNAME_LASTNAME_GENDER, inputFile.getState());
    }

    @Test
    public void inputFileReader_emptyFile() throws IOException {
        String path = getResourcePath("players/invalid/empty.players");

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            InputFileReader inputFile = validateDocument(path);
        });

        Assertions.assertEquals( "Файл *.players пустой", exception.getMessage());
    }

    @Test
    public void inputFileReader_wrongGender() throws IOException {
        String path = getResourcePath("players/invalid/wrongGender.players");

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            InputFileReader inputFile = validateDocument(path);
        });

        Assertions.assertEquals( "Проверьте корректность файла с участниками", exception.getMessage());
    }

    @Test
    public void inputFileReader_wrongNumber() throws IOException {
        String path = getResourcePath("players/invalid/wrongNumber.players");

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            InputFileReader inputFile = validateDocument(path);
        });

        Assertions.assertEquals( "Проверьте корректность файла с участниками", exception.getMessage());
    }

    @Test
    public void inputFileReader_wrongNumberArguments_1() throws IOException {
        String path = getResourcePath("players/invalid/wrongNumberArguments_1.players");

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            InputFileReader inputFile = validateDocument(path);
        });

        Assertions.assertEquals( "Проверьте корректность файла с участниками", exception.getMessage());
    }

    @Test
    public void inputFileReader_wrongOrderArguments_1() throws IOException {
        String path = getResourcePath("players/invalid/wrongOrderArguments_1.players");
        InputFileReader inputFile = validateDocument(path);

        Assertions.assertEquals(InputFileStates.NOT_VALID, inputFile.getState());
    }

    @Test
    public void inputFileReader_wrongOrderArguments_2() throws IOException {
        String path = getResourcePath("players/invalid/wrongOrderArguments_2.players");
        InputFileReader inputFile = validateDocument(path);

        Assertions.assertEquals( InputFileStates.NOT_VALID, inputFile.getState());
    }

    private InputFileReader validateDocument(String path) throws IOException {
        InputFileReader inputFileReader = new InputFileReader(path);
        inputFileReader.validateDocument();

        return inputFileReader;
    }

    private String getResourcePath(String path) {
        return getClass().getClassLoader().getResource(path).toString().replace("file:/", "");
    }


}
