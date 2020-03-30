package fhirgenerator;

import java.io.IOException;

public class FHIRGenerator {

    private static org.hl7.fhir.validation.ValidationEngine validator;

    public static void generateFhirFiles(String mapPath, String structureDefinitionOutputPath, String dataJsonPath, String fhirOutputPath) throws Exception {
        initializeValidationEngine(dataJsonPath, structureDefinitionOutputPath, mapPath);
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
    private static void initializeValidationEngine(String dataJsonPath, String structureDefinitionOutputPath, String mapPath) throws Exception {
        validator = new org.hl7.fhir.validation.ValidationEngine(dataJsonPath);
        validator.loadIg(structureDefinitionOutputPath, false);
        validator.loadIg(mapPath, false);
    }
}
