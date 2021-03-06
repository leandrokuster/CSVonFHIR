package parser;

import csvmodel.CsvTable;
import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.StructureDefinition;

import java.io.IOException;

public class CsvToStructureDefinitionConverter {
    /**
     * Generates a FHIR StructureDefinition based on a parsed CSV file.
     *
     * @param table Table object which represents the parsed CSV
     * @param type  FHIR StructureDefinition type
     * @return A StructureDefinitionObject for the parsed CSV file
     */
    public static StructureDefinition generateStructureDefinitionFromCsv(CsvTable table, String type) {

        StructureDefinition structureDefinition = initializeStructureDefinition(type);

        StructureDefinition.StructureDefinitionDifferentialComponent differentialComponent = new StructureDefinition.StructureDefinitionDifferentialComponent();
        StructureDefinition.StructureDefinitionSnapshotComponent snapshotComponent = new StructureDefinition.StructureDefinitionSnapshotComponent();

        differentialComponent.addElement(createAndPopulateFirstDifferentialElement(type));
        snapshotComponent.addElement(createAndPopulateFirstSnapshotElement(type));

        for (String header : table.getHeaders()) {
            differentialComponent.addElement(populateDifferentialElementDefinition(type, header));
            snapshotComponent.addElement(populateSnapshotElementDefinition(type, header));
        }

        structureDefinition.setDifferential(differentialComponent);
        structureDefinition.setSnapshot(snapshotComponent);

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
        // Fixed value for typ and path because first Element has static values by definition
        first.setBase(generateBaseComponent("Element", ""));

        return first;
    }

    private static ElementDefinition.ElementDefinitionBaseComponent generateBaseComponent(String type, String subPath) {
        String path = type;
        if (subPath.length() > 0) {
            path += "." + subPath;
        }
        ElementDefinition.ElementDefinitionBaseComponent baseComponent = new ElementDefinition.ElementDefinitionBaseComponent();
        baseComponent.setMin(0);
        baseComponent.setMax("*");
        baseComponent.setPath(path);

        return baseComponent;
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
     * @param type   FHIR StructureDefinition type
     * @return ElementDefinition based on a CSV header value
     */
    private static ElementDefinition populateDifferentialElementDefinition(String type, String header) {
        String elementID = type + "." + header;
        ElementDefinition elementDefinition = new ElementDefinition();
        elementDefinition.setId(elementID);
        elementDefinition.setPath(elementID);
        elementDefinition.setMin(0);
        elementDefinition.setMax("1");
        elementDefinition.addType().setCode("string");

        return elementDefinition;
    }

    /**
     * Create ElementDefinition for SnapshotComponents and set the required values.
     *
     * @param header String element containing a CSV header value
     * @param type   FHIR StructureDefinition type
     * @return ElementDefinition based on a CSV header value
     */
    private static ElementDefinition populateSnapshotElementDefinition(String type, String header) {
        String elementID = type + "." + header;
        ElementDefinition elementDefinition = new ElementDefinition();
        elementDefinition.setId(elementID);
        elementDefinition.setPath(elementID);
        elementDefinition.setMin(0);
        elementDefinition.setMax("1");
        elementDefinition.addType().setCode("string");
        elementDefinition.setBase(generateBaseComponent(type, header));

        return elementDefinition;
    }

    public static String generateStructureDefinitionJson(StructureDefinition structureDefinition) throws IOException {
        return new JsonParser().setOutputStyle(IParser.OutputStyle.PRETTY).composeString(structureDefinition);
    }
}
