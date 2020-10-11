package ru.demedyuk.randomize;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SettingsController {

    @FXML
    private TextField input_info;

    @FXML
    private TextField ouput_info;

    @FXML
    private TextField background;

    @FXML
    private Button launch;

    @FXML
    private void handleLaunchButtonAction(ActionEvent event) throws IOException {
        String inputInfo = input_info.getAccessibleText();
        String outputFolder = ouput_info.getAccessibleText();
        String backgroundImagePath = background.getText();

        Image imageF = new Image(getClass().getResourceAsStream(backgroundImagePath));

//        fButton.setGraphic(new ImageView(imageF));
//        StackPane root = new StackPane();
//        root.getChildren().add(fButton);
//        primaryStage.setScene(new Scene(root, 300, 250));
//        primaryStage.show();

        URL locationUrl = getClass().getClassLoader().getResource("ui/preview_1.fxml");
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(locationUrl);
        appStage.setScene(new Scene((Pane) loader.load()));

        PreviewController previewController = loader.<PreviewController>getController();
        previewController.setImege(imageF);

        appStage.show();
    }

}

