package ru.demedyuk.randomize.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.demedyuk.randomize.AppLaunch;
import ru.demedyuk.randomize.configuration.RuntimeSettings;
import ru.demedyuk.randomize.configuration.properties.ActionProperties;
import ru.demedyuk.randomize.configuration.properties.ConfigProperties;
import ru.demedyuk.randomize.configuration.screen.Screen;
import ru.demedyuk.randomize.constants.FileExtensions;
import ru.demedyuk.randomize.constants.Paths;
import ru.demedyuk.randomize.messages.About;
import ru.demedyuk.randomize.messages.OutputMessages;
import ru.demedyuk.randomize.messages.Tooltips;
import ru.demedyuk.randomize.messages.WindowTitles;
import ru.demedyuk.randomize.models.files.InputFileReader;
import ru.demedyuk.randomize.utils.FileUtils;
import ru.demedyuk.randomize.utils.actions.RandomizeAction;

import java.io.*;
import java.net.URL;
import java.util.*;

import static ru.demedyuk.randomize.constants.FileExtensions.*;
import static ru.demedyuk.randomize.constants.PaintColors.BLACK;
import static ru.demedyuk.randomize.constants.PaintColors.GRAY;
import static ru.demedyuk.randomize.constants.Paths.DEFAULT_PROPERTIES;
import static ru.demedyuk.randomize.constants.Paths.PREVIEW_VIEW;
import static ru.demedyuk.randomize.messages.WindowTitles.main_title;
import static ru.demedyuk.randomize.utils.FileUtils.makeDirsIfNotExists;
import static ru.demedyuk.randomize.utils.actions.OutputMessageActions.showErrorMessageWithDefaultDelay;

public class SettingsController implements IController {

    private Stage appStage;
    private Properties props;
    private static Properties currentProps;
    private static String pathToConfig;

    @FXML
    void quitMenuItemActionHandler(ActionEvent event) {
        AppLaunch.stopApplication();
    }

    @FXML
    private TextField input_info;
    @FXML
    private Button selectListOfPlayersButton;
    @FXML
    public TextField ouput_info;
    @FXML
    private Button selectResultDirectoryButton;
    @FXML
    private ChoiceBox<String> countOfPlayers = new ChoiceBox<String>();
    @FXML
    private ChoiceBox<String> textFont = new ChoiceBox<String>();
    @FXML
    private CheckBox isBalansing;
    @FXML
    private CheckBox usePrivatePhoto;
    @FXML
    private Text label_photo;
    @FXML
    private TextField path_to_photo;
    @FXML
    private Button selectPhotoButton;
    @FXML
    private TextField background;
    @FXML
    private Button selectBackgroundImageButton;
    @FXML
    private RadioButton fullScrene;
    @FXML
    private RadioButton inWindow;
    @FXML
    private RadioButton _1920x1080_button;
    @FXML
    private RadioButton _1366x768_button;
    @FXML
    private RadioButton _1024x768_button;
    @FXML
    private RadioButton _800x600_button;
    @FXML
    private ProgressBar progress;
    @FXML
    private Text messageField;
    @FXML
    private Button launch;
    @FXML
    private TextField teamTitle;
    @FXML
    private ColorPicker textColor;
    @FXML
    private Slider textRate;
    @FXML
    private TextField textRateField;
    @FXML
    private Button createListOfPlayersButton;
    @FXML
    private Button editListOfPlayersButton;

    @FXML
    void fileActionHandler(ActionEvent event) {
    }

    @FXML
    void fileNewActionHandler(ActionEvent event) {
        input_info.setText("");
        ouput_info.setText("");
        countOfPlayers.setValue("2 участника");
        isBalansing.setSelected(false);
        usePrivatePhoto.setSelected(false);
        path_to_photo.setText("");

        background.setText("");
        textFont.setValue("Default");
        textColor.setValue(Color.WHITE);
        textRate.setValue(100);
        updateTextRateField();
        inWindow.setSelected(false);
        fullScrene.setSelected(true);
        setScreen(Screen._1920x1080.name);

        checkProgress();
        usePrivatePhotoAction(new ActionEvent());

        appStage.setTitle(main_title);
        updateProps(this.props);
    }

    @FXML
    void fileOpenActionHandler(ActionEvent event) {
        File configDirectory = makeDirsIfNotExists("\\configs");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(configDirectory);
        fileChooser.setTitle(WindowTitles.open_configuration_title);
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Файлы конфигураций", "*.config"));
        File selectedFile = fileChooser.showOpenDialog(null);
        pathToConfig = selectedFile.getPath();

        try (InputStream input = new FileInputStream(this.pathToConfig)) {
            this.props = new Properties();
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        initConfig(event, pathToConfig, this.props);
    }

    @FXML
    void fileSaveActionHandler(ActionEvent event) {
        updateProps(this.props);

        if (pathToConfig != null)
            ActionProperties.saveProperties(this.props, pathToConfig);
        else
            fileSaveAsActionHandler(event);
    }

    @FXML
    void fileSaveAsActionHandler(ActionEvent event) {
        updateProps(this.props);
        File configDirectory = makeDirsIfNotExists("\\configs\\");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(configDirectory);
        fileChooser.setTitle(WindowTitles.save_configuration_title);
        fileChooser.setInitialFileName("new" + CONFIG);
        File selectedFile = fileChooser.showSaveDialog(null);

        pathToConfig = ActionProperties.saveProperties(this.props, selectedFile);
    }

    @FXML
    void helpAboutActionHandler(ActionEvent event) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(this.appStage);
        dialog.setTitle(WindowTitles.about_window_title);
        dialog.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream(AppLaunch.PATH_TO_LOGO)));
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(About.ABOUT_TEXT));
        Scene dialogScene = new Scene(dialogVbox, 450, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void initScene() {
        countOfPlayers.getItems().addAll("2 участника",
                "3 участника",
                "4 участника",
                "5 участников",
                "6 участников",
                "7 участников",
                "8 участников");
        countOfPlayers.setValue("2 участника");
        isBalansing.setSelected(false);
        usePrivatePhoto.setSelected(false);
        label_photo.setFill(GRAY);
        teamTitle.setText("Команда");

        textRate.setTooltip(new Tooltip(Tooltips.slider_text_rate_tooltip));
        textRateField.setTooltip(new Tooltip(Tooltips.text_rate_value_tooltip));

        scanFonts();

        InputStream input = null;
        if (this.pathToConfig == null)
            input = getClass().getClassLoader().getResourceAsStream(DEFAULT_PROPERTIES);
        else {
            try {
                input = new FileInputStream(this.pathToConfig);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        this.props = new Properties();
        try {
            this.props.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        initConfig(null, this.pathToConfig, currentProps != null ? currentProps : this.props);
    }

    public void initConfig(ActionEvent event, String path, Properties props) {
        //загрузка конгфигурации по умолчанию
        input_info.setText(getPropertyIfExists(props, ConfigProperties.PLAYERS_FILE));
        ouput_info.setText(getPropertyIfExists(props, ConfigProperties.RESULT_FILE));
        countOfPlayers.setValue(getPropertyIfExists(props, ConfigProperties.TEAM_SIZE));
        isBalansing.setSelected(getPropertyIfExists(props, ConfigProperties.USE_BALANSE).equals("true") ? true : false);

        boolean usePhoto = getPropertyIfExists(props, ConfigProperties.USE_PHOTO).equals("true") ? true : false;
        usePrivatePhoto.setSelected(usePhoto);
        path_to_photo.setText(getPropertyIfExists(props, ConfigProperties.PHOTO_DIRECTORY_FILE));
        usePrivatePhotoAction(new ActionEvent());

        background.setText(getPropertyIfExists(props, ConfigProperties.BACKGROUD_FILE));
        textColor.setValue(Color.valueOf(getPropertyIfExists(props, ConfigProperties.TEXT_COLOR).equals("") ?
                Color.WHITE.toString() : getPropertyIfExists(props, ConfigProperties.TEXT_COLOR)));
        String textRateProperty = getPropertyIfExists(props, ConfigProperties.TEXT_RATE);
        if (!textRateProperty.equals(""))
            textRate.setValue(Double.parseDouble(textRateProperty));

        String textFontValue = getPropertyIfExists(props, ConfigProperties.TEXT_FONT);
        inWindow.setSelected(false);
        fullScrene.setSelected(true);
        if (!textFontValue.equals("")) {
            for (String item : this.textFont.getItems()) {
                if (textFontValue.equals(item))
                    textFont.setValue(textFontValue);
            }
        }
        teamTitle.setText(getPropertyIfExists(props, ConfigProperties.TEAM_NAME));

        setScreen(getPropertyIfExists(props, ConfigProperties.SCREEN_SIZE));
        if (getPropertyIfExists(props, ConfigProperties.SCREEN_IS_FULL).equals("true") ? true : false) {

        } else {
            inWindow.setSelected(true);
            fullScrene.setSelected(false);
        }

        checkProgress();

        //установка заголовка окна
        if (event != null) {
            String oldTitle = this.appStage.getTitle();
            this.appStage.setTitle(oldTitle + " (" + path + ")");
        }

        checkProgress();
        updateTextRateField();

        textRate.setOnMouseReleased((MouseEvent eventMouse) -> {
            updateTextRateField();
            textRate.setValueChanging(true);
        });

        textRate.setOnMousePressed((MouseEvent eventMouse) -> {
            updateTextRateField();
            textRate.setValueChanging(true);
        });

        textRate.setOnMouseDragReleased((MouseEvent eventMouse) -> {
            updateTextRateField();
            textRate.setValueChanging(true);
        });

        textRate.setOnMouseDragged((MouseEvent eventMouse) -> {
            updateTextRateField();
            textRate.setValueChanging(true);
        });

        textRate.valueChangingProperty().addListener((obs, oldVal, newVal) -> {
            updateTextRateField();
        });
    }

    @FXML
    void handleFullScrene(ActionEvent event) {
        fullScrene.setSelected(true);
        inWindow.setSelected(false);
    }

    @FXML
    void handleInWindow(ActionEvent event) {
        inWindow.setSelected(true);
        fullScrene.setSelected(false);
    }

    @FXML
    void usePrivatePhotoAction(ActionEvent event) {
        boolean value = usePrivatePhoto.isSelected();
        label_photo.setFill(value ? BLACK : GRAY);
        path_to_photo.setDisable(!value);
        selectPhotoButton.setDisable(!value);
        checkProgress();
    }

    @FXML
    void selectListOfPlayersButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(FileUtils.findInitialDirectory(input_info.getText(), "\\players\\"));
        fileChooser.setTitle(WindowTitles.selected_list_oj_players_title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Участники", ALL_PREFIX + PLAYERS),
                new FileChooser.ExtensionFilter("Текстовые файлы", ALL_PREFIX + TXT),
                new FileChooser.ExtensionFilter("Все", ALL_FILES));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            input_info.setText(selectedFile.getAbsolutePath());
            editListOfPlayersButton.setDisable(false);
        }

        checkProgress();
    }

    @FXML
    void createListOfPlayersButtonAction(ActionEvent event) {
        URL locationUrl = getClass().getClassLoader().getResource("views/UserListView" + FXML);

        FXMLLoader loader = new FXMLLoader(locationUrl);

        Stage stage = new Stage();
        try {
            stage.setScene(new Scene((Pane) loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.initModality(Modality.APPLICATION_MODAL);

        UserListController userListController = loader.<UserListController>getController();
        userListController.setPrimaryStage(stage, "Новый список игроков");
        userListController.showContent(input_info.getText(), true);

        stage.setResizable(false);
        stage.showAndWait();

        String inputFilePath = userListController.getInputFilePath();
        if (inputFilePath != null && !inputFilePath.isEmpty()) {
            input_info.setText(inputFilePath);
            editListOfPlayersButton.setDisable(false);
        }
        checkProgress();
    }

    @FXML
    void editListOfPlayersButtonAction(ActionEvent event) {
        URL locationUrl = getClass().getClassLoader().getResource("views/UserListView" + FXML);

        FXMLLoader loader = new FXMLLoader(locationUrl);

        Stage stage = new Stage();
        try {
            stage.setScene(new Scene((Pane) loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        stage.initModality(Modality.APPLICATION_MODAL);

        UserListController userListController = loader.<UserListController>getController();
        userListController.showContent(input_info.getText(), false);

        stage.setResizable(false);
        userListController.setPrimaryStage(stage, "Редактирование списка игроков");
        stage.showAndWait();

        String inputFilePath = userListController.getInputFilePath();
        if (inputFilePath != null && !inputFilePath.isEmpty())
            input_info.setText(inputFilePath);
        checkProgress();
    }

    @FXML
    void selectResultDirectoryButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(FileUtils.findInitialDirectory(ouput_info.getText(), "\\results\\"));
        fileChooser.setTitle(WindowTitles.folder_results_title);
        fileChooser.setInitialFileName("result" + FileExtensions.DOCX);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("results", ALL_PREFIX + DOCX),
                new FileChooser.ExtensionFilter("Все", ALL_FILES));

        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null)
            ouput_info.setText(selectedFile.getAbsolutePath());

        checkProgress();
    }


    @FXML
    void screenResolutionAction(ActionEvent event) {
        HashMap<RadioButton, Boolean> map = new HashMap<RadioButton, Boolean>();
        map.put(_1920x1080_button, false);
        map.put(_1366x768_button, false);
        map.put(_1024x768_button, false);
        map.put(_800x600_button, false);

        RadioButton radio = (RadioButton) event.getSource();
        if (radio.isSelected()) {
            map.remove(radio);
            for (Map.Entry<RadioButton, Boolean> entry : map.entrySet()) {
                entry.getKey().setSelected(false);
            }
        } else {
            radio.setSelected(true);
        }
    }

    @FXML
    void selectPhotoButtonAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(FileUtils.findInitialDirectory(path_to_photo.getText(),
                "\\photos\\"));
        directoryChooser.setTitle(WindowTitles.folder_photos_title);

        File selectedFile = directoryChooser.showDialog(null);

        if (selectedFile != null)
            path_to_photo.setText(selectedFile.getAbsolutePath());

        checkProgress();
    }

    @FXML
    void selectBackgroundImageButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(FileUtils.findInitialDirectory(background.getText(), "\\backgrounds\\"));
        fileChooser.setTitle(WindowTitles.selected_background_title);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "Изображения",
                ALL_PREFIX + JPEG, ALL_PREFIX + JPG));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            background.setText(selectedFile.getAbsolutePath());
        }
        checkProgress();
    }

    @FXML
    void handleLaunchButtonAction(ActionEvent event) {
        checkProgress();
        if (progress.getProgress() < 0.99) {
            return;
        }

        if (teamTitle.getText().length() > 15) {
            showErrorMessageWithDefaultDelay(messageField, OutputMessages.error_team_name_lenth);
            return;
        }

        //save current props
        currentProps = this.props;
        updateProps(currentProps);

        InputFileReader playersFile = new InputFileReader(input_info.getText());

        try {
            playersFile.validateDocument();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            messageField.setText(e.getMessage());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            messageField.setText(OutputMessages.error_write_players_file);
            return;
        }

        int teamSize = Integer.parseInt(countOfPlayers.getValue().substring(0, 1));

        //randomize
        RandomizeAction randomizeAction = null;
        try {
            randomizeAction = new RandomizeAction(
                    playersFile.getAllPlayers(),
                    teamSize,
                    isBalansing.isSelected());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            messageField.setText(e.getMessage());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            messageField.setText(OutputMessages.error_generation_teams);
            return;
        }

        //сохранение результата
        RandomizeAction resultSaveThread = randomizeAction;
        Thread saveResultsThread = new Thread(() ->
                resultSaveThread.saveResults(ouput_info.getText(), teamTitle.getText()));
        saveResultsThread.start();

        RuntimeSettings.SETTING_VIEW_LAST_WIDTH = appStage.getWidth();
        RuntimeSettings.SETTING_VIEW_LAST_HEIGHT = appStage.getHeight();

        Screen screen = getScreen();

        Font font = null;
        try {
            font = loadFont(screen);
        } catch (Exception e) {
            messageField.setText(OutputMessages.error_load_font);
        }

        //сохранение конфигов
        updateProps(this.props);
        currentProps = this.props;

        PreviewController previewController = initNextView(event, getNextViewName());

        previewController.setScreenResolution(screen);
        previewController.setTextValueRate(textRate.getValue());
        previewController.setPrimaryStage(appStage);
        previewController.setTextSettings(textColor.getValue(), font);
        previewController.setTeamTitle(teamTitle.getText());
        previewController.setPathToPhoto(usePrivatePhoto.isSelected(), path_to_photo.getText());
        previewController.setState(playersFile.getState());
        previewController.setTeams(randomizeAction.getResult());
        previewController.configureViewVisibleElements(appStage, new Image(Paths.FILE + background.getText()));

        previewController.setUnvisibleTableOfPlayers();
        previewController.setUnvisibleNavigateButtons();
        previewController.setFullScreenIfNeeded(fullScrene.isSelected());

        updateScene(appStage);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.appStage = primaryStage;
    }

    private void updateProps(Properties props) {
        setPropertyIfExists(props, ConfigProperties.PLAYERS_FILE, input_info.getText());
        setPropertyIfExists(props, ConfigProperties.RESULT_FILE, ouput_info.getText());
        setPropertyIfExists(props, ConfigProperties.TEAM_SIZE, countOfPlayers.getValue());
        setPropertyIfExists(props, ConfigProperties.USE_BALANSE, isBalansing.isSelected() ? "true" : "false");
        setPropertyIfExists(props, ConfigProperties.USE_PHOTO, usePrivatePhoto.isSelected() ? "true" : "false");
        setPropertyIfExists(props, ConfigProperties.PHOTO_DIRECTORY_FILE, path_to_photo.getText());

        setPropertyIfExists(props, ConfigProperties.BACKGROUD_FILE, background.getText());
        setPropertyIfExists(props, ConfigProperties.TEXT_COLOR, textColor.getValue().toString());
        setPropertyIfExists(props, ConfigProperties.TEXT_FONT, textFont.getValue());
        setPropertyIfExists(props, ConfigProperties.TEXT_RATE, String.valueOf(textRate.getValue()));
        setPropertyIfExists(props, ConfigProperties.TEAM_NAME, teamTitle.getText());
        setPropertyIfExists(props, ConfigProperties.SCREEN_SIZE, getScreen().name);
        setPropertyIfExists(props, ConfigProperties.SCREEN_IS_FULL, fullScrene.isSelected() ? "true" : "false");
    }

    private void setPropertyIfExists(Properties props, ConfigProperties property, String value) {
        try {
            props.setProperty(property.key, value);
        } catch (Exception e) {
            //Ignores
        }
    }

    private String getPropertyIfExists(Properties props, ConfigProperties property) {
        try {
            String value = props.getProperty(property.key);
            return value != null ? value : "";
        } catch (Exception e) {
            //Ignore
        }

        return "";
    }

    private void checkProgress() {
        progress.setProgress(0);

        double progressValue = usePrivatePhoto.isSelected() ? 0.25 : 0.34;

        if (!background.getText().isEmpty())
            addProgressValue(progressValue);
        else
            messageField.setText(OutputMessages.help_select_background_file);

        if (usePrivatePhoto.isSelected() && !path_to_photo.getText().isEmpty())
            addProgressValue(progressValue);
        else if (usePrivatePhoto.isSelected() && path_to_photo.getText().isEmpty())
            messageField.setText(OutputMessages.help_select_photo_folder);

        if (!ouput_info.getText().isEmpty())
            addProgressValue(progressValue);
        else
            messageField.setText(OutputMessages.help_select_results_folder);

        if (!input_info.getText().isEmpty())
            addProgressValue(progressValue);
        else
            messageField.setText(OutputMessages.help_select_players_file);

        if (progress.getProgress() >= 0.9) {
            messageField.setText("");
        }

    }

    private void addProgressValue(double value) {
        progress.setProgress(progress.getProgress() + value);
    }

    private void updateTextRateField() {
        textRateField.setText((int) textRate.getValue() + "%");
    }

    private void scanFonts() {
        File folder = new File(".\\fonts");
        File[] listOfFiles = folder.listFiles();

        List<String> fonts = new ArrayList<String>();

        if (listOfFiles != null)
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().contains(FileExtensions.TTF)) {
                    String fileName = file.getName();
                    String fontName = fileName.substring(0, fileName.lastIndexOf("."));
                    fonts.add(fontName);
                }
            }

        textFont.getItems().add("Default");
        textFont.getItems().addAll(fonts);
        textFont.setValue("Default");
    }

    private Font loadFont(Screen screen) {
        if (!textFont.getValue().equals("Default")) {
            String url = ".//fonts//" + textFont.getValue() + ".ttf";
            String absolutePath = java.nio.file.Paths.get(url).toFile().getAbsolutePath();
            return Font.loadFont(Paths.FILE + absolutePath, screen.properties.fontSize);
        }

        return Font.loadFont(getClass().getClassLoader().getResourceAsStream(Paths.DEFAULT_FONT), screen.properties.fontSize);
    }

    private Screen getScreen() {

        if (_1920x1080_button.isSelected())
            return Screen._1920x1080;

        if (_1366x768_button.isSelected())
            return Screen._1366x768;

        if (_1024x768_button.isSelected())
            return Screen._1024x768;

        if (_800x600_button.isSelected())
            return Screen._800x600;

        return Screen._800x600;
    }

    private void setScreen(String name) {
        _1920x1080_button.setSelected(false);
        _1366x768_button.setSelected(false);
        _1024x768_button.setSelected(false);
        _800x600_button.setSelected(false);

        if (name == null) {
            _1920x1080_button.setSelected(true);
            return;
        }


        if (name.equals(Screen._1920x1080.name)) {
            _1920x1080_button.setSelected(true);
            return;
        }

        if (name.equals(Screen._1366x768.name)) {
            _1366x768_button.setSelected(true);
            return;
        }

        if (name.equals(Screen._1024x768.name)) {
            _1024x768_button.setSelected(true);
            return;
        }

        if (name.equals(Screen._800x600.name)) {
            _800x600_button.setSelected(true);
            return;
        }

        _1920x1080_button.setSelected(true);
    }

    @Override
    public <T> T initNextView(ActionEvent event, String viewName) {
        URL locationUrl = getClass().getClassLoader().getResource(viewName);
        appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(locationUrl);

        try {
            appStage.setScene(new Scene((Pane) loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loader.getController();
    }

    @Override
    public String getNextViewName() {
        return PREVIEW_VIEW;
    }
}

