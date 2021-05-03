package ru.demedyuk.randomize.utils.file;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.demedyuk.randomize.models.Player;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {

    private List<Player> players = new ArrayList<>();
    private XSSFWorkbook workbook;

    public ExcelUtils(String filename) {
        try {
            workbook = new XSSFWorkbook(new FileInputStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Player> getPlayers() {
        XSSFSheet sheetAt = workbook.getSheetAt(0);

        int lastRowNum = sheetAt.getLastRowNum();
        int firstRowNum = findFirstRowNum(lastRowNum);

        for (int i = firstRowNum; i <= lastRowNum; i++) {
            XSSFRow row = sheetAt.getRow(i);

            String stringCellValue = row.getCell(0).getStringCellValue();
            String delimeter = " "; // Разделитель
            String[] subStr = stringCellValue.trim().split(delimeter);

            Player player = new Player(subStr[0]).addAge(subStr[1]);
            player.setAge(row.getCell(1).getStringCellValue().replaceAll("\\D+",""));

            players.add(player);
        }

        return players;
    }


    public int findFirstRowNum(int lastRow) {
        for (int i = 0; i < lastRow; i++) {

            XSSFRow row = workbook.getSheetAt(0).getRow(i);

            String delimeter = " "; // Разделитель
            String[] subStr = row.getCell(0).getStringCellValue().trim().split(delimeter);
            boolean containsDigit = row.getCell(1).getStringCellValue().matches(".*\\d+.*");

            if (containsDigit && subStr.length == 2)
                return i;
        }

        return -1;
    }
}
