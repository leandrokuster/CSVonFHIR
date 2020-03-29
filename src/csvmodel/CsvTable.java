package csvmodel;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class CsvTable {
    private List<String> headers;
    private List<CsvRow> rows;

    public CsvTable() {
        this.headers = new ArrayList<>();
        this.rows = new LinkedList<>();
    }

    public CsvTable(List<String> headers) {
        this.headers = headers;
        this.rows = new LinkedList<>();
    }

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

    public List<String> getHeaders() {
        return this.headers;
    }

    public List<CsvRow> getRows() {
        return this.rows;
    }
}
