package ru.demedyuk.randomize;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/settings_1.fxml"));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("ui/persona_camp_icon.png")));
        primaryStage.setTitle("VK Assists");
        primaryStage.show();
        primaryStage.setScene(new Scene(root));
        primaryStage.setFocused(false);
    }

    public static void main(String[] args) {
        Application.launch();
    }
}
