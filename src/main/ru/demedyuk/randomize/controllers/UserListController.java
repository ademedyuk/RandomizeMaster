package ru.demedyuk.randomize.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import ru.demedyuk.randomize.constants.FileExtensions;
import ru.demedyuk.randomize.models.Gender;
import ru.demedyuk.randomize.models.Player;
import ru.demedyuk.randomize.models.files.InputFileReader;
import ru.demedyuk.randomize.models.files.InputFileStates;
import ru.demedyuk.randomize.models.files.InputFileWriter;
import ru.demedyuk.randomize.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static ru.demedyuk.randomize.constants.FileExtensions.*;

public class UserListController {

    private ObservableList<Player> playersData = FXCollections.observableArrayList();
    private Stage appStage;
    private String inputFilePath;

    @FXML
    private TableView<Player> table;

    @FXML
    private TableColumn<Player, Integer> numberColumn;

    @FXML
    private TableColumn<Player, String> firstNameColumn;

    @FXML
    private TableColumn<Player, String> lastNameColumn;

    @FXML
    private TableColumn<Player, StringProperty> genderColumn;

    @FXML
    private Button addItemButton;

    @FXML
    private Button deleteItemButton;

    @FXML
    private TextField pathToPlayersField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button okButton;

    @FXML
    private Text countLabel;

    @FXML
    private Text countField;

    @FXML
    private Text boysLabel;

    @FXML
    private Text girlsLabel;

    @FXML
    private Text boysField;

    @FXML
    private Text girlsField;

    @FXML
    private Text errorText;

    public void showContent(String path, boolean isNew) {
        this.inputFilePath = path;

        if (isNew)
            initEmptyData();
        else
            initData();

        errorText.setVisible(false);
        pathToPlayersField.setText(this.inputFilePath);

        table.setEditable(true);
        table.setItems(playersData);

        StringConverter stringConverter = new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        };

        numberColumn.setCellValueFactory(data -> new SimpleIntegerProperty(Integer.parseInt(data.getValue().number)).asObject());
        numberColumn.setEditable(true);
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        numberColumn.setOnEditCommit(new EventHandler<CellEditEvent<Player, Integer>>() {
            @Override
            public void handle(CellEditEvent<Player, Integer> cell) {
                Player player = cell.getTableView().getItems().get(cell.getTablePosition().getRow());
                playersData.forEach(foo -> {
                    if (foo.equals(player)) {
                        foo.setNumber(cell.getNewValue());
                    }
                });
            }
        });

        numberColumn.setResizable(false);

        firstNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().firstName));
        firstNameColumn.setEditable(true);
        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(stringConverter));
        firstNameColumn.setOnEditCommit(new EventHandler<CellEditEvent<Player, String>>() {
            @Override
            public void handle(CellEditEvent<Player, String> cell) {
                Player player = cell.getTableView().getItems().get(cell.getTablePosition().getRow());
                playersData.forEach(foo -> {
                    if (foo.equals(player)) {
                        foo.setFirstName(cell.getNewValue());
                        calculate();
                    }
                });
            }
        });

        lastNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().lastName));
        lastNameColumn.setEditable(true);
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn(stringConverter));
        lastNameColumn.setOnEditCommit(new EventHandler<CellEditEvent<Player, String>>() {
            @Override
            public void handle(CellEditEvent<Player, String> cell) {
                Player player = cell.getTableView().getItems().get(cell.getTablePosition().getRow());
                playersData.forEach(foo -> {
                    if (foo.equals(player)) {
                        foo.setLastName(cell.getNewValue());
                    }
                    calculate();
                });
            }
        });

        ObservableList<String> options = FXCollections.observableArrayList(
                Gender.BOY.toString(),
                Gender.GIRL.toString(),
                Gender.NONE.toString()
        );

        genderColumn.setCellFactory(col -> {
            TableCell<Player, StringProperty> c = new TableCell<>();
            ComboBox<String> comboBox = new ComboBox<>(options);
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue observable, String oldValue, String newValue) {
                    Player player = c.getTableView().getItems().get(c.getTableRow().getIndex());
                    playersData.forEach(foo -> {
                        if (foo.equals(player)) {
                            foo.setGender(Gender.getGenderByID(newValue));
                            calculate();
                        }
                    });
                }
            });

            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node) null).otherwise(comboBox));
            return c;
        });

        table.getVisibleLeafColumns().forEach(column -> column.setStyle("-fx-alignment: CENTER;"));

        calculate();
        sortTable();
    }

    private void sortTable() {
        if (!numberColumn.getText().equals(""))
            table.getSortOrder().addAll(numberColumn);
        else
            table.getSortOrder().addAll(firstNameColumn);
    }

    public void setPrimaryStage(Stage primaryStage, String title) {
        this.appStage = primaryStage;
        this.appStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("images/edit.png")));
        this.appStage.setTitle(title);
    }

    @FXML
    void addItemButtonHandler(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) {
            selectedIndex = table.getItems().size();

            if (selectedIndex == 0)
                playersData.add(new Player(0, "Имя", "Фамилия"));
            else
                playersData.add(table.getItems().size(),
                        new Player(Integer.parseInt(playersData.get(selectedIndex - 1).number) + 1,
                                "", "", Gender.NONE));

            table.setItems(playersData);
            this.table.scrollTo(selectedIndex);
        } else {
            selectedIndex++;
            playersData.add(selectedIndex,
                    new Player(Integer.parseInt(playersData.get(selectedIndex - 1).number) + 1,
                            "", "", Gender.NONE));
            table.setItems(playersData);
        }
        this.table.getSelectionModel().select(selectedIndex);

        calculate();
        errorText.setVisible(false);
    }

    @FXML
    void deleteItemButtonHandler(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1 || table.getItems().size() < 2)
            return;

        playersData.remove(selectedIndex);
        table.setItems(playersData);

        calculate();
        errorText.setVisible(false);
    }

    @FXML
    void saveButtonActionHandler(ActionEvent event) {
        if (!isValidTable(playersData))
            return;

        Optional<ButtonType> option = showDialogWindow("Save", "Сохранить изменения в файл");

        if (option.get() == ButtonType.OK)
            saveResult();
    }

    @FXML
    void cancelButtonActionHandler(ActionEvent event) {
        Optional<ButtonType> option = showDialogWindow("Save", "Выйти без сохранения изменений");

        if (option.get() == ButtonType.OK) {
            this.appStage.close();
        }
    }

    @FXML
    void okButtonActionHandler(ActionEvent event) {
        if (!isValidTable(playersData))
            return;

        Optional<ButtonType> option = showDialogWindow("Save", "Сохранить изменения в файл");

        if (option.get() == ButtonType.OK && saveResult()) {
            this.appStage.close();
        }
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    private void initData() {
        InputFileReader inputFileReader = new InputFileReader(inputFilePath);
        try {
            inputFileReader.validateDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playersData.addAll(inputFileReader.getAllPlayers());
    }

    private void initEmptyData() {
        pathToPlayersField.clear();
        playersData.add(new Player(1, "Имя", "Фамилия"));
    }

    private Optional<ButtonType> showDialogWindow(Alert.AlertType alertType, String title, String text) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.setContentText(text);

        if (alertType == Alert.AlertType.ERROR) {
            alert.getButtonTypes().clear();
            alert.getButtonTypes().add(ButtonType.OK);
        }

        return alert.showAndWait();
    }

    private Optional<ButtonType> showDialogWindow(String title, String text) {
        return showDialogWindow(Alert.AlertType.CONFIRMATION, title, text);
    }

    private boolean saveResult() {
        if (pathToPlayersField.getText().isEmpty() && selectFileDirectory()) {
            InputFileWriter inputFileWriter = new InputFileWriter(inputFilePath, playersData);
            inputFileWriter.write();

            return true;
        }

        return false;
    }

    private boolean isValidTable(ObservableList<Player> playersData) {
        Player etalonPlayer = playersData.get(0);

        InputFileStates etalonState = etalonPlayer.executeState();

        int i = 0;
        for (Player player : playersData) {
            i++;
            if (player.executeState() != etalonState) {
                showDialogWindow(Alert.AlertType.ERROR, "Ошибка", "Проверьте корректность полей" + i);
                setMessage("Ошибка в строке номер " + i + "...");
                return false;
            }
        }

        return true;
    }

    private void setMessage(String text) {
        new Thread(() -> {
            errorText.setText(text);
            errorText.setVisible(true);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            errorText.setVisible(false);
        }).start();
    }

    private boolean selectFileDirectory() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(FileUtils.makeDirsIfNotExists("\\results\\"));
        fileChooser.setTitle("Каталог для сохранения cписка участников");
        fileChooser.setInitialFileName("players" + PLAYERS);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Results", ALL_PREFIX + PLAYERS),
                new FileChooser.ExtensionFilter("Все", ALL_FILES));

        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            inputFilePath = selectedFile.getAbsolutePath();
            return true;
        }

        return false;
    }

    private void calculate() {
        countField.setText(String.valueOf(playersData.size()));

        int boys = 0;
        int girls = 0;
        for (Player player : playersData) {
            if (player.gender.equals(Gender.BOY))
                boys++;
            else if (player.gender.equals(Gender.GIRL))
                girls++;
        }

        boysField.setText(String.valueOf(boys));
        girlsField.setText(String.valueOf(girls));

        boysGirlsFieldsSetVisible(boys + girls == 0 ? false : true);
    }

    private void boysGirlsFieldsSetVisible(boolean value) {
        boysLabel.setVisible(value);
        boysField.setVisible(value);
        girlsLabel.setVisible(value);
        girlsField.setVisible(value);
    }


    private void stageSizeChangeListener(Stage stage) {
        stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                table.setPrefWidth(newValue.doubleValue());

                double k = (newValue.doubleValue() - oldValue.doubleValue()) / 4;

                numberColumn.setPrefWidth(numberColumn.getWidth() + k);
                firstNameColumn.setPrefWidth(firstNameColumn.getWidth() + k);
                lastNameColumn.setPrefWidth(lastNameColumn.getWidth() + k);
                genderColumn.setPrefWidth(genderColumn.getWidth() + k);
            }
        });

        stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                table.setPrefHeight(Math.abs(table.getHeight() + (newValue.doubleValue()) - oldValue.doubleValue()));
            }
        });
    }
}

