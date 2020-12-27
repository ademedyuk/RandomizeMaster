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
import ru.demedyuk.randomize.controllers.SettingsController;

import java.net.URL;

public class AppLaunch extends Application {

    private Stage appStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        appStage = primaryStage;

        URL locationUrl = getClass().getClassLoader().getResource("views/SettingsView.fxml");
        Parent root = FXMLLoader.load(locationUrl);
        appStage.setResizable(true);
        appStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("images/persona_camp_icon.png")));
        appStage.setTitle("Randomize Master");
        appStage.show();
        appStage.setScene(new Scene(root));

        FXMLLoader loader = new FXMLLoader(locationUrl);
        appStage.setScene(new Scene((Pane) loader.load()));
        SettingsController settingsController = loader.<SettingsController>getController();
        settingsController.initScene();
        settingsController.setPrimaryStage(primaryStage);
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
        final KeyCombination openDir = new KeyCodeCombination(KeyCode.Q, KeyCombination.SHIFT_DOWN);

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

