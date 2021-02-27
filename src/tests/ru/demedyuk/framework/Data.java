package ru.demedyuk.framework;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Data {

    private static List<String> boyFirstNames = Arrays.asList(
            "Андрей", "Максим", "Женя", "Слава", "Дима", "Cемён");

    private static List<String> girlFirstNames = Arrays.asList(
            "Маша", "Аня", "Таня", "Света", "Марина", "Катя");

    private static List<String> boyLastNames = Arrays.asList(
            "Рудаков", "Иванютенко", "Третьяков", "Трубицин", "Купша", "Басалаев");

    private static List<String> girlLastNames = Arrays.asList(
            "Ивановская", "Филинова", "Засыпкина", "Прищенко", "Яковлева", "Попова");

    public static String getRandomPlayerName() {

        if (new Random().nextInt(2) == 1)
            return boyFirstNames.get(new Random().nextInt(boyFirstNames.size()));
        else
            return girlFirstNames.get(new Random().nextInt(girlFirstNames.size()));
    }
}
