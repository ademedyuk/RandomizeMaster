package ru.demedyuk.randomize.utils.file.reader;

import ru.demedyuk.randomize.models.Gender;
import ru.demedyuk.randomize.models.Player;
import ru.demedyuk.randomize.models.files.InputFileStates;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.demedyuk.randomize.models.files.InputFileStates.*;

public class BaseInputFileReader {

    protected List<String> lines = new ArrayList<>();
    private List<Player> allPlayers = new ArrayList<>();
    private InputFileStates state = NOT_VALID;
    protected String fileName;

    public void BaseInputFileReader(String filename) {
        this.fileName = filename;
    }

    public void validateDocument() {
        List<InputFileStates> states = new ArrayList<>();

        for (String line : lines) {
            String delimeter = ";"; // Разделитель
            String[] subStr = line.trim().split(delimeter);

            switch (subStr.length) {
                case (1):
                    if (!isInt(subStr[0])) {
                        states.add(NAME);
                        allPlayers.add(new Player(subStr[0]));
                        break;
                    }

                    states.add(NOT_VALID);
                    break;
                case (2):
                    if (isInt(subStr[0]) && !isInt(subStr[1])) {
                        states.add(NUMBER_NAME);
                        allPlayers.add(new Player(subStr[1]).addNumber(subStr[0]));
                        break;
                    }

                    if (!isInt(subStr[0]) && includeInt(subStr[1])) {
                        states.add(NAME_AGE);
                        allPlayers.add(new Player(subStr[0]).addAge(subStr[1]));
                        break;
                    }

                    if (!isInt(subStr[0]) && isGender(subStr[1])) {
                        states.add(NAME_GENDER);
                        allPlayers.add(new Player(subStr[0]).addGender(subStr[1]));
                        break;
                    }

                    states.add(NOT_VALID);
                    break;
                case (3):
                    if (isInt(subStr[0]) && !isInt(subStr[1]) && isGender(subStr[2])) {
                        states.add(NUMBER_NAME_GENDER);
                        allPlayers.add(new Player(subStr[1]).addNumber(subStr[0]).addGender(subStr[2]));
                        break;
                    }

                    if (isInt(subStr[0]) && !isInt(subStr[1]) && includeInt(subStr[2])) {
                        states.add(NUMBER_NAME_AGE);
                        allPlayers.add(new Player(subStr[1]).addNumber(subStr[0]).addAge(subStr[2]));
                    }

                    if (!isInt(subStr[0]) && !isInt(subStr[1]) && !includeInt(subStr[2])) {
                        states.add(NAME_GENDER_AGE);
                        allPlayers.add(new Player(subStr[0]).addGender(subStr[1]).addAge(subStr[2]));
                    }

                    states.add(NOT_VALID);
                    break;
                case (4):
                    if (isInt(subStr[0]) && !isInt(subStr[1]) && isGender(subStr[2]) && !includeInt(subStr[3])) {
                        states.add(NUMBER_NAME_GENDER_AGE);
                        allPlayers.add(new Player(subStr[1]).addNumber(subStr[0]).addGender(subStr[2]).addAge(subStr[3]));
                        break;
                    }

                    states.add(NOT_VALID);
                    break;
                default:
                    states.add(NOT_VALID);
                    break;
            }
        }

        InputFileStates etalon = null;
        try {
            etalon = states.get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Файл *.players пустой");
        }

        if (etalon == NOT_VALID)
            throw new IllegalArgumentException("Проверьте корректность файла с участниками");

        for (InputFileStates state : states) {
            if (!state.equals(etalon)) {
                throw new IllegalArgumentException("Проверьте корректность файла с участниками");
            }
        }

        this.state = etalon;
    }

    public String getInputPath() {
        return this.fileName;
    }

    public List<Player> getAllPlayers() {
        return this.allPlayers;
    }

    public InputFileStates getState() {
        return this.state;
    }

    private static boolean isInt(String string) {
        try {
            Integer.parseInt(string.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean includeInt(String string) {
        return string.trim().matches(".*\\d+.*");
    }

    private static boolean isGender(String value) {
        return Gender.getGenderByID(value.trim()) != Gender.NONE ? true : false;
    }
}
