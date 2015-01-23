/**
 * 
 */
package application;

import da.DatabaseManager;


/**
 * @author George Lambadas 7077076
 *
 */
public class ProjectManagerApp {

	public static void main(String[] args) {
		DatabaseManager dbm = new DatabaseManager();
		dbm.connect();
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ProjectManager();
			}
		});
	}
}
