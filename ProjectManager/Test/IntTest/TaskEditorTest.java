package IntTest;

import Helper.ReflectionHelper;
import application.ProjectManager;
import authentication.AuthenticationPanel;
import dashboard.DashboardPanel;
import dataAccess.DatabaseManager;
import obj.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Date;
import org.junit.*;
import projectEditor.ProjectEditorPanel;
import taskEditor.TaskEditorPanel;
import ui.UIRobot;

import javax.swing.*;

/**
 * Created by Samuel on 4/1/2015.
 */
public class TaskEditorTest {

    private ProjectManager _pm;
    private Thread _app_thread;
    private UIRobot _bot;
    private DatabaseManager _dbm;

    @Before
    public void setup() throws AWTException, NoSuchFieldException, IllegalAccessException, InterruptedException {

        _bot = new UIRobot();

        Thread botThread = new Thread(_bot);


        String dbFile = "ITtestdb.db";
        File testDbFile = new File(dbFile);
        if (testDbFile.exists()) {
            testDbFile.delete();
        }

        Runnable app = (Runnable) new ProjectManager(dbFile);

        _dbm = new DatabaseManager(dbFile);
        User u = new User(0, "test", "test", "test", 1);
        _dbm.insertUser(u, "test");

        Project p = new Project(0, "testp", new Date(), new Date(), new Date());
        _dbm.insertProject(p, u);

        ProjectUser pu = _dbm.getProjectUsers().get(0);

        Task taskToAdd = new Task(0, p.getId(), "task", new Date(),
                new Date(), new Date(), new Date(), 0);

        _dbm.insertTask(taskToAdd);

        UserTask ut = new UserTask(0, u.getId(), taskToAdd.getId(), pu.getId());
        _dbm.insertUserTask(ut);




        _pm = (ProjectManager) app;
        _app_thread = new Thread(app);

        try {
            _app_thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ReflectionHelper.<JTextField>getElement("usernameField", AuthenticationPanel.class, _pm).setText("test");
        ReflectionHelper.<JPasswordField>getElement("passwordField", AuthenticationPanel.class, _pm).setText("test");

        ReflectionHelper.<JButton>getElement("loginButton", AuthenticationPanel.class, _pm).doClick();
//        getButton(AuthenticationPanel.class, "loginButton").doClick();

//        temp = DashboardPanel.class;

        JTable jt = ReflectionHelper.getElement("table",DashboardPanel.class,_pm);

        jt.setRowSelectionInterval(0, 0);

        ((DashboardPanel) _pm.getActivePanel()).openCurrentSelectedProject();

        jt = ReflectionHelper.getElement("table", ProjectEditorPanel.class,_pm);

        Thread.sleep(500);

        _bot.getRobot().mouseMove(jt.getLocationOnScreen().x + 10, jt.getLocationOnScreen().y + 10);

        _bot.getRobot().mousePress(InputEvent.BUTTON1_MASK);
        _bot.getRobot().mouseRelease(InputEvent.BUTTON1_MASK);
        _bot.getRobot().mousePress(InputEvent.BUTTON1_MASK);
        _bot.getRobot().mouseRelease(InputEvent.BUTTON1_MASK);

        Thread.sleep(500);



    }

    @After
    public void TearDown(){
        _pm.exit();
    }

    @Test
    public void Open_Test() throws InterruptedException, NoSuchFieldException, IllegalAccessException {

        assertTrue(((JTabbedPane) _pm.getActivePanel()).getSelectedComponent() instanceof TaskEditorPanel);
        assertTrue(ReflectionHelper.<JTextField>getElement("nameTextField",TaskEditorPanel.class,_pm).getText() == "task");

    }

    @Test
    public void Save_Test() throws NoSuchFieldException, IllegalAccessException {

        JTextField temp;

        temp = ReflectionHelper.getElement("nameTextField", TaskEditorPanel.class,_pm,1);
        temp.setText(temp.getText() + "123");

        temp = ReflectionHelper.getElement("expectedStartTextField", TaskEditorPanel.class,_pm,1);
        temp.setText("2000-01-01");

        temp = ReflectionHelper.getElement("startTextField", TaskEditorPanel.class,_pm,1);
        temp.setText("2000-01-02");

        temp = ReflectionHelper.getElement("expectedEndTextField", TaskEditorPanel.class,_pm,1);
        temp.setText("2000-01-03");

        temp = ReflectionHelper.getElement("endTextField", TaskEditorPanel.class,_pm,1);
        temp.setText("2000-01-04");

        temp = ReflectionHelper.getElement("valueTextField", TaskEditorPanel.class,_pm,1);
        temp.setText("21");

        ReflectionHelper.<JButton>getElement("btnSave", TaskEditorPanel.class,_pm,1).doClick();

        Task t = _dbm.getTaskByName("task123");
        assertNotNull(t);
        assertEquals("2000-01-01",t.getProjectedStartDate().toString());
        assertEquals("2000-01-02",t.getStartDate().toString());
        assertEquals("2000-01-03",t.getProjectedEndDate().toString());
        assertEquals("2000-01-04",t.getEndDate().toString());
        assertEquals(21,t.getValue());


    }



}
