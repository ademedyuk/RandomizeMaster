package ru.demedyuk.randomize.utils;

import ru.demedyuk.randomize.constants.FileExtensions;
import ru.demedyuk.randomize.models.Player;
import ru.demedyuk.randomize.models.Gender;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RandomizeAction {

    private String filePath;
    private int teamSize;
    private String resultFilePath;
    private String teamLabel;

    //списки нераспределенных игроков
    public List<Player> allPlayers = new ArrayList<>();
    public List<Player> girlsList = new ArrayList<>();
    public List<Player> boysList = new ArrayList<>();

    //количество игроков в команде
    public HashMap<Integer, Integer> teamNumbers = new HashMap<>();

    //готовые команды
    private HashMap<Integer, List<Player>> finalTeams = new HashMap<>();

    public RandomizeAction(String filePath, String resultFilePath, int countOfPlayers, boolean needBalance, String teamLabel) {
        this.filePath = filePath;
        this.resultFilePath = resultFilePath;
        this.teamSize = countOfPlayers;
        this.teamLabel = teamLabel;

        if (needBalance)
            initPlayersBySex();
        else
            initPlayers();

        calculateTeams();

        if (needBalance)
            doRandomBySex();
        else
            doRandom();

        Thread myThready = new Thread(new Runnable() {
            @Override
            public void run() {
                saveResults();
            }
        });
        myThready.start();
    }

    private void calculateTeams() {
        //количество команд
        int countOfTeam = allPlayers.size() / this.teamSize;
        for (int i = 0; i < countOfTeam; i++) {
            teamNumbers.put(i, this.teamSize);
        }

        //при нецелом делении, создаем еще одну команду
        if (allPlayers.size() % this.teamSize != 0) {
            int players = (-1) * ((allPlayers.size() - countOfTeam * this.teamSize) - countOfTeam);//количество лишних игроков
            countOfTeam++;
            teamNumbers.put(countOfTeam, this.teamSize);

            for (int i = 0; i < players; i++) {
                teamNumbers.put(countOfTeam - i, this.teamSize - 1);
            }
        }
    }

    private void doRandomBySex() {
        while (allPlayers.size() != 0) {
            for (int teamIndex : teamNumbers.keySet()) {
                finalTeams.putIfAbsent(teamIndex, new ArrayList<>());

                if (!boysList.isEmpty()) {
                    Player randomPlayer = boysList.get(new Random().nextInt(boysList.size()));
                    finalTeams.get(teamIndex).add(randomPlayer);

                    boysList.remove(randomPlayer);
                    allPlayers.remove(randomPlayer);

                } else if (!girlsList.isEmpty()) {
                    Player randomPlayer = girlsList.get(new Random().nextInt(girlsList.size()));
                    finalTeams.get(teamIndex).add(randomPlayer);

                    girlsList.remove(randomPlayer);
                    allPlayers.remove(randomPlayer);
                }
            }
        }
    }

    private void doRandom() {
        for (int teamIndex = 0; teamIndex < teamNumbers.size(); teamIndex++) {
            List<Player> randomTeam = new ArrayList<>();

            for (int i = 0; i < teamNumbers.get(teamIndex); i++) {
                Player randomPlayer = allPlayers.get(new Random().nextInt(allPlayers.size()));
                randomTeam.add(randomPlayer);
                allPlayers.remove(randomPlayer);
            }

            finalTeams.put(teamIndex, randomTeam);
        }
    }

    public List<Player> getTeam(int index) {
        return finalTeams.get(index - 1);
    }

    private void saveResults() {
        if (finalTeams.size() == 0)
            return;

        DocxGenerate docxGenerate = new DocxGenerate(this.resultFilePath.replace(FileExtensions.DOCX, ""));

        for (int i = 0; i < finalTeams.size(); i++) {
            docxGenerate.addOneTeamInfo(this.teamLabel + " " + (int)(i+1), finalTeams.get(i));
        }

        docxGenerate.generate();
    }

    private void initPlayers() {
        List<String> lines = getTextFromFile();

        for (String line : lines) {
            String delimeter = " "; // Разделитель
            String[] subStr = line.split(delimeter);
            allPlayers.add(new Player(subStr[0], subStr[1], subStr[2]));
        }
    }

    private void initPlayersBySex() {
        List<String> lines = getTextFromFile();

        for (String line : lines) {
            String delimeter = " "; // Разделитель
            String[] subStr = line.split(delimeter);

            String gender = subStr[3].substring(0, 1);
            Player player = new Player(subStr[0], subStr[1], subStr[2]);

            Gender genderByID = Gender.getGenderByID(gender);

            if (genderByID.equals(Gender.BOY))
                boysList.add(player);
            else if (genderByID.equals(Gender.GIRL))
                girlsList.add(player);
            else if (genderByID.equals(Gender.NONE))
                throw new IllegalArgumentException("Gender if not defined");

            allPlayers.add(player);
        }
    }

    private List<String> getTextFromFile() {
        Path file = Paths.get(this.filePath);
        List<String> lines = null;

        try {
            lines = Files.readAllLines(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

}