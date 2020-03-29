package parser;

import com.opencsv.exceptions.CsvValidationException;
import csvmodel.Table;
import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.json.simple.JSONArray;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class CsvToStructureDefinitionParserTest {

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
        StructureDefinition structureDefinition = CsvToStructureDefinitionParser.generateStructureDefinitionFromCsv(table, "CovidDataFinal");

        try {
            new JsonParser().setOutputStyle(IParser.OutputStyle.PRETTY).compose(new FileOutputStream("CovidDataFinalStructureDef.json"), structureDefinition);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(structureDefinition);

        // TODO assert that structuredefinition is not empty
        // Assert.assertTrue(structureDefinition.g.size() > 0);
    }
}
