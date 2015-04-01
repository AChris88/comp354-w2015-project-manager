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

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ProjectManager();
			}
		});
	}
}
