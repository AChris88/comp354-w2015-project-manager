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

	private ArrayList<String> columnNames;
	private ArrayList<Project> data;

	public ProjectTableModel() {
		this.columnNames = new ArrayList<String>();
		columnNames.add("Name");
		columnNames.add("Projected End Date");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	@Override
	public String getColumnName(int column) {
		return columnNames.get(column);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return data.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
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

	public void populateModel(ArrayList<Project> projects) {
		this.data = projects;
	}

	public Project getProjectAt(int index) {
		return data.get(index);
	}

}
