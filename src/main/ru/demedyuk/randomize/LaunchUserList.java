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
import ru.demedyuk.randomize.controllers.UserListController;

import java.net.URL;

import static ru.demedyuk.randomize.constants.FileExtensions.FXML;

public class LaunchUserList extends Application {

    private Stage appStage;;

    @Override
    public void start(Stage primaryStage) throws Exception {
        appStage = primaryStage;

        URL locationUrl = getClass().getClassLoader().getResource("views/UserListView" + FXML);
        Parent root = FXMLLoader.load(locationUrl);
        appStage.setResizable(true);
        appStage.setTitle("Список игроков");
        appStage.setScene(new Scene(root));
        appStage.show();

        FXMLLoader loader = new FXMLLoader(locationUrl);
        appStage.setScene(new Scene((Pane) loader.load()));
        UserListController userListController = loader.<UserListController>getController();
        userListController.setPrimaryStage(primaryStage);

    }

    public static void main(String[] args) {
        Application.launch();
    }

}

