package ru.demedyuk.tests.actions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.demedyuk.framework.Utils;
import ru.demedyuk.randomize.models.Player;
import ru.demedyuk.randomize.models.files.InputFileStates;
import ru.demedyuk.randomize.utils.actions.RandomizeAction;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomizeActionsTests {

    @ParameterizedTest(name = "all players: {0}")
    @ValueSource(ints = {3, 4, 5, 6})
    public void randomizeActions_twoPlayersInTeam(int allPlayers) {
        List<Player> listOfPlayers = Utils.createListOfPlayers(InputFileStates.FIRTSNAME, allPlayers);

        int teamSize = 2;

        RandomizeAction randomizeAction = new RandomizeAction(listOfPlayers, teamSize, false);
        HashMap<Integer, List<Player>> result = randomizeAction.getResult();

        checkResult(allPlayers, result);
        checkTeamSize(teamSize, result);
    }

    @ParameterizedTest(name = "all players: {0}")
    @ValueSource(ints = {0, 1, 2})
    public void randomizeActions_teamSizeLessOrEqualThanAllPlayers(int allPlayers) {
        List<Player> listOfPlayers = Utils.createListOfPlayers(InputFileStates.FIRTSNAME, allPlayers);

        int teamSize = 2;

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new RandomizeAction(listOfPlayers, teamSize, false);
        });

        Assertions.assertEquals("Общее количество игроков, должно превышать выбранный размер команды",
                exception.getMessage());

    }

    @ParameterizedTest(name = "all players: {0}")
    @ValueSource(ints = {4, 5, 6, 7, 8, 9})
    public void randomizeActions_threePlayersInTeam(int allPlayers) {
        List<Player> listOfPlayers = Utils.createListOfPlayers(InputFileStates.FIRTSNAME, allPlayers);

        int teamSize = 3;

        RandomizeAction randomizeAction = new RandomizeAction(listOfPlayers, teamSize, false);
        HashMap<Integer, List<Player>> result = randomizeAction.getResult();

        checkResult(allPlayers, result);
        checkTeamSize(teamSize, result);
    }

    @ParameterizedTest(name = "all players: {0}")
    @ValueSource(ints = {5, 7, 8, 9, 10, 11, 12})
    public void randomizeActions_fourPlayersInTeam(int allPlayers) {
        List<Player> listOfPlayers = Utils.createListOfPlayers(InputFileStates.FIRTSNAME, allPlayers);

        int teamSize = 4;

        RandomizeAction randomizeAction = new RandomizeAction(listOfPlayers, teamSize, false);
        HashMap<Integer, List<Player>> result = randomizeAction.getResult();

        checkResult(allPlayers, result);
        checkTeamSize(teamSize, result);
    }

    @ParameterizedTest(name = "all players: {0}")
    @ValueSource(ints = {6})
    public void randomizeActions_fourPlayersInTeamErrors(int allPlayers) {
        List<Player> listOfPlayers = Utils.createListOfPlayers(InputFileStates.FIRTSNAME, allPlayers);

        int teamSize = 4;

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new RandomizeAction(listOfPlayers, teamSize, false);
        });

        Assertions.assertEquals(String.format("Невозможно разделить %s игроков на команды по %s человек(а)",
                allPlayers, teamSize),
                exception.getMessage());

    }

    @ParameterizedTest(name = "all players: {0}")
    @ValueSource(ints = {6, 8, 9, 10, 11, 12, 13, 14, 15})
    public void randomizeActions_fivePlayersInTeam(int allPlayers) {
        List<Player> listOfPlayers = Utils.createListOfPlayers(InputFileStates.FIRTSNAME, allPlayers);

        int teamSize = 5;

        RandomizeAction randomizeAction = new RandomizeAction(listOfPlayers, teamSize, false);
        HashMap<Integer, List<Player>> result = randomizeAction.getResult();

        checkResult(allPlayers, result);
        checkTeamSize(teamSize, result);
    }

    @ParameterizedTest(name = "all players: {0}")
    @ValueSource(ints = {7})
    public void randomizeActions_fivePlayersInTeamErrors(int allPlayers) {
        List<Player> listOfPlayers = Utils.createListOfPlayers(InputFileStates.FIRTSNAME, allPlayers);

        int teamSize = 5;

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new RandomizeAction(listOfPlayers, teamSize, false);
        });

        Assertions.assertEquals(String.format("Невозможно разделить %s игроков на команды по %s человек(а)",
                allPlayers, teamSize),
                exception.getMessage());

    }

    @ParameterizedTest(name = "all players: {0}")
    @ValueSource(ints = {7, 10, 11, 12, 13, 14, 16})
    public void randomizeActions_sixPlayersInTeam(int allPlayers) {
        List<Player> listOfPlayers = Utils.createListOfPlayers(InputFileStates.FIRTSNAME, allPlayers);

        int teamSize = 6;

        RandomizeAction randomizeAction = new RandomizeAction(listOfPlayers, teamSize, false);
        HashMap<Integer, List<Player>> result = randomizeAction.getResult();

        checkResult(allPlayers, result);
        checkTeamSize(teamSize, result);
    }

    @ParameterizedTest(name = "all players: {0}")
    @ValueSource(ints = {8, 9, 15})
    public void randomizeActions_sixPlayersInTeamErrors(int allPlayers) {
        List<Player> listOfPlayers = Utils.createListOfPlayers(InputFileStates.FIRTSNAME, allPlayers);

        int teamSize = 6;

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new RandomizeAction(listOfPlayers, teamSize, false);
        });

        Assertions.assertEquals(String.format("Невозможно разделить %s игроков на команды по %s человек(а)",
                allPlayers, teamSize),
                exception.getMessage());

    }

    @ParameterizedTest(name = "all players: {0}")
    @ValueSource(ints = {8, 12, 13, 14, 15, 16})
    public void randomizeActions_sevenPlayersInTeam(int allPlayers) {
        List<Player> listOfPlayers = Utils.createListOfPlayers(InputFileStates.FIRTSNAME, allPlayers);

        int teamSize = 7;

        RandomizeAction randomizeAction = new RandomizeAction(listOfPlayers, teamSize, false);
        HashMap<Integer, List<Player>> result = randomizeAction.getResult();

        checkResult(allPlayers, result);
        checkTeamSize(teamSize, result);
    }

    @ParameterizedTest(name = "all players: {0}")
    @ValueSource(ints = {9, 10, 11, 17})
    public void randomizeActions_sevenPlayersInTeamErrors(int allPlayers) {
        List<Player> listOfPlayers = Utils.createListOfPlayers(InputFileStates.FIRTSNAME, allPlayers);

        int teamSize = 7;

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new RandomizeAction(listOfPlayers, teamSize, false);
        });

        Assertions.assertEquals(String.format("Невозможно разделить %s игроков на команды по %s человек(а)",
                allPlayers, teamSize),
                exception.getMessage());

    }

    @ParameterizedTest(name = "all players: {0}")
    @ValueSource(ints = {9, 14, 15, 16, 17, 18})
    public void randomizeActions_eightPlayersInTeam(int allPlayers) {
        List<Player> listOfPlayers = Utils.createListOfPlayers(InputFileStates.FIRTSNAME, allPlayers);

        int teamSize = 8;

        RandomizeAction randomizeAction = new RandomizeAction(listOfPlayers, teamSize, false);
        HashMap<Integer, List<Player>> result = randomizeAction.getResult();

        checkResult(allPlayers, result);
        checkTeamSize(teamSize, result);
    }

    @ParameterizedTest(name = "all players: {0}")
    @ValueSource(ints = {10, 11, 12, 13})
    public void randomizeActions_eightPlayersInTeamErrors(int allPlayers) {
        List<Player> listOfPlayers = Utils.createListOfPlayers(InputFileStates.FIRTSNAME, allPlayers);

        int teamSize = 8;

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new RandomizeAction(listOfPlayers, teamSize, false);
        });

        Assertions.assertEquals(String.format("Невозможно разделить %s игроков на команды по %s человек(а)",
                allPlayers, teamSize),
                exception.getMessage());

    }

    /**
     * Количество игроков в командах совпадает с общим количеством игроков
     * */
    private void checkResult(int expected, HashMap<Integer, List<Player>> actual) {

        int allPlayersCount = 0;
        for (int i = 0; i < actual.size(); i++)
            allPlayersCount += actual.get(i).size();

        assertEquals(expected, allPlayersCount);
    }

    /**
     * Размер команды отличается не более чем на 1 игрока от выбранного пользователем
     * */
    private void checkTeamSize(int expected, HashMap<Integer, List<Player>> actual) {

        for (int i = 0; i < actual.size(); i++)
            assertTrue(Math.abs(expected - actual.get(i).size()) <= 1);
    }
}
