package ru.demedyuk.framework.models;

import ru.demedyuk.framework.Data;
import ru.demedyuk.randomize.models.Player;

public class PlayerByFirstnameLastnameGender implements IPlayer {

    @Override
    public Player generate() {
        return new Player(Data.getRandomPlayerName());
    }
}