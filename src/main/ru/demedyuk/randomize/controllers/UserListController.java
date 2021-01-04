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
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import ru.demedyuk.randomize.AppLaunch;
import ru.demedyuk.randomize.models.Gender;
import ru.demedyuk.randomize.models.Player;
import ru.demedyuk.randomize.models.files.InputFileReader;
import ru.demedyuk.randomize.models.files.InputFileWriter;

import java.io.IOException;
import java.util.Optional;

public class UserListController {

    private ObservableList<Player> playersData = FXCollections.observableArrayList();
    private Stage appStage;
    private static String filePath = "C:\\Users\\demed\\Desktop\\Randomize Master\\players\\test.players";

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

    public void initialize() {
        initData();

        pathToPlayersField.setText(this.filePath);

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

    private int findPlayerIndex(Player player) {

        for (int i = 0; i < playersData.size(); i++) {
            playersData.get(i).equals(player);
            return i;
        }

        return -1;
    }


    public void setPrimaryStage(Stage primaryStage) {
        this.appStage = primaryStage;
        this.appStage.setResizable(false);

        stageSizeChageListener(this.appStage);
    }

    @FXML
    void addItemButtonHandler(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) {
            selectedIndex = table.getItems().size();
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
    }

    @FXML
    void deleteItemButtonHandler(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1)
            return;

        playersData.remove(selectedIndex);
        table.setItems(playersData);

        calculate();
    }


    @FXML
    void cancelButtonActionHandler(ActionEvent event) {
        this.appStage.close();
    }

    @FXML
    void okButtonActionHandler(ActionEvent event) {
        Optional<ButtonType> option = showDialogWindow();

        if (option.get() == ButtonType.OK) {
            this.appStage.close();
        }
    }

    private Optional<ButtonType> showDialogWindow() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Название");
        alert.setHeaderText("");
        alert.setContentText("Уверены?");

        return alert.showAndWait();
    }

    @FXML
    void saveButtonActionHandler(ActionEvent event) {
        showDialogWindow();
    }

    private void saveResult() {
        InputFileWriter inputFileWriter = new InputFileWriter(filePath, playersData);
        inputFileWriter.write();
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


    private void stageSizeChageListener(Stage stage) {
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

    private void initData() {
        InputFileReader inputFileReader = new InputFileReader(filePath);
        try {
            inputFileReader.validateDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playersData.addAll(inputFileReader.getAllPlayers());
    }

}

