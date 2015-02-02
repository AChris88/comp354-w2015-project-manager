package dataAccess;

import obj.Project;
import obj.User;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
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
    private User _test_user_insert;
    private User _test_user_delete;
    private User _test_user_duplicate;
    private User _test_project_manager;
    private Project _test_project_insert;
    private Project _test_project_delete;

    @Before
    public void beforeTest() throws Exception {
        File testDbFile = new File("testDataDb.db");
        if(testDbFile.exists()){
            testDbFile.delete();
        }

        _db_mg = new DatabaseManager("testDataDb.db");
        _test_user_insert = new User(1,"Samuel", "Beland-Leblanc","sbleblanc_insert",0);
        _test_user_delete = new User(1,"Samuel", "Beland-Leblanc","sbleblanc_delete",0);
        _test_user_duplicate = new User(1,"Samuel", "Beland-Leblanc","sbleblanc_duplicate",0);

        _db_mg.insertUser(_test_user_delete, "pwd");
        _db_mg.insertUser(_test_user_duplicate, "pwd");

        //No need to check for duplication since there are no constraints on the date
        _test_project_insert = new Project(0,"Project Insert",new Date(), new Date(),new Date());
        _test_project_delete = new Project(0,"Project Delete",new Date(), new Date(),new Date());

        _test_project_manager = new User(0,"SamuelM", "Beland-LeblancM", "manager",1);

        _db_mg.insertUser(_test_project_manager, "pwd");
        _test_project_manager = getUserByUsername( _db_mg.getUsers(), _test_project_manager.getUsername());
        _db_mg.insertProject(_test_project_delete, _test_project_manager);

//        _db_mg.insertProject()
    }


    @Test
    public void testInsertUser() throws Exception {

        assertTrue("Adding the test user Samuel : ", _db_mg.insertUser(_test_user_insert, "myPass"));

        ArrayList<User> users = _db_mg.getUsers();

        assertTrue("The test user as been succefully added", checkIfUserExists(users, _test_user_insert.getUsername()));

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
    public void testInsertUser_DuplicateID() throws Exception{
        User tempUser = new User(_test_user_duplicate.getId(),"test","test","testDupID",0);
        assertTrue("Adding user with same ID : ",_db_mg.insertUser(tempUser, "myPass"));
        ArrayList<User> users = _db_mg.getUsers();
        assertNotNull("should have at least 1 user in DB", users);
        assertTrue("The user should've been added with same ID", checkIfUserExists(users,tempUser.getUsername()));
    }

    @Test
    public void testInsertUser_DuplicateUsername() throws Exception{
        User tempUser = new User(0,"test","test",_test_user_duplicate.getUsername(),0);
        assertFalse("Adding user with same username : ",_db_mg.insertUser(tempUser, "myPass"));
    }

    @Test
    public void testInsertUser_DuplicateFirstLastName() throws Exception{
        User tempUser = new User(0,_test_user_duplicate.getFirstName(),_test_user_duplicate.getLastName(),"testDupUsername",0);
        assertTrue("Adding user with same first and last name : ",_db_mg.insertUser(tempUser, "myPass"));
        ArrayList<User> users = _db_mg.getUsers();
        assertNotNull("should have at least 1 user in DB", users);
        assertTrue("The user should've been added with same first and last name", checkIfUserExists(users,tempUser.getUsername()));
    }

    @Test
    public void testRemoveUser() throws Exception {
//        String uName = Long.toString(System.currentTimeMillis());
//
//        //Add a base user to test deletion
//        User testUser =  new User(1,"Samuel", "Beland-Leblanc",uName,"salt",0);
//        assertTrue("Inserting dummy user",_db_mg.insertUser(testUser,"lawl"));
//
        ArrayList<User> users = _db_mg.getUsers();
//        assertNotNull("should have at least 1 user in DB", users);
//        User uToDelete = getUserByUsername(users, _test_user_delete.getUsername());
//        assertNotNull("Has the right user to delete", uToDelete);

        assertTrue("Deleting user", _db_mg.removeUser(_test_user_delete));

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

        assertTrue("Adding a project with the test manager", _db_mg.insertProject(_test_project_insert, _test_project_manager));

        ArrayList<Project> projects = _db_mg.getProjects();
        assertNotNull("At least 1 project should be there", projects);

        Project pTemp = projects.get(projects.size() - 1);
        assertEquals("Validating project name ", _test_project_insert.getName(), pTemp.getName());
        assertEquals("Validating project Start Date ", _test_project_insert.getStartDate(), pTemp.getStartDate());
        assertEquals("Validating project Projected End Date ", _test_project_insert.getProjectedEndDate(), pTemp.getProjectedEndDate());
        assertEquals("Validating project End Date ", _test_project_insert.getEndDate(), pTemp.getEndDate());


    }

    public Project getProjectByID(ArrayList<Project> projects, int id){
        for(Project p : projects){
            if (p.getId() == id){
                return p;
            }
        }
        return null;
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