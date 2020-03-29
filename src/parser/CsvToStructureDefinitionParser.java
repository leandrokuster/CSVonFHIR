package parser;

import csvmodel.CsvTable;
import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.StructureDefinition;

import java.io.FileOutputStream;
import java.io.IOException;

public class CsvToStructureDefinitionParser {

    private static final String filePath = "./res/structuredefinition/";
    private static final String fileName = "CovidDataFinalStructureDef.json";

    /**
     * Generates a FHIR StructureDefinition based on a parsed CSV file.
     *
     * @param table Table object which represents the parsed CSV
     * @param type FHIR StructureDefinition type
     * @return A StructureDefinitionObject for the parsed CSV file
     */
    public static StructureDefinition generateStructureDefinitionFromCsv(CsvTable table, String type) {

        StructureDefinition structureDefinition = initializeStructureDefinition(type);

        StructureDefinition.StructureDefinitionDifferentialComponent differentialComponent = new StructureDefinition.StructureDefinitionDifferentialComponent();
        StructureDefinition.StructureDefinitionSnapshotComponent snapshotComponent = new StructureDefinition.StructureDefinitionSnapshotComponent();

        differentialComponent.addElement(createAndPopulateFirstDifferentialElement(type));
        snapshotComponent.addElement(createAndPopulateFirstSnapshotElement(type));

        for (String header : table.getHeaders()) {
            ElementDefinition elementDefinition = populateElementDefinition(header, type);
            differentialComponent.addElement(elementDefinition);
            snapshotComponent.addElement(elementDefinition);
        }

        structureDefinition.setDifferential(differentialComponent);
        structureDefinition.setSnapshot(snapshotComponent);

        generateStructureDefinitionJson(structureDefinition);

        return structureDefinition;
    }

    private static ElementDefinition createAndPopulateFirstDifferentialElement(String type) {
        ElementDefinition first = new ElementDefinition();
        first.setId(type);
        first.setPath(type);
        return first;
    }

    private static ElementDefinition createAndPopulateFirstSnapshotElement(String type) {
        ElementDefinition first = new ElementDefinition();
        first.setId(type);
        first.setPath(type);
        first.setDefinition("Base definition for all elements in a resource.");
        first.setMin(0);
        first.setMax("*");
        ElementDefinition.ElementDefinitionBaseComponent baseComponent = new ElementDefinition.ElementDefinitionBaseComponent();
        baseComponent.setMin(0);
        baseComponent.setMax("*");
        baseComponent.setPath("Element");
        first.setBase(baseComponent);

        return first;
    }

    /**
     * Initialize the structure definition and populate it with required metadata
     *
     * @param type FHIR StructureDefinition type
     * @return StructureDefinition populated with metadata
     */
    private static StructureDefinition initializeStructureDefinition(String type) {
        StructureDefinition structureDefinition = new StructureDefinition();
        structureDefinition.setUrl("http://www.csvonfhir.com/codevscovid19/StructureDefinition/" + type);
        structureDefinition.setName("Healthdata");
        structureDefinition.setStatus(Enumerations.PublicationStatus.UNKNOWN);
        structureDefinition.setKind(StructureDefinition.StructureDefinitionKind.LOGICAL);
        structureDefinition.setAbstract(false);
        structureDefinition.setType(type);
        structureDefinition.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Element");
        structureDefinition.setDerivation(StructureDefinition.TypeDerivationRule.SPECIALIZATION);

        return structureDefinition;
    }

    /**
     * Create ElementDefinition and set the required values.
     *
     * @param header String element containing a CSV header value
     * @param type FHIR StructureDefinition type
     * @return ElementDefinition based on a CSV header value
     */
    private static ElementDefinition populateElementDefinition(String header, String type) {
        String elementID = type + "." + header;
        ElementDefinition elementDefinition = new ElementDefinition();
        elementDefinition.setId(elementID);
        elementDefinition.setPath(elementID);
        elementDefinition.setMin(0);
        elementDefinition.setMax("1");
        elementDefinition.addType().setCode("string");

        return elementDefinition;
    }

    private static void generateStructureDefinitionJson(StructureDefinition structureDefinition) {
        try {
            new JsonParser().setOutputStyle(IParser.OutputStyle.PRETTY).compose(new FileOutputStream(filePath + fileName), structureDefinition);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
