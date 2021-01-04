package ru.demedyuk.randomize.models;

public class Player {
    public String number = "";
    public String firstName;
    public String lastName;
    public Gender gender = Gender.NONE;

    public Player(String firstName) {
        this.firstName = firstName;
    }

    public Player(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Player(int number, String firstName, String lastName) {
        this.number = String.valueOf(number);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Player(int number, String firstName, String lastName, Gender gender) {
        this.number = String.valueOf(number);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public Player(String firstName, String lastName, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = Gender.getGenderByID(gender);
    }

    public Player(String number, String firstName, String lastName, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.gender = Gender.getGenderByID(gender);
    }

    public Player(String number, String firstName, String lastName, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.gender = gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setNumber(int number) {
        this.number = String.valueOf(number);
    }
}
