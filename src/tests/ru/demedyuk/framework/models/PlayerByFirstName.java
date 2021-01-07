package ru.demedyuk.framework.models;

import ru.demedyuk.framework.Data;
import ru.demedyuk.randomize.models.Player;

public class PlayerByFirstName implements IPlayer {

    public PlayerByFirstName PlayerByFirstName() {
        return this;
    }

    @Override
    public Player generate() {
        return new Player(Data.getRandomPlayerName());
    }
}