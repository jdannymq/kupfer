package helper;

import com.sap.conn.jco.JCoTable;
/**
 *
 * @author aruizp
 */
public class TableAdapterReader {
	protected JCoTable table;

	public TableAdapterReader(JCoTable table) {
		this.table = table;
	}

	public String get(String s) {
		return table.getValue(s).toString();
	}

	public Boolean getBoolean(String s) {
		String value = table.getValue(s).toString();
		return value.equals("X");
	}

	public String getMessage() {
		return table.getString("MESSAGE");
	}

	public int size() {
		return table.getNumRows();
	}

	public void next() {
		table.nextRow();
	}
}

