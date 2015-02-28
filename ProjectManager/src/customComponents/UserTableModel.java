/**
 * 
 */
package customComponents;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import obj.User;

/**
 * @author Philippe GENOIS 6527426
 * 
 */
public class UserTableModel extends AbstractTableModel {

	private ArrayList<String> columnNames;
	private ArrayList<User> data;

	public UserTableModel() {
		this.columnNames = new ArrayList<String>();
		columnNames.add("User Name");
		columnNames.add("User Role");
		data = new ArrayList<User>();
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
			return data.get(arg0).getFirstName() + " " + data.get(arg0).getLastName();
		case 1:
			return data.get(arg0).getRole();
		default:
			return null;
		}
	}

	/**
	 * @param users to be displayed in the table
	 */
	public void populateModel(ArrayList<User> users) {
		this.data = users;
	}

	/**
	 * @param index of row
	 * @return user at row
	 */
	public User getUserAt(int row) {
		return row < data.size() ? data.get(row) : null;
	}

	/**
	 * Method to remove an user at a specified row
	 * @param row the row from which to remove an user
	 * @return the user that was removed. Null if empty row selected.
	 */
	public User removeUserAt(int row) {
		return data.size() > row? data.remove(row) : null;
	}

	/**
	 * Method to add a users to the data being represented
	 * @param u the users to be added
	 */
	public void addUser(User u) {
		data.add(u);
	}

	/**
	 * Method to get all of the users currently in the model.
	 * @return list of users in the model
	 */
	public ArrayList<User> getAllUsers() {
		return data;
	}

}
