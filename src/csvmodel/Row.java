package csvmodel;

import java.util.Hashtable;

public class Row {
    private Table table;
    private Hashtable<String, String> rowMap;

    public Row(Table table, Hashtable<String, String> rowMap) {
        this.table = table;
        this.rowMap = rowMap;
    }

    public String getAttribute(String header) {
        if (table.getHeaders().contains(header)) {
            return rowMap.get(header);
        } else {
            throw new IllegalArgumentException("Cannot get attribute without valid table header.");
        }
    }

    public void putAttribute(String header, String attribute) {
        if (table.getHeaders().contains(header)) {
            rowMap.put(header, attribute);
        } else {
            throw new IllegalArgumentException("Cannot insert attribute without valid table header.");
        }
    }
}
