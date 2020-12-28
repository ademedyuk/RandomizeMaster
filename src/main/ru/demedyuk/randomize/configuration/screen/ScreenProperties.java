package ru.demedyuk.randomize.configuration.screen;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class ScreenProperties {

    public int width;
    public int height;
    public int fontSize;
    public Button previousButton;
    public Button nextButton;
    public Label nameLabel;
    public int nameCoefficientY;

    protected ScreenProperties(int width, int height, int fontSize) {
        this.width = width;
        this.height = height;
        this.fontSize = fontSize;
        this.previousButton = new Button();
        this.nextButton = new Button();
        this.nameLabel = new Label();
    }

    protected ScreenProperties setPreviousButtonLayouts(double x, double y) {
        this.previousButton.setLayoutX(x);
        this.previousButton.setLayoutY(y);

        return this;
    }

    protected ScreenProperties setNextButtonLayouts(double x, double y) {
        this.nextButton.setLayoutX(x);
        this.nextButton.setLayoutY(y);

        return this;
    }

    protected ScreenProperties setPrefSizeSwitchButtons(double prefWidth, double prefHeight) {
        this.previousButton.setPrefSize(prefWidth, prefHeight);
        this.nextButton.setPrefSize(prefWidth, prefHeight);

        return this;
    }

    protected ScreenProperties setFontSwitchButtons(Font value) {
        this.previousButton.setFont(value);
        this.nextButton.setFont(value);

        return this;
    }

    protected ScreenProperties setNameLabel(double layoutX, double layoutY) {
        this.nameLabel.setLayoutX(layoutX);
        this.nameLabel.setLayoutY(layoutY);

        return this;
    }

    protected ScreenProperties setNameCoefficientY(int value) {
        this.nameCoefficientY = value;

        return this;
    }
}
