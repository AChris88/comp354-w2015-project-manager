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
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import obj.Project;
import obj.Task;
import obj.TaskRequirement;
import obj.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import customComponents.PertTableModel;

import projectEditor.PertTablePanel;
import projectEditor.ProjectEditorPanel;

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
public class PertAnalysisTest {
	private DatabaseManager _dbm;
	private Project _project;
	private JFrame _frame;
	private Thread _app_thread;
	private UIRobot _bot;
	private ProjectManager _pm;

	@Before
	public void testSetup() throws NoSuchFieldException,
			IllegalAccessException, AWTException {

		_bot = new UIRobot();

		Thread botThread = new Thread(_bot);

		File testDbFile = new File("itTest.db");
		if (testDbFile.exists()) {
			testDbFile.delete();
		}

		_dbm = new DatabaseManager("itTest.db");

		_project = new Project(0, "Integration test project", date(1, 1, 2015),
				date(1, 1, 2015), date(1, 1, 2015));

		User pMan = new User(0, "PManager", "Manager", "mananger", 1);

		_dbm.insertUser(pMan, "pwd");
		_dbm.insertProject(_project, pMan);

		int task1, task2, task3;
		Task temp = new Task(-1, _project.getId(), "Task 1", date(1, 1, 2015),
				date(1, 1, 2015), date(2, 1, 2015), date(2, 1, 2015), 10);
		
		_dbm.insertTask(temp);
		task1 = temp.getId();
		
		temp = new Task(-1, _project.getId(), "Task 2", date(1, 1, 2015),
				date(1, 1, 2015), date(3, 1, 2015), date(3, 1, 2015), 20);
		
		_dbm.insertTask(temp);
		task2 = temp.getId();
		
		temp = new Task(-1, _project.getId(), "Task 3", date(1, 1, 2015),
				date(1, 1, 2015), date(4, 1, 2015), date(4, 1, 2015), 5);
		
		_dbm.insertTask(temp);
		task3 = temp.getId();
		
		_dbm.insertTaskRequirement(new TaskRequirement(-1, task2, task1));
		_dbm.insertTaskRequirement(new TaskRequirement(-1, task3, task1));
		_dbm.insertTaskRequirement(new TaskRequirement(-1, task3, task3));

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

		ReflectionHelper.<JButton> getElement("loginButton",
				AuthenticationPanel.class, _pm).doClick();

		JTable jt = ReflectionHelper.getElement("table", DashboardPanel.class,
				_pm);

		jt.setRowSelectionInterval(0, 0);
		((DashboardPanel) _pm.getActivePanel()).openCurrentSelectedProject();

	}

	@After
	public void TearDown() {
		_pm.exit();
	}

	@Test
	public void testPertAnalysis() throws NoSuchFieldException,
			IllegalAccessException, InterruptedException {
		_app_thread.sleep(2000);
		JButton btn = ReflectionHelper.getElement("btnPertAnalysis",
				ProjectEditorPanel.class, _pm);
		btn.doClick();
		assertTrue(((JTabbedPane) _pm.getActivePanel()).getComponentAt(1) instanceof PertTablePanel);
		_app_thread.sleep(2000);
		PertTableModel model = ReflectionHelper.getElement("tableModel",
				PertTablePanel.class, _pm, 1);
		assertEquals(_pm.db.getTasksForProject(_pm.currentProject).size(),
				model.getRowCount());
	}
	
	private static Date date(final int day, final int month, final int year) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        final Date result = calendar.getTime();
        return result;

    }
}
