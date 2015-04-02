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
		data = new ArrayList<Task>();
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
		return row < data.size() ? data.get(row) : null;
	}

	/**
	 * Method to remove a task at a specified row
	 * @param row the row from which to remove a task
	 * @return the task that was removed. Null if empty row selected.
	 */
	public Task removeTaskAt(int row) {
		return data.size() > row? data.remove(row) : null;
	}

	/**
	 * Method to add a task to the data being represented
	 * @param t the task to be added
	 */
	public void addTask(Task t) {
		data.add(t);
	}

	/**
	 * Method to get all of the tasks currently in the model.
	 * @return list of tasks in the model
	 */
	public ArrayList<Task> getAllTasks() {
		return data;
	}
	
	public void removeTask(Task t) {
		for(int i = 0; i < data.size(); i++) {
			if (data.get(i).getId() == t.getId()) {
				data.remove(i);
				i = data.size();
			}
		}
	}

}
