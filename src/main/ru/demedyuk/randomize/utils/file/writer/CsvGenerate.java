package ru.demedyuk.randomize.utils.file.writer;

import ru.demedyuk.randomize.constants.FileExtensions;
import ru.demedyuk.randomize.models.Player;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

public class CsvGenerate extends BaseFileGenerate implements IResultFileGenerate {

    public CsvGenerate(String filePath, String teamName, HashMap<Integer, List<Player>> finalTeams) {
        super(filePath.replace(FileExtensions.CSV, ""), teamName, finalTeams);
    }

    public void generate() throws IOException {
        File file = new File( this.filePath + "_" + currentDate + FileExtensions.CSV);
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file, false);
        Writer writer = new OutputStreamWriter(fileOutputStream, Charset.forName("Cp1251"));

        Player firstPlayer = this.finalTeams.get(0).get(0);

        boolean numberExists = firstPlayer.numberIsExists();
        boolean ageExists = firstPlayer.ageIsExists();

        for (int i = 1; i <= this.finalTeams.size(); i++) {
            if (numberExists)
                writer.write(";");

            writer.write(teamName + " " + i + ";");
            writer.write("\n");

            for (Player players : finalTeams.get(i-1)) {
                if (numberExists)
                    writer.write(players.number + ";");

                writer.write(players.name + ";");

                if (ageExists)
                    writer.write(players.age + " лет;");

                writer.write("\n");
            }

            if (numberExists)
                writer.write(";");

            writer.write(";");
            writer.write("\n");
            writer.flush();
        }

        writer.close();
    }
}
