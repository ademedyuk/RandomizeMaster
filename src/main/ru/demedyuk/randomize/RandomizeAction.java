package ru.demedyuk.randomize;

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
    private String resultFilePath;
    private String fileContent = "";

    //списки нераспределенных игроков
    public List<Player> allPlayers = new ArrayList<>();
    public List<Player> girlsList = new ArrayList<>();
    public List<Player> boysList = new ArrayList<>();

    public HashMap<Integer, Integer> teams = new HashMap<>();//key - номер, value - количество игроков

    public RandomizeAction(String filePath, String resultFilePath, int countOfPlayers) {
        //путь до файла с настройками
        this.filePath = filePath;
        this.resultFilePath = resultFilePath;
        initPlayers();

        //количество команд
        int countOfTeam = allPlayers.size() / countOfPlayers;
        for (int i = 0; i < countOfTeam; i++) {
            teams.put(i, countOfPlayers);
        }

        //при нецелом делении, создаем еще одну команду
        if (allPlayers.size() % countOfPlayers != 0) {
            int players = (-1)*((allPlayers.size() - countOfTeam * countOfPlayers) - countOfTeam);//количество лишних игроков
            countOfTeam++;
            teams.put(countOfTeam, countOfPlayers);

            for (int i = 0; i < players; i++) {
                teams.put(countOfTeam - i, countOfPlayers - 1);
            }
        }
    }

    public List<Player> doRandom(int index) {
        List<Player> randomPlayers = new ArrayList<>();

        for (int i = 0; i < teams.get(index); i++) {
            if (allPlayers.size() == 0)
                break;
            int playerKey = new Random().nextInt(allPlayers.size());
            randomPlayers.add(allPlayers.get(playerKey));
            allPlayers.remove(playerKey);
        }

        saveResults();
        return randomPlayers;
    }

    private void saveResults() {
        if (allPlayers.size() != 0)
            return;

//       TODO: Сохранение результатов жребьевки в файл

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

    private void initPlayers() {
        List<String> lines = getTextFromFile();
        for (String line : lines) {
            String delimeter = " "; // Разделитель
            String[] subStr = line.split(delimeter);
            allPlayers.add(new Player(subStr[0], subStr[1], subStr[2]));
        }
    }

}
