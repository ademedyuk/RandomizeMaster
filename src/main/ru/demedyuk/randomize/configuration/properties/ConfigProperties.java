package ru.demedyuk.randomize.configuration.properties;

public enum ConfigProperties {

    PLAYERS_FILE("players.file.path"),
    RESULT_FILE("result.file.path"),
    USE_BALANSE("usePhoto.value"),
    USE_PHOTO("usePhoto.value"),
    PHOTO_DIRECTORY_FILE("input.file.path"),

    BACKGROUD_FILE("input.file.path"),
    TEXT_COLOR("textColor.value"),
    TEAM_NAME("teamName.value"),
    SCREEN_SIZE("screenSize.value"),
    SCREEN_IS_FULL("screenIsFull.value");

    public String key;

    ConfigProperties(String key) {
        this.key = key;
    }
}