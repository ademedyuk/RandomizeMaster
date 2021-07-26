package ru.demedyuk.randomize.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ru.demedyuk.randomize.configuration.screen.Screen;
import ru.demedyuk.randomize.configuration.screen.ScreenProperties;
import ru.demedyuk.randomize.configuration.RuntimeSettings;
import ru.demedyuk.randomize.constants.FileExtensions;
import ru.demedyuk.randomize.constants.Paths;
import ru.demedyuk.randomize.models.Gender;
import ru.demedyuk.randomize.models.Player;
import ru.demedyuk.randomize.models.files.InputFileStates;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.demedyuk.randomize.constants.Paths.IMAGES;
import static ru.demedyuk.randomize.constants.Paths.SETTINGS_VIEW;


public class PreviewController implements IController {

    private static final String BOY = "boy";
    private static final String GIRL = "girl";
    private static final String BOY_PNG = BOY + FileExtensions.PNG;
    private static final String GIRL_PNG = GIRL + FileExtensions.PNG;
    private static final String NO_GENDER = "no_gender" + FileExtensions.PNG;

    private Stage appStage;
    private ScreenProperties screenResolutionProperties;
    private Color textColor;
    private HashMap<Integer, List<Player>> finalTeams;
    private Double textValueRate;
    private int teamsCount;
    private boolean usePhoto;
    private String pathToPhoto;
    private String teamTitle;

    private static final double RESIZABLE_RATE = 1.02;
    private static final double PHOTO_RESIZABLE_RATE = 1.5;

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
    private ImageView photo9;

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
    private Label name9;

    @FXML
    private Label teamLabel;

    @FXML
    private Button startButton;
    @FXML
    private ImageView startIcon;
    @FXML
    private Button nextButton;
    @FXML
    private ImageView nextIcon;
    @FXML
    private Button previousButton;
    @FXML
    private ImageView previousIcon;

    private static Label[] names;
    private ImageView[] photos;

    private int currentPageIndex = 0;
    private int currentItemIndex = 0;

    private InputFileStates state;
    private Font font;

    @FXML
    void handleStartButtonAction(ActionEvent event) {
        currentPageIndex = 0;
        startIcon.setVisible(false);

        setVisibleNavigateButtons(true);
        //handleNextButtonAction(null);

        teamLabel.setText(this.teamTitle + " " + (currentPageIndex + 1));
        teamLabel.setVisible(true);
    }

    @FXML
    void handlePreviousButtonAction(ActionEvent event) {
        if (currentPageIndex - 1 < 0 && currentItemIndex == 0)
            return;

        //currentPageIndex--;
        new Thread(() -> playButtonEffect(previousIcon)).start();
        //showNewList();

        showPreviousListItem();
    }

    @FXML
    void handleNextButtonAction(ActionEvent event) {
        if (currentPageIndex + 1 > teamsCount)
            return;
        if (event != null)
            new Thread(() -> playButtonEffect(nextIcon)).start();

        //currentPageIndex++;
        //showNewList();

        showNewListItem();
    }

    private void showNewList() {
        cleanTable();

        int index = 0;
        //for (Player player : finalTeams.get(currentPageIndex - 1)) {
        for (Player player : finalTeams.get(currentPageIndex)) {
            showNames(player, index);
            if (this.usePhoto) {
                Image image = resizeImage(getPhoto(player));

                double rad = photos[index].getFitHeight() / 2;
                Circle circle = new Circle(rad);
                circle.setCenterX(rad);
                circle.setCenterY(rad);
                circle.setFill(new ImagePattern(image, 0, 0, rad * 2, rad * 2, false));

                photos[index].setStyle("border-radius: 10px;");
                photos[index].setImage(image);
                photos[index].setClip(circle);
            }
            imageView.setPreserveRatio(true);

            index++;
        }

        teamLabel.setText(this.teamTitle + " " + (currentPageIndex + 1));
        setVisibleTablaOfPlayers(finalTeams.get(currentPageIndex).size(), true);
        //setVisibleTablaOfPlayers(finalTeams.get(currentPageIndex).size() - 1, true);
    }

    private Image resizeImage(Image image) {
        PixelReader pixelReader = image.getPixelReader();

        double height = image.getHeight();
        double width = image.getWidth();

        if (height > width)
            return new WritableImage(pixelReader, 0, 0, (int) width, (int) width);

        if (height < width)
            return new WritableImage(pixelReader, 0, 0, (int) height, (int) height);

        return image;
    }

    private void showNewListItem() {
        if (currentPageIndex >= finalTeams.size())
            return;

        if (currentItemIndex >= finalTeams.get(currentPageIndex).size()) {

            if (currentPageIndex + 1 < finalTeams.size()) {
                currentPageIndex++;
                currentItemIndex = 0;
                cleanTable();
                teamLabel.setText(this.teamTitle + " " + (currentPageIndex + 1));
            }
            return;
        }

        Player player = finalTeams.get(currentPageIndex).get(currentItemIndex);
        showNames(player, currentItemIndex);
        if (this.usePhoto) {
            Image image = getPhoto(player);
            double rad = photos[currentItemIndex].getFitHeight() / 2;
            Circle circle = new Circle(rad);
            circle.setCenterX(rad);
            circle.setCenterY(rad);
            circle.setFill(new ImagePattern(image, 0, 0, rad * 2, rad * 2, false));

            photos[currentItemIndex].setStyle("border-radius: 10px;");
            photos[currentItemIndex].setImage(image);
            photos[currentItemIndex].setClip(circle);

        }
        imageView.setPreserveRatio(true);

        setVisibleOnePlayer(currentItemIndex, true);

        currentItemIndex++;
    }

    private void showPreviousListItem() {
        if (currentPageIndex < 0)
            return;

        if (currentItemIndex <= 0) {
            currentItemIndex = finalTeams.get(currentPageIndex).size();

            currentPageIndex--;
            showNewList();
            return;
        }

        setVisibleOnePlayer(currentItemIndex - 1, false);
        currentItemIndex--;
    }

    private void showNames(Player player, int index) {
        if (this.state.equals(InputFileStates.NUMBER_NAME)
                || this.state.equals(InputFileStates.NUMBER_NAME_GENDER)
                || this.state.equals(InputFileStates.NUMBER_NAME_AGE)
                || this.state.equals(InputFileStates.NUMBER_NAME_GENDER_AGE)) {
            names[index].setText(player.number + " " + player.name);
            return;
        } else
            names[index].setText(player.name);
    }

    private void cleanTable() {
        for (int i = 0; i < names.length; i++) {
            names[i].setText("");
            photos[i].setImage(null);
        }
    }

    public void setVisibleTablaOfPlayers(int size, boolean value) {
        teamLabel.setVisible(value);
        for (int i = 0; i < size; i++) {
            names[i].setVisible(value);
            photos[i].setVisible(value);
        }
    }

    public void setVisibleOnePlayer(int index, boolean value) {
        names[index].setVisible(value);
        photos[index].setVisible(value);
    }

    public void setVisibleNavigateButtons(boolean value) {
        nextButton.setVisible(value);
        nextIcon.setVisible(value);
        previousButton.setVisible(value);
        previousIcon.setVisible(value);
    }

    public void setUnvisibleTableOfPlayers() {
        setVisibleTablaOfPlayers(names.length, false);
    }

    public void setUnvisibleNavigateButtons() {
        setVisibleNavigateButtons(false);
    }

    public PreviewController setScreenResolution(Screen value) {
        this.screenResolutionProperties = value.properties;
        return this;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.appStage = primaryStage;
    }

    public void setTeamTitle(String teamTitle) {
        this.teamTitle = teamTitle;
    }

    public void setTeams(HashMap<Integer, List<Player>> teams) {
        this.finalTeams = teams;
        this.teamsCount = teams.size();
    }

    public void configureViewVisibleElements(Stage stage, Image image) {
        this.appStage = stage;
        addHotkeysEvents();

        previousButton.setOnKeyReleased((event) -> {
            if (event.getCode() == new KeyCodeCombination(KeyCode.LEFT).getCode()) {
                handlePreviousButtonAction(new ActionEvent());
            }
        });

        nextButton.setOnKeyReleased((event) -> {
            if (event.getCode() == new KeyCodeCombination(KeyCode.RIGHT).getCode()) {
                handleNextButtonAction(new ActionEvent());
            }
        });

        names = new Label[]{name1, name2, name3, name4, name5, name6, name7, name8, name9};
        photos = new ImageView[]{photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9};

        //hide all visible items
        for (int i = 0; i < names.length; i++) {
            names[i].setVisible(false);
            photos[i].setVisible(false);
        }

        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(screenResolutionProperties.height);
        imageView.setFitWidth(screenResolutionProperties.width);
        appStage.setHeight(screenResolutionProperties.height);
        appStage.setWidth(screenResolutionProperties.width);

        startIcon.setLayoutX(screenResolutionProperties.width / 2 - startIcon.getFitWidth() / 2);
        startIcon.setLayoutY(screenResolutionProperties.height / 2 - startIcon.getFitHeight() / 2);
        startButton.setPrefSize(startIcon.getFitWidth(), startIcon.getFitHeight());
        startButton.setLayoutX(startIcon.getLayoutX());
        startButton.setLayoutY(startIcon.getLayoutY());

        //buttons
        previousButton.setLayoutX(screenResolutionProperties.previousButton.getLayoutX());
        previousIcon.setLayoutX(screenResolutionProperties.previousButton.getLayoutX());
        previousButton.setLayoutY(screenResolutionProperties.previousButton.getLayoutY());
        previousIcon.setLayoutY(screenResolutionProperties.previousButton.getLayoutY());
        previousButton.setPrefSize(previousIcon.getFitWidth(), previousIcon.getFitHeight());
        //previousButton.setOpacity(1);//debug

        nextButton.setLayoutX(screenResolutionProperties.nextButton.getLayoutX());
        nextIcon.setLayoutX(screenResolutionProperties.nextButton.getLayoutX());
        nextButton.setLayoutY(screenResolutionProperties.nextButton.getLayoutY());
        nextIcon.setLayoutY(screenResolutionProperties.nextButton.getLayoutY());
        nextButton.setPrefSize(nextIcon.getFitWidth(), nextIcon.getFitHeight());
        //nextButton.setOpacity(1);//debug

        teamLabel.setFont(this.font);
        teamLabel.setLayoutX(screenResolutionProperties.width / 2 - teamLabel.getPrefWidth() / 2);
        teamLabel.setLayoutY(screenResolutionProperties.height * 0.03);
        teamLabel.setTextFill(this.textColor);
        teamLabel.setFont(Font.font(this.font.getFamily(), this.font.getSize() * 1.2));
        teamLabel.setText(this.teamTitle);

        //players
        int teamMaxSize = initMaxCount();

        Map<Integer, Double> fontSizeMap = new HashMap<>();
        fontSizeMap.put(2, 0.3);
        fontSizeMap.put(3, 0.48);
        fontSizeMap.put(4, 0.6);
        fontSizeMap.put(5, 0.6);
        fontSizeMap.put(6, 0.65);
        fontSizeMap.put(7, 0.6);
        fontSizeMap.put(8, 0.6);
        fontSizeMap.put(9, 0.58);

        for (int i = 0; i < teamMaxSize; i++) {

            names[i].setPrefWidth(screenResolutionProperties.width * 0.7);
            names[i].setPrefHeight(screenResolutionProperties.height * 0.75 / teamMaxSize);
            names[i].setTextFill(this.textColor);
            names[i].setFont(Font.font(this.font.getFamily(),
                    names[i].getPrefHeight() * fontSizeMap.get(teamMaxSize) * textValueRate));

            names[i].setLayoutX(screenResolutionProperties.width * 0.2);
            if (i == 0) {
                names[i].setLayoutY(screenResolutionProperties.height * 0.1);
            } else {
                names[i].setLayoutY(names[i - 1].getLayoutY() + photos[i - 1].getFitHeight() + screenResolutionProperties.nameCoefficientY);
            }

            photos[i].setPreserveRatio(true);
            photos[i].setFitHeight(names[i].getFont().getSize() * PHOTO_RESIZABLE_RATE);
            photos[i].setFitWidth(names[i].getFont().getSize() * PHOTO_RESIZABLE_RATE);
            photos[i].setLayoutX(names[i].getLayoutX() - photos[i].getFitWidth() * 1.2);
            photos[i].setLayoutY(names[i].getLayoutY() + (names[i].getPrefHeight() - photos[i].getFitHeight()) * 0.5);
        }

        //выравнивание по вертикали
        Double minY = photos[0].getLayoutY() - screenResolutionProperties.height * 0.05;
        Double maxY = screenResolutionProperties.height - (names[teamMaxSize - 1].getLayoutY() + names[teamMaxSize - 1].getPrefHeight());

        if (maxY < screenResolutionProperties.height * 0.8) {
            double resY = (maxY + minY) / 2;
            double newY = (resY - minY);

            for (int i = 0; i < teamMaxSize; i++) {
                names[i].setLayoutY(names[i].getLayoutY() + newY);
                photos[i].setLayoutY(photos[i].getLayoutY() + newY);
            }
        }
    }

    private synchronized void playButtonEffect(ImageView nextIcon) {
        double startWidth = nextIcon.getFitWidth();
        double startHeight = nextIcon.getFitHeight();
        double startLayoutX = nextIcon.getLayoutX();
        double startLayoutY = nextIcon.getLayoutY();

        nextIcon.setFitWidth(startWidth * 0.85);
        nextIcon.setFitHeight(startHeight * 0.85);
        nextIcon.setLayoutX(startLayoutX + (startWidth * 0.15) / 2);
        nextIcon.setLayoutY(startLayoutY + (startHeight * 0.15) / 2);

        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        nextIcon.setFitWidth(startWidth);
        nextIcon.setFitHeight(startHeight);
        nextIcon.setLayoutX(startLayoutX);
        nextIcon.setLayoutY(startLayoutY);
    }

    private int initMaxCount() {
        int max = 0;

        for (Map.Entry<Integer, List<Player>> team : finalTeams.entrySet()) {
            int count = team.getValue().size();
            if (count > max)
                max = count;
        }

        return max;
    }

    public void setFullScreenIfNeeded(boolean isNeed) {
        if (!isNeed) {
            appStage.setWidth(screenResolutionProperties.width);
            appStage.setHeight(screenResolutionProperties.height);

            return;
        }

        appStage.setFullScreen(true);
        imageView.setPreserveRatio(true);

        imageView.setFitHeight(javafx.stage.Screen.getPrimary().getBounds().getMaxY() * RESIZABLE_RATE);
        imageView.setFitWidth(javafx.stage.Screen.getPrimary().getBounds().getMaxX() * RESIZABLE_RATE);
    }

    public void setPathToPhoto(boolean usePhoto, String pathToPhoto) {
        this.usePhoto = usePhoto;
        this.pathToPhoto = pathToPhoto;
    }

    public void setTextSettings(Color value, Font font) {
        this.textColor = value;
        this.font = font;
    }

    public void setTextValueRate(Double value) {
        this.textValueRate = value / 100;
    }

    public void setState(InputFileStates value) {
        this.state = value;
    }

    private Image getPhoto(Player player) {
        if (!player.genderIsExists()) {
            if (!photoExists(pathToPhoto + "//" + player.name).equals("")) {
                return new Image(Paths.FILE + pathToPhoto + "//" + player.name);
            }

            return new Image(getClass().getClassLoader().getResourceAsStream(IMAGES + NO_GENDER));
        }

        String urlPhotoByName = photoExists(pathToPhoto + "//" + player.name);
        String urlPhotoByNum = photoExists(pathToPhoto + "//" + player.number);
        String urlCustomPhoto = pathToPhoto + "//";

        String photoUrl = null;
        if (!urlPhotoByName.equals(""))
            photoUrl = urlPhotoByName;
        else if (!urlPhotoByNum.equals(""))
            photoUrl = urlPhotoByNum;


        if (photoUrl != null)
            return new Image(Paths.FILE + photoUrl);

        else if (!photoExists(urlCustomPhoto + BOY).equals("")
                || !photoExists(urlCustomPhoto + GIRL).equals(""))
            return player.genderIsExists() && player.gender.equals(Gender.BOY) ?
                    new Image(Paths.FILE + photoExists(urlCustomPhoto + BOY))
                    : new Image(Paths.FILE + photoExists(urlCustomPhoto + GIRL));
        else
            return player.genderIsExists() && player.gender.equals(Gender.BOY) ?
                    new Image(getClass().getClassLoader().getResourceAsStream(IMAGES + BOY_PNG))
                    : new Image(getClass().getClassLoader().getResourceAsStream(IMAGES + GIRL_PNG));
    }

    private String photoExists(String url) {
        if (java.nio.file.Paths.get(url + FileExtensions.PNG).toFile().exists())
            return url + FileExtensions.PNG;
        if (java.nio.file.Paths.get(url + FileExtensions.JPEG).toFile().exists())
            return url + FileExtensions.JPEG;
        if (java.nio.file.Paths.get(url + FileExtensions.JPG).toFile().exists())
            return url + FileExtensions.JPG;

        return "";
    }

    private KeyCombination restartHotKey = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
    private KeyCombination fullScreenHotKey = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
    private KeyCombination nextButtonHotKey = new KeyCodeCombination(KeyCode.RIGHT);
    private KeyCombination previousButtonHotKey = new KeyCodeCombination(KeyCode.LEFT);
    private KeyCombination startButtonHotKey = new KeyCodeCombination(KeyCode.ENTER);

    private void addHotkeysEvents() {
        appStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (restartHotKey.match(event)) {
                SettingsController settingsController = initNextView(new ActionEvent(), getNextViewName());
                settingsController.initScene(this.appStage);

                this.finalTeams.clear();
                currentPageIndex = 0;
                currentItemIndex = 0;

                appStage.setWidth(RuntimeSettings.SETTING_VIEW_LAST_WIDTH);
                appStage.setHeight(RuntimeSettings.SETTING_VIEW_LAST_HEIGHT);

                settingsController.setPrimaryStage(appStage);
            }
        });

        appStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (fullScreenHotKey.match(event)) {
                appStage.setFullScreen(true);
            }
        });

        appStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (previousButtonHotKey.match(event)) {
                handlePreviousButtonAction(new ActionEvent());
            }
        });

        appStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (nextButtonHotKey.match(event)) {
                handleNextButtonAction(new ActionEvent());
            }
        });

        appStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (startButtonHotKey.match(event)) {
                handleStartButtonAction(new ActionEvent());
            }
        });
    }

    @Override
    public String getNextViewName() {
        return SETTINGS_VIEW;
    }

    @Override
    public <T> T initNextView(ActionEvent event, String viewName) {
        URL locationUrl = getClass().getClassLoader().getResource(viewName);
        FXMLLoader loader = new FXMLLoader(locationUrl);

        try {
            appStage.setScene(new Scene((Pane) loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loader.getController();
    }
}