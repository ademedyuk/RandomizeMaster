package ru.demedyuk.framework.models;

import ru.demedyuk.framework.Data;
import ru.demedyuk.randomize.models.Player;

public class PlayerByFirstnameLastname implements IPlayer {

    @Override
    public Player generate() {
        return new Player(Data.getRandomPlayerName(), Data.getRandomPlayerName());
    }
}