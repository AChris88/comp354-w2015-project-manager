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
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Samuel on 4/2/2015.
 */
public class CriticalPathTest {

    private ProjectManager _pm;
    private Thread _app_thread;
    private DatabaseManager _dbm;

    @Before
    public void setup() throws AWTException, NoSuchFieldException, IllegalAccessException, InterruptedException {



        String dbFile = "iTtestdb.db";
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

        Task taskToAdd = new Task(0, p.getId(), "task", date(1,1,2000),
                date(2,1,2000), date(3,1,2000), date(4,1,2000), 0);

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
    public void CPValue_Test() throws NoSuchFieldException, IllegalAccessException, InterruptedException {

        long beforeTime = System.currentTimeMillis();
        ReflectionHelper.<JButton>getElement("btnGetCriticalPath", ProjectEditorPanel.class, _pm).doClick();
        assertEquals(2,((ProjectEditorPanel)((JTabbedPane)_pm.getActivePanel()).getComponent(0)).cpValForTest);
        assertTrue("If the user had to click ok, it'll take time to come back",System.currentTimeMillis() > 1000 );

    }

    private static Date date(final int day, final int month, final int year) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        final Date result = calendar.getTime();
        return result;

    }

}
