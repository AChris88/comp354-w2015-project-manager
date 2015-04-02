package IntTest;

import Helper.ReflectionHelper;
import application.ProjectManager;
import authentication.AuthenticationPanel;
import dashboard.DashboardPanel;
import dataAccess.DatabaseManager;
import obj.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Date;
import org.junit.*;

import ui.UIRobot;

import javax.swing.*;


public class DashboardPanelTest {

	private DatabaseManager _dbm;
	private Project _project;
	private JFrame _frame;
	private Thread _app_thread;
	private UIRobot _bot;
	private ProjectManager _pm;

	@Before
	public void testSetup() throws NoSuchFieldException, IllegalAccessException, AWTException, InterruptedException {

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
        _app_thread.sleep(3000);
        ((DashboardPanel) _pm.getActivePanel()).openCurrentSelectedProject();

	}
	
	@After
    public void TearDown(){
        _pm.exit();
    }
	
	@Test
	public void testOpenProject() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
		JTable jt = ReflectionHelper.getElement("table",DashboardPanel.class,_pm);
        
        jt.setRowSelectionInterval(0, 0);
        _app_thread.sleep(3000);
        ((DashboardPanel) _pm.getActivePanel()).openCurrentSelectedProject();
        JTabbedPane pane;
        assertTrue(((JTabbedPane)_pm.getActivePanel()).getComponentAt(0) instanceof ProjectEditorPanel);
		
	}

}
