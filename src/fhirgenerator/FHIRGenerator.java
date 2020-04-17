package fhirgenerator;

import org.hl7.fhir.r5.elementmodel.Element;
import org.hl7.fhir.r5.formats.IParser;
import org.hl7.fhir.r5.model.FhirPublication;
import org.hl7.fhir.r5.validation.ValidationEngine;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;

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
        writeJsonToFile(TEMP_FILE_PATH, jsonObject);
        ValidationEngine validator = initializeValidationEngine(structureDefinitionPath, mapPath);
        // TODO: Implement dynamic reading of map name.
        Element transformedElement = validator.transform(TEMP_FILE_PATH, "http://hl7.org/fhir/StructureMap/CovidDataFinalMap");
        deleteFile(TEMP_FILE_PATH);
        writeElementToFile(outputFilePath, validator, transformedElement);
    }

    private static void writeJsonToFile(String path, JSONObject jsonObject) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(jsonObject.toJSONString());
        fileWriter.close();
    }

    private static void writeElementToFile(String path, ValidationEngine validator, Element element) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(path);
        new org.hl7.fhir.r5.elementmodel.JsonParser(validator.getContext()).compose(element, outputStream, IParser.OutputStyle.PRETTY, null);
        outputStream.close();
    }

    private static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    /**
     * Copy/Paste from run config:
     * <p>
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
    private static ValidationEngine initializeValidationEngine(String structureDefinitionOutputPath, String mapPath) throws Exception {
        ValidationEngine validationEngine = new ValidationEngine("hl7.fhir.r4.core", null, null, FhirPublication.R4);
        validationEngine.setDebug(true);
        validationEngine.loadIg(structureDefinitionOutputPath, false);
        validationEngine.loadIg(mapPath, false);
        return validationEngine;
    }
}
