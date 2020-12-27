package ru.demedyuk.randomize.models;

public enum Gender {
    NONE,
    GIRL,
    BOY;

    public static Gender getGenderByID(String value) {
        if (value.equals("G") || value.equals("Д") || value.equals("W"))
            return Gender.GIRL;
        if (value.equals("B") || value.equals("М") || value.equals("M"))
            return Gender.BOY;

        return Gender.NONE;
    }
}
