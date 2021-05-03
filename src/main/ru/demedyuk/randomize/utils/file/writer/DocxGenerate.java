package ru.demedyuk.randomize.utils.file.writer;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import ru.demedyuk.randomize.constants.FileExtensions;
import ru.demedyuk.randomize.models.Player;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class DocxGenerate extends BaseFileGenerate implements IResultFileGenerate {

    private WordprocessingMLPackage wordPackage;

    private static final String TITLE = "Title";

    public DocxGenerate(String filePath, String teamName, HashMap<Integer, List<Player>> finalTeams) {
        super(filePath.replace(FileExtensions.DOCX, ""), teamName, finalTeams);

        try {
            wordPackage = WordprocessingMLPackage.createPackage();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    public void generate() throws Docx4JException {
        MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();

        for (int i = 1; i <= this.finalTeams.size(); i++) {
            mainDocumentPart.addStyledParagraphOfText(TITLE, teamName + " " + i);

            for (Player player : finalTeams.get(i-1)) {
                mainDocumentPart.addParagraphOfText(player.name);
            }
        }

        mainDocumentPart.addParagraphOfText("date:" + this.currentDate);

        File exportFile = new File(this.filePath + "_" + currentDate + FileExtensions.DOCX);
        wordPackage.save(exportFile);
    }
}
