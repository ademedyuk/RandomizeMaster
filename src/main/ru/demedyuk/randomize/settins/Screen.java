package ru.demedyuk.randomize.settins;

import javafx.scene.control.RadioButton;
import ru.demedyuk.randomize.SettingsController;

public enum Screen {

    _800x600(800, 600, 20),
    _1024x768(1024, 768, 20),
    _1366x768(1366, 768, 20),
    _1920x1080(1920, 1080,50);

    public int width;
    public int height;
    public int fontSize;

    Screen(int width, int height, int fontSize) {
        this.width = width;
        this.height = height;
        this.fontSize = fontSize;
    }
}
