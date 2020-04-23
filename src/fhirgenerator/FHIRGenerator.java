package fhirgenerator;

import org.hl7.fhir.r5.elementmodel.Element;
import org.hl7.fhir.r5.model.FhirPublication;
import org.hl7.fhir.r5.validation.ValidationEngine;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import utilities.FileUtilities;

import java.io.FileReader;

public class FHIRGenerator {
    private static int fileCounter = 0;
    private static final String TEMP_FILE_PATH = "./temp.json";

    public static void generateFhirFiles(String mapPath, String structureDefinitionPath, String dataJsonPath, String fhirOutputDirectory) throws Exception {
        FileReader fileReader = new FileReader(dataJsonPath);
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(fileReader);
        String mapUrl = MapUtils.getMapUrl(mapPath);
        ValidationEngine validator = initializeValidationEngine(structureDefinitionPath, mapPath);

        jsonArray.forEach(obj -> {
            try {
                String outputFilePath = fhirOutputDirectory + fileCounter + ".json"; // TODO: Implement creation of path, if it doesn't exist
                generateSingleFhirFile(validator, mapUrl, (JSONObject) obj, outputFilePath);
                fileCounter++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void generateSingleFhirFile(ValidationEngine validator, String mapUrl, JSONObject jsonObject, String outputFilePath) throws Exception {
        FileUtilities.writeJsonToFile(TEMP_FILE_PATH, jsonObject);
        Element transformedElement = validator.transform(TEMP_FILE_PATH, mapUrl);
        FileUtilities.deleteFile(TEMP_FILE_PATH);
        FileUtilities.writeElementToFile(outputFilePath, transformedElement, validator);
    }

    private static ValidationEngine initializeValidationEngine(String structureDefinitionOutputPath, String mapPath) throws Exception {
        ValidationEngine validationEngine = new ValidationEngine("hl7.fhir.r4.core", null, null, FhirPublication.R4);
        validationEngine.loadIg(structureDefinitionOutputPath, false);
        validationEngine.loadIg(mapPath, false);
        return validationEngine;
    }
}
