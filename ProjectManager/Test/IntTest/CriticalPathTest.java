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
import projectEditor.ProjectEditorPanel;
import ui.UIRobot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.Date;

/**
 * Created by Samuel on 4/2/2015.
 */
public class CriticalPathTest {

    private ProjectManager _pm;
    private Thread _app_thread;
    private DatabaseManager _dbm;

    @Before
    public void setup() throws AWTException, NoSuchFieldException, IllegalAccessException, InterruptedException {


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

        JTable jt = ReflectionHelper.getElement("table", DashboardPanel.class,_pm);

        jt.setRowSelectionInterval(0, 0);

        ((DashboardPanel) _pm.getActivePanel()).openCurrentSelectedProject();

    }

    @After
    public void TearDown(){
        _pm.exit();
    }

    @Test
    public void CP_Test(){



    }

}
