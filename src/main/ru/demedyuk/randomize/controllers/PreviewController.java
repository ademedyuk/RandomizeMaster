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
import ru.demedyuk.randomize.constants.Paths;
import ru.demedyuk.randomize.models.Player;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;


public class PreviewController implements IController {

    private Stage appStage;
    private ScreenProperties screenResolutionProperties;
    private Color textColor;

    private HashMap<Integer, List<Player>> finalTeams;
    private int teamSizePreference;
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
    private ImageView photo9;

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
    private Label name9;

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
        for (Player player : finalTeams.get(currentPageIndex - 1)) {
            names[index + this.startIndex].setText(player.number + " " + player.firstName + " " + player.lastName);
            photos[index + this.startIndex].setImage(getPhoto(player));
            imageView.setPreserveRatio(true);

            index++;
        }

        teamLabel.setText(this.teamTitle + " " + currentPageIndex);
        setVisibleTablaOfPlayers(finalTeams.get(currentPageIndex - 1).size(), this.startIndex, true);
    }

    public void setVisibleTablaOfPlayers(int size, int startIndexValue, boolean value) {
        teamLabel.setVisible(value);
        for (int i = 0; i < size; i++) {
            names[i + startIndexValue].setVisible(value);
            photos[i + startIndexValue].setVisible(value);
        }
    }

    public void setUnvisibleTableOfPlayers() {
        setVisibleTablaOfPlayers(names.length, 0, false);
    }

    public void setVisibleNavigateButtons(boolean value) {
        nextButton.setVisible(value);
        previousButton.setVisible(value);
    }

    public void setUnvisibleNavigateButtons() {
        setVisibleNavigateButtons(false);
    }

    public PreviewController setScreenResolution(Screen value) {
        this.screenResolutionProperties = value.properties;
        return this;
    }

    private void cleanTable() {
        for (int i = 0; i < teamSizePreference + 1; i++) {
            names[i + this.startIndex].setText("");
            photos[i + this.startIndex].setImage(null);
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.appStage = primaryStage;
    }

    public void setTeamTitle(String teamTitle) {
        this.teamTitle = teamTitle;
    }

    public void setTeams(HashMap<Integer, List<Player>> teams) {
        this.finalTeams = teams;
        this.teamsCount = teams.size();
    }

    public void configureViewVisibleElements(Stage stage, Image image) {
        this.appStage = stage;
        addEvents();

        names = new Label[]{name1, name2, name3, name4, name5, name6, name7, name8, name9};
        photos = new ImageView[]{photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9};

        //hide all visible items
        for (int i = 0; i < names.length; i++) {
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
        for (int i = 0; i < 9; i++) {
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

    public void setTeamSizePreference(int teamSize) {
        this.teamSizePreference = teamSize;
        initStartIndex();
    }

    private void initStartIndex() {
        switch (this.teamSizePreference) {
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

        return new Image(Paths.FILE + pathToPhoto + "//" + player.number + FileExtensions.PNG);
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

        final KeyCombination nextButtonHotKey = new KeyCodeCombination(KeyCode.RIGHT);
        appStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (nextButtonHotKey.match(event)) {
                handleNextButtonAction(new ActionEvent());
            }
        });

        final KeyCombination previousButtonHotKey = new KeyCodeCombination(KeyCode.LEFT);
        appStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (previousButtonHotKey.match(event)) {
                handlePreviousButtonAction(new ActionEvent());
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