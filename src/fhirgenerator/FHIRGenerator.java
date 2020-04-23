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

    /**
     * Generates FHIR-compliant JSON files from an arbitrary data JSON file and stores them in an output folder.
     * For each entry in the input JSON, a new numbered output file is created.
     *
     * @param mapPath                 The path where the FHIR mapping language file to be used in the conversion is stored.
     * @param structureDefinitionPath The path where the structure definition file to be used in the conversion is stored.
     * @param dataJsonPath            The path where the input data JSON file is stored.
     * @param fhirOutputDirectory     The directory where the output files will be generated.
     * @throws Exception Thrown if the conversion process fails.
     */
    public static void generateFhirFiles(String mapPath, String structureDefinitionPath, String dataJsonPath, String fhirOutputDirectory) throws Exception {
        FileReader fileReader = new FileReader(dataJsonPath);
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(fileReader);
        String mapUrl = MapHelper.getMapUrl(mapPath);
        ValidationEngine validator = initializeValidationEngine(structureDefinitionPath, mapPath);

        jsonArray.forEach(obj -> {
            try {
                String outputFilePath = fhirOutputDirectory + fileCounter + ".json";
                generateSingleFhirFile(validator, mapUrl, (JSONObject) obj, outputFilePath);
                fileCounter++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Generates a single FHIR file from a JSON object by converting it with a validation engine.
     *
     * @param validator      The validation engine used for the conversion.
     * @param mapUrl         The URL of the map used in the conversion.
     * @param jsonObject     The JSON object to be converted to FHIR.
     * @param outputFilePath The path where the output file will be generated.
     * @throws Exception Thrown if the conversion process fails.
     */
    private static void generateSingleFhirFile(ValidationEngine validator, String mapUrl, JSONObject jsonObject, String outputFilePath) throws Exception {
        FileUtilities.writeJsonToFile(TEMP_FILE_PATH, jsonObject);
        Element transformedElement = validator.transform(TEMP_FILE_PATH, mapUrl);
        FileUtilities.deleteFile(TEMP_FILE_PATH);
        FileUtilities.writeElementToFile(outputFilePath, transformedElement, validator);
    }

    /**
     * Initializes a validation engine for use in conversions.
     *
     * @param structureDefinitionPath The path to the structure definition the validation engine should use later.
     * @param mapPath                 The path to the map the validation engine should use later.
     * @return A new validation engine ready to use with the given inputs.
     * @throws Exception If the creation process fails and no validation engine is created.
     */
    private static ValidationEngine initializeValidationEngine(String structureDefinitionPath, String mapPath) throws Exception {
        ValidationEngine validationEngine = new ValidationEngine("hl7.fhir.r4.core", null, null, FhirPublication.R4);
        validationEngine.loadIg(structureDefinitionPath, false);
        validationEngine.loadIg(mapPath, false);
        return validationEngine;
    }
}
