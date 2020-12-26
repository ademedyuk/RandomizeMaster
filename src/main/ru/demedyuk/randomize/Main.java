package ru.demedyuk.randomize;

import com.sun.scenario.Settings;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL locationUrl = getClass().getClassLoader().getResource("ui/settings_2.fxml");
        Parent root = FXMLLoader.load(locationUrl);
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("ui/persona_camp_icon.png")));
        primaryStage.setTitle("Randomize Master");
        primaryStage.show();
        primaryStage.setScene(new Scene(root));

        FXMLLoader loader = new FXMLLoader(locationUrl);
        primaryStage.setScene(new Scene((Pane) loader.load()));
        SettingsController settingsController = loader.<SettingsController>getController();
        settingsController.initScene();
        settingsController.setPrimaryStage(primaryStage);
    }

    public static void main(String[] args) {
        Application.launch();
    }

    public static void stopApplication() {
        Platform.exit();
        System.exit(0);
    }
}

