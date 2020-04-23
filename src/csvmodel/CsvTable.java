package csvmodel;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class CsvTable {
    private final List<String> headers;
    private final List<CsvRow> rows;

    /**
     * Generates an empty CsvTable prepared with a header for each column and ready to accept rows.
     *
     * @param headers A list of headers which represent the column headers used in the table
     */
    public CsvTable(List<String> headers) {
        this.headers = headers;
        this.rows = new LinkedList<>();
    }

    /**
     * Inserts a new row into the table.
     * Values inserted must correspond to the column headers defined previously.
     *
     * @param rowValues The values to insert with the new row
     */
    public void insertRow(List<String> rowValues) {
        if (rowValues.size() == this.headers.size()) {
            Hashtable<String, String> rowTable = new Hashtable<>();
            for (int i = 0; i < this.headers.size(); i++) {
                rowTable.put(this.headers.get(i), rowValues.get(i));
            }
            rows.add(new CsvRow(this, rowTable));
        } else {
            throw new IllegalArgumentException("Illegal amount of parameters to insert.");
        }
    }

    /**
     * @return The header row of the table, one entry for each column
     */
    public List<String> getHeaders() {
        return this.headers;
    }

    /**
     * @return All data rows currently contained in the table, without the header row
     */
    public List<CsvRow> getRows() {
        return this.rows;
    }
}
