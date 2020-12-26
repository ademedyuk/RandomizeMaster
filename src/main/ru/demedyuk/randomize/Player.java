package ru.demedyuk.randomize;

public class Player {
    public String firstName;
    public String lastName;
    public String number;

    public Player(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Player(String number, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
    }
}
