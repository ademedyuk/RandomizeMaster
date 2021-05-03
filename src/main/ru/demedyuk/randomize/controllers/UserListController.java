package ru.demedyuk.randomize.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import ru.demedyuk.randomize.models.Gender;
import ru.demedyuk.randomize.models.Player;
import ru.demedyuk.randomize.models.TableBean;
import ru.demedyuk.randomize.models.files.*;
import ru.demedyuk.randomize.utils.file.FileUtils;
import ru.demedyuk.randomize.utils.file.reader.IInputFileReader;
import ru.demedyuk.randomize.utils.file.writer.InputFileWriter;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static ru.demedyuk.randomize.constants.FileExtensions.*;

public class UserListController {

    private ObservableList<TableBean> playersData = FXCollections.observableArrayList();
    private Stage appStage;
    private String inputFilePath;
    private boolean isDirty = false;
    private boolean isNew = false;

    @FXML
    private TableView<TableBean> table;

    @FXML
    private TableColumn<TableBean, String> numberColumn;

    @FXML
    private TableColumn<TableBean, String> nameColumn;

    @FXML
    private TableColumn<TableBean, ComboBox> genderColumn;

    @FXML
    private TableColumn<TableBean, String> ageColumn;

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

    public void showEmptyContent() {
        initEmptyData();

        showContent();
    }

    public void showExistingContent(IInputFileReader iInputFileReader) {
        this.inputFilePath = iInputFileReader.getInputPath();
        pathToPlayersField.setText(this.inputFilePath);
        playersData.addAll(TableBean.getListTableBean(iInputFileReader.getAllPlayers()));

        showContent();
    }

    private void showContent() {
        saveButton.setDisable(true);
        errorText.setVisible(false);

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


        numberColumn.setCellValueFactory(new PropertyValueFactory<TableBean, String>("numberString"));
        numberColumn.setCellFactory(TextFieldTableCell.forTableColumn(stringConverter));
        numberColumn.setEditable(true);
        numberColumn.setOnEditCommit(new EventHandler<CellEditEvent<TableBean, String>>() {
            @Override
            public void handle(CellEditEvent<TableBean, String> cell) {
                String newValue = cell.getNewValue();

                if (!newValue.isEmpty()) {
                    try {
                        Integer.parseInt(newValue);
                    } catch (NumberFormatException e) {
                        return;
                    }
                }

                TableBean player = cell.getTableView().getItems().get(cell.getTablePosition().getRow());
                playersData.forEach(foo -> {
                    if (foo.equals(player)) {
                        foo.setNumber(newValue);
                        setDirty(true);
                    }
                });
            }
        });
        numberColumn.setResizable(false);

        nameColumn.setCellValueFactory(new PropertyValueFactory<TableBean, String>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn(stringConverter));
        nameColumn.setEditable(true);
        nameColumn.setOnEditCommit(new EventHandler<CellEditEvent<TableBean, String>>() {
            @Override
            public void handle(CellEditEvent<TableBean, String> cell) {
                TableBean player = cell.getTableView().getItems().get(cell.getTablePosition().getRow());
                playersData.forEach(foo -> {
                    if (foo.equals(player)) {
                        foo.setName(cell.getNewValue());
                        calculate();
                        setDirty(true);
                    }
                });
            }
        });

        genderColumn.setCellValueFactory(new PropertyValueFactory<TableBean, ComboBox>("genderOption"));
        EventHandler<ActionEvent> eventHandler = (event) -> {
            updateGender();
            calculate();
            setDirty(true);
        };

        ageColumn.setCellValueFactory(new PropertyValueFactory<TableBean, String>("ageString"));
        ageColumn.setCellFactory(TextFieldTableCell.forTableColumn(stringConverter));
        ageColumn.setEditable(true);
        ageColumn.setOnEditCommit(new EventHandler<CellEditEvent<TableBean, String>>() {
            @Override
            public void handle(CellEditEvent<TableBean, String> cell) {
                String newValue = cell.getNewValue();

                if (!newValue.isEmpty()) {
                    try {
                        Integer.parseInt(newValue);
                    } catch (NumberFormatException e) {
                        return;
                    }
                }

                TableBean player = cell.getTableView().getItems().get(cell.getTablePosition().getRow());
                playersData.forEach(foo -> {
                    if (foo.equals(player)) {
                        foo.setAge(newValue);
                        setDirty(true);
                    }
                });
            }
        });
        ageColumn.setResizable(false);

        playersData.forEach(x -> x.genderOption.setOnAction(eventHandler));
        table.getVisibleLeafColumns().forEach(column -> column.setStyle("-fx-alignment: CENTER;"));

        table.getColumns().get(0).setContextMenu(createContextMenu("number"));
        table.getColumns().get(2).setContextMenu(createContextMenu("gender"));

        calculate();
        sortTable();
    }

    private ContextMenu createContextMenu(String userData) {
        MenuItem menuItem = new MenuItem();
        menuItem.setText("Очистить");
        menuItem.setUserData(userData);

        menuItem.setOnAction((event) -> {
            cleanColumn(event);
            setDirty(true);
        });

        MenuItem visibleItem = new MenuItem();
        visibleItem.setText("Скрыть");
        visibleItem.setUserData(userData);

        visibleItem.setOnAction((event) -> {
            setVisible(false, (String) visibleItem.getUserData());
            cleanColumn(event);
            setDirty(true);
        });

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(menuItem, visibleItem);

        return contextMenu;
    }

    private void setVisible(boolean value, String userData) {
        switch (userData) {
            case "number":
                numberColumn.setVisible(value);
                break;
            case "gender":
                genderColumn.setVisible(value);
                break;
        }
    }

    private void cleanColumn(ActionEvent actionEvent) {
        if (((MenuItem) actionEvent.getSource()).getUserData() == "number") {
            table.getColumns().get(0).getTableView().getItems().forEach(x -> x.setNumber(""));

            playersData.set(0, playersData.get(0));
        }

        if (((MenuItem) actionEvent.getSource()).getUserData() == "gender") {
            table.getColumns().get(0).getTableView().getItems().forEach(x -> x.genderOption.setValue(Gender.NONE.toString()));

            playersData.set(0, playersData.get(0));
        }
    }

    private void sortTable() {
        if (!numberColumn.getText().equals(""))
            table.getSortOrder().addAll(numberColumn);
        else
            table.getSortOrder().addAll(nameColumn);
    }

    public void setPrimaryStage(Stage primaryStage, String title) {
        this.appStage = primaryStage;
        this.appStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("images/edit.png")));
        this.appStage.setTitle(title);
    }

    @FXML
    void addItemButtonHandler(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        int newIndex = 0;

        if (selectedIndex == -1) {
            newIndex = table.getItems().size();

            if (newIndex == 0)
                addDefaultPlayer(newIndex);
            else
                addNewPlayer(newIndex);

            this.table.scrollTo(newIndex);
        } else {
            newIndex = selectedIndex + 1;
            addNewPlayer(newIndex);
        }
        table.setItems(playersData);
        this.table.getSelectionModel().select(newIndex);

        calculate();
        errorText.setVisible(false);
        setDirty(true);
    }

    private void addPlayer(int index, String name) {
        if (playersData.isEmpty() || !playersData.isEmpty() && playersData.get(0).number.isEmpty()) {
            playersData.add(index, new TableBean(new Player(name).addGender(Gender.NONE)));
            return;
        }

        playersData.add(index, new TableBean(new Player(name).addNumber(playersData.get(index - 1).getNumber() + 1).addGender(Gender.NONE)));

    }

    private void addNewPlayer(int index) {
        addPlayer(index, "");
    }

    private void addDefaultPlayer(int index) {
        addPlayer(index, "Имя");
    }

    @FXML
    void deleteItemButtonHandler(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1 || table.getItems().size() < 1)
            return;

        playersData.remove(selectedIndex);
        table.setItems(playersData);

        calculate();
        errorText.setVisible(false);
        setDirty(true);
    }

    @FXML
    void saveButtonActionHandler(ActionEvent event) {
        if (!isValidTable(playersData))
            return;

        Optional<ButtonType> option = showDialogWindow("Save", "Сохранить изменения в файл");

        if (option.get() == ButtonType.OK) {
            saveResult();
        }
    }

    private void updateGender() {
        calculate();

        int columnGenderIndex = 3;

        ObservableList<TableBean> items = table.getColumns().get(columnGenderIndex).getTableView().getItems();
        int i = 0;
        for (TableBean tableBean : playersData) {
            tableBean.setGenderOption(items.get(i).genderOption);
            i++;
        }

    }

    @FXML
    void cancelButtonActionHandler(ActionEvent event) {
        if (!isDirty) {
            this.appStage.close();
            return;
        }

        Optional<ButtonType> option = showDialogWindow("Save", "Выйти без сохранения изменений");

        if (option.get() == ButtonType.OK) {
            this.appStage.close();
        }
    }

    @FXML
    void okButtonActionHandler(ActionEvent event) {
        if (!isValidTable(playersData))
            return;

        if (!isDirty) {
            this.appStage.close();
            return;
        }

        Optional<ButtonType> option = showDialogWindow("Save", "Сохранить изменения в файл");

        if (option.get() == ButtonType.OK && saveResult()) {
            this.appStage.close();
        }
    }

    public void setDirty(boolean state) {
        isDirty = state;
        saveButton.setDisable(!state);

        String oldTitle = this.appStage.getTitle();

        if (state && oldTitle.indexOf("*") == -1) {
            this.appStage.setTitle("*" + oldTitle);
            return;
        }

        if (!state && oldTitle.indexOf("*") != -1)
            this.appStage.setTitle(oldTitle.substring(1));
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    private void initEmptyData() {
        pathToPlayersField.clear();
        playersData.add(new TableBean(new Player("Имя").addNumber("1")));
    }

    private Optional<ButtonType> showDialogWindow(Alert.AlertType alertType, String title, String text) {
        Alert alert = new Alert(alertType);
        alert.initOwner(this.appStage);
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
        if (pathToPlayersField.getText().isEmpty()) {
            if (!selectFileDirectory())
                return false;
        }

        setDirty(false);

        InputFileWriter inputFileWriter = new InputFileWriter(inputFilePath, playersData);

        try {
            if (inputFilePath.indexOf(".players") != -1)
                inputFileWriter.writeToTxt();

            if (inputFilePath.indexOf(".xlsx") != -1)
                inputFileWriter.writeToCsv();
        } catch (IOException e) {
            setMessage("Ошибка записи в файл");
            return false;
        }

        setMessage("Файл успешно сохранен");
        return true;
    }

    private boolean isValidTable(ObservableList<TableBean> playersData) {
        Player etalonPlayer = playersData.get(0);
        InputFileStates etalonState = etalonPlayer.executeState();

        if (etalonState == InputFileStates.NOT_VALID) {
            showDialogWindow(Alert.AlertType.ERROR, "Ошибка", "Количество параметров в каждой строке должно быть одинаковым");
            return false;
        }

        int i = 0;
        String errorMessage = "";
        for (TableBean player : playersData) {
            i++;
            if (player.executeState() != etalonState) {
                String errorPlayer = "";

                if (!player.number.isEmpty())
                    errorPlayer += " " + player.number;
                if (!player.name.isEmpty())
                    errorPlayer += " " + player.name;
                if (!errorPlayer.isEmpty()) {
                    errorMessage = "Ошибка в строке: '" + errorPlayer.trim() + "'";
                } else
                    errorMessage = "Ошибка в строке '" + i + "'";

                showDialogWindow(Alert.AlertType.ERROR, "Ошибка", "Количество параметров в каждой строке должно быть одинаковым. " + errorMessage);
                setMessage(errorMessage);
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
        fileChooser.setInitialDirectory(FileUtils.makeDirsIfNotExists("\\players\\"));
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
        for (TableBean player : playersData) {
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
                nameColumn.setPrefWidth(nameColumn.getWidth() + k);
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

