package ru.demedyuk.randomize;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;


public class PreviewController {

    public RandomizeAction randomizeAction;
    private ru.demedyuk.randomize.settins.Screen screenResolution; //разрешение экрана
    private int countOfPlayers; //количество человек в команде
    private int countOfTeams; //количество человек в команде
    private int startIndex; //индекс начала отображения игроков
    private String pathToPhoto; //путь до каталога с фотографиями

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
    private Label teamLabel;

    @FXML
    private Button startButton;

    @FXML
    private Button nextButton;

    @FXML
    private Button previousButton;

    private static Label[] names;
    private ImageView[] photos;

    public static HashMap<Integer, List<Player>> randomHistory = new HashMap<>();
    public static int index = 0;

    @FXML
    void handleNextButtonAction(ActionEvent event) {
        index++;
        if (index > countOfTeams) {
            index--;
            return;
        }
        if (randomHistory.get(index) != null) {
            for (int i = 0; i < randomHistory.get(index).size(); i++) {
                Player player = randomHistory.get(index).get(i);
                names[i + this.startIndex].setText(player.number + " " + player.firstName + " " + player.lastName);
                photos[i + this.startIndex].setImage(getPhoto(player));
                imageView.setPreserveRatio(true);
            }

            //удаление лишних записей
            if (randomHistory.get(index).size() != countOfPlayers)
                for (int j = countOfPlayers; j > randomHistory.get(index).size(); j--)
                    names[j-1].setVisible(false);
            return;
        }

        //если необходимо создать новую команду
        List<Player> randomPlayers = randomizeAction.doRandom(index-1);
        randomHistory.put(index, randomPlayers);

        for (int i = 0; i < randomPlayers.size(); i++) {
            if (randomPlayers.get(i) != null) {
                Player player = randomPlayers.get(i);
                names[i + this.startIndex].setText(player.number + " " + player.firstName + " " + player.lastName);
                photos[i + this.startIndex].setImage(getPhoto(player));
                imageView.setPreserveRatio(true);
            }
        }

        setVisibleTablaOfPlayers(true);

        //удаление лишних записей
        if (randomPlayers.size() != countOfPlayers)
            for (int j = countOfPlayers; j > randomPlayers.size(); j--)
            names[j-1].setVisible(false);
    }

    @FXML
    void handlePreviousButtonAction(ActionEvent event) {
        index--;
        if (index < 1) {
            index++;
            return;
        }

        for (int i = 0; i < randomHistory.get(index).size(); i++) {
            Player player = randomHistory.get(index).get(i);
            names[i + this.startIndex].setText(player.number + " " + player.firstName + " " + player.lastName);
            photos[i + this.startIndex].setImage(getPhoto(player));
        }

        setVisibleTablaOfPlayers(true);

        //удаление лишних записей
        if (randomHistory.get(index).size() != countOfPlayers)
            for (int j = countOfPlayers; j > randomHistory.get(index).size(); j--)
                names[j-1].setVisible(false);
    }

    @FXML
    void handleStartButtonAction(ActionEvent event) {
        startButton.setVisible(false);
        setVisibleNavigateButtons(true);
        handleNextButtonAction(new ActionEvent());
    }

    public void setVisibleTablaOfPlayers(boolean value) {
        teamLabel.setVisible(value);
        for (int i = 0; i < countOfPlayers; i++) {
            names[i+this.startIndex].setVisible(value);
            photos[i+this.startIndex].setVisible(value);
        }
    }

    public void setVisibleNavigateButtons(boolean value) {
        nextButton.setVisible(value);
        previousButton.setVisible(value);
    }

    public PreviewController setScreenResolution(ru.demedyuk.randomize.settins.Screen value) {
        this.screenResolution = value;
        return this;
    }


    public void init(Stage stage, Image image, String filePath, String resultFilePath) {
//        stageSizeChageListener(stage);
        names = new Label[] {name1, name2, name3, name4, name5, name6, name7, name8};
        photos = new ImageView[]{photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8};

        imageView.setImage(image);
        imageView.setPreserveRatio(true);

        stage.setHeight(screenResolution.height);
        stage.setWidth(screenResolution.width);
        imageView.setFitHeight(screenResolution.height);
        imageView.setFitWidth(screenResolution.width);

        //скрываем перед началом представления все блоки
        for (int i = 0; i < 8; i++) {
            names[i].setVisible(false);
            photos[i].setVisible(false);
        }

        randomizeAction = new RandomizeAction(filePath, resultFilePath, countOfPlayers);
        countOfTeams = randomizeAction.teams.size();

        for (int i = 0; i < 8; i++) {
            names[i].setTextFill(Paint.valueOf("#ffffff"));//установка цвета текста
            names[i].setLayoutX(screenResolution.width*0.25);
            names[i].setLayoutY(names[i].getLayoutY()*1.25);
            names[i].setPrefWidth(screenResolution.width*0.5);
            names[i].setFont(Font.font(screenResolution.fontSize));

            //фото
            photos[i].setLayoutX(names[i].getLayoutX() - photos[i].getFitHeight()*1.5);
            photos[i].setLayoutY(names[i].getLayoutY());
            photos[i].setFitHeight(names[i].getFont().getSize()*1.5);
            photos[i].setFitWidth(names[i].getFont().getSize()*1.5);

            previousButton.setLayoutX(Screen.getPrimary().getBounds().getWidth()*0.8);
            previousButton.setLayoutY(Screen.getPrimary().getBounds().getHeight()*0.85);
            nextButton.setLayoutX(previousButton.getLayoutX()+previousButton.getWidth()*1.1);
            nextButton.setLayoutY(previousButton.getLayoutY());
        }
    }

    public void setFullScreen(Stage appStage) {
        appStage.setFullScreen(true);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(Screen.getPrimary().getBounds().getWidth());
        imageView.setFitHeight(Screen.getPrimary().getBounds().getHeight());
    }

    private void stageSizeChageListener(Stage stage) {
        stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                imageView.setFitWidth(newValue.doubleValue());
//                double res = name1.getLayoutX() - photo1.getLayoutX();
//                    for (int i = 0; i <8; i++) {
//                        names[i].setPrefWidth(names[i].getPrefWidth() + (newValue.doubleValue() - oldValue.doubleValue()));
//                        names[i].setLayoutX(newValue.doubleValue()/5);
//                        photos[i].setLayoutX(newValue.doubleValue()/5 - res);
//                    }
            }
        });

        stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                imageView.setFitHeight(newValue.doubleValue());
//                for (int i = 0; i <8; i++) {
//                    names[i].setFont(Font.font(names[i].getFont().getSize() * (newValue.doubleValue() / oldValue.doubleValue())));
//                }
            }
        });
    }

    public void setCountOfPlayers(int countOfPlayers) {
        this.countOfPlayers = countOfPlayers;
        initStartIndex();
    }

    private void initStartIndex() {
        switch (this.countOfPlayers) {
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

    public void setPathToPhoto(String pathToPhoto) {
        this.pathToPhoto = pathToPhoto;
    }

    private Image getPhoto(Player player) {
        return new Image("file:///" + pathToPhoto + "//" + player.number + ".png");
    }
}