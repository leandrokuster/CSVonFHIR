package view;

import com.opencsv.exceptions.CsvValidationException;
import csvmodel.CsvTable;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.json.simple.JSONArray;
import parser.CsvParser;
import parser.CsvToJsonParser;
import parser.CsvToStructureDefinitionParser;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private static final String INPUT_PATH_FLAG = "-i";
    private static final String TYPE_FLAG = "-t";
    private static final String STRUCTURE_DEFINITION_PATH_FLAG = "-s";
    private static final String DATA_PATH_FLAG = "-d";
    private static final String MAP_PATH_FLAG = "-m";

    private static final String DEFAULT_STRUCTURE_DEFINITION_PATH = "./structure-definition.json";
    private static final String DEFAULT_DATA_PATH = "./data.json";

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelpMessage();
            return;
        }
        String csvInputPath = getCsvInputPath(args);
        String type = getType(args);
        String mapPath = getMapPath(args);
        System.out.println(mapPath);
        String structureDefinitionOutputPath = getStructureDefinitionOutputPath(args);
        String dataJsonPath = getDataJsonPath(args);

        System.out.println("Reading CSV...");
        CsvTable inputTable = parseCsvFromFile(csvInputPath);

        System.out.println("Generating structure definition...");
        StructureDefinition structureDefinition = CsvToStructureDefinitionParser.generateStructureDefinitionFromCsv(inputTable, type);
        writeStructureDefinition(structureDefinitionOutputPath, structureDefinition);

        System.out.println("Generating data file...");
        JSONArray inputTableJson = CsvToJsonParser.generateJSONFromCSV(inputTable, type);
        writeDataJson(dataJsonPath, inputTableJson);

        // TODO: Hook up to transformator/validator
    }

    private static String getCsvInputPath(String[] args) {
        try {
            return getParameter(INPUT_PATH_FLAG, args);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Input CSV argument (" + INPUT_PATH_FLAG + ") not found, terminating.");
            System.exit(-1);
        }
        throw new IllegalStateException();
    }

    private static String getType(String[] args) {
        try {
            return getParameter(TYPE_FLAG, args);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Type argument (" + TYPE_FLAG + ") not found, terminating.");
            System.exit(-1);
        }
        throw new IllegalStateException();
    }

    private static String getMapPath(String[] args) {
        try {
            return getParameter(MAP_PATH_FLAG, args);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Map argument (" + MAP_PATH_FLAG + ") not found, terminating.");
            System.exit(-1);
        }
        throw new IllegalStateException();
    }

    private static String getStructureDefinitionOutputPath(String[] args) {
        try {
            return getParameter(STRUCTURE_DEFINITION_PATH_FLAG, args);
        } catch (IllegalArgumentException e) {
            System.out.println("Warning: Structure definition output argument (" + STRUCTURE_DEFINITION_PATH_FLAG + ") not found, using default path (" + DEFAULT_STRUCTURE_DEFINITION_PATH + ").");
            return DEFAULT_STRUCTURE_DEFINITION_PATH;
        }
    }

    private static String getDataJsonPath(String[] args) {
        try {
            return getParameter(DATA_PATH_FLAG, args);
        } catch (IllegalArgumentException e) {
            System.out.println("Warning: Data output argument (" + DATA_PATH_FLAG + ") not found, using default path (" + DEFAULT_DATA_PATH + ").");
            return DEFAULT_DATA_PATH;
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

    private static void writeStructureDefinition(String path, StructureDefinition definition) {
        try {
            String structureDefinitionJson = CsvToStructureDefinitionParser.generateStructureDefinitionJson(definition);
            writeToFile(path, structureDefinitionJson);
        } catch (IOException e) {
            System.err.println("Error: Generation of structure definition JSON file failed.");
            System.exit(-1);
        }
    }

    private static void writeDataJson(String path, JSONArray data) {
        try {
            writeToFile(path, data.toJSONString());
        } catch (IOException e) {
            System.err.println("Error: Generation of data JSON file failed.");
            System.exit(-1);
        }
    }

    private static void writeToFile(String path, String input) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(input);
        fileWriter.close();
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

    private static void printHelpMessage() {
        System.out.println("CSVonFHIR help:");
        System.out.println("  Required arguments:\n    -i [path]: Path to input CSV file\n    -t [string]: Type string\n    -m [path]: Path to FHIR mapping file");
        System.out.println("  Optional arguments:\n    -d [path]: Path to output data JSON file\n    -s [path]: Path to to output structure definition file");
    }
}
