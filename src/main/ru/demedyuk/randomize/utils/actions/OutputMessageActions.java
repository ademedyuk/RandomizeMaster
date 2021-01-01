package ru.demedyuk.randomize.utils.actions;

import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class OutputMessageActions {

    private static int DEFAULT_DELAY = 3;

    public static void showErrorMessageWithDelay(Text textField, String message, int seconds) {
        textField.setFill(Paint.valueOf("#FF0000"));
        textField.setText(message);

        Thread delayThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(seconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                textField.setText("");
            }
        });
        delayThread.start();
    }

    public static void showErrorMessageWithDefaultDelay(Text textField, String message) {
        showErrorMessageWithDelay(textField, message, DEFAULT_DELAY);
    }
}