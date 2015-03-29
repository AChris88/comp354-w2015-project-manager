/**
 * 
 */
package customComponents;

import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.table.AbstractTableModel;

import obj.User;

/**
 * @author George Lambadas 7077076
 *
 */
public class UserListModel extends AbstractTableModel {

	private static final long serialVersionUID = 5406896354133211296L;
	private ArrayList<User> data;
	private ArrayList<String> columnNames;
	
	public UserListModel() {
		columnNames = new ArrayList<String>();
		columnNames.add("Name");
		data = new ArrayList<User>();
	}
	
	public User getElementAt(int index) {
		return data.get(index);
	}

	public int getSize() {
		return data.size();
	}
	
	public ArrayList<User> getAllUsers() {
		return data;
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
	public Object getValueAt(int x, int y) {
		return data.get(x).getFirstName() + " " + data.get(x).getLastName();
	}

	/**
	 * @param u the user to be added
	 */
	public void addElement(User u) {		
		data.add(u);
	}
	
	public void populateModel(ArrayList<User> users) {
		this.data = users;
		this.fireTableDataChanged();
	}

	/**
	 * Method to add a users to the data being represented
	 * @param u the users to be added
	 */
	public void addUser(User u) {
		data.add(u);
	}
}
