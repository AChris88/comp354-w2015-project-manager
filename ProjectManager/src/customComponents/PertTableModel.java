/**
 * 
 */
package customComponents;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


/**
 * @author George Lambadas 7077076
 * 
 */
public class PertTableModel extends AbstractTableModel {

	private ArrayList<String> columnNames;
	private ArrayList<String[]> data;

	public PertTableModel() {
		this.columnNames = new ArrayList<String>();
		columnNames.add("Name");
		columnNames.add("Prerequisites");
		columnNames.add("Optimistic");
		columnNames.add("Most Likely");
		columnNames.add("Pessimistic");
		columnNames.add("Expected");
		columnNames.add("Standard Deviation");
		data = new ArrayList<String[]>();
	}

	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	@Override
	public String getColumnName(int column) {
		return columnNames.get(column);
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		return data.get(arg0)[arg1] != null ? data.get(arg0)[arg1] : "";
	}
	
	public void populateModel(ArrayList<String[]> data) {
		this.data = data;
	}

}
