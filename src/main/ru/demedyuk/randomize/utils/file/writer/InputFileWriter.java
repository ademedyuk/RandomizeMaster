package ru.demedyuk.randomize.utils.file.writer;

import javafx.collections.ObservableList;
import ru.demedyuk.randomize.constants.FileExtensions;
import ru.demedyuk.randomize.models.TableBean;
import ru.demedyuk.randomize.models.Gender;

import java.io.*;
import java.nio.charset.Charset;

public class InputFileWriter {

    private final String filePath;
    private ObservableList<TableBean> playersData;

    public InputFileWriter(String filePath, ObservableList<TableBean> playersData) {
        this.filePath = filePath;
        this.playersData = playersData;
    }

    public void writeToTxt() throws IOException {
        try(FileWriter writer = new FileWriter(this.filePath, false))
        {
            for(TableBean player : playersData) {
                if (player.numberIsExists())
                    writer.write(player.number + ";");

                writer.write(player.name);

                if (player.genderIsExists())
                    writer.write(" ;" + player.gender.toOutputValue());

                if (player.ageIsExists())
                    writer.write(" ;" + player.age);

                writer.write("\n");
            }
            writer.flush();
        }
    }

    public void writeToCsv() throws IOException {
        File file = new File( this.filePath.replace(FileExtensions.XLSX, FileExtensions.CSV));
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file, false);

        try(Writer writer = new OutputStreamWriter(fileOutputStream, Charset.forName("Cp1251")))
        {
            for(TableBean player : playersData) {
                if (player.numberIsExists())
                    writer.write(player.number + ";");

                writer.write(player.name + ";");

                if (player.genderIsExists())
                    writer.write(player.gender + ";");

                if (player.ageIsExists())
                    writer.write(player.age + ";");

                writer.write("\n");
            }

            writer.flush();
        }
    }
}
