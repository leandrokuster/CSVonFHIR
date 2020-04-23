package parser;

import com.opencsv.exceptions.CsvValidationException;
import csvmodel.CsvTable;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;

public class CsvToJsonConverterTest {

    private static final String TEST_CSV_PATH = "res/inputCSV/Covid_Data_Final.csv";

    @Test
    // TODO refactor
    public void testThatCSVToJSONParserFunctions() throws NullPointerException {
        CsvTable table = null;
        try {
            table = CsvParser.readCsvFromFile(TEST_CSV_PATH);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(table.getHeaders().size() > 0);
        JSONObject jsonObject = CsvToJsonConverter.generateJSONFromRow(table, "CovidDataFinal", 0);

        System.out.println(jsonObject.toString());

        try {
            FileWriter fileWriter = new FileWriter("./res/parsedCSV/PatientData.json");
            fileWriter.write(jsonObject.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(jsonObject.size() > 0);



    }
}
