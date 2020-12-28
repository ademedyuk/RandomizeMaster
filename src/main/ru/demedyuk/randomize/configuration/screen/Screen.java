package ru.demedyuk.randomize.configuration.screen;

import javafx.scene.text.Font;

public enum Screen {

    _800x600("800x600", new ScreenProperties(800, 600, 30)
            .setPreviousButtonLayouts(590, 524)
            .setNextButtonLayouts(679, 524)
            .setPrefSizeSwitchButtons(83, 31)
            .setFontSwitchButtons(Font.font(15))
            .setNameLabel(206, 95)
            .setNameCoefficientY(-12)),

    _1024x768("1024x768", new ScreenProperties(1024, 768, 35)
            .setPreviousButtonLayouts(759, 689)
            .setNextButtonLayouts(862, 689)
            .setPrefSizeSwitchButtons(99, 31)
            .setFontSwitchButtons(Font.font(15))
            .setNameLabel(263, 120)
            .setNameCoefficientY(0)),

    _1366x768("1366x768", new ScreenProperties(1366, 768, 40)
            .setPreviousButtonLayouts(1074, 683)
            .setNextButtonLayouts(1177, 683)
            .setPrefSizeSwitchButtons(99, 31)
            .setFontSwitchButtons(Font.font(15))
            .setNameLabel(353, 120)
            .setNameCoefficientY(8)),

    _1920x1080("1920x1080", new ScreenProperties(1920, 1080, 55)
            .setPreviousButtonLayouts(1574, 955)
            .setNextButtonLayouts(1730, 955)
            .setPrefSizeSwitchButtons(145, 39)
            .setFontSwitchButtons(Font.font(18))
            .setNameLabel(495, 167)
            .setNameCoefficientY(30));

    public String name;
    public ScreenProperties properties;

    Screen(String name, ScreenProperties properties) {
        this.name = name;
        this.properties = properties;
    }

}
