package ru.demedyuk.randomize.models;

import ru.demedyuk.randomize.models.files.InputFileStates;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.demedyuk.randomize.models.files.InputFileStates.*;

public class Player {
    public String number = "";
    public String name;
    public Gender gender = Gender.NONE;
    public int age = 0;
    public InputFileStates state;

    public Player(String firstName) {
        this.name = firstName;
    }

    public Player addNumber(String number) {
        this.number = number;
        return this;
    }

    public Player addNumber(int number) {
        this.number = String.valueOf(number);
        return this;
    }

    public Player addAge(String age) {
        this.age = getAge(age);
        return this;
    }

    public Player addGender(String gender) {
        this.gender = Gender.getGenderByID(gender);
        return this;
    }

    public Player addGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    private static int getAge(String string) {
        Pattern MY_PATTERN = Pattern.compile("\\d+");
        Matcher m = MY_PATTERN.matcher(string);

        if (m.find())
            return Integer.parseInt(m.group());

        return 0;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getNumber() {
        return Integer.parseInt(number);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setAge(int value) {
        this.age = value;
    }

    public void setAge(String value) {
        this.age = Integer.parseInt(value);
    }

    public void setNumber(int number) {
        this.number = String.valueOf(number);
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public InputFileStates executeState() {
        if (numberIsExists() && nameIsExists() && genderIsExists() && ageIsExists())
            return NUMBER_NAME_GENDER_AGE;

        if (numberIsExists() && nameIsExists() && genderIsExists() && !ageIsExists())
            return NUMBER_NAME_GENDER;

        if (numberIsExists() && nameIsExists() && !genderIsExists() && ageIsExists())
            return NUMBER_NAME_AGE;

        if (!numberIsExists() && nameIsExists() && genderIsExists() && ageIsExists())
            return NAME_GENDER_AGE;

        if (numberIsExists() && nameIsExists() && !genderIsExists() && !ageIsExists())
            return NUMBER_NAME;

        if (!numberIsExists() && nameIsExists() && !genderIsExists() && ageIsExists())
            return NAME_AGE;

        if (!numberIsExists() && nameIsExists() && genderIsExists() && !ageIsExists())
            return NAME_GENDER;

        if (!numberIsExists() && nameIsExists() && !genderIsExists() && !ageIsExists())
            return NAME;

        return InputFileStates.NOT_VALID;
    }

    public boolean numberIsExists() {
        return this.number != null && !this.number.trim().isEmpty() ? true : false;
    }

    public boolean nameIsExists() {
        return this.name != null && !this.name.trim().isEmpty() ? true : false;
    }

    public boolean genderIsExists() {
        return this.gender != Gender.NONE ? true : false;
    }

    public boolean ageIsExists() {
        return this.age != 0 ? true : false;
    }
}
