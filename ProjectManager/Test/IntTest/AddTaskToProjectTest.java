/**
 * 
 */
package IntTest;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.io.File;
import java.util.Calendar;
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

import Helper.ReflectionHelper;
import application.ProjectManager;
import authentication.AuthenticationPanel;

import taskEditor.TaskEditorPanel;
import ui.UIRobot;

import dashboard.DashboardPanel;
import dataAccess.DatabaseManager;

/**
 * @author George Lambadas 7077076
 * 
 */
public class AddTaskToProjectTest {

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
	public void AddTaskTest() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
		String task_name = "New Task";
		
		JButton btn = ReflectionHelper.getElement("btnAddTask",ProjectEditorPanel.class,_pm);
		_app_thread.sleep(2000);
		btn.doClick();
		_app_thread.sleep(1000);
		ReflectionHelper.<JTextField>getElement("nameTextField",TaskEditorPanel.class,_pm, 1).setText("New Task");
		ReflectionHelper.<JTextField>getElement("expectedStartTextField",TaskEditorPanel.class,_pm, 1).setText("2000-01-01");
		ReflectionHelper.<JTextField>getElement("startTextField",TaskEditorPanel.class,_pm, 1).setText("2000-01-02");
		ReflectionHelper.<JTextField>getElement("expectedEndTextField",TaskEditorPanel.class,_pm, 1).setText("2000-01-03");
		ReflectionHelper.<JTextField>getElement("endTextField",TaskEditorPanel.class,_pm, 1).setText("2000-01-04");
		_app_thread.sleep(1000);
		ReflectionHelper.<JButton>getElement("btnSave",TaskEditorPanel.class,_pm, 1).doClick();
		_app_thread.sleep(1000);
		ReflectionHelper.<JButton>getElement("btnCloseTab",TaskEditorPanel.class,_pm, 1).doClick();
		_app_thread.sleep(3000);
		
		JTable jt = ReflectionHelper.getElement("table",ProjectEditorPanel.class,_pm);
		jt.setRowSelectionInterval(0, 0);
		String new_value = (String) jt.getModel().getValueAt(0, 0);
		assertEquals(task_name,new_value);
	}
}
