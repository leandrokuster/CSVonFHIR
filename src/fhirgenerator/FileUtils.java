package fhirgenerator;

import org.hl7.fhir.r5.elementmodel.Element;
import org.hl7.fhir.r5.formats.IParser;
import org.hl7.fhir.r5.validation.ValidationEngine;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    public static void ensurePathExists(String folderPath) throws IOException {
        File path = new File(folderPath);
        if (!path.exists()) {
            if (path.mkdirs()) {
                return;
            } else {
                throw new IOException("Folder structure could not be established.");
            }
        }
    }

    public static void writeJsonToFile(String path, JSONObject jsonObject) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(jsonObject.toJSONString());
        fileWriter.close();
    }

    public static void writeElementToFile(String path, Element element, ValidationEngine validator) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(path);
        new org.hl7.fhir.r5.elementmodel.JsonParser(validator.getContext()).compose(element, outputStream, IParser.OutputStyle.PRETTY, null);
        outputStream.close();
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }
}
