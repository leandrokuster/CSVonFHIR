package parser;

import csvmodel.Row;
import csvmodel.Table;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CsvToJsonParser {

    public static JSONArray generateJSONFromCSV(Table table) {
        JSONArray object = new JSONArray();
        for (Row row : table.getRows()) {
            JSONObject rowObject = new JSONObject();
            for (String key : table.getHeaders()) {
                rowObject.put(key, row.getAttribute(key));
            }
            object.add(rowObject);
        }
        return object;
    }
}
