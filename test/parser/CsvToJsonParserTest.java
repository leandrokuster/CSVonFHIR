package parser;

import com.opencsv.exceptions.CsvValidationException;
import csvmodel.Table;
import org.json.simple.JSONArray;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CsvToJsonParserTest {

    private static final String TEST_CSV_PATH = "res/inputCSV/Covid_Data_Final.csv";

    @Test
    public void testThatCSVToJSONParserFunctions() {
        Table table = new Table();
        try {
            table = CsvParser.readCsvFromFile(TEST_CSV_PATH);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(table.getHeaders().size() > 0);
        JSONArray jsonObject = CsvToJsonParser.generateJSONFromCSV(table);
        System.out.println(jsonObject.toString());
        Assert.assertTrue(jsonObject.size() > 0);
    }
}
