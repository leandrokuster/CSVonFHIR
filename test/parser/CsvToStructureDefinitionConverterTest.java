package parser;

import com.opencsv.exceptions.CsvValidationException;
import csvmodel.CsvTable;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class CsvToStructureDefinitionConverterTest {

    private static final String TEST_CSV_PATH = "res/inputCSV/Covid_Data_Final.csv";
    private static StructureDefinition structureDefinition = null;
    private static final String type = "CovidDataFinal";

    @Before
    public void createAndPopulateStructureDefinition() throws NullPointerException {
        CsvTable table = null;
        try {
            table = CsvParser.readCsvFromFile(TEST_CSV_PATH);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(table.getHeaders().size() > 0);
        structureDefinition = CsvToStructureDefinitionConverter.generateStructureDefinitionFromCsv(table, type);
        System.out.println(structureDefinition);
    }

    @Test
    public void testThatStructureDefinitionGetsPopulated() {
        Assert.assertNotNull(structureDefinition);
        Assert.assertFalse(structureDefinition.isEmpty());
    }

    @Test
    public void testThatStructureDefinitionHasDifferential() {
        Assert.assertTrue(structureDefinition.hasDifferential());
    }

    @Test
    public void testThatStructureDefinitionHasSnapshot() {
        Assert.assertTrue(structureDefinition.hasSnapshot());
    }

    @Test
    public void testThatStructureDefinitionJsonIsCreated() {
        String filePath = "./res/structuredefinition/";
        String fileName = "CovidDataFinalStructureDef.json";
        File file = new File(filePath + fileName);
        System.out.println(file.getAbsoluteFile().toString());
        Assert.assertTrue(file.exists());
    }
}
