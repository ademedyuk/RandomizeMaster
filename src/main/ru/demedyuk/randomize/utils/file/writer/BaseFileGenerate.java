package ru.demedyuk.randomize.utils.file.writer;

import ru.demedyuk.randomize.models.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BaseFileGenerate {

    protected String filePath;
    protected String teamName;
    protected HashMap<Integer, List<Player>> finalTeams;

    private static final String DATE_FORMAT_PATTERN = "dd-MM-YYYY'_time_'HH-mm-ss";
    protected String currentDate = new SimpleDateFormat(DATE_FORMAT_PATTERN).format(new Date());

    public BaseFileGenerate(String filePath, String teamName, HashMap<Integer, List<Player>> finalTeams) {
        this.filePath = filePath;
        this.teamName = teamName;
        this.finalTeams = finalTeams;
    }
}
