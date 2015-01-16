/**
 * 
 */
package application;


/**
 * @author George Lambadas 7077076
 *
 */
public class ProjectManagerApp {

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ProjectManager();
			}
		});
	}
}
