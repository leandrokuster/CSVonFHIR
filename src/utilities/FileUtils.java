package utilities;

import org.hl7.fhir.r5.elementmodel.Element;
import org.hl7.fhir.r5.formats.IParser;
import org.hl7.fhir.r5.validation.ValidationEngine;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    public static void writeStringToFile(String path, String writeString) throws IOException {
        ensurePathExists(path);
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(writeString);
        fileWriter.close();
    }

    public static void writeJsonToFile(String path, JSONObject jsonObject) throws IOException {
        ensurePathExists(path);
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(jsonObject.toJSONString());
        fileWriter.close();
    }

    public static void writeElementToFile(String path, Element element, ValidationEngine validator) throws IOException {
        ensurePathExists(path);
        FileOutputStream outputStream = new FileOutputStream(path);
        new org.hl7.fhir.r5.elementmodel.JsonParser(validator.getContext()).compose(element, outputStream, IParser.OutputStyle.PRETTY, null);
        outputStream.close();
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    private static void ensurePathExists(String path) throws IOException {
        File file = new File(path);
        if (file.isDirectory()) {
            ensureDirectoryPathExists(file);
        } else {
            ensureFilePathExists(file);
        }
    }

    private static void ensureDirectoryPathExists(File directory) throws IOException {
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("Directory structure could not be established.");
            }
        }
    }

    private static void ensureFilePathExists(File file) throws IOException {
        if (!file.exists()) {
            String directoryPath = file.getAbsoluteFile().getParent();
            File directory = new File(directoryPath);
            ensureDirectoryPathExists(directory);
        }
    }
}
