package parser;

import csvmodel.Row;
import csvmodel.Table;
import org.json.simple.JSONObject;

public class CsvToJsonParser {

    // TODO REFACTOR
    public static JSONObject generateJSONFromCSV(Table table, String type) {

        /*
        JSONArray object = new JSONArray();
        for (Row row : table.getRows()) {
            JSONObject rowObject = new JSONObject();
            for (String key : table.getHeaders()) {
                rowObject.put(key, row.getAttribute(key));
            }
            object.add(rowObject);
        }
        */
        JSONObject object1 = new JSONObject();
        Row firstRow = table.getRows().get(0);
        object1.put("resourceType", type);
        for (String key : table.getHeaders()) {
            object1.put(key, firstRow.getAttribute(key));
        }


        return object1;
    }
}
