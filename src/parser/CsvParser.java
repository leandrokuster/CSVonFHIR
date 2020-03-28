package parser;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import csvmodel.Table;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public class CsvParser {
    public static Table readCsvFromFile(String path) throws IOException, CsvValidationException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        return readCsv(reader);
    }

    public static Table readCsv(Reader reader) throws IOException, CsvValidationException {
        CSVReader csvReader = new CSVReader(reader);
        String[] currentLine = csvReader.readNext();
        Table table = new Table(Arrays.asList(currentLine));
        while ((currentLine = csvReader.readNext()) != null) {
            table.insertRow(Arrays.asList(currentLine));
        }
        return table;
    }
}
