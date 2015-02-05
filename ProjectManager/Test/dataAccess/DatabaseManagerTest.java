package dataAccess;

/*
author : Samuel Beland-Leblanc

Unit test for the DatabaseManager class.


 */



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
		if (testDbFile.exists()) {
			testDbFile.delete();
		}

		_db_mg = new DatabaseManager("testDataDb.db");
		_test_user_insert = new User(1, "Samuel", "Beland-Leblanc",
				"sbleblanc_insert", 0);
		_test_user_delete = new User(1, "Samuel", "Beland-Leblanc",
				"sbleblanc_delete", 0);
		_test_user_duplicate = new User(1, "Samuel", "Beland-Leblanc",
				"sbleblanc_duplicate", 0);

		_db_mg.insertUser(_test_user_delete, "pwd");
		_db_mg.insertUser(_test_user_duplicate, "pwd");

		// No need to check for duplication since there are no constraints on
		// the date
		_test_project_insert = new Project(0, "Project Insert", new Date(),
				new Date(), new Date());
		_test_project_delete = new Project(0, "Project Delete", new Date(),
				new Date(), new Date());

		_test_project_manager = new User(0, "SamuelM", "Beland-LeblancM",
				"manager", 1);

		_db_mg.insertUser(_test_project_manager, "pwd");
		// _test_project_manager = getUserByUsername( _db_mg.getUsers(),
		// _test_project_manager.getUsername());
		_db_mg.insertProject(_test_project_delete, _test_project_manager);

		// _db_mg.insertProject()
	}

	@Test
	public void testInsertUser() throws Exception {

		assertTrue("Adding the test user Samuel : ",
				_db_mg.insertUser(_test_user_insert, "myPass"));

		ArrayList<User> users = _db_mg.getUsers();

		assertTrue("The test user as been succefully added",
				checkIfUserExists(users, _test_user_insert.getUsername()));

	}

	private boolean checkIfUserExists(ArrayList<User> users, String uName) {
		for (User u : users) {
			if (u.getUsername().equals(uName)) {
				return true;
			}
		}
		return false;
	}

	@Test
	public void testInsertUser_DuplicateID() throws Exception {
		User tempUser = new User(_test_user_duplicate.getId(), "test", "test",
				"testDupID", 0);
		assertTrue("Adding user with same ID : ",
				_db_mg.insertUser(tempUser, "myPass"));
		ArrayList<User> users = _db_mg.getUsers();
		assertNotNull("should have at least 1 user in DB", users);
		assertTrue("The user should've been added with same ID",
				checkIfUserExists(users, tempUser.getUsername()));
	}

	@Test
	public void testInsertUser_DuplicateUsername() throws Exception {
		User tempUser = new User(0, "test", "test",
				_test_user_duplicate.getUsername(), 0);
		assertFalse("Adding user with same username : ",
				_db_mg.insertUser(tempUser, "myPass"));
	}

	@Test
	public void testInsertUser_DuplicateFirstLastName() throws Exception {
		User tempUser = new User(0, _test_user_duplicate.getFirstName(),
				_test_user_duplicate.getLastName(), "testDupUsername", 0);
		assertTrue("Adding user with same first and last name : ",
				_db_mg.insertUser(tempUser, "myPass"));
		ArrayList<User> users = _db_mg.getUsers();
		assertNotNull("should have at least 1 user in DB", users);
		assertTrue(
				"The user should've been added with same first and last name",
				checkIfUserExists(users, tempUser.getUsername()));
	}

	@Test
	public void testRemoveUser() throws Exception {
		ArrayList<User> users = _db_mg.getUsers();

		assertTrue("Deleting user", _db_mg.removeUser(_test_user_delete));

		ArrayList<User> usersAfterDelete = _db_mg.getUsers();
		assertEquals("Should have 1 less record in db", users.size() - 1,
				users.size() - (users.size() - usersAfterDelete.size()));

	}

	private User getUserByUsername(ArrayList<User> users, String username) {
		for (User u : users) {
			if (u.getUsername().equals(username)) {
				return u;
			}
		}
		return null;
	}

	@Test
	public void testInsertProject() throws Exception {

		assertTrue("Adding a project with the test manager",
				_db_mg.insertProject(_test_project_insert,
						_test_project_manager));

		ArrayList<Project> projects = _db_mg.getProjects();
		assertNotNull("At least 1 project should be there", projects);

		Project pTemp = getProjectByID(projects, _test_project_insert.getId());
		assertNotNull("The Project should exist in the database", pTemp);

		// check if all the data saves correctly
		assertEquals("Validating project name ",
				_test_project_insert.getName(), pTemp.getName());
		assertEquals("Validating project Start Date ",
				_test_project_insert.getStartDate(), pTemp.getStartDate());
		assertEquals("Validating project Projected End Date ",
				_test_project_insert.getProjectedEndDate(),
				pTemp.getProjectedEndDate());
		assertEquals("Validating project End Date ",
				_test_project_insert.getEndDate(), pTemp.getEndDate());

	}

	@Test
	public void testRemoveProject() throws Exception {

		ArrayList<Project> projects = _db_mg.getProjects();
		Project pTemp = getProjectByID(projects, _test_project_delete.getId());
		assertNotNull("The test project to be deleted should be in the DB",
				pTemp);

		assertTrue("Deleting the test project", _db_mg.removeProject(pTemp));

		projects = _db_mg.getProjects();
		pTemp = getProjectByID(projects, _test_project_delete.getId());
		assertNull("The test project should not exist anymore", pTemp);

	}

	public void testRemoveProject_Cascade() throws Exception {

		Project pToAdd = new Project(0, "p", new Date(), new Date(), new Date());
		assumeTrue(_db_mg.insertProject(pToAdd, _test_project_manager)); // Skips
																			// the
																			// test
																			// if
																			// the
																			// insert
																			// does
																			// not
																			// work

		ArrayList<ProjectUser> pUsers = _db_mg.getProjectUsers();
		ProjectUser pu = getPUByProjectId(pUsers, pToAdd.getId());
		assumeNotNull(pu); // Skips the test if the get does not work

		Task taskToAdd = new Task(0, pToAdd.getId(), "task", new Date(),
				new Date(), new Date(), new Date(), null);
		assumeTrue(_db_mg.insertTask(taskToAdd));

		ArrayList<Task> tasks = _db_mg.getTasks();
		Task tTemp = getTaskByProjectId(tasks, pToAdd.getId());
		assumeNotNull(tTemp);

		assumeTrue(_db_mg.removeProject(pToAdd)); // Skips the delete if the
													// insert does not work

		pUsers = _db_mg.getProjectUsers();
		pu = getPUByProjectId(pUsers, pToAdd.getId());
		tasks = _db_mg.getTasks();
		tTemp = getTaskByProjectId(tasks, pToAdd.getId());

		assertNull("The project user record should not be there anymore", pu);
		assertNull("The tasks linked should've been deleted", tTemp);

	}

	@Test
	public void testInsertTask() throws Exception {

		Task taskToAdd = new Task(0, _test_project_delete.getId(), "task",
				new Date(), new Date(), new Date(), new Date(), null);
		assertTrue(_db_mg.insertTask(taskToAdd));

		ArrayList<Task> tasks = _db_mg.getTasks();
		Task tTemp = getTaskById(tasks, taskToAdd.getId());

		assertNotNull(tTemp);

	}

	@Test
	public void testRemoveTask() throws Exception {

		Task taskToDelete = new Task(0, _test_project_delete.getId(), "task",
				new Date(), new Date(), new Date(), new Date(), null);
		assumeTrue(_db_mg.insertTask(taskToDelete));

		ArrayList<Task> tasks = _db_mg.getTasks();
		Task tTemp = getTaskById(tasks, taskToDelete.getId());
		assumeNotNull(tTemp);

		assertTrue("Deleting task", _db_mg.removeTask(taskToDelete));

		tasks = _db_mg.getTasks();
		tTemp = getTaskById(tasks, taskToDelete.getId());

		assertNull("The task should not be in the DB anymore", tTemp);
	}

	@Test
	public void testGetUsers() throws Exception {

		ArrayList<User> users = _db_mg.getUsers();
		assertNotNull("There should be at least a test user in the DB", users);

		User uTemp = getUserByUsername(users, _test_user_delete.getUsername());
		assertNotNull("The test user should be in the database", uTemp);

	}

	@Test
	public void testGetProjects() throws Exception {

		Project pToAdd = new Project(0, "p", new Date(), new Date(), new Date());
		assumeTrue(_db_mg.insertProject(pToAdd, _test_project_manager)); // Skips
																			// the
																			// test
																			// if
																			// the
																			// insert
																			// does
																			// not
																			// work

		ArrayList<Project> projects = _db_mg.getProjects();
		assertNotNull("There should be at least 1 project in the DB", projects);
		Project p = getProjectByID(projects, pToAdd.getId());
		assertNotNull("The test project should be in the DB", p);

	}

	@Test
	public void testGetTasks() throws Exception {

		Task taskToDelete = new Task(0, _test_project_delete.getId(), "task",
				new Date(), new Date(), new Date(), new Date(), null);
		assumeTrue(_db_mg.insertTask(taskToDelete));

		ArrayList<Task> tasks = _db_mg.getTasks();
		assertNotNull("There should be at least 1 task in the DB", tasks);
		Task tTemp = getTaskById(tasks, taskToDelete.getId());
		assertNotNull("The test task should be in the DB", tTemp);

	}

	@Test
	public void testLogin() throws Exception {

		User loggedInUser = _db_mg.login(_test_project_manager.getUsername(),
				"pwd");
		assertNotNull("The test project manager should be in the database",
				loggedInUser);
		assertEquals("The retrieved user should be good",
				_test_project_manager.getUsername(), loggedInUser.getUsername());
	}

	@Test
	public void testLogin_Invalid() throws Exception {

		User loggedInUser = _db_mg.login(_test_project_manager.getUsername(),
				"pwdert343t3434");
		assertNull("The login should fail by wrong password", loggedInUser);
		loggedInUser = _db_mg.login(_test_project_manager.getUsername()
				+ "hewfjh", "pwd");
		assertNull("The login should fail by wrong username", loggedInUser);

	}

	@Test
	public void testInsertProjectUser() throws Exception {

		ProjectUser pUserToAdd = new ProjectUser(0,
				_test_project_delete.getId(), _test_user_delete.getId(), 0);
		assertTrue("Adding the project user in the DB",
				_db_mg.insertProjectUser(pUserToAdd));

		ArrayList<ProjectUser> pUsers = _db_mg.getProjectUsers();
		assertNotNull("There should be a test PU in the DB", pUsers);
		ProjectUser pu = getPUById(pUsers, pUserToAdd.getId());
		assertNotNull("The test PU should be in the DB", pu);
	}

	@Test
	public void testInsertUserTask() throws Exception {

		Task taskToAdd = new Task(0, _test_project_delete.getId(), "task",
				new Date(), new Date(), new Date(), new Date(), null);
		assumeTrue(_db_mg.insertTask(taskToAdd));
		ProjectUser pUserToAdd = new ProjectUser(0,
				_test_project_delete.getId(), _test_user_delete.getId(), 0);
		assumeTrue(_db_mg.insertProjectUser(pUserToAdd));

		UserTask ut = new UserTask(0, _test_user_delete.getId(),
				taskToAdd.getId(), pUserToAdd.getProjectId());
		assertTrue("Inserting a test UserTask", _db_mg.insertUserTask(ut));
		assertFalse("Should not allow duplicates UT", _db_mg.insertUserTask(ut));

		ArrayList<UserTask> uTasks = _db_mg.getUserTasks();
		assertNotNull(uTasks);
		UserTask utTemp = getUTById(uTasks, ut.getId());
		assertNotNull("The UT should be in the DB", utTemp);

	}

	@Test
	public void testInsertTaskRequirement() throws Exception {

		Task taskToAdd1 = new Task(0, _test_project_delete.getId(), "task1",
				new Date(), new Date(), new Date(), new Date(), null);
		Task taskToAdd2 = new Task(0, _test_project_delete.getId(), "task2",
				new Date(), new Date(), new Date(), new Date(), null);

		assumeTrue(_db_mg.insertTask(taskToAdd1));
		assumeTrue(_db_mg.insertTask(taskToAdd2));

		TaskRequirement trToAdd = new TaskRequirement(0, taskToAdd1.getId(),
				taskToAdd2.getId());
		assertTrue("Inserting TR in DB", _db_mg.insertTaskRequirement(trToAdd));

		ArrayList<TaskRequirement> tReqs = _db_mg.getTaskRequirements();
		assertNotNull("There should be at least 1 task requirement in the DB",
				tReqs);
		assertNotNull("The link between the task should be in the DB",
				getTRById(tReqs, trToAdd.getId()));
	}

	@Test
	public void testGetProjectByName() throws Exception {

		Project p = _db_mg.getProjectByName(_test_project_delete.getName());
		assertNotNull("Should have retrieved a test project", p);

		assertEquals("Should be the right project that got retrived",
				_test_project_delete.getId(), p.getId());

	}

	@Test
	public void testGetProjectUsers() throws Exception {

		ProjectUser pUserToAdd = new ProjectUser(0,
				_test_project_delete.getId(), _test_user_delete.getId(), 0);
		assumeTrue(_db_mg.insertProjectUser(pUserToAdd));

		ArrayList<ProjectUser> pUsers = _db_mg.getProjectUsers();
		assertNotNull("There should be at least 1 test PU in the DB", pUsers);
		ProjectUser pu = getPUById(pUsers, pUserToAdd.getId());
		assertNotNull("A specific test PU should've be in the DB", pu);

	}

	@Test
	public void testGetUserTasks() throws Exception {

		Task taskToAdd = new Task(0, _test_project_delete.getId(), "task",
				new Date(), new Date(), new Date(), new Date(), null);
		assumeTrue(_db_mg.insertTask(taskToAdd));
		ProjectUser pUserToAdd = new ProjectUser(0,
				_test_project_delete.getId(), _test_user_delete.getId(), 0);
		assumeTrue(_db_mg.insertProjectUser(pUserToAdd));

		UserTask ut = new UserTask(0, _test_user_delete.getId(),
				taskToAdd.getId(), pUserToAdd.getProjectId());
		assumeTrue(_db_mg.insertUserTask(ut));

		ArrayList<UserTask> uTasks = _db_mg.getUserTasks();
		assertNotNull(uTasks);
		UserTask utTemp = getUTById(uTasks, ut.getId());
		assertNotNull("The UT should be in the DB", utTemp);

	}

	@Test
	public void testGetTaskRequirements() throws Exception {

		Task taskToAdd1 = new Task(0, _test_project_delete.getId(), "task1",
				new Date(), new Date(), new Date(), new Date(), null);
		Task taskToAdd2 = new Task(0, _test_project_delete.getId(), "task2",
				new Date(), new Date(), new Date(), new Date(), null);

		assumeTrue(_db_mg.insertTask(taskToAdd1));
		assumeTrue(_db_mg.insertTask(taskToAdd2));

		TaskRequirement trToAdd = new TaskRequirement(0, taskToAdd1.getId(),
				taskToAdd2.getId());
		assumeTrue(_db_mg.insertTaskRequirement(trToAdd));

		ArrayList<TaskRequirement> tReqs = _db_mg.getTaskRequirements();
		assertNotNull("There should be at least 1 task requirement in the DB",
				tReqs);
		assertNotNull("The link between the task should be in the DB",
				getTRById(tReqs, trToAdd.getId()));

	}

	@Test
	public void testGetTasksForProject() throws Exception {

		Task taskToAdd1 = new Task(0, _test_project_delete.getId(), "task1",
				new Date(), new Date(), new Date(), new Date(), null);

		ArrayList<Task> tasks = _db_mg.getTasksForProject(_test_project_delete);

		assertNotNull("Should not have returned by an exception", tasks);
		assertEquals("The project should not have task right now", 0,
				tasks.size());

		assumeTrue(_db_mg.insertTask(taskToAdd1));

		tasks = _db_mg.getTasksForProject(_test_project_delete);
		assertNotNull("Should not have returned by an exception", tasks);
		assertFalse("The project should not have task right now",
				tasks.size() == 0);

	}

	@Test
	public void testGetProjectForUsers() throws Exception {

		ArrayList<Project> projects = _db_mg
				.getProjectForUsers(_test_project_manager);
		assertNotNull(projects);
		assertEquals("Should have 1 project for a certain user", 1,
				projects.size());

		Project p = new Project(0, "Project 2", new Date(), new Date(),
				new Date());
		assumeTrue(_db_mg.insertProject(p, _test_project_manager));

		projects = _db_mg.getProjectForUsers(_test_project_manager);
		assertNotNull(projects);
		assertEquals("Should have 2 project for a certain user", 2,
				projects.size());

	}

	@Test
	public void testGetUsersForProject() throws Exception {

		ArrayList<User> users = _db_mg.getUsersForProject(_test_project_delete);
		assertNotNull(users);
		assertEquals("There should be 1 user for a certain project", 1,
				users.size());

		assumeTrue(_db_mg.insertProjectUser(new ProjectUser(0,
				_test_project_delete.getId(), _test_user_duplicate.getId(), 0)));

		users = _db_mg.getUsersForProject(_test_project_delete);
		assertNotNull(users);
		assertEquals("There should be 2 users for a certain project", 2,
				users.size());

	}

	@Test
	public void testGetUsersForTask() throws Exception {

		Task taskToAdd = new Task(0, _test_project_delete.getId(), "task",
				new Date(), new Date(), new Date(), new Date(), null);
		assumeTrue(_db_mg.insertTask(taskToAdd));
		ProjectUser pUserToAdd = new ProjectUser(0,
				_test_project_delete.getId(), _test_user_delete.getId(), 0);
		assumeTrue(_db_mg.insertProjectUser(pUserToAdd));

		ArrayList<User> users = _db_mg.getUsersForTask(taskToAdd);
		assertNotNull(users);
		assertEquals(0, users.size());

		UserTask ut = new UserTask(0, _test_user_delete.getId(),
				taskToAdd.getId(), pUserToAdd.getProjectId());
		assumeTrue(_db_mg.insertUserTask(ut));

		users = _db_mg.getUsersForTask(taskToAdd);
		assertNotNull(users);
		assertEquals(1, users.size());

	}

	@Test
	public void testUpdateUser() throws Exception {

		_test_user_delete.setFirstName("1");
		_test_user_delete.setLastName("1");
		_test_user_delete.setRole(6);

		assertTrue("Updating user", _db_mg.updateUser(_test_user_delete));

		ArrayList<User> users = _db_mg.getUsers();
		assertNotNull(users);
		User u = getUserByUsername(users, _test_user_delete.getUsername());
		assertNotNull(u);

		assertEquals(u.getFirstName(), _test_user_delete.getFirstName());
		assertEquals(u.getLastName(), _test_user_delete.getLastName());
		assertEquals(u.getRole(), _test_user_delete.getRole());

	}

	@Test
	public void testUpdateProject() throws Exception {

		_test_project_delete.setName("lol a name");
		_test_project_delete.setEndDate(new Date(11111111118L));
		_test_project_delete.setProjectedEndDate(new Date(11111111119L));
		_test_project_delete.setStartDate(new Date(111111111110L));

		assertTrue(_db_mg.updateProject(_test_project_delete));

		ArrayList<Project> projects = _db_mg.getProjects();
		assertNotNull("There should be at least 1 project in the DB", projects);
		Project p = getProjectByID(projects, _test_project_delete.getId());
		assertNotNull("The test project should be in the DB", p);

		assertEquals(p.getName(), _test_project_delete.getName());
		assertEquals(p.getEndDate(), _test_project_delete.getEndDate());
		assertEquals(p.getStartDate(), _test_project_delete.getStartDate());
		assertEquals(p.getProjectedEndDate(),
				_test_project_delete.getProjectedEndDate());

	}

	@Test
	public void testUpdateTask() throws Exception {

		Task taskToAdd = new Task(0, _test_project_delete.getId(), "task",
				new Date(), new Date(), new Date(), new Date(), null);
		assumeTrue(_db_mg.insertTask(taskToAdd));

		taskToAdd.setName("lol");
		taskToAdd.setStartDate(new Date(1111111111230L));
		taskToAdd.setProjectedEndDate(new Date(1111111111231L));
		taskToAdd.setEndDate(new Date(1111111111232L));
		taskToAdd.setProjectedStartDate(new Date(1111111111233L));

		assertTrue(_db_mg.updateTask(taskToAdd));

		ArrayList<Task> tasks = _db_mg.getTasks();
		assertNotNull(tasks);
		Task t = getTaskById(tasks, taskToAdd.getId());
		assertNotNull(t);

		assertEquals(t.getName(), taskToAdd.getName());
		assertEquals(t.getEndDate(), new java.sql.Date(taskToAdd.getEndDate()
				.getTime()));
		assertEquals(t.getProjectedEndDate(), taskToAdd.getProjectedEndDate());
		assertEquals(t.getStartDate(), taskToAdd.getStartDate());
		assertEquals(t.getProjectedStartDate(),
				taskToAdd.getProjectedStartDate());

	}

	@Test
	public void testCircularDependency() {
		Task task1 = new Task(0, _test_project_delete.getId(), "task 1",
				new Date(), new Date(), new Date(), new Date(), null);
		assumeTrue(_db_mg.insertTask(task1));

		task1 = _db_mg.getTaskByName("task 1");

		Task task2 = new Task(0, _test_project_delete.getId(), "task 2",
				new Date(), new Date(), new Date(), new Date(), null);
		assumeTrue(_db_mg.insertTask(task2));

		task2 = _db_mg.getTaskByName("task 2");

		Task task3 = new Task(0, _test_project_delete.getId(), "task 3",
				new Date(), new Date(), new Date(), new Date(), null);
		assumeTrue(_db_mg.insertTask(task3));

		task3 = _db_mg.getTaskByName("task 3");

		Task task4 = new Task(0, _test_project_delete.getId(), "task 4",
				new Date(), new Date(), new Date(), new Date(), null);
		assumeTrue(_db_mg.insertTask(task4));

		task4 = _db_mg.getTaskByName("task 4");

		// tast 3 requires task 2
		TaskRequirement tr1 = new TaskRequirement(-1, task3.getId(),
				task2.getId());
		assumeTrue(_db_mg.insertTaskRequirement(tr1));

		// task 2 requires task 1
		TaskRequirement tr2 = new TaskRequirement(-1, task2.getId(),
				task1.getId());
		assumeTrue(_db_mg.insertTaskRequirement(tr2));

		// potential prerequisites for task1 should only be task 4
		ArrayList<Task> potentialReqs = _db_mg.getPotentialPrerequisites(task1);

		assertEquals(1, potentialReqs.size());
		assertEquals("task 4", task4.getName());
	}

	public Task getTaskById(ArrayList<Task> tasks, int id) {
		for (Task t : tasks) {
			if (t.getId() == id) {
				return t;
			}
		}
		return null;
	}

	public Task getTaskByProjectId(ArrayList<Task> tasks, int pId) {
		for (Task t : tasks) {
			if (t.getProjectId() == pId) {
				return t;
			}
		}
		return null;
	}

	public ProjectUser getPUByProjectId(ArrayList<ProjectUser> pUsers, int pId) {
		for (ProjectUser pu : pUsers) {
			if (pu.getProjectId() == pId) {
				return pu;
			}
		}
		return null;
	}

	public ProjectUser getPUById(ArrayList<ProjectUser> pUsers, int id) {
		for (ProjectUser pu : pUsers) {
			if (pu.getId() == id) {
				return pu;
			}
		}
		return null;
	}

	public Project getProjectByID(ArrayList<Project> projects, int id) {
		for (Project p : projects) {
			if (p.getId() == id) {
				return p;
			}
		}
		return null;
	}

	public UserTask getUTById(ArrayList<UserTask> uTasks, int id) {
		for (UserTask ut : uTasks) {
			if (ut.getId() == id) {
				return ut;
			}
		}
		return null;
	}

	public TaskRequirement getTRById(ArrayList<TaskRequirement> tReqs, int id) {
		for (TaskRequirement tr : tReqs) {
			if (tr.getId() == id) {
				return tr;
			}
		}
		return null;
	}
}