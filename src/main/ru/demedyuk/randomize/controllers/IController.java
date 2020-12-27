package ru.demedyuk.randomize.controllers;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

public interface IController {

    String getNextViewName();

    <T> T initNextView(ActionEvent event, String viewName);

    default void updateScene(Stage stage) {
        stage.show();
    }

}
