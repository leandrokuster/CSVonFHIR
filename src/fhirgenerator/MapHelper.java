package fhirgenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapHelper {
    /**
     * Scans a map file at a given path for the contained URL which designates the map and returns that URL.
     *
     * @param mapPath The path to the map file.
     * @return The URL used to designate the map in the map file.
     * @throws IOException           Thrown if a file operation on the map file fails.
     * @throws IllegalStateException Thrown if the map file cannot provide the desired information.
     */
    public static String getMapUrl(String mapPath) throws IOException, IllegalStateException {
        String line = findLineContainingUrl(mapPath);
        return getMapUrlFromLine(line);
    }

    private static String findLineContainingUrl(String mapPath) throws IOException, IllegalStateException {
        BufferedReader reader = new BufferedReader(new FileReader(mapPath));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("map")) {
                return line;
            }
        }
        throw new IllegalStateException();
    }

    private static String getMapUrlFromLine(String line) throws IllegalStateException {
        Pattern pattern = Pattern.compile("\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String url = matcher.group();
            return url.replace("\"", "");
        }
        throw new IllegalStateException();
    }
}
