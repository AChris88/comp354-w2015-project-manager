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
import java.io.File;
import java.util.Date;
import org.junit.*;

import javax.swing.*;

/**
 * Created by Sam on 2015-03-31.
 */
public class LoginTestModule {

    private ProjectManager _pm;
    private Thread _app_thread;

    @Before
    public void setup() throws AWTException, NoSuchFieldException, IllegalAccessException {

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
        _app_thread = new Thread(app);

        try {
            _app_thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @After
    public void AfterCleanup() throws InterruptedException {
        _pm.exit();
    }

    /*
    private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton cancelButton;
	private JButton loginButton;
	private ProjectManager manager;
	private JLabel errorMessageLabel;
    * */

    //Test1 : The window opens first and everything is visible

    @Test
    public void ModuleOpens_Test() throws NoSuchFieldException, IllegalAccessException {

        assertTrue(_pm.getActivePanel() instanceof AuthenticationPanel);
        assertTrue(ReflectionHelper.<JButton>getElement("loginButton", AuthenticationPanel.class, _pm).isVisible());
        assertTrue(ReflectionHelper.<JButton>getElement("cancelButton",AuthenticationPanel.class,_pm).isVisible());
        assertTrue(ReflectionHelper.<JTextField>getElement("usernameField", AuthenticationPanel.class, _pm).isVisible());
        assertTrue(ReflectionHelper.<JTextField>getElement("usernameField", AuthenticationPanel.class, _pm).isEditable());
        assertTrue(ReflectionHelper.<JPasswordField>getElement("passwordField",AuthenticationPanel.class,_pm).isVisible());
        assertTrue(ReflectionHelper.<JPasswordField>getElement("passwordField",AuthenticationPanel.class,_pm).isEditable());
        assertTrue(ReflectionHelper.<JLabel>getElement("errorMessageLabel", AuthenticationPanel.class, _pm).getText().length() == 0);

    }

    //Test2 : The user cancels and everything closes
    @Test
    public void ModuleCloses_Test() throws NoSuchFieldException, IllegalAccessException, InterruptedException {

        JButton btnTemp = ReflectionHelper.<JButton>getElement("cancelButton", AuthenticationPanel.class, _pm);
        WindowEventListener wel = new WindowEventListener();
        _pm.addWindowListener(wel);

        btnTemp.doClick();
        Thread.sleep(500);
        assertTrue("The window should've been closed. This might also mean that the listener didn;t caught on before the assertion", wel.getHasBeenClosed());
    }


    //Test3 : The user enters wrong login, text is visible
    @Test
    public void WrongLogin_Test()throws NoSuchFieldException, IllegalAccessException {

        JButton btnLog = ReflectionHelper.<JButton>getElement("loginButton", AuthenticationPanel.class, _pm);
        JTextField txtUsername = ReflectionHelper.<JTextField>getElement("usernameField", AuthenticationPanel.class, _pm);
        JPasswordField pwd = ReflectionHelper.<JPasswordField>getElement("passwordField", AuthenticationPanel.class, _pm);

        txtUsername.setText("test");
        pwd.setText("this is not the pwd");

        btnLog.doClick();

        assertFalse(ReflectionHelper.<JLabel>getElement("errorMessageLabel", AuthenticationPanel.class, _pm).getText().length() == 0);
    }


    //Test4 : User logins, the module closes and transistion is ok
    @Test
    public void GoodLogin_Test()throws NoSuchFieldException, IllegalAccessException {

        JButton btnLog = ReflectionHelper.<JButton>getElement("loginButton", AuthenticationPanel.class, _pm);
        JTextField txtUsername = ReflectionHelper.<JTextField>getElement("usernameField", AuthenticationPanel.class, _pm);
        JPasswordField pwd = ReflectionHelper.<JPasswordField>getElement("passwordField", AuthenticationPanel.class, _pm);

        txtUsername.setText("test");
        pwd.setText("test");

        btnLog.doClick();

        assertEquals("The current user should be correct (firstname)", _pm.currentUser.getFirstName(), "test");
        assertEquals("The current user should be correct(lastname)", _pm.currentUser.getLastName(), "test");

        assertFalse(_pm.getActivePanel() instanceof AuthenticationPanel);
        assertTrue(_pm.getActivePanel() instanceof DashboardPanel);
    }


    //Test5 : User inputs input that is way too long
    @Test
    public void BadLoginInfo_Test()throws NoSuchFieldException, IllegalAccessException {

        JButton btnLog = ReflectionHelper.<JButton>getElement("loginButton", AuthenticationPanel.class, _pm);
        JTextField txtUsername = ReflectionHelper.<JTextField>getElement("usernameField", AuthenticationPanel.class, _pm);
        JPasswordField pwd = ReflectionHelper.<JPasswordField>getElement("passwordField", AuthenticationPanel.class, _pm);

        txtUsername.setText("rhgdshbjknckjndjknsdfkjnvfkjnkjnfdjnjnfdljndslfkjnlsdjfnvlsdjfnvljndfljhdfjhkjhfkjhfjkhkjdfjkfjkfkh;ij;kv;fkjhvfkj;kjfvkjdflkjjhfvkjhfljvhsfldjkvhsldkfjvhlksjdfhvlksjdvhlkdjfvhlkjfhvlkjhijncijnkdjnkjdhfkjdhfk");
        pwd.setText("rhgdshbjknckjndjknsdfkjnvfkjnkjnfdjnjnfdljndslfkjnlsdjfnvlsdjfnvljndfljhdfjhkjhfkjhfjkhkjdfjkfjkfkh;ij;kv;fkjhvfkj;kjfvkjdflkjjhfvkjhfljvhsfldjkvhsldkfjvhlksjdfhvlksjdvhlkdjfvhlkjfhvlkjhijncijnkdjnkjdhfkjdhfk");

        btnLog.doClick();
        assertFalse(ReflectionHelper.<JLabel>getElement("errorMessageLabel", AuthenticationPanel.class, _pm).getText().length() == 0);


    }


}
