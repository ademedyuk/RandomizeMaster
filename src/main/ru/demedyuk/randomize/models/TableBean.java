package ru.demedyuk.randomize.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.List;

public class TableBean extends Player {

    ObservableList<String> options = FXCollections.observableArrayList(
            Gender.BOY.toString(),
            Gender.GIRL.toString(),
            Gender.NONE.toString()
    );

    public ComboBox<String> genderOption = new ComboBox<>(options);

    public TableBean(Player player) {
        super(player.number, player.firstName, player.lastName, player.gender);

        genderOption.setValue(player.gender.toString());
    }

    public static List<TableBean> getListTableBean(List<Player> playersList) {
        ArrayList<TableBean> tableBeans = new ArrayList<>();

        for (Player player : playersList) {
            tableBeans.add(new TableBean(player));
        }

        return tableBeans;
    }

    public String getNumberString() {
        return String.valueOf(this.number);
    }

    public void setNumberString(String value) {
        this.setNumber(value);
    }

    public ComboBox<String> getGenderOption() {
        return genderOption;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

//    public void setNumber(int number) {
//        this.player.setNumber(number);
//    }
//
//    public void setFirstName(String firstName) {
//        this.player.setFirstName(firstName);
//    }
//
//    public void setLastName(String lastName) {
//        this.player.setLastName(lastName);
//    }

    public void setGenderOption(ComboBox<String> genderOption) {
        this.setGender(Gender.getGenderString(genderOption.getValue()));
        this.genderOption = genderOption;
    }
}
