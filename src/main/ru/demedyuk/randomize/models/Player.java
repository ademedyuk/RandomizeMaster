package ru.demedyuk.randomize.models;

public class Player {
    public String firstName;
    public String lastName;
    public String number;
    public Gender gender = Gender.NONE;

    public Player(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Player(String number, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
