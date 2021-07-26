package ru.demedyuk.randomize.utils.file.reader;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.demedyuk.randomize.exceptions.EmptyRowException;
import ru.demedyuk.randomize.messages.OutputMessages;
import ru.demedyuk.randomize.models.Player;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XLSXInputFileReader extends BaseInputFileReader implements IInputFileReader {

    private XSSFWorkbook workbook;
    private List<Player> players = new ArrayList<>();
    private int currentRow = -1;

    public XLSXInputFileReader(String filename) {
        try {
            workbook = new XSSFWorkbook(new FileInputStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.BaseInputFileReader(filename);
    }

    public void readFile() throws EmptyRowException {
        XSSFSheet sheetAt = workbook.getSheetAt(0);

        int lastRowNum = sheetAt.getLastRowNum();
        int firstRowNum = findFirstRowNum();

        for (int i = firstRowNum; i <= lastRowNum; i++) {
            XSSFRow row = sheetAt.getRow(i);
            currentRow = i;

            short lastCellNum = getLastCell(row);

            if (lastCellNum < 1) {
                throw new EmptyRowException(String.format(OutputMessages.error_invalid_number_of_cell_xlsx, i, lastCellNum));
            }

            if (lastCellNum == 1) {
                lines.add(getCell(row,0));
                continue;
            }

            if (lastCellNum == 2) {
                lines.add(getCell(row,0) + ";" + getCell(row,1));
                continue;
            }

            if (lastCellNum >= 3) {
                lines.add(getCell(row,0) + ";" + getCell(row,1) + ";" + getCell(row,2));
                continue;
            }
        }
    }

    private String getCell(XSSFRow row, int index) throws EmptyRowException {
        try {
            return row.getCell(index).getStringCellValue();
        } catch (NullPointerException e) {
            throw new EmptyRowException(String.format(OutputMessages.error_invalid_parse_xlsx, currentRow + 1));
        }
    }

    private short getLastCell(XSSFRow row) throws EmptyRowException {
        try {
            return row.getLastCellNum();
        } catch (NullPointerException e) {
            throw new EmptyRowException(String.format(OutputMessages.error_invalid_parse_xlsx, currentRow + 1));
        }
    }

    private int findFirstRowNum() {
        for (int i = 0; i <= 1; i++) {
            XSSFRow row = workbook.getSheetAt(0).getRow(i);

            String delimeter = " "; // Разделитель
            String[] subStr = row.getCell(0).getStringCellValue().trim().split(delimeter);
            //boolean containsDigit = row.getCell(1).getStringCellValue().matches(".*\\d+.*");

            if (!subStr.equals("Имя") || !subStr.equals("Name"))
                return i;
            else continue;
        }

        throw new RuntimeException("Ошибка при поиске первого игрока");
    }
}
