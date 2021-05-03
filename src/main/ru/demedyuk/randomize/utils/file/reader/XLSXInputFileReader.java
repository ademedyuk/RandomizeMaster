package ru.demedyuk.randomize.utils.file.reader;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.demedyuk.randomize.models.Player;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XLSXInputFileReader extends BaseInputFileReader implements IInputFileReader {

    private XSSFWorkbook workbook;
    private List<Player> players = new ArrayList<>();


    public XLSXInputFileReader(String filename) {
        try {
            workbook = new XSSFWorkbook(new FileInputStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.BaseInputFileReader(filename);
    }

    public void readFile() {
        XSSFSheet sheetAt = workbook.getSheetAt(0);

        int lastRowNum = sheetAt.getLastRowNum();
        int firstRowNum = 1;

        for (int i = firstRowNum; i <= lastRowNum; i++) {
            XSSFRow row = sheetAt.getRow(i);

            short lastCellNum = row.getLastCellNum();
            if (lastCellNum <= 0) {
                lines.add(row.getCell(0).getStringCellValue());
            }

            if (lastCellNum == 1) {
                lines.add(row.getCell(0).getStringCellValue() + ";" + row.getCell(1).getStringCellValue());
            }

            if (lastCellNum >= 2) {
                lines.add(row.getCell(0).getStringCellValue() + ";" + row.getCell(1).getStringCellValue() + ";" + row.getCell(2).getStringCellValue());
            }
        }
    }

    private int findFirstRowNum(int lastRow) {
        for (int i = 0; i < lastRow; i++) {

            XSSFRow row = workbook.getSheetAt(0).getRow(i);

            String delimeter = " "; // Разделитель
            String[] subStr = row.getCell(0).getStringCellValue().trim().split(delimeter);
            boolean containsDigit = row.getCell(1).getStringCellValue().matches(".*\\d+.*");

            if (containsDigit && subStr.length == 2)
                return i;
        }

        return -1;
        //throw new Exception("Не найдены данные игроков");
    }
}
