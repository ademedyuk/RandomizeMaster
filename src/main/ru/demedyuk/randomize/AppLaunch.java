package ru.demedyuk.randomize;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.demedyuk.randomize.constants.Paths;
import ru.demedyuk.randomize.controllers.SettingsController;

import java.net.URL;

public class AppLaunch extends Application {

    public static final String VERSION = "3.0";
    public static final String RELEASE_DATE = "22.03.2021";

    private Stage appStage;

    public static final String PATH_TO_LOGO = "images/logo.png";
    private static final String MAIN_TITLE = "Randomize Master";

    @Override
    public void start(Stage primaryStage) throws Exception {
        appStage = primaryStage;

        URL locationUrl = getClass().getClassLoader().getResource(Paths.SETTINGS_VIEW);
        Parent root = FXMLLoader.load(locationUrl);
        appStage.setResizable(true);
        appStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream(PATH_TO_LOGO)));
        appStage.setTitle(MAIN_TITLE);
        appStage.show();
        appStage.setScene(new Scene(root));

        FXMLLoader loader = new FXMLLoader(locationUrl);
        appStage.setScene(new Scene((Pane) loader.load()));
        SettingsController settingsController = loader.<SettingsController>getController();
        settingsController.setPrimaryStage(primaryStage);
        settingsController.initScene(this.appStage);
        addEvent();
    }

    public static void main(String[] args) {
        Application.launch();
    }

    public static void stopApplication() {
        Platform.exit();
        System.exit(0);
    }

    private void addEvent() {
        final KeyCombination openDir = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);

        appStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (openDir.match(event)) {
                AppLaunch.stopApplication();
            }
        });
    }

    @Deprecated
    public static void restart() {
        stopApplication();
        Application.launch();
    }
}

