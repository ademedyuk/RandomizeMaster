package ru.demedyuk.randomize.utils.actions;

import ru.demedyuk.randomize.constants.FileExtensions;
import ru.demedyuk.randomize.models.Gender;
import ru.demedyuk.randomize.models.GenderGroup;
import ru.demedyuk.randomize.models.Player;
import ru.demedyuk.randomize.utils.file.writer.CsvGenerate;
import ru.demedyuk.randomize.utils.file.writer.DocxGenerate;
import ru.demedyuk.randomize.utils.file.writer.IResultFileGenerate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RandomizeAction {

    private int teamSize;

    //списки нераспределенных игроков
    private List<Player> allPlayers;
    private List<Player> girlsList = new ArrayList<>();
    private List<Player> boysList = new ArrayList<>();

    //количество игроков в команде
    private HashMap<Integer, Integer> teamNumbers = new HashMap<>();

    //готовые команды
    private HashMap<Integer, List<Player>> finalTeams = new HashMap<>();

    private HashMap<Integer, GenderGroup> genderGroups = new HashMap<>();

    private HashMap<Integer, List<Player>> playersByAgeGroup = new HashMap<>();

    public RandomizeAction(List<Player> allPlayers, int teamSize, HashMap<Integer, GenderGroup> genderGroups, RandomizeOptions randomizeOptions) {
        if (teamSize >= allPlayers.size())
            throw new IllegalArgumentException("Общее количество игроков должно превышать выбранный размер команды");

        this.allPlayers = allPlayers;
        this.teamSize = teamSize;
        this.genderGroups = genderGroups;

        switch (randomizeOptions) {
            case NONE:
                calculateTeams();
                doRandom();
                break;

            case BY_GENDER:
                initPlayersBySex();
                calculateTeams();
                doRandomByGender();
                break;

            case BY_AGE:
                calculateTeams();
                initPlayersByAgeGroup();
                doRandomByAge();
                break;
        }
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

    private void initPlayersByAgeGroup() {
        for (int i = 0; i < this.genderGroups.size(); i++) {
            GenderGroup genderGroup = this.genderGroups.get(i);

            List<Player> ageGroup = new ArrayList<>();
            for (Player player : new ArrayList<>(allPlayers)) {
                if (player.getAge() >= genderGroup.startAge && player.getAge() <= genderGroup.finalAge) {
                    ageGroup.add(player);
                    allPlayers.remove(player);
                }
            }

            this.playersByAgeGroup.put(i, ageGroup);
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

        int allCalcutale = 0;
        for (int i : teamNumbers.keySet())
            allCalcutale += teamNumbers.get(i);
        if (allPlayers.size() < allCalcutale)
            throw new IllegalArgumentException(String.format(
                    "Невозможно разделить %s игроков на команды по %s человек(а)",
                    allPlayers.size(), this.teamSize));
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

    private void doRandomByAge() {
        int teamNumber = teamNumbers.size() - 1;

        for (int i = this.playersByAgeGroup.size() - 1; i >= 0; i--) {
            List<Player> players = this.playersByAgeGroup.get(i);

            for (int a; players.size() != 0; teamNumber--) {
                List<Player> playerList = finalTeams.get(teamNumber);
                if (playerList == null || playerList.size() < teamNumbers.get(teamNumber)) {
                    Player randomPlayer = players.get(new Random().nextInt(players.size()));
                    players.remove(randomPlayer);

                    if (playerList == null) {
                        playerList = new ArrayList<>();
                    }

                    playerList.add(randomPlayer);
                    finalTeams.put(teamNumber, playerList);
                }

                if (teamNumber - 1 < 0) {
                    teamNumber = teamNumbers.size();
                }
            }
        }
    }

    public HashMap<Integer, List<Player>> getResult() {
        return finalTeams;
    }

    public void saveResults(String resultsFilePath, String teamLabel) {
        if (finalTeams.size() == 0)
            return;

        IResultFileGenerate fileGenerate = null;

        if (resultsFilePath.endsWith(FileExtensions.DOCX)) {
            fileGenerate = new DocxGenerate(resultsFilePath, teamLabel, finalTeams);
        }

        if (resultsFilePath.endsWith(FileExtensions.CSV)) {
            fileGenerate = new CsvGenerate(resultsFilePath, teamLabel, finalTeams);
        }

        try {
            fileGenerate.generate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}