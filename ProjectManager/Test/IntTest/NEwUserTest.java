package IntTest;

import Helper.ReflectionHelper;
import application.ProjectManager;
import authentication.AuthenticationPanel;
import dashboard.DashboardPanel;
import dataAccess.DatabaseManager;
import junit.framework.Assert;
import obj.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import projectEditor.ProjectEditorPanel;
import ui.UIRobot;
import userEditor.UserEditorModel;
import userEditor.UserEditorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Samuel on 4/2/2015.
 */
public class NEwUserTest {

    private ProjectManager _pm;
    private Thread _app_thread;
    private UIRobot _bot;
    private DatabaseManager _dbm;
    private Task _task;

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

        _task = new Task(0, p.getId(), "task", new Date(),
                new Date(), new Date(), new Date(), 0);

        _dbm.insertTask(_task);

        UserTask ut = new UserTask(0, u.getId(), _task.getId(), pu.getId());
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

    }

    @After
    public void TearDown(){
        _pm.exit();
    }

    @Test
    public void NewUser_Opens() throws NoSuchFieldException, IllegalAccessException {
        ReflectionHelper.<JButton>getElement("btnNewUser",DashboardPanel.class,_pm).doClick();
        assertTrue(((JTabbedPane)_pm.getActivePanel()).getComponent(0) instanceof UserEditorPanel);
    }

    @Test
    public void NewUser_Closes() throws NoSuchFieldException, IllegalAccessException, InterruptedException {

        ReflectionHelper.<JButton>getElement("btnNewUser",DashboardPanel.class,_pm).doClick();
        ReflectionHelper.<JButton>getElement("btnCloseTab", UserEditorPanel.class,_pm).doClick();
        assertTrue(_pm.getActivePanel() instanceof DashboardPanel);
//        ReflectionHelper.<JButton>getElement("btnNewUser",)


    }

    @Test
    public void NewUser_ProperSave() throws NoSuchFieldException, IllegalAccessException {

        ReflectionHelper.<JButton>getElement("btnNewUser",DashboardPanel.class,_pm).doClick();

        JTextField txt;

        txt = ReflectionHelper.getElement("txtUserName",UserEditorPanel.class,_pm);
        txt.setText("1");

        txt = ReflectionHelper.getElement("txtFirstName",UserEditorPanel.class,_pm);
        txt.setText("1");

        txt = ReflectionHelper.getElement("txtLastName",UserEditorPanel.class,_pm);
        txt.setText("1");

        JPasswordField jpf = ReflectionHelper.getElement("passwordField",UserEditorPanel.class,_pm);
        jpf.setText("1");

        ReflectionHelper.<JButton>getElement("btnSave", UserEditorPanel.class,_pm).doClick();

        assertNotNull(_dbm.login("1","1"));


    }



}
