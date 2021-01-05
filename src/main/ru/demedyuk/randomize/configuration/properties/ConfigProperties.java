package ru.demedyuk.randomize.configuration.properties;

public enum ConfigProperties {

    PLAYERS_FILE("players.file.path"),
    RESULT_FILE("result.file.path"),
    TEAM_SIZE("teamSize.value"),
    USE_BALANSE("useBalanse.value"),
    USE_PHOTO("usePhoto.value"),
    PHOTO_DIRECTORY_FILE("photo.directory.path"),

    BACKGROUD_FILE("background.file.path"),
    TEXT_COLOR("textColor.value"),
    TEXT_FONT("textFont.value"),
    TEXT_RATE("textRate.value"),
    TEAM_NAME("teamName.value"),
    SCREEN_SIZE("screenSize.value"),
    SCREEN_IS_FULL("screenIsFull.value");

    public String key;

    ConfigProperties(String key) {
        this.key = key;
    }
}
