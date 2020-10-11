package ru.demedyuk.randomize;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PreviewController {

    @FXML
    private ImageView imageView;

    public void setImege(Image image) {
        imageView.setImage(image);
    }

}