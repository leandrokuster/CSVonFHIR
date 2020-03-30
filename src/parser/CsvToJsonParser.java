package parser;

import csvmodel.CsvRow;
import csvmodel.CsvTable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CsvToJsonParser {
    public static JSONObject generateJSONFromRow(CsvTable table, String resourceType, int rowIndex) {
        CsvRow targetRow = table.getRows().get(rowIndex);
        JSONObject rowObject = new JSONObject();
        rowObject.put("resourceType", resourceType);
        for (String key : table.getHeaders()) {
            rowObject.put(key, targetRow.getColumnValue(key));
        }
        return rowObject;
    }

    public static JSONArray generateJSONFromCSV(CsvTable table, String resourceType) {
        JSONArray tableArray = new JSONArray();
        for (int i = 0; i < table.getRows().size(); i++) {
            tableArray.add(generateJSONFromRow(table, resourceType, i));
        }
        return tableArray;
    }
}
