package ru.demedyuk.randomize.models.files;

import javafx.collections.ObservableList;
import ru.demedyuk.randomize.models.TableBean;
import ru.demedyuk.randomize.models.Gender;

import java.io.FileWriter;
import java.io.IOException;

public class InputFileWriter {

    private final String filePath;
    private ObservableList<TableBean> playersData;

    public InputFileWriter(String filePath, ObservableList<TableBean> playersData) {
        this.filePath = filePath;
        this.playersData = playersData;
    }

    public void write() {
        try(FileWriter writer = new FileWriter(this.filePath, false))
        {
            for(TableBean player : playersData) {
                if (!player.number.isEmpty() && player.number != null)
                    writer.write(player.number + " ");

                writer.write(player.firstName);

                if (!player.lastName.isEmpty() && player.lastName != null)
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
