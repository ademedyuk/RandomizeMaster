package ru.demedyuk.randomize.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.demedyuk.randomize.models.GenderGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class AgeSettingsController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button okButton;

    @FXML
    private Button cleatButton;

    @FXML
    private ComboBox<String> age1LeftComboBox;

    @FXML
    private ComboBox<String> age1RightComboBox;

    @FXML
    private Group group2;

    @FXML
    private ComboBox<String> age2LeftComboBox;

    @FXML
    private Text quantity2Text;

    @FXML
    private ComboBox<String> age2RightComboBox;

    @FXML
    private Group group3;

    @FXML
    private Text quantity3Text;

    @FXML
    private ComboBox<String> age3LeftComboBox;

    @FXML
    private ComboBox<String> age3RightComboBox;

    @FXML
    private Group group4;

    @FXML
    private Text quantity4Text;

    @FXML
    private ComboBox<String> age4RightComboBox;

    @FXML
    private ComboBox<String> age4LeftComboBox;

    @FXML
    private Group group5;

    @FXML
    private Text quantity5Text;

    @FXML
    private ComboBox<String> age5RightComboBox;

    @FXML
    private ComboBox<String> age5LeftComboBox;

    @FXML
    private Text quantity1Text;

    @FXML
    private Text num1Text;

    @FXML
    private Text num2Text;

    @FXML
    private Text num3Text;

    @FXML
    private Text num4Text;

    @FXML
    private Text num5Text;

    @FXML
    private CheckBox num3CheckBox;

    @FXML
    private CheckBox num4CheckBox;

    @FXML
    private CheckBox num5CheckBox;

    @FXML
    private Text errorText;

    private static ArrayList ageValues = new ArrayList<>();
    private Stage appStage;
    private SettingsController settingsController;
    private HashMap<Integer, GenderGroup> genderGroups = new HashMap<>();

    static {
        ageValues.add("6 лет");
        ageValues.add("7 лет");
        ageValues.add("8 лет");
        ageValues.add("9 лет");
        ageValues.add("10 лет");
        ageValues.add("11 лет");
        ageValues.add("12 лет");
        ageValues.add("13 лет");
        ageValues.add("14 лет");
        ageValues.add("15 лет");
        ageValues.add("16 лет");
        ageValues.add("17 лет");
    }

    public void init(SettingsController settingsController, HashMap<Integer, GenderGroup> genderGroups) {
        this.settingsController = settingsController;
        this.errorText.setVisible(false);
        this.genderGroups = genderGroups;

        initComboBoxes();

        if (genderGroups.isEmpty()) {
            age1LeftComboBox.setValue("6 лет");
            age1RightComboBox.setValue("10 лет");

            age2LeftComboBox.setValue("11 лет");
            age2RightComboBox.setValue("13 лет");

            age3LeftComboBox.setValue("14 лет");
            age3RightComboBox.setValue("17 лет");

            group3.setVisible(true);
            num3CheckBox.setSelected(true);
            group4.setVisible(false);
            group5.setVisible(false);
        } else {
            updateView();
        }
    }

    private void updateView() {
        switch (this.genderGroups.size()) {
            case 2:
                age1LeftComboBox.setValue(String.valueOf(this.genderGroups.get(0).startAge) + " лет");
                age1RightComboBox.setValue(String.valueOf(this.genderGroups.get(0).finalAge) + " лет");

                age2LeftComboBox.setValue(String.valueOf(this.genderGroups.get(1).startAge) + " лет");
                age2RightComboBox.setValue(String.valueOf(this.genderGroups.get(1).finalAge) + " лет");
                break;
            case 3:
                age1LeftComboBox.setValue(String.valueOf(this.genderGroups.get(0).startAge) + " лет");
                age1RightComboBox.setValue(String.valueOf(this.genderGroups.get(0).finalAge) + " лет");

                age2LeftComboBox.setValue(String.valueOf(this.genderGroups.get(1).startAge) + " лет");
                age2RightComboBox.setValue(String.valueOf(this.genderGroups.get(1).finalAge) + " лет");

                group3.setVisible(true);
                num3CheckBox.setSelected(true);
                age3LeftComboBox.setValue(String.valueOf(this.genderGroups.get(2).startAge) + " лет");
                age3RightComboBox.setValue(String.valueOf(this.genderGroups.get(2).finalAge) + " лет");
                break;
            case 4:
                age1LeftComboBox.setValue(String.valueOf(this.genderGroups.get(0).startAge));
                age1RightComboBox.setValue(String.valueOf(this.genderGroups.get(0).finalAge));

                age2LeftComboBox.setValue(String.valueOf(this.genderGroups.get(1).startAge) + " лет");
                age2RightComboBox.setValue(String.valueOf(this.genderGroups.get(1).finalAge) + " лет");

                group3.setVisible(true);
                num3CheckBox.setSelected(true);
                age3LeftComboBox.setValue(String.valueOf(this.genderGroups.get(2).startAge) + " лет");
                age3RightComboBox.setValue(String.valueOf(this.genderGroups.get(2).finalAge) + " лет");

                group4.setVisible(true);
                num4CheckBox.setSelected(true);
                age4LeftComboBox.setValue(String.valueOf(this.genderGroups.get(3).startAge) + " лет");
                age4RightComboBox.setValue(String.valueOf(this.genderGroups.get(4).finalAge) + " лет");
                break;
            case 5:
                age1LeftComboBox.setValue(String.valueOf(this.genderGroups.get(0).startAge) + " лет");
                age1RightComboBox.setValue(String.valueOf(this.genderGroups.get(0).finalAge) + " лет");

                age2LeftComboBox.setValue(String.valueOf(this.genderGroups.get(1).startAge) + " лет");
                age2RightComboBox.setValue(String.valueOf(this.genderGroups.get(1).finalAge) + " лет");

                group3.setVisible(true);
                num3CheckBox.setSelected(true);
                age3LeftComboBox.setValue(String.valueOf(this.genderGroups.get(2).startAge) + " лет");
                age3RightComboBox.setValue(String.valueOf(this.genderGroups.get(2).finalAge) + " лет");

                group4.setVisible(true);
                num4CheckBox.setSelected(true);
                age4LeftComboBox.setValue(String.valueOf(this.genderGroups.get(3).startAge) + " лет");
                age4RightComboBox.setValue(String.valueOf(this.genderGroups.get(3).finalAge) + " лет");

                group5.setVisible(true);
                num5CheckBox.setSelected(true);
                age5LeftComboBox.setValue(String.valueOf(this.genderGroups.get(4).startAge) + " лет");
                age5RightComboBox.setValue(String.valueOf(this.genderGroups.get(4).finalAge) + " лет");
                break;
        }
    }

    public void initComboBoxes() {
        age1LeftComboBox.getItems().addAll(ageValues);
        age1RightComboBox.getItems().addAll(ageValues);

        age2LeftComboBox.getItems().addAll(ageValues);
        age2RightComboBox.getItems().addAll(ageValues);

        age3LeftComboBox.getItems().addAll(ageValues);
        age3RightComboBox.getItems().addAll(ageValues);

        age3LeftComboBox.getItems().addAll(ageValues);
        age3RightComboBox.getItems().addAll(ageValues);

        age4LeftComboBox.getItems().addAll(ageValues);
        age4RightComboBox.getItems().addAll(ageValues);
    }

    public void setPrimaryStage(Stage primaryStage, String title) {
        this.appStage = primaryStage;
        //TODO: add a new icon
        this.appStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("images/edit.png")));
        this.appStage.setTitle(title);
    }

    @FXML
    void handleAddNewGroup(ActionEvent event) {
        String id = ((CheckBox) event.getSource()).getId();

        switch (id) {
            case "num3CheckBox":
                if (num3CheckBox.isSelected()) {
                    group3.setVisible(true);
                    num4CheckBox.setDisable(false);
                } else {
                    setVisibleGroup(false, group3, group4, group5);
                    setDisableCheckbox(true, num4CheckBox, num5CheckBox);
                }
                break;

            case "num4CheckBox":
                if (num4CheckBox.isSelected()) {
                    group4.setVisible(true);
                    num5CheckBox.setDisable(false);
                } else {
                    setVisibleGroup(false, group4, group5);
                    setDisableCheckbox(true, num5CheckBox);
                }
                break;

            case "num5CheckBox":
                if (num5CheckBox.isSelected()) {
                    group5.setVisible(true);
                } else {
                    setVisibleGroup(false, group5);
                    setDisableCheckbox(true, num5CheckBox);
                }
                break;
        }
    }

    private void setVisibleGroup(boolean value, Group... groups) {
        for (Group group : groups) {
            group.setVisible(value);
        }
    }

    private void setDisableCheckbox(boolean value, CheckBox... checkBoxes) {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setDisable(value);
            checkBox.setSelected(false);
        }
    }

    private void updateComboBoxValues(int value, ComboBox... comboBoxes) {
        for (ComboBox comboBox : comboBoxes) {
            comboBox.getItems().clear();

            ArrayList newValues = new ArrayList(ageValues);
            newValues.removeIf(v -> Integer.parseInt(((String) v).replace(" лет", "")) <= value);
            comboBox.getItems().addAll(newValues);

            if (comboBox.getItems().isEmpty())
                comboBox.getItems().add((value + 1) + " лет");
        }
    }

    @FXML
    void handleSelectComboBox(ActionEvent event) {
        String id = ((ComboBox) event.getSource()).getId();

        switch (id) {
            case "age1LeftComboBox":
                updateComboBoxValues(getIntegerValueFromCombo(age1LeftComboBox), age1RightComboBox);
                break;

            case "age1RightComboBox":
                updateComboBoxValues(getIntegerValueFromCombo(age1RightComboBox), age2LeftComboBox);
                break;

            case "age2LeftComboBox":
                updateComboBoxValues(getIntegerValueFromCombo(age2LeftComboBox), age2RightComboBox);
                break;

            case "age2RightComboBox":
                updateComboBoxValues(getIntegerValueFromCombo(age2RightComboBox), age3LeftComboBox);
                break;

            case "age3LeftComboBox":
                updateComboBoxValues(getIntegerValueFromCombo(age3LeftComboBox), age3RightComboBox);
                break;

            case "age3RightComboBox":
                updateComboBoxValues(getIntegerValueFromCombo(age3RightComboBox), age4LeftComboBox);
                break;

            case "age4LeftComboBox":
                updateComboBoxValues(getIntegerValueFromCombo(age4LeftComboBox), age4RightComboBox);
                break;

            case "age4RightComboBox":
                updateComboBoxValues(getIntegerValueFromCombo(age4RightComboBox), age5LeftComboBox);
                break;

            case "age5LeftComboBox":
                updateComboBoxValues(getIntegerValueFromCombo(age5LeftComboBox), age5RightComboBox);
                break;
        }

    }

    private int getIntegerValueFromCombo(ComboBox<String> comboBox) {
        return Integer.parseInt(comboBox.getValue().replace(" лет", ""));
    }

    @FXML
    void cancelButtonActionHandler(ActionEvent event) {
        this.appStage.close();
    }

    @FXML
    void okButtonActionHandler(ActionEvent event) {
        if (!isValid()) {
            showMessage("Проверьте корректность данных");
            return;
        }

        this.settingsController.setGenderGroups(this.genderGroups);
        this.appStage.close();
    }

    private void showMessage(String text) {
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

    @FXML
    void clearButtonActionHandler(ActionEvent event) {
        initComboBoxes();

        age1LeftComboBox.setValue(null);
        age1RightComboBox.setValue(null);

        age2LeftComboBox.setValue(null);
        age2RightComboBox.setValue(null);

        age3LeftComboBox.setValue(null);
        age3RightComboBox.setValue(null);

        age4LeftComboBox.setValue(null);
        age4RightComboBox.setValue(null);

        age5LeftComboBox.setValue(null);
        age5RightComboBox.setValue(null);

        group3.setVisible(false);
        num3CheckBox.setSelected(false);
        group4.setVisible(false);
        num4CheckBox.setSelected(false);
        group5.setVisible(false);
        num5CheckBox.setSelected(false);
    }

    private boolean isValid() {
        try {

            if (!num3CheckBox.isSelected() && !num4CheckBox.isSelected() && !num5CheckBox.isSelected()) {

                genderGroups.clear();
                genderGroups.put(0, new GenderGroup(getIntegerValueFromCombo(age1LeftComboBox), getIntegerValueFromCombo(age1RightComboBox)));
                genderGroups.put(1, new GenderGroup(getIntegerValueFromCombo(age2LeftComboBox), getIntegerValueFromCombo(age2RightComboBox)));


                return getIntegerValueFromCombo(age1LeftComboBox) < getIntegerValueFromCombo(age1RightComboBox) &&
                        getIntegerValueFromCombo(age2LeftComboBox) < getIntegerValueFromCombo(age2RightComboBox)

                        && getIntegerValueFromCombo(age1LeftComboBox) < getIntegerValueFromCombo(age2LeftComboBox)

                        && getIntegerValueFromCombo(age1RightComboBox) < getIntegerValueFromCombo(age2RightComboBox);
            }

            if (num3CheckBox.isSelected() && !num4CheckBox.isSelected() && !num5CheckBox.isSelected()) {

                genderGroups.clear();
                genderGroups.put(0, new GenderGroup(getIntegerValueFromCombo(age1LeftComboBox), getIntegerValueFromCombo(age1RightComboBox)));
                genderGroups.put(1, new GenderGroup(getIntegerValueFromCombo(age2LeftComboBox), getIntegerValueFromCombo(age2RightComboBox)));
                genderGroups.put(2, new GenderGroup(getIntegerValueFromCombo(age3LeftComboBox), getIntegerValueFromCombo(age3RightComboBox)));

                return getIntegerValueFromCombo(age1LeftComboBox) < getIntegerValueFromCombo(age1RightComboBox) &&
                        getIntegerValueFromCombo(age2LeftComboBox) < getIntegerValueFromCombo(age2RightComboBox) &&
                        getIntegerValueFromCombo(age3LeftComboBox) < getIntegerValueFromCombo(age3RightComboBox)

                        && getIntegerValueFromCombo(age1LeftComboBox) < getIntegerValueFromCombo(age2LeftComboBox)
                        && getIntegerValueFromCombo(age2LeftComboBox) < getIntegerValueFromCombo(age3LeftComboBox)

                        && getIntegerValueFromCombo(age1RightComboBox) < getIntegerValueFromCombo(age2RightComboBox)
                        && getIntegerValueFromCombo(age2RightComboBox) < getIntegerValueFromCombo(age3RightComboBox);
            }

            if (num3CheckBox.isSelected() && num4CheckBox.isSelected() && !num5CheckBox.isSelected()) {

                genderGroups.clear();
                genderGroups.put(0, new GenderGroup(getIntegerValueFromCombo(age1LeftComboBox), getIntegerValueFromCombo(age1RightComboBox)));
                genderGroups.put(1, new GenderGroup(getIntegerValueFromCombo(age2LeftComboBox), getIntegerValueFromCombo(age2RightComboBox)));
                genderGroups.put(2, new GenderGroup(getIntegerValueFromCombo(age3LeftComboBox), getIntegerValueFromCombo(age3RightComboBox)));
                genderGroups.put(3, new GenderGroup(getIntegerValueFromCombo(age4LeftComboBox), getIntegerValueFromCombo(age4RightComboBox)));

                return getIntegerValueFromCombo(age1LeftComboBox) < getIntegerValueFromCombo(age1RightComboBox) &&
                        getIntegerValueFromCombo(age2LeftComboBox) < getIntegerValueFromCombo(age2RightComboBox) &&
                        getIntegerValueFromCombo(age3LeftComboBox) < getIntegerValueFromCombo(age3RightComboBox) &&
                        getIntegerValueFromCombo(age4LeftComboBox) < getIntegerValueFromCombo(age4RightComboBox)

                        && getIntegerValueFromCombo(age1LeftComboBox) < getIntegerValueFromCombo(age2LeftComboBox)
                        && getIntegerValueFromCombo(age2LeftComboBox) < getIntegerValueFromCombo(age3LeftComboBox)
                        && getIntegerValueFromCombo(age3LeftComboBox) < getIntegerValueFromCombo(age4LeftComboBox)

                        && getIntegerValueFromCombo(age1RightComboBox) < getIntegerValueFromCombo(age2RightComboBox)
                        && getIntegerValueFromCombo(age2RightComboBox) < getIntegerValueFromCombo(age3RightComboBox)
                        && getIntegerValueFromCombo(age3RightComboBox) < getIntegerValueFromCombo(age4RightComboBox);
            }

            if (num3CheckBox.isSelected() && num4CheckBox.isSelected() && num5CheckBox.isSelected()) {

                genderGroups.clear();
                genderGroups.put(0, new GenderGroup(getIntegerValueFromCombo(age1LeftComboBox), getIntegerValueFromCombo(age1RightComboBox)));
                genderGroups.put(1, new GenderGroup(getIntegerValueFromCombo(age2LeftComboBox), getIntegerValueFromCombo(age2RightComboBox)));
                genderGroups.put(2, new GenderGroup(getIntegerValueFromCombo(age3LeftComboBox), getIntegerValueFromCombo(age3RightComboBox)));
                genderGroups.put(3, new GenderGroup(getIntegerValueFromCombo(age4LeftComboBox), getIntegerValueFromCombo(age4RightComboBox)));
                genderGroups.put(4, new GenderGroup(getIntegerValueFromCombo(age5LeftComboBox), getIntegerValueFromCombo(age5RightComboBox)));

                return getIntegerValueFromCombo(age1LeftComboBox) < getIntegerValueFromCombo(age1RightComboBox) &&
                        getIntegerValueFromCombo(age2LeftComboBox) < getIntegerValueFromCombo(age2RightComboBox) &&
                        getIntegerValueFromCombo(age3LeftComboBox) < getIntegerValueFromCombo(age3RightComboBox) &&
                        getIntegerValueFromCombo(age4LeftComboBox) < getIntegerValueFromCombo(age4RightComboBox) &&
                        getIntegerValueFromCombo(age5LeftComboBox) < getIntegerValueFromCombo(age5RightComboBox)

                        && getIntegerValueFromCombo(age1LeftComboBox) < getIntegerValueFromCombo(age2LeftComboBox)
                        && getIntegerValueFromCombo(age2LeftComboBox) < getIntegerValueFromCombo(age3LeftComboBox)
                        && getIntegerValueFromCombo(age3LeftComboBox) < getIntegerValueFromCombo(age4LeftComboBox)
                        && getIntegerValueFromCombo(age4LeftComboBox) < getIntegerValueFromCombo(age5LeftComboBox)

                        && getIntegerValueFromCombo(age1RightComboBox) < getIntegerValueFromCombo(age2RightComboBox)
                        && getIntegerValueFromCombo(age2RightComboBox) < getIntegerValueFromCombo(age3RightComboBox)
                        && getIntegerValueFromCombo(age3RightComboBox) < getIntegerValueFromCombo(age4RightComboBox)
                        && getIntegerValueFromCombo(age4RightComboBox) < getIntegerValueFromCombo(age5RightComboBox);
            }

        } catch (Exception e) {
            return false;
        }

        return false;
    }

}

