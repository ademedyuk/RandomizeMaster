package ru.demedyuk.framework;

import ru.demedyuk.framework.models.IPlayer;
import ru.demedyuk.framework.models.PlayerByFirstName;
import ru.demedyuk.randomize.models.Player;
import ru.demedyuk.randomize.models.files.InputFileStates;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    /**
     * Создание списка игроков
     * */
    public static List<Player> createListOfPlayers(InputFileStates state, int count) {
        List<Player> allPlayers = new ArrayList<>();

        IPlayer player = null;

        if (state.equals(InputFileStates.NAME)) {
            player = new PlayerByFirstName();
        }

        for (int i = 0; i < count; i++) {
            allPlayers.add(player.generate());
        }

        return allPlayers;
    }


}
