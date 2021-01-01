package ru.demedyuk.randomize.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
import ru.demedyuk.randomize.models.files.InputFileReader;
import ru.demedyuk.randomize.utils.FileUtils;
import ru.demedyuk.randomize.utils.actions.RandomizeAction;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static ru.demedyuk.randomize.configuration.properties.ConfigProperties.*;
import static ru.demedyuk.randomize.constants.FileExtensions.*;
import static ru.demedyuk.randomize.constants.PaintColors.BLACK;
import static ru.demedyuk.randomize.constants.PaintColors.GRAY;
import static ru.demedyuk.randomize.utils.FileUtils.makeDirsIfNotExists;
import static ru.demedyuk.randomize.utils.actions.OutputMessageActions.showErrorMessageWithDefaultDelay;

public class SettingsController implements IController {

    private static final double PROGRESS_VALUE = 0.25;
    private Stage appStage;
    private Properties props;
    private static String pathToConfig;
    private static File pathToSettings;

    private static final String DEFAULT_PROPERTIES = "properties/default.properties";

    @FXML
    void fileActionHandler(ActionEvent event) {
    }

    @FXML
    void fileNewActionHandler(ActionEvent event) {
        input_info.setText("");
        ouput_info.setText("");
        countOfPlayers.setValue("2 участника");
        background.setText("");
        isBalansing.setSelected(false);
        usePrivatePhoto.setSelected(false);
        path_to_photo.setText("");
        checkProgress();
        usePrivatePhotoAction(new ActionEvent());

        appStage.setTitle("Randomize Master");
        updateProps();
    }


    @FXML
    void fileOpenActionHandler(ActionEvent event) {
        File configDirectory = makeDirsIfNotExists("\\configs");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(configDirectory);
        fileChooser.setTitle("Открыть конфигурацию");
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Файлы конфигураций", "*.config"));
        File selectedFile = fileChooser.showOpenDialog(null);
        pathToConfig = selectedFile.getPath();

        initConfig(event, pathToConfig);
    }

    @FXML
    void fileSaveActionHandler(ActionEvent event) {
        updateProps();

        if (pathToConfig != null)
            ActionProperties.saveProperties(this.props, pathToConfig);
        else
            fileSaveAsActionHandler(event);
    }


    @FXML
    void fileSaveAsActionHandler(ActionEvent event) {
        updateProps();
        File configDirectory = makeDirsIfNotExists("\\configs\\");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(configDirectory);
        fileChooser.setTitle("Сохранить конфигурацию");
        fileChooser.setInitialFileName("new" + CONFIG);
        File selectedFile = fileChooser.showSaveDialog(null);

        pathToConfig = ActionProperties.saveProperties(this.props, selectedFile);
    }

    @FXML
    void helpAboutActionHandler(ActionEvent event) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(this.appStage);
        dialog.setTitle("About");
        dialog.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream(AppLaunch.PATH_TO_LOGO)));
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(About.ABOUT_TEXT));
        Scene dialogScene = new Scene(dialogVbox, 450, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    @FXML
    void quitMenuItemActionHandler(ActionEvent event) {
        AppLaunch.stopApplication();
    }

    private void updateProps() {
        setPropertyIfExists(PLAYERS_FILE, input_info.getText());
        setPropertyIfExists(RESULT_FILE, ouput_info.getText());
        setPropertyIfExists(USE_BALANSE, isBalansing.isSelected() ? "true" : "false");
        setPropertyIfExists(USE_PHOTO, isBalansing.isSelected() ? "true" : "false");
        setPropertyIfExists(PHOTO_DIRECTORY_FILE, path_to_photo.getText());

        setPropertyIfExists(BACKGROUD_FILE, background.getText());
        setPropertyIfExists(TEXT_COLOR, textColor.getValue().toString());
        setPropertyIfExists(TEAM_NAME, teamTitle.getText());
        setPropertyIfExists(SCREEN_SIZE, getScreen().name);
        setPropertyIfExists(SCREEN_IS_FULL, fullScrene.isSelected() ? "true" : "false");
    }

    private void setPropertyIfExists(ConfigProperties property, String value) {
        try {
            props.setProperty(property.key, value);
        } catch (Exception e) {
            System.out.println("Ошибка при сохранеии " + property.key);
        }
    }

    private String getPropertyIfExists(ConfigProperties property) {
        try {
            String value = props.getProperty(property.key);
            return value != null ? value : "";
        } catch (Exception e) {
            System.out.println("Ошибка при получении " + property.key);
        }

        return "";
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
        messageField.setText("Выберите файл с участниками");
        teamTitle.setText("Команда");

        if (this.pathToConfig == null)
            this.pathToConfig = getClass().getClassLoader().getResource(DEFAULT_PROPERTIES).getFile();

        initConfig(null, this.pathToConfig);
    }

    public void initConfig(ActionEvent event, String path) {
        //загрузка конгфигурации по умолчанию
        try (InputStream input = new FileInputStream(path)) {

            this.props = new Properties();
            props.load(input);

            input_info.setText(getPropertyIfExists(PLAYERS_FILE));
            ouput_info.setText(getPropertyIfExists(RESULT_FILE));
            isBalansing.setSelected(getPropertyIfExists(USE_BALANSE).equals("true") ? true : false);

            boolean usePhoto = getPropertyIfExists(USE_PHOTO).equals("true") ? true : false;
            usePrivatePhoto.setSelected(usePhoto);
            path_to_photo.setText(getPropertyIfExists(PHOTO_DIRECTORY_FILE));
            usePrivatePhotoAction(new ActionEvent());

            background.setText(getPropertyIfExists(BACKGROUD_FILE));
            textColor.setValue(Color.valueOf(getPropertyIfExists(TEXT_COLOR).equals("") ? Color.WHITE.toString() : getPropertyIfExists(TEXT_COLOR)));
            teamTitle.setText(getPropertyIfExists(TEAM_NAME));
            setScreen(getPropertyIfExists(SCREEN_SIZE));
            if (getPropertyIfExists(SCREEN_SIZE).equals("false") ? true : false) {
                inWindow.setSelected(true);
                fullScrene.setSelected(false);
            } else {
                inWindow.setSelected(false);
                fullScrene.setSelected(true);
            }

            checkProgress();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //установка заголовка окна
        if (event != null) {
            String oldTitle = this.appStage.getTitle();
            this.appStage.setTitle(oldTitle + " (" + path + ")");
        }

        checkProgress();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.appStage = primaryStage;
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

    private void checkProgress() {
        progress.setProgress(0);

        if (!background.getText().isEmpty())
            addProgressValue();
        else
            messageField.setText("Выберите фон");

        if (!usePrivatePhoto.isSelected())
            addProgressValue();
        else if (usePrivatePhoto.isSelected() && !path_to_photo.getText().isEmpty())
            addProgressValue();
        else if (usePrivatePhoto.isSelected() && path_to_photo.getText().isEmpty())
            messageField.setText("Укажите каталог с личными фото");

        if (!ouput_info.getText().isEmpty())
            addProgressValue();
        else
            messageField.setText("Выберите каталог для сохранения результата");

        if (!input_info.getText().isEmpty())
            addProgressValue();
        else
            messageField.setText("Выберите файл с участниками");

        if (progress.getProgress() >= 0.9) {
            messageField.setText("");
        }

    }

    private void addProgressValue() {
        double fixValue = usePrivatePhoto.isSelected() ? PROGRESS_VALUE : 0.34;
        progress.setProgress(progress.getProgress() + PROGRESS_VALUE);
    }

    @FXML
    void selectListOfPlayersButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(FileUtils.findInitialDirectory(input_info.getText(), "\\players\\"));
        fileChooser.setTitle("Выберите список участников");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Участники", ALL_PREFIX + PLAYERS),
                new FileChooser.ExtensionFilter("Текстовые файлы", ALL_PREFIX + TXT),
                new FileChooser.ExtensionFilter("Все", ALL_FILES));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null)
            input_info.setText(selectedFile.getAbsolutePath());
        checkProgress();
    }

    @FXML
    void selectResultDirectoryButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(FileUtils.findInitialDirectory(ouput_info.getText(), "\\results\\"));
        fileChooser.setTitle("Каталог для сохранения результатов");
        fileChooser.setInitialFileName("result" + FileExtensions.DOCX);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Results", ALL_PREFIX + DOCX),
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
        directoryChooser.setInitialDirectory(FileUtils.findInitialDirectory(path_to_photo.getText(), "\\photos\\"));
        directoryChooser.setTitle("Укажите каталог с фотографиями участников");

        File selectedFile = directoryChooser.showDialog(null);

        if (selectedFile != null)
            path_to_photo.setText(selectedFile.getAbsolutePath());

        checkProgress();
    }

    @FXML
    void selectBackgroundImageButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(FileUtils.findInitialDirectory(background.getText(), "\\backgrounds\\"));
        fileChooser.setTitle("Выберите фон");
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
            showErrorMessageWithDefaultDelay(messageField, "Название команды слишком длинное");
            return;
        }

        updateProps();

        InputFileReader playersFile = new InputFileReader(input_info.getText());

        try {
            playersFile.validateDocument();
        } catch (IOException e) {
            e.printStackTrace();
            messageField.setText("Ошибка чтения файла с участниками");
        } catch (IllegalArgumentException e) {
            messageField.setText("Файл с участниками составлен некорректно");
            return;
        }

        int teamSize = Integer.parseInt(countOfPlayers.getValue().substring(0, 1));

        //randomize
        RandomizeAction randomizeAction = null;
        try {
            randomizeAction = new RandomizeAction(
                    playersFile.getAllPlayers(),
                    ouput_info.getText(),
                    teamSize,
                    isBalansing.isSelected(),
                    teamTitle.getText());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            messageField.setText(e.getMessage());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            messageField.setText("Ошибка при генерации команд");
            return;
        }

        RuntimeSettings.SETTING_VIEW_LAST_WIDTH = appStage.getWidth();
        RuntimeSettings.SETTING_VIEW_LAST_HEIGHT = appStage.getHeight();

        PreviewController previewController = initNextView(event, getNextViewName());

        Image backgroundImage = new Image(Paths.FILE + background.getText());

        previewController.setScreenResolution(getScreen());
        previewController.setTeamSizePreference(teamSize);
        previewController.setPrimaryStage(appStage);
        previewController.setTextColor(textColor.getValue());
        previewController.setTeamTitle(teamTitle.getText());
        previewController.setPathToPhoto(usePrivatePhoto.isSelected(), path_to_photo.getText());
        previewController.setTeams(randomizeAction.getResult());
        previewController.configureViewVisibleElements(appStage, backgroundImage);

        previewController.setUnvisibleTableOfPlayers();
        previewController.setUnvisibleNavigateButtons();
        previewController.setFullScreenIfNeeded(fullScrene.isSelected());

        updateScene(appStage);
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
        return "views/PreviewView" + FXML;
    }
}

