package csvmodel;

import java.util.Hashtable;

public class CsvRow {
    private CsvTable table;
    private Hashtable<String, String> rowMap;

    /**
     * Generates a new row to include in a CsvTable.
     *
     * @param table  The table to insert the row in
     * @param rowMap A map of the column headers to the row data contents
     */
    public CsvRow(CsvTable table, Hashtable<String, String> rowMap) {
        this.table = table;
        this.rowMap = rowMap;
    }

    /**
     * Retrieves data from a certain column in the row.
     *
     * @param header The column header for the attribute
     * @return The data in the given column
     */
    public String getAttribute(String header) {
        if (table.getHeaders().contains(header)) {
            return rowMap.get(header);
        } else {
            throw new IllegalArgumentException("Cannot get attribute without valid table header.");
        }
    }

    /**
     * Stores a piece of data in a certain column in the row, replacing any current entry.
     *
     * @param header    The column header to store the data in
     * @param attribute The data to store in the given column
     */
    public void putAttribute(String header, String attribute) {
        if (table.getHeaders().contains(header)) {
            rowMap.put(header, attribute);
        } else {
            throw new IllegalArgumentException("Cannot insert attribute without valid table header.");
        }
    }
}
