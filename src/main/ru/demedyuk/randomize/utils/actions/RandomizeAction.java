package ru.demedyuk.randomize.utils.actions;

import ru.demedyuk.randomize.constants.FileExtensions;
import ru.demedyuk.randomize.models.Gender;
import ru.demedyuk.randomize.models.Player;
import ru.demedyuk.randomize.utils.DocxGenerate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RandomizeAction {

    private String resultFilePath;
    private String teamLabel;
    private int teamSize;

    //списки нераспределенных игроков
    private List<Player> allPlayers;
    private List<Player> girlsList = new ArrayList<>();
    private List<Player> boysList = new ArrayList<>();

    //количество игроков в команде
    private HashMap<Integer, Integer> teamNumbers = new HashMap<>();

    //готовые команды
    private HashMap<Integer, List<Player>> finalTeams = new HashMap<>();

    public RandomizeAction(List<Player> allPlayers, String resultFilePath, int countOfPlayers, boolean needBalance, String teamLabel) {
        this.allPlayers = allPlayers;
        this.resultFilePath = resultFilePath;
        this.teamSize = countOfPlayers;
        this.teamLabel = teamLabel;

        if (needBalance) {
            initPlayersBySex();
            calculateTeams();
            doRandomByGender();
        } else {
            calculateTeams();
            doRandom();
        }

        Thread saveResultsThread = new Thread(this::saveResults);
        saveResultsThread.start();
    }

    private void initPlayersBySex() {
        for (Player player : allPlayers) {
            if (player.gender.equals(Gender.BOY))
                boysList.add(player);
            else if (player.gender.equals(Gender.GIRL))
                girlsList.add(player);
            else if (player.gender.equals(Gender.NONE)) {
                throw new IllegalArgumentException("Пол участников не указан");
            }
        }
    }

    private void calculateTeams() {
        //количество команд
        int countOfTeam = allPlayers.size() / this.teamSize;
        for (int i = 0; i < countOfTeam; i++) {
            teamNumbers.put(i, this.teamSize);
        }

        if (allPlayers.size() % this.teamSize != 0) {
            //команды с разным количеством человек
            int players = allPlayers.size() - (this.teamSize * teamNumbers.size());//количество лишних игроков

            if (this.teamSize / players < 2) {
                //new team
                countOfTeam++;
                teamNumbers.put(countOfTeam - 1, this.teamSize);

                for (int i = 0; i < (this.teamSize - players); i++) {
                    teamNumbers.put(countOfTeam - (i + 1), this.teamSize - 1);
                }
            } else {
                //without new team
                for (int i = 0; i < players; i++) {
                    teamNumbers.put(countOfTeam - (i + 1), this.teamSize + 1);
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

    private void doRandomByGender() {
        while (allPlayers.size() != 0) {
            for (int teamIndex : teamNumbers.keySet()) {
                finalTeams.putIfAbsent(teamIndex, new ArrayList<>());

                if (finalTeams.get(teamIndex).size() < teamNumbers.get(teamIndex)) {
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
    }

    public HashMap<Integer, List<Player>> getResult() {
        return finalTeams;
    }

    private void saveResults() {
        if (finalTeams.size() == 0)
            return;

        DocxGenerate docxGenerate = new DocxGenerate(this.resultFilePath.replace(FileExtensions.DOCX, ""));

        for (int i = 0; i < finalTeams.size(); i++) {
            docxGenerate.addOneTeamInfo(this.teamLabel + " " + (i + 1), finalTeams.get(i));
        }

        docxGenerate.generate();
    }
}