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
    private static final char[] ILLEGAL_CHARACTERS = {' ', '-', '_'};

    /**
     * Reads a CSV file and parses it into a CsvTable object.
     *
     * @param path The path of the file to be read
     * @return The read CSV file as a CsvTable
     * @throws IOException            Thrown if there is an error while reading the file
     * @throws CsvValidationException Thrown if there is an error while validating the CSV
     */
    public static CsvTable readCsvFromFile(String path) throws IOException, CsvValidationException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        return readCsv(reader);
    }

    /**
     * Reads CSV-formatted input from any reader and parses it onto a CsvTable object.
     *
     * @param reader A reader which reads from CSV-formatted data
     * @return The read CSV input as a CsvTable
     * @throws IOException            Thrown if there is an error while reading the file
     * @throws CsvValidationException Thrown if there is an error while validating the CSV
     */
    public static CsvTable readCsv(Reader reader) throws IOException, CsvValidationException {
        CSVReader csvReader = new CSVReader(reader);
        String[] currentLine = csvReader.readNext();
        currentLine = removeIllegalCharactersFromHeaders(currentLine);
        CsvTable table = new CsvTable(Arrays.asList(currentLine));
        while ((currentLine = csvReader.readNext()) != null) {
            table.insertRow(Arrays.asList(currentLine));
        }
        return table;
    }

    /**
     * Removes all illegal characters (defined in constants) from a string array and replaces them.
     * Serves to make the headers FHIR-compliant.
     *
     * @param headers The array of headers to remove illegal characters from
     */
    private static String[] removeIllegalCharactersFromHeaders(String[] headers) {
        String[] legalHeaders = new String[headers.length];
        for (int i = 0; i < headers.length; i++) {
            for (char illegalCharacter : ILLEGAL_CHARACTERS) {
                legalHeaders[i] = headers[i].replace(String.valueOf(illegalCharacter), "");
            }
        }
        return legalHeaders;
    }
}
