package dataAccess;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import obj.Project;
import obj.ProjectUser;
import obj.Task;
import obj.TaskRequirement;
import obj.User;
import obj.UserTask;
import util.PasswordUtil;

/**
 * 
 * @author Christian Allard 7026188
 * 
 */
public class DatabaseManager {
	private Connection connection;
	private ResultSet resultSet;
	private PreparedStatement preparedStatement;
	private Statement statement;
	private String dbName;

	//getTaskForProject
	//getProjectForUsers
	//getUsersForProject
	//getUsersForTask
	//public ArrayList<Task> getRequiredTasks(Task task)
	
	public DatabaseManager() {
		this("testdb.db");
	}

	public DatabaseManager(String dbName) {
		this.dbName = dbName;
		connection = null;
		resultSet = null;
		statement = null;
		preparedStatement = null;
		createTables();
	}

	private void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			statement = connection.createStatement();
			statement.execute("PRAGMA foreign_keys = ON");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void close() {
		try {
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createTables() {
		try {
			connect();
			// checking if tables are created
			DatabaseMetaData data = connection.getMetaData();
			statement = connection.createStatement();

			resultSet = data.getTables(null, null, "users", null);
			if (!resultSet.next()) {
				statement
						.execute("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, "
								+ "first_name TEXT, "
								+ "last_name TEXT, "
								+ "username TEXT UNIQUE, "
								+ "password TEXT, "
								+ "salt TEXT, " + "role INTEGER(1));");
			}

			resultSet = data.getTables(null, null, "projects", null);
			if (!resultSet.next()) {
				statement
						.execute("CREATE TABLE projects(id INTEGER PRIMARY KEY AUTOINCREMENT, "
								+ "name TEXT UNIQUE, "
								+ "start_date DATE, "
								+ "projected_end DATE, " + "end_date DATE);");
			}

			resultSet = data.getTables(null, null, "tasks", null);
			if (!resultSet.next()) {
				statement
						.execute("CREATE TABLE tasks(id INTEGER PRIMARY KEY AUTOINCREMENT, "
								+ "project_id INTEGER, "
								+ "name TEXT UNIQUE, "
								+ "projected_start DATE, "
								+ "actual_start DATE, "
								+ "projected_end DATE, "
								+ "actual_end DATE, "
								+ "to_do TEXT,"
								+ "FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE);");
			}

			resultSet = data.getTables(null, null, "task_reqs", null);
			if (!resultSet.next()) {
				statement
						.execute("CREATE TABLE task_reqs(id INTEGER PRIMARY KEY AUTOINCREMENT, "
								+ "task_id INTEGER, "
								+ "task_req INTEGER, "
								+ "FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE, "
								+ "FOREIGN KEY (task_req) REFERENCES tasks(id) ON DELETE CASCADE, "
								+ "UNIQUE (task_id,  task_req));");
			}

			resultSet = data.getTables(null, null, "project_users", null);
			if (!resultSet.next()) {
				statement
						.execute("CREATE TABLE project_users(id INTEGER PRIMARY KEY AUTOINCREMENT, "
								+ "project_id INTEGER, "
								+ "user_id INTEGER, "
								+ "project_role INTEGER, "
								+ "FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE, "
								+ "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, "
								+ "UNIQUE (project_id,  user_id));");
			}

			resultSet = data.getTables(null, null, "user_tasks", null);
			if (!resultSet.next()) {
				statement
						.execute("CREATE TABLE user_tasks(id INTEGER PRIMARY KEY AUTOINCREMENT, "
								+ "user_id INTEGER, "
								+ "task_id INTEGER, "
								+ "project_users INTEGER, "
								+ "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, "
								+ "FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE, "
								+ "FOREIGN KEY (project_users) REFERENCES project_users(id) ON DELETE CASCADE, "
								+ "UNIQUE(user_id,  task_id));");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// INSERTS - C

	public boolean insertUser(User user, String password) {
		boolean success = true;
		PasswordUtil util = new PasswordUtil(password);
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("INSERT INTO users (id, first_name, last_name, username, password, salt, role) VALUES(?,?,?,?,?,?,?)");
			preparedStatement.setString(1, null);
			preparedStatement.setString(2, user.getFirstName());
			preparedStatement.setString(3, user.getLastName());
			preparedStatement.setString(4, user.getUsername());
			preparedStatement.setString(5, util.getHash());
			preparedStatement.setString(6, util.getSalt());
			preparedStatement.setInt(7, user.getRole());
			int records = preparedStatement.executeUpdate();
			if (records != 1)
				success = false;
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		} finally {
			close();
			try {
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	public boolean insertProject(Project project, User user) {
		boolean success = true;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("INSERT INTO projects (id, name, start_date, projected_end, end_date) VALUES(?,?,?,?,?)");
			preparedStatement.setString(1, null);
			preparedStatement.setString(2, project.getName());
			preparedStatement.setDate(3, new java.sql.Date(project
					.getStartDate().getTime()));
			preparedStatement.setDate(4, new java.sql.Date(project
					.getProjectedEndDate().getTime()));
			preparedStatement.setDate(5, new java.sql.Date(project.getEndDate()
					.getTime()));
			int records = preparedStatement.executeUpdate();
			resultSet = statement
					.executeQuery("SELECT rowid as id FROM projects");
			ProjectUser projectUser = null;
			if (resultSet.next()) {
				
				projectUser = new ProjectUser(1, resultSet.getInt("id"),
						user.getId(), user.getRole());
				close();
				if (records != 1 || !insertProjectUser(projectUser))
					success = false;
			}
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		} finally {
			close();
			try {
				statement.close();
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	public boolean insertTask(Task task) {
		boolean success = true;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("INSERT INTO tasks (id, project_id, name, projected_start, actual_start, projected_end, actual_end, to_do) VALUES(?,?,?,?,?,?,?,?)");
			preparedStatement.setString(1, null);
			preparedStatement.setInt(2, task.getProjectId());
			preparedStatement.setString(3, task.getName());
			preparedStatement.setDate(4, new java.sql.Date(task
					.getProjectedStartDate().getTime()));
			preparedStatement.setDate(5, new java.sql.Date(task.getStartDate()
					.getTime()));
			preparedStatement.setDate(6, new java.sql.Date(task
					.getProjectedEndDate().getTime()));
			preparedStatement.setDate(7, new java.sql.Date(task.getEndDate()
					.getTime()));
			preparedStatement.setString(8, new Gson().toJson(task.getToDo()));
			int records = preparedStatement.executeUpdate();
			if (records != 1)
				success = false;
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		} finally {
			close();
			try {
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	public boolean insertProjectUser(ProjectUser projectUser) {
		boolean success = true;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("INSERT INTO project_users (id, project_id, user_id, project_role) VALUES(?,?,?,?)");
			preparedStatement.setString(1, null);
			preparedStatement.setInt(2, projectUser.getProjectId());
			preparedStatement.setInt(3, projectUser.getUserId());
			preparedStatement.setInt(4, projectUser.getProjectRole());
			System.out.println(projectUser);
			int records = preparedStatement.executeUpdate();
			if (records != 1)
				success = false;
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		} finally {
			close();
			try {
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	public boolean insertUserTask(UserTask userTask) {
		boolean success = true;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("INSERT INTO user_tasks (id, user_id, task_id, project_users) VALUES(?,?,?,?)");
			preparedStatement.setString(1, null);
			preparedStatement.setInt(2, userTask.getUserId());
			preparedStatement.setInt(3, userTask.getTaskId());
			preparedStatement.setInt(4, userTask.getProjectUsers());
			int records = preparedStatement.executeUpdate();
			if (records != 1)
				success = false;
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		} finally {
			close();
			try {
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	public boolean insertTaskRequirement(TaskRequirement taskRequirement) {
		boolean success = true;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("INSERT INTO task_reqs (id, task_id, task_req) VALUES(?,?,?)");
			preparedStatement.setString(1, null);
			preparedStatement.setInt(2, taskRequirement.getTaskId());
			preparedStatement.setInt(3, taskRequirement.getTaskReq());
			int records = preparedStatement.executeUpdate();
			if (records != 1)
				success = false;
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		} finally {
			close();
			try {
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	// GETS - R

	public ArrayList<User> getUsers() {
		ArrayList<User> users = null;
		try {
			connect();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM users");
			users = new ArrayList<User>();
			while (resultSet.next()) {
				users.add(new User(resultSet.getInt("id"), resultSet
						.getString("first_name"), resultSet
						.getString("last_name"), resultSet
						.getString("username"),
						resultSet.getInt("role")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return users;
	}

	public ArrayList<Project> getProjects() {
		ArrayList<Project> projects = null;
		try {
			connect();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM projects");
			projects = new ArrayList<Project>();
			while (resultSet.next()) {
				projects.add(new Project(resultSet.getInt("id"), resultSet
						.getString("name"), resultSet.getDate("start_date"),
						resultSet.getDate("projected_end"), resultSet
								.getDate("end_date")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return projects;
	}

	public Project getProjectByName(String name) {
		Project project = null;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("SELECT * FROM projects WHERE name = ?");
			preparedStatement.setString(1, name);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next())
				project = new Project(resultSet.getInt("id"),
						resultSet.getString("name"),
						resultSet.getDate("start_date"),
						resultSet.getDate("projected_end"),
						resultSet.getDate("end_date"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
			try {
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return project;
	}

	public ArrayList<Task> getTasks() {
		ArrayList<Task> tasks = null;
		try {
			connect();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM tasks");
			ArrayList<Task> toDo;
			tasks = new ArrayList<Task>();
			Gson gson = new Gson();
			while (resultSet.next()) {
				toDo = gson.fromJson(resultSet.getString("to_do"),
						new TypeToken<ArrayList<Task>>() {
						}.getType());
				tasks.add(new Task(resultSet.getInt("id"), resultSet
						.getInt("project_id"), resultSet.getString("name"),
						resultSet.getDate("projected_start"), resultSet
								.getDate("actual_start"), resultSet
								.getDate("projected_end"), resultSet
								.getDate("actual_end"), toDo));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tasks;
	}

	public ArrayList<ProjectUser> getProjectUsers() {
		ArrayList<ProjectUser> projectUsers = null;
		try {
			connect();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM project_users");
			projectUsers = new ArrayList<ProjectUser>();
			while (resultSet.next()) {
				projectUsers.add(new ProjectUser(resultSet.getInt("id"),
						resultSet.getInt("project_id"), resultSet
								.getInt("user_id"), resultSet
								.getInt("project_role")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return projectUsers;
	}

	// getPorjectUsers(Project project)

	public ArrayList<UserTask> getUserTasks() {
		ArrayList<UserTask> userTasks = null;
		try {
			connect();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM user_task");
			userTasks = new ArrayList<UserTask>();
			while (resultSet.next()) {
				userTasks.add(new UserTask(resultSet.getInt("id"), resultSet
						.getInt("user_id"), resultSet.getInt("task_id"),
						resultSet.getInt("project_users")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return userTasks;
	}

	// getUserTasks(User user)

	public ArrayList<TaskRequirement> getTaskRequirements() {
		ArrayList<TaskRequirement> taskReqs = null;
		try {
			connect();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM task_reqs");
			taskReqs = new ArrayList<TaskRequirement>();
			while (resultSet.next()) {
				taskReqs.add(new TaskRequirement(resultSet.getInt("id"),
						resultSet.getInt("task_id"), resultSet
								.getInt("task_req")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return taskReqs;
	}

	// getTaskRequirements(Task task)

	// UPDATES - U

	public boolean updateUser(User user) {
		boolean valid = true;
		try {
			String update = "UPDATE users SET first_name= '"
					+ user.getFirstName() + "', last_name = '"
					+ user.getLastName() + "', username = '"
					+ user.getUsername() + "', role = " + user.getRole()
					+ " WHERE id= " + user.getId();
			connect();
			preparedStatement = connection.prepareStatement(update);
			int records = preparedStatement.executeUpdate();
			if (records != 1)
				valid = false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
			try {
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return valid;
	}

	public boolean updateProject(Project project) {
		boolean valid = true;
		try {
			String update = "UPDATE projects SET name= '"
					+ project.getName()
					+ "', start_date = "
					+ new java.sql.Date(project.getStartDate().getTime())
					+ ", projected_end = "
					+ new java.sql.Date(project.getProjectedEndDate().getTime())
					+ ", end_date = "
					+ new java.sql.Date(project.getEndDate().getTime())
					+ " WHERE id= " + project.getId();
			connect();
			preparedStatement = connection.prepareStatement(update);
			int records = preparedStatement.executeUpdate();
			if (records != 1)
				valid = false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
			try {
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return valid;
	}

	public boolean updateTask(Task task) {
		boolean valid = true;
		try {
			String update = "UPDATE tasks SET project_id= "
					+ task.getProjectId() + ", name = '" + task.getName()
					+ "', projected_start = "
					+ new java.sql.Date(task.getProjectedStartDate().getTime())
					+ ", actual_start = "
					+ new java.sql.Date(task.getStartDate().getTime())
					+ ", projected_end = "
					+ new java.sql.Date(task.getProjectedEndDate().getTime())
					+ ", actual_end = "
					+ new java.sql.Date(task.getEndDate().getTime())
					+ ", to_do = '" + task.getToDo() + "' WHERE id= "
					+ task.getId();
			connect();
			preparedStatement = connection.prepareStatement(update);
			int records = preparedStatement.executeUpdate();
			if (records != 1)
				valid = false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
			try {
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return valid;
	}

	// DELETES - D

	public boolean removeUser(User user) {
		boolean removed = true;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("DELETE FROM users WHERE id = ?");
			preparedStatement.setInt(1, user.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			removed = false;
		} finally {
			close();
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return removed;
	}

	public boolean removeProject(Project project) {
		boolean removed = true;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("DELETE FROM projects WHERE id = ?");
			preparedStatement.setInt(1, project.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			removed = false;
		} finally {
			close();
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return removed;
	}

	public boolean removeTask(Task task) {
		boolean removed = true;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("DELETE FROM tasks WHERE id = ?");
			preparedStatement.setInt(1, task.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			removed = false;
		} finally {
			close();
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return removed;
	}

	// ADDITIONAL OR UTILITY METHODS

	public User login(String username, String password) {
		User user = null;
		String salt;
		String userHash;
		PasswordUtil util;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("SELECT salt FROM users WHERE username = ?");
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				salt = resultSet.getString("salt");
				util = new PasswordUtil(password, salt);
				userHash = util.getHash();
				preparedStatement = connection
						.prepareStatement("SELECT id, first_name, last_name, username, role FROM users WHERE username = ? AND password = ?");
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, userHash);
				resultSet = preparedStatement.executeQuery();
				if (resultSet.next()) {
					user = new User(resultSet.getInt("id"),
							resultSet.getString("first_name"),
							resultSet.getString("last_name"),
							resultSet.getString("username"),
							resultSet.getInt("role"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				close();
				preparedStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return user;
	}

	public void useCaseTest() {
		User user = new User(0, "Chris", "Allard", "slaiy", 1);
		insertUser(user, "password");
		System.out.println("Data insert success: " + dataInsert());
		System.out.println("Manager search and update success: "
				+ managerSearch(user.getUsername(), "password"));
	}

	private boolean dataInsert() {
		boolean success = true;
		User user = new User(0, "fName", "lName", "username", 0);
		Project project = new Project(0, "Project name", new java.sql.Date(1),
				new java.sql.Date(2), new java.sql.Date(3));
		Task task = new Task(0, 1, "Task name", new java.sql.Date(1),
				new java.sql.Date(2), new java.sql.Date(3),
				new java.sql.Date(4), new ArrayList<Task>());
		try {
			connect();
			if (!insertUser(user, "password2") || !insertProject(project, user)
					|| !insertTask(task))
				success = false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return success;
	}

	private boolean managerSearch(String username, String password) {
		boolean success = true;
		User user = login(username, password);
		ArrayList<User> users = null;
		ArrayList<Project> projects = null;
		ArrayList<Task> tasks = null;
		if (user.getRole() == 1) {
			users = getUsers();
			projects = getProjects();
			tasks = getTasks();

			users.get(0).setUsername("new username");
			projects.get(0).setName("new project name");
			tasks.get(0).setName("new task name");

			if (!updateUser(users.get(0)) || !updateProject(projects.get(0))
					|| !updateTask(tasks.get(0)))
				success = false;
		}
		return success;
	}
}