package ru.demedyuk.randomize.utils.file.reader;

import ru.demedyuk.randomize.exceptions.EmptyRowException;
import ru.demedyuk.randomize.models.Player;
import ru.demedyuk.randomize.models.files.InputFileStates;

import java.io.IOException;
import java.util.List;

public interface IInputFileReader {

    void readFile() throws IOException, EmptyRowException;

    void validateDocument();

    List<Player> getAllPlayers();

    InputFileStates getState();

    String getInputPath();

}
