package dataAccess;

import obj.User;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assume.*;

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

        String uName = Long.toString(System.currentTimeMillis());
        User testUser =  new User(1,"Samuel", "Beland-Leblanc",uName,"salt",0);

        assertTrue("Adding the test user Samuel : ", _db_mg.insertUser(testUser, "myPass"));

        ArrayList<User> users = _db_mg.getUsers();

        assertTrue("The test user as been succefully added", checkIfUserExists(users, uName));

    }

    private boolean checkIfUserExists(ArrayList<User> users, String uName){
        for(User u : users){
            if (u.getUsername().equals(uName)){
                return true;
            }
        }
        return false;
    }

    @Test
    public void testInsertUser_Duplicates() throws Exception {

        //3 dummy unique username for testing
        String uName = Long.toString(System.currentTimeMillis());
        String uName2 = Long.toString(System.currentTimeMillis() + 1);
        String uName3 = Long.toString(System.currentTimeMillis() + 2);

        //Add a base user to test duplication validation
        User testUser =  new User(1,"Samuel", "Beland-Leblanc",uName,"salt",0);
        assertTrue("Inserting dummy user", _db_mg.insertUser(testUser,"lawl"));

        ArrayList<User> users;

        //4 dummy with 4 different case of possible duplication
        User dup1 = new User(testUser.getId(), "Dup1", "Dup2", uName2,"salt",0);
        User dup2 = new User(2, "Dup1", "Dup2", testUser.getUsername(),"salt",0);
        User dup3 = new User(testUser.getId(), "Dup1", "Dup2", testUser.getUsername(),"salt",0);
        User dup4 = new User(1, testUser.getFirstName(),testUser.getLastName(),uName3,"salt",0);


        assertTrue("Adding user with same ID : ",_db_mg.insertUser(dup1, "myPass"));
        users = _db_mg.getUsers();
        assertNotNull("should have at least 1 user in DB", users);
        assertTrue("The user should've been added with same ID", checkIfUserExists(users,uName2));

        assertFalse("Adding user with same username : ",_db_mg.insertUser(dup2, "myPass"));
        assertFalse("Adding user with same ID and username : ",_db_mg.insertUser(dup3, "myPass"));


        assertTrue("Adding user with the same first and last name : ", _db_mg.insertUser(dup4,"khdfksdhb"));
        users = _db_mg.getUsers();
        assertNotNull("should have at least 1 user in DB", users);
        assertTrue("The user should've been added with same first and last name", checkIfUserExists(users,uName3));
    }

    @Test
    public void testRemoveUser() throws Exception {
        String uName = Long.toString(System.currentTimeMillis());

        //Add a base user to test deletion
        User testUser =  new User(1,"Samuel", "Beland-Leblanc",uName,"salt",0);
        assertTrue("Inserting dummy user",_db_mg.insertUser(testUser,"lawl"));

        ArrayList<User> users = _db_mg.getUsers();
        assertNotNull("should have at least 1 user in DB", users);
        User uToDelete = getUserByUsername(users, testUser.getUsername());
        assertNotNull("Has the right user to delete", uToDelete);

        assertTrue("Deleting user", _db_mg.removeUser(uToDelete));

        ArrayList<User> usersAfterDelete = _db_mg.getUsers();
        assertEquals("Should have 1 less record in db", users.size() - 1, users.size() - (users.size() - usersAfterDelete.size()));


    }

    private User getUserByUsername(ArrayList<User> users, String username){
        for(User u : users){
            if (u.getUsername().equals(username)){
                return u;
            }
        }
        return null;
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
    public void testRemoveProject() throws Exception {

    }

    @Test
    public void testRemoveTask() throws Exception {

    }

    @Test
    public void testLogin() throws Exception {

    }
}