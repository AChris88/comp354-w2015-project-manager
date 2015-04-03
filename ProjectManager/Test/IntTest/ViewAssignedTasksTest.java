package IntTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.AWTException;
import java.io.File;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;

import customComponents.PertTableModel;
import obj.Project;
import obj.Task;
import obj.User;
import obj.UserTask;
import projectEditor.PertTablePanel;
import projectEditor.ProjectEditorPanel;
import taskEditor.ViewTaskPanel;
import ui.UIRobot;
import Helper.ReflectionHelper;
import application.ProjectManager;
import authentication.AuthenticationPanel;
import dashboard.DashboardPanel;
import dataAccess.DatabaseManager;

public class ViewAssignedTasksTest {

    private ProjectManager _pm;
    private Thread _app_thread;
    private UIRobot _bot;
    private DatabaseManager _dbm;
    private Project _project;
    private Task _task;
    private UserTask _userTask;
    private User pMan;
	
    @Before
	public void testSetup() throws NoSuchFieldException, IllegalAccessException, AWTException {

        _bot = new UIRobot();
		
		Thread botThread = new Thread(_bot);

		File testDbFile = new File("itTest.db");
		if (testDbFile.exists()) {
			testDbFile.delete();
		}

		_dbm = new DatabaseManager("itTest.db");

		_project = new Project(1, "Integration test project", new Date(),
				new Date(), new Date());

		_task = new Task(1, 1, "Integration test task", null, null, null, null, 1);
		
		_userTask = new UserTask(0, 1, 1, 1);

		pMan = new User(1, "PManager", "Manager", "mananger", 1);

		_dbm.insertUser(pMan, "pwd");
		_dbm.insertProject(_project, pMan);
		_dbm.insertTask(_task);
		_dbm.insertUserTask(_userTask);
		
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
	public void testViewAssignedTasks() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
		_app_thread.sleep(2000);
		JButton btn = ReflectionHelper.getElement("btnViewTask",ProjectEditorPanel.class,_pm);
		btn.doClick();
		_app_thread.sleep(2000);
		JTable table = ReflectionHelper.getElement("table", ViewTaskPanel.class,_pm,1);
		assertEquals(_pm.db.getTasksForUser(pMan).size(), table.getRowCount());
		_app_thread.sleep(5000);
	}
}
