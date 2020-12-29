package ru.demedyuk.randomize.utils;

import ru.demedyuk.randomize.models.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestExample {

    private int teamSize;
    public HashMap<Integer, Integer> teamNumbers = new HashMap<>();

    private void calculateTeams(List<Player> allPlayers, int teamSize) {
        this.teamSize = teamSize;

        //количество команд
        int countOfTeam = allPlayers.size() / this.teamSize;
        for (int i = 1; i <= countOfTeam; i++) {
            teamNumbers.put(i, this.teamSize);
        }

        if (allPlayers.size() % this.teamSize != 0) {
            //команды с разным количеством человек
            int players = allPlayers.size() - (this.teamSize * teamNumbers.size());//количество лишних игроков

            if (this.teamSize / players < 2) {
                //new team
                countOfTeam++;
                teamNumbers.put(countOfTeam, this.teamSize);

                for (int i = 0; i < countOfTeam; i++)
                    teamNumbers.put(countOfTeam - i, this.teamSize - 1);

            } else {
                //without new team
                for (int i = 0; i < players; i++)
                    teamNumbers.put(countOfTeam - i, this.teamSize + 1);
            }
        }

        int res = 0;
        for (int i : teamNumbers.keySet()) {
            res += teamNumbers.get(i);
        }

        System.out.println(teamNumbers);
        System.out.println(res == allPlayers.size() ? "true" : "false");
    }

    public static void main(String[] args) {
        int counts = 19;
        int teamSize = 6;

        List<Player> allPlayers = new ArrayList<>();

        for (int i = 0; i < counts; i++) {
            allPlayers.add(new Player("A", "D"));
        }

        new TestExample().calculateTeams(allPlayers, teamSize);
    }


    //Exceptions
    //if teamSize >= counts
}
