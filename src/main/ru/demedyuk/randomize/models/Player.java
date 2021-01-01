package ru.demedyuk.randomize.models;

public class Player {
    public String number;
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

    public void setGender(Gender gender) {
        this.gender = gender;
    }

}
