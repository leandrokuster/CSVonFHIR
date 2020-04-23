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
    /**
     * Writes a string to a file at a certain path. Checks beforehand if the path exists and creates it if it doesn't.
     *
     * @param path        The path to write to.
     * @param writeString The string to write into the file.
     * @throws IOException Thrown if the write process fails.
     */
    public static void writeStringToFile(String path, String writeString) throws IOException {
        ensurePathExists(path);
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(writeString);
        fileWriter.close();
    }

    /**
     * Writes a JSONObject to a file at a certain path. Checks beforehand if the path exists and creates it if it doesn't.
     *
     * @param path       The path to write to.
     * @param jsonObject The JSONObject to write into the file.
     * @throws IOException Thrown if the write process fails.
     */
    public static void writeJsonToFile(String path, JSONObject jsonObject) throws IOException {
        ensurePathExists(path);
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(jsonObject.toJSONString());
        fileWriter.close();
    }

    /**
     * Writes a FHIR element as JSON to a file at a certain path. Checks beforehand if the path exists and creates it if it doesn't.
     *
     * @param path      The path to write to.
     * @param element   The JSONObject to write into the file.
     * @param validator The validator needed to parse the element to writeable JSON.
     * @throws IOException Thrown if the write process fails.
     */
    public static void writeElementToFile(String path, Element element, ValidationEngine validator) throws IOException {
        ensurePathExists(path);
        FileOutputStream outputStream = new FileOutputStream(path);
        new org.hl7.fhir.r5.elementmodel.JsonParser(validator.getContext()).compose(element, outputStream, IParser.OutputStyle.PRETTY, null);
        outputStream.close();
    }

    /**
     * Deletes a file at a certain path.
     *
     * @param path The path to delete the file at.
     * @return A boolean indicating if the operation took place or not.
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    /**
     * Ensures a given path exists (which means it is created if it doesn't already exist).
     *
     * @param path The path to make sure of it exists.
     * @throws IOException Thrown if the path doesn't exist and couldn't be created.
     */
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
