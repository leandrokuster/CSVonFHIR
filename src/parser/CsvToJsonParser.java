package parser;

import csvmodel.Row;
import csvmodel.Table;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class CsvToJsonParser {
    public static JSONObject generateJSONFromRow(Table table, String resourceType, int rowIndex) {
        Row targetRow = table.getRows().get(rowIndex);
        JSONObject rowObject = new JSONObject();
        rowObject.put("resourceType", resourceType);
        for (String key : table.getHeaders()) {
            System.out.println(key);
            rowObject.put(key, targetRow.getAttribute(key));
        }
        return rowObject;
    }

    public static List<JSONObject> generateJSONFromCSV(Table table, String resourceType) {
        List<JSONObject> tableList = new LinkedList<>();
        for (int i = 0; i < table.getRows().size(); i++) {
            tableList.add(generateJSONFromRow(table, resourceType, i));
        }
        return tableList;
    }
}
