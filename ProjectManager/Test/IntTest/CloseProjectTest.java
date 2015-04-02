/**
 * 
 */
package IntTest;

import java.awt.AWTException;
import java.io.File;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;

import obj.Project;
import obj.User;

import org.junit.Before;
import org.junit.Test;

import projectEditor.ProjectEditorPanel;

import taskEditor.TaskEditorPanel;
import ui.UIRobot;
import Helper.ReflectionHelper;
import application.ProjectManager;
import authentication.AuthenticationPanel;
import dashboard.DashboardPanel;
import dataAccess.DatabaseManager;

/**
 * @author George Lambadas 7077076
 *
 */
public class CloseProjectTest {

	private DatabaseManager _dbm;
	private Project _project;
	private JFrame _frame;
	private Thread _app_thread;
	private UIRobot _bot;
	private ProjectManager _pm;

	@Before
	public void testSetup() throws NoSuchFieldException, IllegalAccessException, AWTException {

        _bot = new UIRobot();
		
		Thread botThread = new Thread(_bot);

		File testDbFile = new File("itTest.db");
		if (testDbFile.exists()) {
			testDbFile.delete();
		}

		_dbm = new DatabaseManager("itTest.db");

		_project = new Project(0, "Integration test project", new Date(),
				new Date(), new Date());

		User pMan = new User(0, "PManager", "Manager", "mananger", 1);

		_dbm.insertUser(pMan, "pwd");
		_dbm.insertProject(_project, pMan);

		Runnable app = (Runnable) new ProjectManager("itTest.db");

		_pm = (ProjectManager) app;

		_app_thread = new Thread(app);

		try {
			_app_thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		ReflectionHelper.<JTextField> getElement("usernameField",
				AuthenticationPanel.class, _pm).setText("mananger");
		ReflectionHelper.<JPasswordField> getElement("passwordField",
				AuthenticationPanel.class, _pm).setText("pwd");
		
		ReflectionHelper.<JButton>getElement("loginButton", AuthenticationPanel.class, _pm).doClick();
		
        JTable jt = ReflectionHelper.getElement("table",DashboardPanel.class,_pm);
        
        jt.setRowSelectionInterval(0, 0);
        ((DashboardPanel) _pm.getActivePanel()).openCurrentSelectedProject();
	}
	
	@Test
	public void CloseProjectTest() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
		_app_thread.sleep(1000);
		JButton btn = ReflectionHelper.getElement("btnCloseTab",ProjectEditorPanel.class,_pm);
		_app_thread.sleep(2000);
		btn.doClick();
		_app_thread.sleep(1000);	
	}
}