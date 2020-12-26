package ru.demedyuk.randomize;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.demedyuk.randomize.settins.Screen;

import java.io.*;
import java.net.URL;
import java.util.*;

public class SettingsController {

    private Stage primaryStage;
    private Properties props;
    private String pathToConfig;

    //меню
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu file;
    @FXML
    private MenuItem fileNew;
    @FXML
    private MenuItem fileOpen;
    @FXML
    private MenuItem fileSave;
    @FXML
    private MenuItem fileSaveAs;
    @FXML
    private MenuItem quitMenuItem;
    @FXML
    private MenuItem aboutItem;

    @FXML
    void fileActionHandler(ActionEvent event) {
    }

    @FXML
    void fileNewActionHandler(ActionEvent event) {
        props = null;

        input_info.setText("");
        ouput_info.setText("");
        countOfPlayers.setValue("2 участника");
        background.setText("");
        isBalansing.setSelected(false);
        usePrivatePhoto.setSelected(false);
        path_to_photo.setText("");
        checkProgress();
        usePrivatePhotoAction(new ActionEvent());

        primaryStage.setTitle("Randomize Master");
    }


    @FXML
    void fileOpenActionHandler(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\demed\\Desktop\\RandomizeMaster\\"));
        fileChooser.setTitle("Открыть конфигурацию");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файлы конфигураций",
                "*.config"));
        File selectedFile = fileChooser.showOpenDialog(null);
        pathToConfig = selectedFile.getPath();

        initConfig(event, pathToConfig);
    }

    @FXML
    void fileSaveActionHandler(ActionEvent event) {
        updateProps();
        if (pathToConfig != null) {
            try (OutputStream output = new FileOutputStream(pathToConfig)) {
                Properties prop = this.props;

                // save properties to project root folder
                prop.store(output, null);

            } catch (IOException io) {
                io.printStackTrace();
            }
        }

    }



    @FXML
    void fileSaveAsActionHandler(ActionEvent event) {
        updateProps();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить конфигурацию");
        fileChooser.setInitialFileName("new.config");
        File selectedFile = fileChooser.showSaveDialog(null);
        pathToConfig = selectedFile.getPath();

        if (selectedFile != null) {
            try (OutputStream output = new FileOutputStream(selectedFile)) {
                Properties prop = this.props;

                // save properties to project root folder
                prop.store(output, null);

            } catch (IOException io) {
                io.printStackTrace();
            }
        }

    }

    @FXML
    void quitMenuItemActionHandler(ActionEvent event) {
        Main.stopApplication();
    }

    private void updateProps() {
        props.setProperty("input.file.path", input_info.getText());
        props.setProperty("output.file.path", ouput_info.getText());
        props.setProperty("background.file.path", background.getText());
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

    public void initScene(){
        countOfPlayers.getItems().addAll( "2 участника",
                "3 участника",
                "4 участника",
                "5 участников",
                "6 участников",
                "7 участников",
                "8 участников");
        countOfPlayers.setValue("2 участника");
        isBalansing.setSelected(true);
        usePrivatePhoto.setSelected(false);
        label_photo.setFill(Paint.valueOf("DBDBDB"));
        messageField.setText("Выберите файл с участниками...");
        initConfig(null, getClass().getClassLoader().getResource("default.properties").getFile());
    }

    public void initConfig(ActionEvent event, String path) {
        //загрузка конгфигурации по умолчанию
        try (InputStream input = new FileInputStream(path)) {

            this.props = new Properties();
            props.load(input);

            input_info.setText(props.getProperty("input.file.path"));
            ouput_info.setText(props.getProperty("output.file.path"));
            background.setText(props.getProperty("background.file.path"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //установка заголовка окна
        if (event != null) {
            String oldTitle = this.primaryStage.getTitle();
            this.primaryStage.setTitle(oldTitle + " (" + path +  ")");
        }

        checkProgress();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
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
    void selectBackgroundImageButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\demed\\Desktop\\RandomizeMaster\\backgrounds\\"));
        fileChooser.setTitle("Выберите фон");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Изображения",
                "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            background.setText(selectedFile.getAbsolutePath());
        }
        checkProgress();
    }

    @FXML
    void usePrivatePhotoAction(ActionEvent event) {
        boolean value = usePrivatePhoto.isSelected();
        label_photo.setFill(value ? Paint.valueOf("000000") : Paint.valueOf("DBDBDB"));
        path_to_photo.setDisable(!value);
        selectPhotoButton.setDisable(!value);
        checkProgress();
    }

    private void checkProgress() {
        progress.setProgress(0);
        if (background != null && !background.getText().isEmpty())
            addProgressValue();
        else
            messageField.setText("Выберите фон...");

        if (usePrivatePhoto.isSelected() && path_to_photo != null && !path_to_photo.getText().isEmpty())
            addProgressValue();
        else if (usePrivatePhoto.isSelected())
            messageField.setText("Укажите каталог с личными фото");

        if (ouput_info != null && !ouput_info.getText().isEmpty())
            addProgressValue();
        else
            messageField.setText("Выберите каталог для сохранения результата...");

        if (input_info != null && !input_info.getText().isEmpty())
            addProgressValue();
        else
            messageField.setText("Выберите файл с участниками...");
    }

    private void addProgressValue() {
        double fix_value = usePrivatePhoto.isSelected() ? 0.25 : 0.34;
        progress.setProgress(progress.getProgress() + fix_value);
    }

    @FXML
    void selectListOfPlayersButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\Users\\demed\\Desktop\\RandomizeMaster\\"));
        fileChooser.setTitle("Выберите список участников");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Участники", "*.players"),
                new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"),
                new FileChooser.ExtensionFilter("Все", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null)
            input_info.setText(selectedFile.getAbsolutePath());
        checkProgress();
    }

    @FXML
    void selectResultDirectoryButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        File file = new File("C:\\Users\\demed\\Desktop\\RandomizeMaster\\results\\");
        if (!file.exists()) {
           file.mkdirs();
        }

        fileChooser.setInitialDirectory(file);
        fileChooser.setTitle("Каталог для сохранения результатов");
        fileChooser.setInitialFileName("result.txt");
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null)
            ouput_info.setText(selectedFile.getAbsolutePath());
        checkProgress();
    }

    @FXML
    void screenResolutionAction(ActionEvent event) {
        //TODO: не используется value
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
        directoryChooser.setInitialDirectory(new File("C:\\Users\\demed\\Desktop\\RandomizeMaster\\"));
        directoryChooser.setTitle("Укажите каталог с фотографиями участников");
        File selectedFile = directoryChooser.showDialog(null);

        if (selectedFile != null)
            path_to_photo.setText(selectedFile.getAbsolutePath());
        checkProgress();
    }

    @FXML
    private void handleLaunchButtonAction(ActionEvent event) throws IOException {
        if (progress.getProgress() < 0.99) {
            //TODO: добавить вывод сообщения по таймеру
            return;
        }

        Image backgroundImage = new Image("file:///" + background.getText());

        URL locationUrl = getClass().getClassLoader().getResource("ui/preview_2.fxml");
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(locationUrl);
        appStage.setScene(new Scene((Pane) loader.load()));

        PreviewController previewController = loader.<PreviewController>getController();

        previewController.setScreenResolution(getScreen());
        previewController.setCountOfPlayers(Integer.parseInt(countOfPlayers.getValue().substring(0,1)));
        previewController.init(appStage, backgroundImage, input_info.getText(), ouput_info.getText());
        previewController.setVisibleTablaOfPlayers(false);
        previewController.setVisibleNavigateButtons(false);
        previewController.setPathToPhoto(path_to_photo.getText());

        if (fullScrene.isSelected())
            previewController.setFullScreen(appStage);

        appStage.show();
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
}

