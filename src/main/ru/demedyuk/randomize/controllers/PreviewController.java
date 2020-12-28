package ru.demedyuk.randomize.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ru.demedyuk.randomize.configuration.RuntimeSettings;
import ru.demedyuk.randomize.configuration.screen.Screen;
import ru.demedyuk.randomize.configuration.screen.ScreenProperties;
import ru.demedyuk.randomize.constants.FileExtensions;
import ru.demedyuk.randomize.constants.Path;
import ru.demedyuk.randomize.models.Player;
import ru.demedyuk.randomize.utils.RandomizeAction;

import java.io.IOException;
import java.net.URL;


public class PreviewController implements IController {

    public RandomizeAction randomizeAction;

    private Stage appStage;
    private ScreenProperties screenResolutionProperties;
    private Color textColor;
    private Thread randomizeThread;

    private int teamSize;
    private int teamsCount;
    private int startIndex; //индекс начала отображения игроков

    private String pathToPhoto;
    private String teamTitle;

    private static final double RESIZABLE_RATE = 1.02;
    private static final double PHOTO_RESIZABLE_RATE = 1.5;

    @FXML
    private ImageView imageView;

    @FXML
    private Pane playerPane1;

    @FXML
    private ImageView photo1;

    @FXML
    private ImageView photo2;

    @FXML
    private ImageView photo3;

    @FXML
    private ImageView photo4;

    @FXML
    private ImageView photo5;

    @FXML
    private ImageView photo6;

    @FXML
    private ImageView photo7;

    @FXML
    private ImageView photo8;

    @FXML
    private Label name1;

    @FXML
    private Label name2;

    @FXML
    private Label name3;

    @FXML
    private Label name4;

    @FXML
    private Label name5;

    @FXML
    private Label name6;

    @FXML
    private Label name7;

    @FXML
    private Label name8;

    @FXML
    private Label teamLabel;

    @FXML
    private Button startButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button previousButton;

    private static Label[] names;
    private ImageView[] photos;

    private int currentPageIndex = 0;

    @FXML
    void handleStartButtonAction(ActionEvent event) {
        currentPageIndex = 0;
        startButton.setVisible(false);
        setVisibleNavigateButtons(true);
        handleNextButtonAction(event);
    }

    @FXML
    void handlePreviousButtonAction(ActionEvent event) {
        if (currentPageIndex - 1 <= 0)
            return;

        currentPageIndex--;
        showNewList();
    }

    @FXML
    void handleNextButtonAction(ActionEvent event) {
        if (currentPageIndex + 1 > teamsCount)
            return;

        currentPageIndex++;
        showNewList();
    }

    private void showNewList() {
        cleanTable();

        int index = 0;
        for (Player player : randomizeAction.getTeam(currentPageIndex)) {
            names[index + this.startIndex].setText(player.number + " " + player.firstName + " " + player.lastName);
            photos[index + this.startIndex].setImage(getPhoto(player));
            imageView.setPreserveRatio(true);

            index++;
        }

        teamLabel.setText(this.teamTitle + " " + currentPageIndex);
        setVisibleTablaOfPlayers(true);
    }

    public void setVisibleTablaOfPlayers(boolean value) {
        teamLabel.setVisible(value);
        for (int i = 0; i < teamSize; i++) {
            names[i + this.startIndex].setVisible(value);
            photos[i + this.startIndex].setVisible(value);
        }
    }

    public void setVisibleNavigateButtons(boolean value) {
        nextButton.setVisible(value);
        previousButton.setVisible(value);
    }

    public PreviewController setScreenResolution(Screen value) {
        this.screenResolutionProperties = value.properties;
        return this;
    }

    private void cleanTable() {
        for (int i = 0; i < teamSize; i++) {
            names[i + this.startIndex].setText("");
            photos[i + this.startIndex].setImage(null);
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.appStage = primaryStage;
    }

    public void configureViewVisibleElements(Stage stage, boolean needBalance, Image image, String filePath, String resultFilePath, String teamTitle) {
        this.appStage = stage;
        this.teamTitle = teamTitle;
        addEvents();

        names = new Label[]{name1, name2, name3, name4, name5, name6, name7, name8};
        photos = new ImageView[]{photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8};

        //hide all visible items
        for (int i = 0; i < 8; i++) {
            names[i].setVisible(false);
            photos[i].setVisible(false);
        }

        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(screenResolutionProperties.height);
        imageView.setFitWidth(screenResolutionProperties.width);
        appStage.setHeight(screenResolutionProperties.height);
        appStage.setWidth(screenResolutionProperties.width);

        startButton.setLayoutX(screenResolutionProperties.width / 2 - startButton.getPrefWidth() / 2);
        startButton.setLayoutY(screenResolutionProperties.height / 2 - startButton.getPrefHeight() / 2);

        //buttons
        previousButton.setPrefSize(screenResolutionProperties.previousButton.getPrefWidth(), screenResolutionProperties.previousButton.getPrefHeight());
        previousButton.setLayoutX(screenResolutionProperties.previousButton.getLayoutX());
        previousButton.setLayoutY(screenResolutionProperties.previousButton.getLayoutY());
        previousButton.setFont(screenResolutionProperties.previousButton.getFont());

        nextButton.setPrefSize(screenResolutionProperties.nextButton.getPrefWidth(), screenResolutionProperties.nextButton.getPrefHeight());
        nextButton.setLayoutX(screenResolutionProperties.nextButton.getLayoutX());
        nextButton.setLayoutY(screenResolutionProperties.nextButton.getLayoutY());
        nextButton.setFont(screenResolutionProperties.nextButton.getFont());

        teamLabel.setLayoutX(screenResolutionProperties.width / 2 - teamLabel.getPrefWidth() / 2);
        teamLabel.setLayoutY(screenResolutionProperties.height * 0.03);
        teamLabel.setTextFill(this.textColor);
        teamLabel.setFont(Font.font(screenResolutionProperties.fontSize * 1.2));
        teamLabel.setText(this.teamTitle);

        //players
        for (int i = 0; i < 8; i++) {
            names[i].setTextFill(this.textColor);
            names[i].setLayoutX(screenResolutionProperties.nameLabel.getLayoutX());

            if (i == 0) {
                names[i].setLayoutY(screenResolutionProperties.nameLabel.getLayoutY());
            } else {
                names[i].setLayoutY(names[i - 1].getLayoutY() + names[i - 1].getPrefHeight() + screenResolutionProperties.nameCoefficientY);
            }

            names[i].setPrefWidth(screenResolutionProperties.width * 0.5);
            names[i].setFont(Font.font(screenResolutionProperties.fontSize));

            photos[i].setLayoutX(names[i].getLayoutX() - photos[i].getFitHeight() * PHOTO_RESIZABLE_RATE);
            photos[i].setLayoutY(names[i].getLayoutY());
            photos[i].setFitHeight(names[i].getFont().getSize() * PHOTO_RESIZABLE_RATE);
            photos[i].setFitWidth(names[i].getFont().getSize() * PHOTO_RESIZABLE_RATE);
        }

        randomizeAction = new RandomizeAction(filePath, resultFilePath, teamSize, needBalance, teamLabel.getText());
        teamsCount = randomizeAction.teamNumbers.size();
    }

    public void setFullScreenIfNeeded(boolean isNeed) {
        if (!isNeed) {
            appStage.setWidth(screenResolutionProperties.width);
            appStage.setHeight(screenResolutionProperties.height);

            return;
        }

        appStage.setFullScreen(true);
        imageView.setPreserveRatio(true);

        imageView.setFitHeight(javafx.stage.Screen.getPrimary().getBounds().getMaxY() * RESIZABLE_RATE);
        imageView.setFitWidth(javafx.stage.Screen.getPrimary().getBounds().getMaxX() * RESIZABLE_RATE);
    }

    public void setPathToPhoto(String pathToPhoto) {
        this.pathToPhoto = pathToPhoto;
    }

    public void setTextColor(Color value) {
        this.textColor = value;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
        initStartIndex();
    }

    private void initStartIndex() {
        switch (this.teamSize) {
            case (2):
                this.startIndex = 3;
                break;
            case (3):
            case (4):
                this.startIndex = 2;
                break;
            case (5):
            case (6):
                this.startIndex = 1;
                break;
            default:
                this.startIndex = 0;
                break;
        }
    }

    private Image getPhoto(Player player) {

        return new Image(Path.FILE + pathToPhoto + "//" + player.number + FileExtensions.PNG);
    }

    private void addEvents() {
        final KeyCombination restartHotKey = new KeyCodeCombination(KeyCode.R, KeyCombination.SHIFT_DOWN);

        appStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (restartHotKey.match(event)) {
                SettingsController settingsController = initNextView(new ActionEvent(), getNextViewName());
                settingsController.initScene();

                appStage.setWidth(RuntimeSettings.SETTING_VIEW_LAST_WIDTH);
                appStage.setHeight(RuntimeSettings.SETTING_VIEW_LAST_HEIGHT);

                settingsController.setPrimaryStage(appStage);
            }
        });

        final KeyCombination fullScreenHotKey = new KeyCodeCombination(KeyCode.F, KeyCombination.SHIFT_DOWN);
        appStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (fullScreenHotKey.match(event)) {
                appStage.setFullScreen(true);
            }
        });
    }

    @Override
    public String getNextViewName() {
        return "views/SettingsView.fxml";
    }

    @Override
    public <T> T initNextView(ActionEvent event, String viewName) {
        URL locationUrl = getClass().getClassLoader().getResource(viewName);
        FXMLLoader loader = new FXMLLoader(locationUrl);

        try {
            appStage.setScene(new Scene((Pane) loader.load()));
        } catch (IOException e) {
            //TODO: залогировать
            e.printStackTrace();
        }

        return loader.getController();
    }
}