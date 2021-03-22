package ru.demedyuk.randomize.models;

public enum Gender {

    NONE("none"),
    GIRL("Girl"),
    BOY("Boy");

    private String stringValue;

    Gender(String stringValue) {
        this.stringValue = stringValue;
    }

    public static Gender getGenderByID(String value) {
        if (value.equals("G") || value.equals("Д") || value.equals("W") || value.equals("Girl"))
            return Gender.GIRL;
        if (value.equals("B") || value.equals("М") || value.equals("M") || value.equals("Boy"))
            return Gender.BOY;

        return Gender.NONE;
    }

    public static Gender getGenderString(String value) {
        if (value.equals(GIRL.stringValue))
            return Gender.GIRL;
        if (value.equals(BOY.stringValue))
            return Gender.BOY;

        return Gender.NONE;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }

    public String toOutputValue() {
        if (this == Gender.GIRL)
            return "G";
        if (this == Gender.BOY)
            return "B";

        return "";
    }
}
