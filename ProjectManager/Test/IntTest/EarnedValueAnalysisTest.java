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

import obj.Project;
import obj.Task;
import obj.User;
import obj.UserTask;

import org.junit.Before;
import org.junit.Test;

import projectEditor.ProjectEditorPanel;
import taskEditor.ViewTaskPanel;
import ui.UIRobot;
import util.ProjectAnalysisUtil;
import Helper.ReflectionHelper;
import application.ProjectManager;
import authentication.AuthenticationPanel;
import dashboard.DashboardPanel;
import dataAccess.DatabaseManager;

public class EarnedValueAnalysisTest {
	private ProjectManager _pm;
    private Thread _app_thread;
    private UIRobot _bot;
    private DatabaseManager _dbm;
    private Project _project;
    private Task _task;
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

		_task = new Task(1, 1, "Integration test task", new Date(), new Date(), new Date(), new Date(), 20);
		
		pMan = new User(1, "PManager", "Manager", "mananger", 1);

		_dbm.insertUser(pMan, "pwd");
		_dbm.insertProject(_project, pMan);
		_dbm.insertTask(_task);
		
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
	public void testEarnedValueAnalysis() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
		long beforeTime = System.currentTimeMillis();
		ProjectAnalysisUtil util = new ProjectAnalysisUtil(_project);
        ReflectionHelper.<JButton>getElement("btnEarnedValueAnalysis", ProjectEditorPanel.class, _pm).doClick();
        assertEquals(9,((ProjectEditorPanel)((JTabbedPane)_pm.getActivePanel()).getComponent(0)).evaValForTest[0]);
        assertTrue("If the user had to click ok, it'll take time to come back",System.currentTimeMillis() > 1000 );
	}
}
