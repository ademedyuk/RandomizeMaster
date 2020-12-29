package ru.demedyuk.randomize.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static ru.demedyuk.randomize.models.InputFileStates.*;
import static ru.demedyuk.randomize.models.InputFileStates.FIRTSNAME;
import static ru.demedyuk.randomize.models.InputFileStates.NOT_VALID;

public class InputFileReader {

    private String filePath;
    private List<Player> allPlayers = new ArrayList<>();

    public InputFileReader(String filePath) {
        this.filePath = filePath;
    }

    public void validateDocument() {
        List<String> lines = getTextFromFile();
        List<InputFileStates> states = new ArrayList<>();

        for (String line : lines) {
            String delimeter = " "; // Разделитель
            String[] subStr = line.split(delimeter);

            switch (subStr.length) {
                case (1):
                    states.add(isValidFirstName(subStr) ? FIRTSNAME : NOT_VALID);
                    allPlayers.add(new Player(subStr[0]));
                    break;
                case (2):
                    states.add(isValidFirstNameAndLastName(subStr) ? FIRTSNAME_LASTNAME : NOT_VALID);
                    allPlayers.add(new Player(subStr[0], subStr[1]));
                    break;
                case (3):
                    if (isValidNumberFirstNameAndLastName(subStr)) {
                        states.add(NUMBER_FIRTSNAME_LASTNAME);
                        allPlayers.add(new Player(Integer.parseInt(subStr[0]), subStr[1], subStr[2]));
                    } else if (isValidFirstNameAndLastNameAndGender(subStr)) {
                        states.add(FIRTSNAME_LASTNAME_GENDER);
                        allPlayers.add(new Player(subStr[0], subStr[1], subStr[2]));
                    } else
                        states.add(NOT_VALID);
                    break;
                case (4):
                    states.add(isValidNumberFirstNameAndLastNameAndGender(subStr) ? NUMBER_FIRTSNAME_LASTNAME_GENDER : NOT_VALID);
                    allPlayers.add(new Player(subStr[0], subStr[1], subStr[2], subStr[3]));
                    break;
                default:
                    throw new IllegalArgumentException("Невалидый файл");
            }
        }

        InputFileStates etalon = states.get(0);
        for (InputFileStates state : states) {
            if (!state.equals(etalon)) {
                throw new IllegalArgumentException("Невалидый файл");
            }
        }
    }

    public List<Player> getAllPlayers() {
        return this.allPlayers;
    }

    private List<String> getTextFromFile() {
        Path file = Paths.get(this.filePath);
        List<String> lines = null;

        try {
            lines = Files.readAllLines(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    private static boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isGender(String value) {
        return Gender.getGenderByID(value) != Gender.NONE ? true : false;
    }

    private boolean isValidFirstName(String[] str) {
        return !isInt(str[0]) ? true : false;
    }

    private boolean isValidFirstNameAndLastName(String[] str) {
        return (!isInt(str[0]) && !isInt(str[1])) ? true : false;
    }

    private boolean isValidNumberFirstNameAndLastName(String[] str) {
        return (isInt(str[0]) && !isInt(str[1]) && !isInt(str[2])) ? true : false;
    }

    private boolean isValidFirstNameAndLastNameAndGender(String[] str) {
        return (!isInt(str[0]) && !isInt(str[1]) && isGender(str[2])) ? true : false;
    }

    private boolean isValidNumberFirstNameAndLastNameAndGender(String[] str) {
        return (isInt(str[0]) && !isInt(str[1]) && !isInt(str[2]) && isGender(str[3])) ? true : false;
    }
}
