package dataAccess;

import obj.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseManagerTest {

    private DatabaseManager _db_mg;
    private User _test_user;

    @Before
    public void beforeTest() throws Exception {
        _db_mg = new DatabaseManager();
        _test_user = new User(1,"Samuel", "Beland-Leblanc","sbleblanc","salt",0);
    }

    @Test
    public void testInsertUser() throws Exception {
        assertTrue("Adding the test user Samuel : ",_db_mg.insertUser(_test_user, "myPass"));
    }

    @Test
    public void testInsertUser_Duplicates() throws Exception {

        User dup1 = new User(_test_user.getId(), "Dup1", "Dup2", "dupeUserName1","salt",0);
        User dup2 = new User(2, "Dup1", "Dup2", _test_user.getUsername(),"salt",0);
        User dup3 = new User(_test_user.getId(), "Dup1", "Dup2", _test_user.getUsername(),"salt",0);
        User dup4 = new User(1, _test_user.getFirstName(),_test_user.getLastName(),"lol_a_user","salt",0);


        assertTrue("Adding user with same ID : ",_db_mg.insertUser(dup1, "myPass"));
        assertFalse("Adding user with same username : ",_db_mg.insertUser(dup2, "myPass"));
        assertFalse("Adding user with same ID and username : ",_db_mg.insertUser(dup3, "myPass"));
        assertTrue("Adding user with the same first and last name : ", _db_mg.insertUser(dup4,"khdfksdhb"));
    }

    @Test
    public void testInsertProject() throws Exception {

    }

    @Test
    public void testInsertTask() throws Exception {

    }

    @Test
    public void testGetUsers() throws Exception {

    }

    @Test
    public void testGetProjects() throws Exception {

    }

    @Test
    public void testGetTasks() throws Exception {

    }

    @Test
    public void testRemoveUser() throws Exception {

    }

    @Test
    public void testRemoveProject() throws Exception {

    }

    @Test
    public void testRemoveTask() throws Exception {

    }

    @Test
    public void testLogin() throws Exception {

    }
}