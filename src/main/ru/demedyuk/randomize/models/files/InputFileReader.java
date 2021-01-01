package ru.demedyuk.randomize.models.files;

import ru.demedyuk.randomize.models.Gender;
import ru.demedyuk.randomize.models.Player;
import ru.demedyuk.randomize.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ru.demedyuk.randomize.models.files.InputFileStates.*;

public class InputFileReader {

    private String filePath;
    private List<Player> allPlayers = new ArrayList<>();
    private InputFileStates state = NOT_VALID;

    public InputFileReader(String filePath) {
        this.filePath = filePath;
    }

    public void validateDocument() throws IOException {
        List<String> lines = FileUtils.getTextFromFile(this.filePath);
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
                    throw new IllegalArgumentException("Проверьте корректность файла с участниками");
            }
        }

        InputFileStates etalon = states.get(0);
        for (InputFileStates state : states) {
            if (!state.equals(etalon)) {
                throw new IllegalArgumentException("Проверьте корректность файла с участниками");
            }
        }

        this.state = etalon;
    }

    public List<Player> getAllPlayers() {
        return this.allPlayers;
    }
    public InputFileStates getState() {
        return this.state;
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
