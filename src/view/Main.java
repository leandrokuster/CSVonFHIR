package view;

import com.opencsv.exceptions.CsvValidationException;
import csvmodel.CsvTable;
import org.hl7.fhir.r4.model.StructureDefinition;
import parser.CsvParser;
import parser.CsvToStructureDefinitionParser;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private static final String INPUT_PATH_FLAG = "-i";
    private static final String TYPE_FLAG = "-t";
    private static final String STRUCTURE_DEFINITION_PATH_FLAG = "-s";

    public static void main(String[] args) {
        String csvInputPath = getCsvInputPath(args);
        String type = getType(args);
        String structureDefinitionOutputPath = getStructureDefinitionOutputPath(args);

        System.out.println("Reading CSV...");
        CsvTable inputTable = parseCsvFromFile(csvInputPath);

        System.out.println("Generating structure definition...");
        StructureDefinition structureDefinition = CsvToStructureDefinitionParser.generateStructureDefinitionFromCsv(inputTable, type);
        if (structureDefinitionOutputPath != null) {
            try {
                String structureDefinitionJson = CsvToStructureDefinitionParser.generateStructureDefinitionJson(structureDefinition);
                writeToFile(structureDefinitionOutputPath, structureDefinitionJson);
            } catch (IOException e) {
                System.out.println("Warning: Generation of structure definition JSON failed.");
            }
        }
    }

    private static String getCsvInputPath(String[] args) {
        try {
            return getParameter(INPUT_PATH_FLAG, args);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Input CSV argument (-i) not found, terminating.");
            System.exit(-1);
        }
        throw new IllegalStateException();
    }

    private static String getType(String[] args) {
        try {
            return getParameter(TYPE_FLAG, args);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Type argument (-t) not found, terminating.");
            System.exit(-1);
        }
        throw new IllegalStateException();
    }

    private static String getStructureDefinitionOutputPath(String[] args) {
        try {
            return getParameter(STRUCTURE_DEFINITION_PATH_FLAG, args);
        } catch (IllegalArgumentException e) {
            System.out.println("Warning: Structure definition output argument (-s) not found, no file will be generated.");
            return null;
        }
    }

    private static String getParameter(String parameterFlag, String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(parameterFlag) && i != args.length - 1) {
                return args[i + 1];
            }
        }
        throw new IllegalArgumentException("Given parameter was not found.");
    }

    private static void writeToFile(String path, String input) {
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(input);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Warning: Write to " + path + "failed.");
        }
    }

    private static CsvTable parseCsvFromFile(String path) {
        try {
            return CsvParser.readCsvFromFile(path);
        } catch (IOException e) {
            System.err.println("Error: Input file could not be read.");
            System.exit(-1);
        } catch (CsvValidationException e) {
            System.err.println("Error: CSV invalid.");
            System.exit(-1);
        }
        throw new IllegalStateException();
    }

}
