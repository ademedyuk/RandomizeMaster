package ru.demedyuk.randomize.utils;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import ru.demedyuk.randomize.constants.FileExtensions;
import ru.demedyuk.randomize.models.Player;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DocxGenerate {

    private WordprocessingMLPackage wordPackage;
    private String path;

    private static final String TITLE = "Title";
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH-mm-ss";

    public DocxGenerate(String path) {
        try {
            this.path = path;
            wordPackage = WordprocessingMLPackage.createPackage();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    public void addOneTeamInfo(String title, List<Player> players) {
        MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
        mainDocumentPart.addStyledParagraphOfText(TITLE, title);

        for (Player player : players) {
            mainDocumentPart.addParagraphOfText(player.firstName + " " + player.lastName);
        }
    }

    public void generate() {
        Date date = new Date();
        String currentDate = new SimpleDateFormat(DATE_FORMAT_PATTERN).format(date);

        MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
        mainDocumentPart.addParagraphOfText("date:" + currentDate);

        File exportFile = new File(this.path + "_"+ currentDate + FileExtensions.DOCX);
        try {
            wordPackage.save(exportFile);
        } catch (Docx4JException e) {
            e.printStackTrace();
        }
    }
}
