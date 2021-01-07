package ru.demedyuk.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Data {

    private static List<String> boyNames = Arrays.asList(
            "Андрей", "Максим", "Женя", "Слава", "Дима", "Cемён");

    private static List<String> girlNames = Arrays.asList(
            "Маша", "Аня", "Таня", "Света", "Марина", "Катя");

    public static String getRandomPlayerName() {

        if (new Random().nextInt(2) == 1)
            return boyNames.get(new Random().nextInt(boyNames.size()));
        else
            return girlNames.get(new Random().nextInt(girlNames.size()));
    }
}
