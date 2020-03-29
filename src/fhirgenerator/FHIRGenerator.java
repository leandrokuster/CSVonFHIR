package fhirgenerator;

import org.hl7.fhir.r4.model.StructureDefinition;
import org.json.simple.JSONObject;

import java.io.IOException;

public class FHIRGenerator {

    private static org.hl7.fhir.validation.ValidationEngine validator;

    public void FHIRGenerator() {
        try {
            validator = initializeValidationEngine();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * @return
     * @throws IOException
     */
    private static org.hl7.fhir.validation.ValidationEngine initializeValidationEngine() throws IOException {
        org.hl7.fhir.validation.ValidationEngine validator = null;
        try {
            validator = new org.hl7.fhir.validation.ValidationEngine("./res/parsedCSV/PatientData.json");
            validator.loadIg("./res/structuredefinition/CovidDataFinalStructureDef.json", false);
            validator.loadIg("./res/maps/CovidDataFinalMap.map", false);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return validator;
    }

    public void generateFHIRFileFromCSV(StructureDefinition structureDefinition, JSONObject inputFile, ) {

    }
}
