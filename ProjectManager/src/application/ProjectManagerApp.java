/**
 * 
 */
package application;

import dataAccess.DatabaseManager;
import obj.User;

/**
 * @author George Lambadas 7077076
 * 
 */
public class ProjectManagerApp {
	public static void main(String[] args) {

		DatabaseManager dbm = new DatabaseManager();
		
//		dbm.insertUser(new User(0, "admin", "admin", "admin", 1), "password");
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ProjectManager();
			}
		});
	}
}
