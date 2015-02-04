/**
 * 
 */
package customComponents;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import obj.Task;

/**
 * @author George Lambadas 7077076
 * 
 */
public class TaskTableModel extends AbstractTableModel {

	private ArrayList<String> columnNames;
	private ArrayList<Task> data;

	public TaskTableModel() {
		this.columnNames = new ArrayList<String>();
		columnNames.add("Task Name");
		columnNames.add("Projected End Date");
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
	 * @param tasks
	 *            tasks to be displayed in the table
	 */
	public void populateModel(ArrayList<Task> tasks) {
		this.data = tasks;
	}

	/**
	 * @param row
	 *            index of row
	 * @return task at row
	 */
	public Task getTaskAt(int row) {
		return data.get(row);
	}

	public Task removeTaskAt(int row) {
		return data.remove(row);
	}

	public void addTask(Task t) {
		data.add(t);
	}

	public ArrayList<Task> getAllTasks() {
		return data;
	}

}
