package ru.demedyuk.randomize.models.files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.demedyuk.randomize.models.Gender;
import ru.demedyuk.randomize.models.Player;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class InputFileWriter {

    private final String filePath;
    private ObservableList<Player> playersData;

    public InputFileWriter(String filePath, ObservableList<Player> playersData) {
        this.filePath = filePath;
        this.playersData = playersData;
    }

    public void write() {
        try(FileWriter writer = new FileWriter(this.filePath, false))
        {
            for(Player player : playersData) {
                String text;

                if (player.number != null)
                    writer.write(player.number + " ");

                writer.write(player.firstName);

                if (player.lastName != null)
                    writer.write(" " + player.lastName);

                if (player.gender != Gender.NONE)
                    writer.write(" " + player.gender.toOutputValue());

                writer.write("\n");
            }

            writer.flush();
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
}
