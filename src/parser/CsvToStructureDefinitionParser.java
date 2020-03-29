package parser;

import csvmodel.Table;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r5.validation.BaseValidator;

public class CsvToStructureDefinitionParser {

    public static StructureDefinition generateStructureDefinitionFromCsv(Table table, String type) {

        StructureDefinition structureDefinition = new StructureDefinition();
        structureDefinition.setUrl("http://www.csvonfhir.com/codevscovid19/StructureDefinition/" + type);
        structureDefinition.setName("Healthdata");
        structureDefinition.setStatus(Enumerations.PublicationStatus.UNKNOWN);
        structureDefinition.setKind(StructureDefinition.StructureDefinitionKind.LOGICAL);
        structureDefinition.setAbstract(false);
        structureDefinition.setType(type);
        structureDefinition.setBaseDefinition("http://hl7.org/fhir/StructureDefinition/Element");
        structureDefinition.setDerivation(StructureDefinition.TypeDerivationRule.SPECIALIZATION);



        StructureDefinition.StructureDefinitionDifferentialComponent structureDefinitionDifferentialComponent = new StructureDefinition.StructureDefinitionDifferentialComponent();
        ElementDefinition first = new ElementDefinition();
        first.setId(type);
        first.setPath(type);



        structureDefinitionDifferentialComponent.addElement(first);

        for(String header : table.getHeaders()) {
            String elementID = type + "." + header;
            ElementDefinition elementDefinition = new ElementDefinition();
            elementDefinition.setId(elementID);
            elementDefinition.setPath(elementID);
            elementDefinition.setMin(0);
            elementDefinition.setMax("1");
            elementDefinition.addType().setCode("string");

            structureDefinitionDifferentialComponent.addElement(elementDefinition);
        }

        structureDefinition.setDifferential(structureDefinitionDifferentialComponent);
        return structureDefinition;
    }


}
