/**
 * Created by Sam on 2015-03-11.
 */

import application.ProjectManager;
import authentication.AuthenticationPanel;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
import dashboard.DashboardPanel;
import dataAccess.DatabaseManager;
import obj.Project;
import obj.ProjectUser;
import obj.User;
import org.junit.*;
import projectEditor.ProjectEditorPanel;
import ui.UIRobot;
import userEditor.AddProjectUserPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assume.*;

import static org.junit.Assert.*;

public class ResourceModuleTest {

    private ProjectManager _pm;

    @Before
    public void setup() throws AWTException, NoSuchFieldException, IllegalAccessException {

//        UIRobot bot = new UIRobot();
        String dbFile="ITtestdb.db";
        File testDbFile = new File(dbFile);
        if (testDbFile.exists()) {
            testDbFile.delete();
        }

        Runnable app = (Runnable) new ProjectManager(dbFile);

        DatabaseManager dbm = new DatabaseManager(dbFile);
        User u = new User(0,"test1","test1","test1",1);
        dbm.insertUser(u,"test1");

        User u1 = new User(0,"notA","notA","NotA",1);
        dbm.insertUser(u1,"test1");

        User u2 = new User(0,"Ass","Ass","Ass",1);
        dbm.insertUser(u2,"test1");

        Project p = new Project(0,"testp",new Date(),new Date(),new Date());
        dbm.insertProject(p,u);

        ProjectUser pu = new ProjectUser(0,p.getId(),u2.getId(),0);
        dbm.insertProjectUser(pu);



        _pm = (ProjectManager)app;


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
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void IsModuleAccessible() throws NoSuchFieldException, IllegalAccessException, InterruptedException {

        Class temp = AuthenticationPanel.class;

        getTF(temp,"usernameField").setText("test1");
        getPF(temp,"passwordField").setText("test1");

        getButton(AuthenticationPanel.class,"loginButton").doClick();

        temp = DashboardPanel.class;

        JTable jt = getTable(temp,"table");

        jt.setRowSelectionInterval(0,0);

        ((DashboardPanel)_pm.getActivePanel()).openCurrentSelectedProject();

        assertTrue(getButton(ProjectEditorPanel.class,"btnAddUser").isVisible());

//
//
//        Thread.sleep(15000);

    }

    @Test
    public void IsModuleProperlyLoadedWithData() throws NoSuchFieldException, IllegalAccessException, InterruptedException {

        Class temp = AuthenticationPanel.class;

        getTF(temp,"usernameField").setText("test1");
        getPF(temp,"passwordField").setText("test1");

        getButton(AuthenticationPanel.class,"loginButton").doClick();

        temp = DashboardPanel.class;

        JTable jt = getTable(temp,"table");

        jt.setRowSelectionInterval(0,0);

        ((DashboardPanel)_pm.getActivePanel()).openCurrentSelectedProject();

        assertTrue(getButton(ProjectEditorPanel.class,"btnAddUser").isVisible());

        getButton(ProjectEditorPanel.class,"btnAddUser").doClick();

//        private JTable allUsersTable;
//        private JTable projectUsersTable


        JTable jtNotA = getTable2(AddProjectUserPanel.class,"allUsersTable",1);
        JTable jtAss = getTable2(AddProjectUserPanel.class,"projectUsersTable",1);

        jtNotA.setRowSelectionInterval(0,0);
        jtAss.setRowSelectionInterval(0,0);

        assertEquals(jtNotA.getSelectedRow(),0);
        assertEquals(jtAss.getSelectedRow(),0);

//
//
//        Thread.sleep(15000);

    }

    private JTable getTable(Class c, String t) throws NoSuchFieldException, IllegalAccessException {

        Field f = c.getDeclaredField(t);
        f.setAccessible(true);
        JTable test;
        if (_pm.getActivePanel() instanceof JTabbedPane){
            test = (JTable) f.get(((JTabbedPane)_pm.getActivePanel()).getComponent(0));
        }else{
            test = (JTable) f.get(_pm.getActivePanel());

        }
        return test;

    }

    private JButton getButton(Class c, String button) throws NoSuchFieldException, IllegalAccessException {

        Field f = c.getDeclaredField(button);
        f.setAccessible(true);
        JButton test;
        if (_pm.getActivePanel() instanceof JTabbedPane){
            test = (JButton) f.get(((JTabbedPane)_pm.getActivePanel()).getComponent(0));
        }else{
            test = (JButton) f.get(_pm.getActivePanel());

        }
        return test;

    }
    private JTextField getTF(Class c, String tf) throws NoSuchFieldException, IllegalAccessException {

        Field f = c.getDeclaredField(tf);
        f.setAccessible(true);
        JTextField test;
        if (_pm.getActivePanel() instanceof JTabbedPane){
            test = (JTextField) f.get(((JTabbedPane)_pm.getActivePanel()).getComponent(0));
        }else{
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

    private JTable getTable2(Class c, String t, int i) throws NoSuchFieldException, IllegalAccessException {

        Field f = c.getDeclaredField(t);
        f.setAccessible(true);
        JTable test;
        if (_pm.getActivePanel() instanceof JTabbedPane){
            test = (JTable) f.get(((JTabbedPane)_pm.getActivePanel()).getComponent(i));
        }else{
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
