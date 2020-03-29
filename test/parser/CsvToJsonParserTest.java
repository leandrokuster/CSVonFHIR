package parser;

import com.opencsv.exceptions.CsvValidationException;
import csvmodel.Table;
import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.formats.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class CsvToJsonParserTest {

    private static final String TEST_CSV_PATH = "res/inputCSV/Covid_Data_Final.csv";

    @Test
    // TODO refactor
    public void testThatCSVToJSONParserFunctions() {
        Table table = new Table();
        try {
            table = CsvParser.readCsvFromFile(TEST_CSV_PATH);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(table.getHeaders().size() > 0);
        JSONObject jsonObject = CsvToJsonParser.generateJSONFromRow(table, "CovidDataFinal", 0);

        System.out.println(jsonObject.toString());

        try {
            FileWriter fileWriter = new FileWriter("PatientData.json");
            fileWriter.write(jsonObject.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(jsonObject.size() > 0);



    }
}
