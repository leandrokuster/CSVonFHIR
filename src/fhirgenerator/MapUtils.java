package fhirgenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapUtils {
    public static String getMapUrl(String mapPath) throws IOException, IllegalStateException {
        String line = findLineContainingUrl(mapPath);
        return getMapUrlFromLine(line);
    }

    private static String findLineContainingUrl(String mapPath) throws IOException, IllegalStateException {
        BufferedReader reader = new BufferedReader(new FileReader(mapPath));
        String line = reader.readLine();
        while (line != null) {
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
