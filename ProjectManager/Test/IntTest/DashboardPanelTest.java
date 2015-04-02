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
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Date;
import org.junit.*;

import customComponents.ProjectTableModel;

import projectEditor.ProjectEditorPanel;

import ui.UIRobot;
import userEditor.UserEditorPanel;

import javax.swing.*;

public class DashboardPanelTest {

	private DatabaseManager _dbm;
	private Project _project;
	private JFrame _frame;
	private Thread _app_thread;
	private UIRobot _bot;
	private ProjectManager _pm;

	@Before
	public void testSetup() throws NoSuchFieldException,
			IllegalAccessException, AWTException, InterruptedException {

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

		ReflectionHelper.<JButton> getElement("loginButton",
				AuthenticationPanel.class, _pm).doClick();

	}

	@After
	public void TearDown() {
		_pm.exit();
	}

	@Test
	public void testOpenProject() throws NoSuchFieldException,
			IllegalAccessException, InterruptedException {
		JTable jt = ReflectionHelper.getElement("table", DashboardPanel.class,
				_pm);

		jt.setRowSelectionInterval(0, 0);
		ProjectTableModel model = ReflectionHelper.getElement("model",
				DashboardPanel.class, _pm);
		Project p = model.getProjectAt(0);
		_app_thread.sleep(2000);
		((DashboardPanel) _pm.getActivePanel()).openCurrentSelectedProject();

		assertTrue(((JTabbedPane) _pm.getActivePanel()).getComponentAt(0) instanceof ProjectEditorPanel);

		assertEquals(p.getId(), _pm.currentProject.getId());

	}

	@Test
	public void testCreateUser() throws NoSuchFieldException,
			IllegalAccessException, InterruptedException {
		ReflectionHelper.<JButton> getElement("btnNewUser",
				DashboardPanel.class, _pm).doClick();

		_app_thread.sleep(1000);

		assertTrue(((JTabbedPane) _pm.getActivePanel()).getComponentAt(0) instanceof UserEditorPanel);

		String username = "newusername";
		String password = "newpassword";
		ReflectionHelper.<JTextField> getElement("txtFirstName",
				UserEditorPanel.class, _pm).setText("New");
		ReflectionHelper.<JTextField> getElement("txtLastName",
				UserEditorPanel.class, _pm).setText("User");
		ReflectionHelper.<JTextField> getElement("txtUserName",
				UserEditorPanel.class, _pm).setText(username);
		ReflectionHelper.<JPasswordField> getElement("passwordField",
				UserEditorPanel.class, _pm).setText(password);

		_app_thread.sleep(1000);

		ReflectionHelper.<JButton> getElement("btnSave", UserEditorPanel.class,
				_pm).doClick();

		User u = _pm.db.login(username, password);
		assertTrue(u != null);
		assertEquals(username, u.getUsername());
	}

	@Test
	public void testCreateProject() throws NoSuchFieldException,
			IllegalAccessException, InterruptedException {
		int numberOfProjects = ReflectionHelper.<ProjectTableModel> getElement(
				"model", DashboardPanel.class, _pm).getRowCount();

		ReflectionHelper.<JButton> getElement("btnNewProject",
				DashboardPanel.class, _pm).doClick();

		_app_thread.sleep(1000);

		assertTrue(((JTabbedPane) _pm.getActivePanel()).getComponentAt(0) instanceof ProjectEditorPanel);

		ReflectionHelper.<JTextField> getElement("txtProjectName",
				ProjectEditorPanel.class, _pm).setText("Newest Project");
		ReflectionHelper.<JTextField> getElement("txtStartDate",
				ProjectEditorPanel.class, _pm).setText("2015-01-01");
		ReflectionHelper.<JTextField> getElement("txtProjectedEndDate",
				ProjectEditorPanel.class, _pm).setText("2015-01-02");
		ReflectionHelper.<JTextField> getElement("txtActualEndDate",
				ProjectEditorPanel.class, _pm).setText("2015-01-03");

		_app_thread.sleep(1000);

		ReflectionHelper.<JButton> getElement("btnSave", ProjectEditorPanel.class,
				_pm).doClick();

		_app_thread.sleep(1000);

		ReflectionHelper.<JButton> getElement("btnCloseTab",
				ProjectEditorPanel.class, _pm).doClick();
		_app_thread.sleep(1000);

		assertEquals(
				numberOfProjects + 1,
				ReflectionHelper.<ProjectTableModel> getElement("model",
						DashboardPanel.class, _pm).getRowCount());
		assertEquals(
				"Newest Project",
				ReflectionHelper.<ProjectTableModel> getElement("model",
						DashboardPanel.class, _pm).getProjectAt(
						numberOfProjects).getName());
	}

}
