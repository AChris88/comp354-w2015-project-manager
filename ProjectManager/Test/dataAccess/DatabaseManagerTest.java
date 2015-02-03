package dataAccess;

import obj.*;
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
//        _test_project_manager = getUserByUsername( _db_mg.getUsers(), _test_project_manager.getUsername());
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
        ArrayList<User> users = _db_mg.getUsers();

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

        Project pTemp = getProjectByID(projects, _test_project_insert.getId());
        assertNotNull("The Project should exist in the database", pTemp);

        //check if all the data saves correctly
        assertEquals("Validating project name ", _test_project_insert.getName(), pTemp.getName());
        assertEquals("Validating project Start Date ", _test_project_insert.getStartDate(), pTemp.getStartDate());
        assertEquals("Validating project Projected End Date ", _test_project_insert.getProjectedEndDate(), pTemp.getProjectedEndDate());
        assertEquals("Validating project End Date ", _test_project_insert.getEndDate(), pTemp.getEndDate());

    }



    @Test
    public void testRemoveProject() throws Exception {

        ArrayList<Project> projects = _db_mg.getProjects();
        Project pTemp = getProjectByID(projects, _test_project_delete.getId());
        assertNotNull("The test project to be deleted should be in the DB", pTemp);

        assertTrue("Deleting the test project", _db_mg.removeProject(pTemp));

        projects = _db_mg.getProjects();
        pTemp = getProjectByID(projects, _test_project_delete.getId());
        assertNull("The test project should not exist anymore", pTemp);

    }

    public void testRemoveProject_Cascade() throws Exception {

        Project pToAdd = new Project(0,"p", new Date(),new Date(),new Date());
        assumeTrue(_db_mg.insertProject(pToAdd, _test_project_manager)); //Skips the test if the insert does not work

        ArrayList<ProjectUser> pUsers = _db_mg.getProjectUsers();
        ProjectUser pu = getPUByProjectId(pUsers,pToAdd.getId());
        assumeNotNull(pu); //Skips the test if the get does not work

        Task taskToAdd = new Task(0,pToAdd.getId(),"task",new Date(),new Date(),new Date(),new Date(),null);
        assumeTrue(_db_mg.insertTask(taskToAdd));

        ArrayList<Task> tasks = _db_mg.getTasks();
        Task tTemp = getTaskByProjectId(tasks, pToAdd.getId());
        assumeNotNull(tTemp);


        assumeTrue(_db_mg.removeProject(pToAdd)); //Skips the delete if the insert does not work

        pUsers = _db_mg.getProjectUsers();
        pu = getPUByProjectId(pUsers,pToAdd.getId());
        tasks = _db_mg.getTasks();
        tTemp = getTaskByProjectId(tasks, pToAdd.getId());

        assertNull("The project user record should not be there anymore", pu);
        assertNull("The tasks linked should've been deleted", tTemp);

    }

    @Test
    public void testInsertTask() throws Exception {

        Task taskToAdd = new Task(0,_test_project_delete.getId(),"task",new Date(),new Date(),new Date(),new Date(),null);
        assertTrue(_db_mg.insertTask(taskToAdd));

        ArrayList<Task> tasks = _db_mg.getTasks();
        Task tTemp = getTaskById(tasks, taskToAdd.getId());

        assertNotNull(tTemp);

    }

    @Test
    public void testRemoveTask() throws Exception {

        Task taskToDelete = new Task(0,_test_project_delete.getId(),"task",new Date(),new Date(),new Date(),new Date(),null);
        assumeTrue(_db_mg.insertTask(taskToDelete));

        ArrayList<Task> tasks = _db_mg.getTasks();
        Task tTemp = getTaskById(tasks, taskToDelete.getId());
        assumeNotNull(tTemp);

        assertTrue("Deleting task", _db_mg.removeTask(taskToDelete));

        tasks = _db_mg.getTasks();
        tTemp = getTaskById(tasks, taskToDelete.getId());

        assertNull("The task should not be in the DB anymore",tTemp);
    }

    @Test
    public void testGetUsers() throws Exception {

        ArrayList<User> users = _db_mg.getUsers();
        assertNotNull("There should be at least a test user in the DB",users);

        User uTemp = getUserByUsername(users, _test_user_delete.getUsername());
        assertNotNull("The test user should be in the database", uTemp);

    }

    @Test
    public void testGetProjects() throws Exception {

        Project pToAdd = new Project(0,"p", new Date(),new Date(),new Date());
        assumeTrue(_db_mg.insertProject(pToAdd, _test_project_manager)); //Skips the test if the insert does not work

        ArrayList<Project> projects = _db_mg.getProjects();
        assertNotNull("There should be at least 1 project in the DB", projects);
        Project p = getProjectByID(projects,pToAdd.getId());
        assertNotNull("The test project should be in the DB",p);

    }

    @Test
    public void testGetTasks() throws Exception {

        Task taskToDelete = new Task(0,_test_project_delete.getId(),"task",new Date(),new Date(),new Date(),new Date(),null);
        assumeTrue(_db_mg.insertTask(taskToDelete));

        ArrayList<Task> tasks = _db_mg.getTasks();
        assertNotNull("There should be at least 1 task in the DB", tasks);
        Task tTemp = getTaskById(tasks, taskToDelete.getId());
        assertNotNull("The test task should be in the DB", tTemp);

    }

    @Test
    public void testLogin() throws Exception {

        User loggedInUser = _db_mg.login(_test_project_manager.getUsername(), "pwd");
        assertNotNull("The test project manager should be in the database", loggedInUser);
        assertEquals("The retrieved user should be good", _test_project_manager.getUsername(), loggedInUser.getUsername());
    }

    @Test
    public void testLogin_Invalid() throws Exception {

        User loggedInUser = _db_mg.login(_test_project_manager.getUsername(), "pwdert343t3434");
        assertNull("The login should fail by wrong password", loggedInUser);
        loggedInUser = _db_mg.login(_test_project_manager.getUsername() + "hewfjh", "pwd");
        assertNull("The login should fail by wrong username", loggedInUser);

    }

    @Test
    public void testInsertProjectUser() throws Exception {

        ProjectUser pUserToAdd = new ProjectUser(0,_test_project_delete.getId(),_test_user_delete.getId(), 0);
        assertTrue("Adding the project user in the DB", _db_mg.insertProjectUser(pUserToAdd));

        ArrayList<ProjectUser> pUsers = _db_mg.getProjectUsers();
        assertNotNull("There should be a test PU in the DB",pUsers );
        ProjectUser pu = getPUById(pUsers, pUserToAdd.getId());
        assertNotNull("The test PU should be in the DB", pu);
    }

    @Test
    public void testInsertUserTask() throws Exception {

        Task taskToAdd = new Task(0,_test_project_delete.getId(),"task",new Date(),new Date(),new Date(),new Date(),null);
        assumeTrue(_db_mg.insertTask(taskToAdd));
        ProjectUser pUserToAdd = new ProjectUser(0,_test_project_delete.getId(),_test_user_delete.getId(), 0);
        assumeTrue(_db_mg.insertProjectUser(pUserToAdd));

        UserTask ut = new UserTask(0,_test_user_delete.getId(),taskToAdd.getId(),pUserToAdd.getProjectId());
        assertTrue("Inserting a test UserTask", _db_mg.insertUserTask(ut));
        assertFalse("Should not allow duplicates UT", _db_mg.insertUserTask(ut));

        ArrayList<UserTask> uTasks = _db_mg.getUserTasks();
        assertNotNull(uTasks);
        UserTask utTemp = getUTById(uTasks,ut.getId());
        assertNotNull("The UT should be in the DB",utTemp);

    }


    @Test
    public void testInsertTaskRequirement() throws Exception {

    }

    @Test
    public void testGetProjectByName() throws Exception {

    }


    @Test
    public void testGetProjectUsers() throws Exception {

    }

    @Test
    public void testGetUserTasks() throws Exception {

    }

    @Test
    public void testGetTaskRequirements() throws Exception {

    }

    @Test
    public void testGetTasksForProject() throws Exception {

    }

    @Test
    public void testGetProjectForUsers() throws Exception {

    }

    @Test
    public void testGetUsersForProject() throws Exception {

    }

    @Test
    public void testGetUsersForTask() throws Exception {

    }


    @Test
    public void testUpdateUser() throws Exception {

    }

    @Test
    public void testUpdateProject() throws Exception {

    }

    @Test
    public void testUpdateTask() throws Exception {

    }

    @Test
    public void testUseCaseTest() throws Exception {

    }

    public Task getTaskById(ArrayList<Task> tasks, int id){
        for(Task t : tasks){
            if(t.getId() == id){
                return t;
            }
        }
        return null;
    }

    public Task getTaskByProjectId(ArrayList<Task> tasks, int pId){
        for(Task t : tasks){
            if(t.getProjectId() == pId){
                return t;
            }
        }
        return null;
    }

    public ProjectUser getPUByProjectId(ArrayList<ProjectUser> pUsers, int pId){
        for(ProjectUser pu : pUsers){
            if (pu.getProjectId() == pId){
                return pu;
            }
        }
        return null;
    }

    public ProjectUser getPUById(ArrayList<ProjectUser> pUsers, int id){
        for(ProjectUser pu : pUsers){
            if (pu.getId() == id){
                return pu;
            }
        }
        return null;
    }

    public Project getProjectByID(ArrayList<Project> projects, int id){
        for(Project p : projects){
            if (p.getId() == id){
                return p;
            }
        }
        return null;
    }
    public UserTask getUTById(ArrayList<UserTask> uTasks, int id)
    {
        for(UserTask ut : uTasks){
            if (ut.getId() == id){
                return ut;
            }
        }
        return null;
    }
}