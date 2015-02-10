/**
 * 
 */
package customComponents;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import obj.Project;

/**
 * @author George Lambadas 7077076
 * 
 */
public class ProjectTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 423434553990284961L;
	private ArrayList<String> columnNames;
	private ArrayList<Project> data;

	public ProjectTableModel() {
		this.columnNames = new ArrayList<String>();
		columnNames.add("Name");
		columnNames.add("Projected End Date");
		data = new ArrayList<Project>();
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
		switch (arg1) {
		case 0:
			return data.get(arg0).getName();
		case 1:
			return data.get(arg0).getProjectedEndDate();
		default:
			return null;
		}
	}

	/**
	 * Method to set the data to be represented in the model
	 * @param projects the projects to be represented
	 */
	public void populateModel(ArrayList<Project> projects) {
		this.data = projects;
	}

	/**
	 * Method to return the project at a given row index
	 * @param index the index that was selected
	 * @return The project at that index. Null if an empty row was selected
	 */
	public Project getProjectAt(int index) {
		return index < data.size() ? data.get(index) : null;
	}

}
