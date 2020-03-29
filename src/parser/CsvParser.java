package parser;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import csvmodel.CsvTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public class CsvParser {
    private static final char[] ILLEGAL_CHARACTERS = {' ', '_'};
    private static final char[] REPLACEMENT_CHARACTERS = {'-', '-'};

    public static CsvTable readCsvFromFile(String path) throws IOException, CsvValidationException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        return readCsv(reader);
    }

    public static CsvTable readCsv(Reader reader) throws IOException, CsvValidationException {
        CSVReader csvReader = new CSVReader(reader);
        String[] currentLine = csvReader.readNext();
        removeIllegalCharactersFromHeaders(currentLine);
        CsvTable table = new CsvTable(Arrays.asList(currentLine));
        while ((currentLine = csvReader.readNext()) != null) {
            table.insertRow(Arrays.asList(currentLine));
        }
        return table;
    }

    private static void removeIllegalCharactersFromHeaders(String[] headers) {
        for (String currentHeader : headers) {
            for (int i = 0; i < ILLEGAL_CHARACTERS.length; i++) {
                currentHeader.replace(ILLEGAL_CHARACTERS[i], REPLACEMENT_CHARACTERS[i]);
            }
        }
    }
}
