/**
 * Created by Sam on 2015-03-11.
 */

package IntTest;

import application.ProjectManager;
import authentication.AuthenticationPanel;

import dashboard.DashboardPanel;
import dataAccess.DatabaseManager;
import obj.Project;
import obj.ProjectUser;
import obj.Task;
import obj.User;
import obj.UserTask;

import org.junit.*;

import projectEditor.ProjectEditorPanel;
import taskEditor.ViewTaskPanel;
import ui.UIRobot;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assume.*;
import static org.junit.Assert.*;

public class MemberModuleTest {

    private ProjectManager _pm;

    @Before
    public void setup() throws AWTException, NoSuchFieldException, IllegalAccessException {

//        UIRobot bot = new UIRobot();
        String dbFile = "ITtestdb.db";
        File testDbFile = new File(dbFile);
        if (testDbFile.exists()) {
            testDbFile.delete();
        }

        Runnable app = (Runnable) new ProjectManager(dbFile);

        DatabaseManager dbm = new DatabaseManager(dbFile);
        User u = new User(0, "test", "test", "test", 0);
        dbm.insertUser(u, "test");

        Project p = new Project(0, "testp", new Date(), new Date(), new Date());
        dbm.insertProject(p, u);

        ProjectUser pu = dbm.getProjectUsers().get(0);

        Task taskToAdd = new Task(0, p.getId(), "task", new Date(),
                new Date(), new Date(), new Date(), 0);

        dbm.insertTask(taskToAdd);

        UserTask ut = new UserTask(0, u.getId(), taskToAdd.getId(), pu.getId());
        dbm.insertUserTask(ut);

        _pm = (ProjectManager) app;


//        Thread botThread = new Thread(bot);
        Thread appThread = new Thread(app);

        try {
            appThread.start();
//            botThread.start();

//            bot.login();

//            Thread.sleep(1500);
//            Field f = AuthenticationPanel.class.getDeclaredField("loginButton");
//            f.setAccessible(true);
//            Object o = ((ProjectManager)app).getComponent(0);
//            JButton test = (JButton) f.get(((ProjectManager)app).getActivePanel());
//
//            test.doClick();
//            Thread.sleep(15000);
//
//            bot.selectProject();
//
//            Thread.sleep(500);

//            bot.clickGanttButton();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @After
    public void tearDown() throws InterruptedException {
        _pm.exit();
        Thread.sleep(1000);
    }

    @Test
    public void LimitedDashboardTest() throws NoSuchFieldException, IllegalAccessException, InterruptedException {

        Class temp = AuthenticationPanel.class;

        getTF(temp, "usernameField").setText("test");
        getPF(temp, "passwordField").setText("test");

        getButton(AuthenticationPanel.class, "loginButton").doClick();

        temp = DashboardPanel.class;

        assertNull(getButton(temp, "btnNewUser"));
        assertNull(getButton(temp, "btnNewProject"));

//
//
//        Thread.sleep(15000);

    }

    @Test
    public void LimitedProjectEditor() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        Class temp = AuthenticationPanel.class;

        getTF(temp, "usernameField").setText("test");
        getPF(temp, "passwordField").setText("test");

        getButton(AuthenticationPanel.class, "loginButton").doClick();

        temp = DashboardPanel.class;

        JTable jt = getTable(temp, "table");

        jt.setRowSelectionInterval(0, 0);

        ((DashboardPanel) _pm.getActivePanel()).openCurrentSelectedProject();


//        JButton btnAddU = getButton(ProjectEditorPanel.class,"btnAddUser");

        assertFalse(getButton(ProjectEditorPanel.class, "btnAddUser").isVisible());
        assertFalse(getButton(ProjectEditorPanel.class, "btnSave").isVisible());
        assertFalse(getButton(ProjectEditorPanel.class, "btnAddTask").isVisible());
        assertFalse(getButton(ProjectEditorPanel.class, "btnDeleteProject").isVisible());
        assertFalse(getButton(ProjectEditorPanel.class, "btnCreateGANTTChart").isVisible());

        assertFalse(getTF(ProjectEditorPanel.class, "txtProjectName").isVisible());
        assertFalse(getTF(ProjectEditorPanel.class, "txtStartDate").isVisible());
        assertFalse(getTF(ProjectEditorPanel.class, "txtProjectedEndDate").isVisible());
        assertFalse(getTF(ProjectEditorPanel.class, "txtActualEndDate").isVisible());


        assertFalse(getTable(ProjectEditorPanel.class, "table").isVisible());


    }

    @Test
    public void LimitedTaskViewer() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        Class temp = AuthenticationPanel.class;

        getTF(temp, "usernameField").setText("test");
        getPF(temp, "passwordField").setText("test");

        getButton(AuthenticationPanel.class, "loginButton").doClick();

        temp = DashboardPanel.class;

        JTable jt = getTable(temp, "table");

        jt.setRowSelectionInterval(0, 0);

        ((DashboardPanel) _pm.getActivePanel()).openCurrentSelectedProject();


//        JButton btnAddU = getButton(ProjectEditorPanel.class,"btnAddUser");


        getButton(ProjectEditorPanel.class, "btnViewTask").doClick();


//        Thread.sleep(15000);

        JTable jt2 = getTable2(ViewTaskPanel.class, "table", 1);

        jt2.setRowSelectionInterval(0, 0);

        assertEquals(0, jt2.getSelectedRow());


    }

    private JTable getTable2(Class c, String t, int i) throws NoSuchFieldException, IllegalAccessException {

        Field f = c.getDeclaredField(t);
        f.setAccessible(true);
        JTable test;
        if (_pm.getActivePanel() instanceof JTabbedPane) {
            test = (JTable) f.get(((JTabbedPane) _pm.getActivePanel()).getComponent(i));
        } else {
            test = (JTable) f.get(_pm.getActivePanel());

        }
        return test;

    }

    private JButton getButton(Class c, String button) throws NoSuchFieldException, IllegalAccessException {

        Field f = c.getDeclaredField(button);
        f.setAccessible(true);
        JButton test;
        if (_pm.getActivePanel() instanceof JTabbedPane) {
            test = (JButton) f.get(((JTabbedPane) _pm.getActivePanel()).getComponent(0));
        } else {
            test = (JButton) f.get(_pm.getActivePanel());

        }
        return test;

    }

    private JTextField getTF(Class c, String tf) throws NoSuchFieldException, IllegalAccessException {

        Field f = c.getDeclaredField(tf);
        f.setAccessible(true);
        JTextField test;
        if (_pm.getActivePanel() instanceof JTabbedPane) {
            test = (JTextField) f.get(((JTabbedPane) _pm.getActivePanel()).getComponent(0));
        } else {
            test = (JTextField) f.get(_pm.getActivePanel());

        }
        return test;

    }

    private JPasswordField getPF(Class c, String pf) throws NoSuchFieldException, IllegalAccessException {

        Field f = c.getDeclaredField(pf);
        f.setAccessible(true);
        JPasswordField test = (JPasswordField) f.get(_pm.getActivePanel());
        return test;

    }

    private JTable getTable(Class c, String t) throws NoSuchFieldException, IllegalAccessException {

        Field f = c.getDeclaredField(t);
        f.setAccessible(true);
        JTable test;
        if (_pm.getActivePanel() instanceof JTabbedPane) {
            test = (JTable) f.get(((JTabbedPane) _pm.getActivePanel()).getComponent(0));
        } else {
            test = (JTable) f.get(_pm.getActivePanel());

        }
        return test;

    }

    private JLabel getLbl(Class c, String lbl) throws NoSuchFieldException, IllegalAccessException {

        Field f = c.getDeclaredField(lbl);
        f.setAccessible(true);
        JLabel test = (JLabel) f.get(_pm.getActivePanel());
        return test;

    }


}
