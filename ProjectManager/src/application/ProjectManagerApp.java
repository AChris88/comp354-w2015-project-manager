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
        User u = new User(0,"test1","test1","test1",1);
        dbm.insertUser(u,"test1");

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ProjectManager();
			}
		});
	}
}
