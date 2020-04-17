package fhirgenerator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FHIRGenerator {

    private static int fileCounter = 0;
    private static final String TEMP_FILE_PATH = "./temp.json";

    public static void generateFhirFiles(String mapPath, String structureDefinitionOutputPath, String dataJsonPath, String fhirOutputDirectory) throws Exception {
        FileReader fileReader = new FileReader(dataJsonPath);
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(fileReader);
        jsonArray.forEach(obj -> {
            try {
                String outputFilePath = fhirOutputDirectory + fileCounter + ".json";
                generateSingleFhirFile((JSONObject) obj, structureDefinitionOutputPath, mapPath, outputFilePath);
                fileCounter++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void generateSingleFhirFile(JSONObject jsonObject, String structureDefinitionPath, String mapPath, String outputFilePath) throws Exception {
        writeToFile(TEMP_FILE_PATH, jsonObject);
        org.hl7.fhir.validation.ValidationEngine validator = initializeValidationEngine(TEMP_FILE_PATH, structureDefinitionPath, mapPath);
        validator.convert(TEMP_FILE_PATH, outputFilePath);
    }

    private static void writeToFile(String path, JSONObject jsonObject) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(jsonObject.toJSONString());
        fileWriter.close();
        deleteFile(path);
    }

    private static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    /**
     * ./res/parsedCSV/PatientData.json
     * -transform
     * http://hl7.org/fhir/StructureMap/CovidDataFinalMap
     * -version
     * 4.0.1
     * -ig
     * ./res/structuredefinition/CovidDataFinalStructureDef.json
     * -ig
     * ./res/maps/CovidDataFinalMap.map
     * -log
     * test.txt
     * -output
     * ./output.json
     *
     * @throws IOException when loading an ig into the validator failed
     */
    private static org.hl7.fhir.validation.ValidationEngine initializeValidationEngine(String dataJsonPath, String structureDefinitionOutputPath, String mapPath) throws Exception {
        org.hl7.fhir.validation.ValidationEngine validator;
        validator = new org.hl7.fhir.validation.ValidationEngine(dataJsonPath);
        validator.loadIg(structureDefinitionOutputPath, false);
        validator.loadIg(mapPath, false);
        return validator;
    }
}
