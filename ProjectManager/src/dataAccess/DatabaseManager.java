package dataAccess;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

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
 * @author Christian Allard 27026188
 */
public class DatabaseManager {
	private Connection connection;
	private ResultSet resultSet;
	private PreparedStatement preparedStatement;
	private Statement statement;
	private String dbName;

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
	
	//sqlite connector
	
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
	
	
	//mysql connector
	/*
	 private void connect() {
		try {
			String url = "jdbc:mysql://localhost:3306/testdb";
			String user = "root";
			String password = "";
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
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
								+ "name TEXT, "
								+ "projected_start DATE, "
								+ "actual_start DATE, "
								+ "projected_end DATE, "
								+ "actual_end DATE, "
								+ "value INTEGER,"
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
					.prepareStatement(
							"INSERT INTO users (id, first_name, last_name, username, password, salt, role) VALUES(?,?,?,?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, null);
			preparedStatement.setString(2, user.getFirstName());
			preparedStatement.setString(3, user.getLastName());
			preparedStatement.setString(4, user.getUsername());
			preparedStatement.setString(5, util.getHash());
			preparedStatement.setString(6, util.getSalt());
			preparedStatement.setInt(7, user.getRole());
			int records = preparedStatement.executeUpdate();

			if (records != 0) {
				ResultSet gk = preparedStatement.getGeneratedKeys();
				if (gk.next()) {
					user.setId((int) gk.getLong(1));
				}
			} else {
				success = false;
			}
			// if (records != 1)
			// success = false;
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
					.prepareStatement(
							"INSERT INTO projects (id, name, start_date, projected_end, end_date) VALUES(?,?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, null);
			preparedStatement.setString(2, project.getName());
			preparedStatement.setDate(3,
					project.getStartDate() != null ? new java.sql.Date(project
							.getStartDate().getTime()) : null);
			preparedStatement.setDate(4,
					project.getProjectedEndDate() != null ? new java.sql.Date(
							project.getProjectedEndDate().getTime()) : null);
			preparedStatement.setDate(5,
					project.getEndDate() != null ? new java.sql.Date(project
							.getEndDate().getTime()) : null);
			int records = preparedStatement.executeUpdate();

			if (records != 0) {
				ResultSet gk = preparedStatement.getGeneratedKeys();
				if (gk.next()) {
					project.setId((int) gk.getLong(1));
				}
			}

			ProjectUser projectUser = null;

			projectUser = new ProjectUser(1, project.getId(), user.getId(),
					user.getRole());
			close();
			if (records != 1 || !insertProjectUser(projectUser))
				success = false;
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
					.prepareStatement(
							"INSERT INTO tasks (id, project_id, name, projected_start, actual_start, projected_end, actual_end, value) VALUES(?,?,?,?,?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, null);
			preparedStatement.setInt(2, task.getProjectId());
			preparedStatement.setString(3, task.getName());
			preparedStatement.setDate(4,
					task.getProjectedStartDate() != null ? new java.sql.Date(
							task.getProjectedStartDate().getTime()) : null);
			preparedStatement.setDate(5,
					task.getStartDate() != null ? new java.sql.Date(task
							.getStartDate().getTime()) : null);
			preparedStatement.setDate(6,
					task.getProjectedEndDate() != null ? new java.sql.Date(task
							.getProjectedEndDate().getTime()) : null);
			preparedStatement.setDate(7,
					task.getEndDate() != null ? new java.sql.Date(task
							.getEndDate().getTime()) : null);
			preparedStatement.setInt(8, task.getValue());
			int records = preparedStatement.executeUpdate();

			if (records != 0) {
				ResultSet gk = preparedStatement.getGeneratedKeys();
				if (gk.next()) {
					task.setId((int) gk.getLong(1));
				}
			} else {
				success = false;
			}
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
					.prepareStatement(
							"INSERT INTO project_users (id, project_id, user_id, project_role) VALUES(?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, null);
			preparedStatement.setInt(2, projectUser.getProjectId());
			preparedStatement.setInt(3, projectUser.getUserId());
			preparedStatement.setInt(4, projectUser.getProjectRole());

			int records = preparedStatement.executeUpdate();
			if (records != 0) {
				ResultSet gk = preparedStatement.getGeneratedKeys();
				if (gk.next()) {
					projectUser.setId((int) gk.getLong(1));
				}
			} else {
				success = false;
			}
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
					.prepareStatement(
							"INSERT INTO user_tasks (id, user_id, task_id, project_users) VALUES(?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, null);
			preparedStatement.setInt(2, userTask.getUserId());
			preparedStatement.setInt(3, userTask.getTaskId());
			preparedStatement.setInt(4, userTask.getProjectUsers());
			int records = preparedStatement.executeUpdate();
			if (records != 0) {
				ResultSet gk = preparedStatement.getGeneratedKeys();
				if (gk.next()) {
					userTask.setId((int) gk.getLong(1));
				}
			} else {
				success = false;
			}
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
					.prepareStatement(
							"INSERT INTO task_reqs (id, task_id, task_req) VALUES(?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, null);
			preparedStatement.setInt(2, taskRequirement.getTaskId());
			preparedStatement.setInt(3, taskRequirement.getTaskReq());
			int records = preparedStatement.executeUpdate();
			if (records != 0) {
				ResultSet gk = preparedStatement.getGeneratedKeys();
				if (gk.next()) {
					taskRequirement.setId((int) gk.getLong(1));
				}
			} else {
				success = false;
			}
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
						.getString("username"), resultSet.getInt("role")));
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

	/**
	 * @param p project
	 * @param currentUser user
	 * 
	 * @return project-user relationship
	 */
	public ProjectUser getProjectUser(Project p, User u) {
		ProjectUser projectUser = null;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("select * FROM project_users WHERE project_id = ? AND user_id = ?");
			preparedStatement.setInt(1, p.getId());
			preparedStatement.setInt(2, u.getId());
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				projectUser = new ProjectUser(resultSet.getInt("id"),
						resultSet.getInt("project_id"), resultSet
								.getInt("user_id"), resultSet
								.getInt("project_role"));
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
		
		return projectUser;
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

	public Project getProjectByID(int id) {
		Project project = null;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("SELECT * FROM projects WHERE id = ?");
			preparedStatement.setInt(1, id);
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
			tasks = new ArrayList<Task>();
			while (resultSet.next()) {
				tasks.add(new Task(resultSet.getInt("id"), resultSet
						.getInt("project_id"), resultSet.getString("name"),
						resultSet.getDate("projected_start"), resultSet
								.getDate("actual_start"), resultSet
								.getDate("projected_end"), resultSet
								.getDate("actual_end"), resultSet.getInt("value")));
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

	public ArrayList<Task> getTasksForUser(User u) {
		ArrayList<Task> tasks = null;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("SELECT t.* FROM tasks t INNER JOIN user_tasks ut ON ut.task_id = t.id  WHERE ut.user_id = ?");
			preparedStatement.setInt(1, u.getId());
			resultSet = preparedStatement.executeQuery();
			
			tasks = new ArrayList<Task>();
			while (resultSet.next()) {
				tasks.add(new Task(resultSet.getInt("id"), resultSet
						.getInt("project_id"), resultSet.getString("name"),
						resultSet.getDate("projected_start"), resultSet
								.getDate("actual_start"), resultSet
								.getDate("projected_end"), resultSet
								.getDate("actual_end"), resultSet.getInt("value")));
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
	public ArrayList<Task> getTasksForProjectUser(ProjectUser pu) {
		ArrayList<Task> tasks = null;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("SELECT t.* FROM tasks t INNER JOIN user_tasks ut ON ut.task_id = t.id WHERE ut.project_users = ?");
			preparedStatement.setInt(1, pu.getId());
			resultSet = preparedStatement.executeQuery();
			
			tasks = new ArrayList<Task>();
			while (resultSet.next()) {
				tasks.add(new Task(resultSet.getInt("id"), resultSet
						.getInt("project_id"), resultSet.getString("name"),
						resultSet.getDate("projected_start"), resultSet
								.getDate("actual_start"), resultSet
								.getDate("projected_end"), resultSet
								.getDate("actual_end"), resultSet.getInt("value")));
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

	public ArrayList<UserTask> getUserTasks() {
		ArrayList<UserTask> userTasks = null;
		try {
			connect();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM user_tasks");
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

	/**
	 * Method used to get all of the tasks of a project that are not prerequisites to any other tasks
	 * @param p project in question
	 * @return project's tasks that are not prerequisites to anything
	 */
	public ArrayList<Task> getBottomLevelTasks(Project p) {
		Task req = null;
		ArrayList<Task> tasks = null;
		try {
			connect();
			preparedStatement = connection.prepareStatement("select * from tasks where project_id = ? AND id not in (select task_req from task_reqs)");
			preparedStatement.setInt(1, p.getId());
			resultSet = preparedStatement.executeQuery();
			
			tasks = new ArrayList<Task>();
			while (resultSet.next()) {
				tasks.add(new Task(
						resultSet.getInt("id"),
						resultSet.getInt("project_id"),
						resultSet.getString("name"),
						resultSet.getDate("projected_start") != null ? new java.sql.Date(
								resultSet.getDate("projected_start").getTime())
								: null,
						resultSet.getDate("actual_start") != null ? new java.sql.Date(
								resultSet.getDate("actual_start").getTime())
								: null,
						resultSet.getDate("projected_end") != null ? new java.sql.Date(
								resultSet.getDate("projected_end").getTime())
								: null,
						resultSet.getDate("actual_end") != null ? new java.sql.Date(
								resultSet.getDate("actual_end").getTime())
								: null, resultSet.getInt("value")));
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
	
	/**
	 * Method that returns all potential prerequisites for a given task. That
	 * is, all tasks that can be made prerequisites without causing circular
	 * dependency.
	 * 
	 * @param task
	 *            the task for which to get the prerequisites
	 * @return list of all tasks that could be made prerequisites
	 */
	public ArrayList<Task> getPotentialPrerequisites(Task t) {
		ArrayList<Task> tasks = null;
		Set<Integer> listOfIds = (Set<Integer>) new TreeSet<Integer>();
		listOfIds.add(t.getId());

		try {
			connect();
			int previousSize;
			String idString;

			// get all ids that would cause circular dependency
			do {
				previousSize = listOfIds.size();
				idString = "";
				Iterator<Integer> iterator = listOfIds.iterator();

				for (; iterator.hasNext();) {
					idString += iterator.next() + ", ";
				}
				idString = idString.substring(0, idString.length() - 2);

				preparedStatement = connection
						.prepareStatement("select task_id from task_reqs where task_req in ("
								+ idString + ")");

				resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					listOfIds.add(resultSet.getInt("task_id"));
				}
			} while (previousSize != listOfIds.size());

			Iterator<Integer> iterator = listOfIds.iterator();
			idString = "";
			for (; iterator.hasNext();) {
				idString += iterator.next() + ", ";
			}
			idString = idString.substring(0, idString.length() - 2);

			preparedStatement = connection
					.prepareStatement("select * from tasks where id not in ("
							+ idString
							+ ") AND id not in (select task_req from task_reqs where task_id = ?) AND project_id = ?");
			preparedStatement.setInt(1, t.getId());
			preparedStatement.setInt(2, t.getProjectId());

			resultSet = preparedStatement.executeQuery();
			tasks = new ArrayList<Task>();
			while (resultSet.next()) {
						tasks.add(new Task(
						resultSet.getInt("id"),
						resultSet.getInt("project_id"),
						resultSet.getString("name"),
						resultSet.getDate("projected_start") != null ? new java.sql.Date(
								resultSet.getDate("projected_start").getTime())
								: null,
						resultSet.getDate("actual_start") != null ? new java.sql.Date(
								resultSet.getDate("actual_start").getTime())
								: null,
						resultSet.getDate("projected_end") != null ? new java.sql.Date(
								resultSet.getDate("projected_end").getTime())
								: null,
						resultSet.getDate("actual_end") != null ? new java.sql.Date(
								resultSet.getDate("actual_end").getTime())
								: null, resultSet.getInt("value")));
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

	public ArrayList<UserTask> getUserTasksForUser(User user, ProjectUser p) {
		ArrayList<UserTask> userTasks = null;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("SELECT * FROM user_tasks WHERE user_id = ? AND project_users = ?");
			preparedStatement.setInt(1, user.getId());
			preparedStatement.setInt(2, p.getId());
			resultSet = preparedStatement.executeQuery();
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

	public ArrayList<Task> getTasksForProject(Project project) {
		ArrayList<Task> tasks = null;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("SELECT * FROM tasks WHERE project_id = ?");
			preparedStatement.setInt(1, project.getId());
			resultSet = preparedStatement.executeQuery();
			tasks = new ArrayList<Task>();
			while (resultSet.next()) {
				tasks.add(new Task(
						resultSet.getInt("id"),
						resultSet.getInt("project_id"),
						resultSet.getString("name"),
						resultSet.getDate("projected_start") != null ? new java.sql.Date(
								resultSet.getDate("projected_start").getTime())
								: null,
						resultSet.getDate("actual_start") != null ? new java.sql.Date(
								resultSet.getDate("actual_start").getTime())
								: null,
						resultSet.getDate("projected_end") != null ? new java.sql.Date(
								resultSet.getDate("projected_end").getTime())
								: null,
						resultSet.getDate("actual_end") != null ? new java.sql.Date(
								resultSet.getDate("actual_end").getTime())
								: null, resultSet.getInt("value")));
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

	public Task getTaskByName(String taskName) {
		Task t = null;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("SELECT * FROM tasks WHERE name = ?");
			preparedStatement.setString(1, taskName);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				t = new Task(
						resultSet.getInt("id"),
						resultSet.getInt("project_id"),
						resultSet.getString("name"),
						resultSet.getDate("projected_start") != null ? new java.sql.Date(
								resultSet.getDate("projected_start").getTime())
								: null,
						resultSet.getDate("actual_start") != null ? new java.sql.Date(
								resultSet.getDate("actual_start").getTime())
								: null,
						resultSet.getDate("projected_end") != null ? new java.sql.Date(
								resultSet.getDate("projected_end").getTime())
								: null,
						resultSet.getDate("actual_end") != null ? new java.sql.Date(
								resultSet.getDate("actual_end").getTime())
								: null, resultSet.getInt("value"));
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
		return t;
	}

	public ArrayList<Project> getProjectForUsers(User user) {
		ArrayList<Project> projects = null;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("SELECT projects.id, projects.name, projects.start_date, projects.projected_end, projects.end_date FROM projects INNER JOIN project_users ON project_users.project_id = projects.id WHERE project_users.user_id = ?");
			preparedStatement.setInt(1, user.getId());
			resultSet = preparedStatement.executeQuery();
			projects = new ArrayList<Project>();
			while (resultSet.next()) {
				projects.add(new Project(
						resultSet.getInt("id"),
						resultSet.getString("name"),
						resultSet.getDate("start_date") != null ? new java.sql.Date(
								resultSet.getDate("start_date").getTime())
								: null,
						resultSet.getDate("projected_end") != null ? new java.sql.Date(
								resultSet.getDate("projected_end").getTime())
								: null,
						resultSet.getDate("end_date") != null ? new java.sql.Date(
								resultSet.getDate("end_date").getTime()) : null));
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

	public ArrayList<User> getUsersForProject(Project project) {
		ArrayList<User> users = null;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("SELECT users.id, users.first_name, users.last_name, users.username, users.role FROM users INNER JOIN project_users ON project_users.user_id = users.id WHERE project_users.project_id = ?");
			preparedStatement.setInt(1, project.getId());
			resultSet = preparedStatement.executeQuery();
			users = new ArrayList<User>();
			while (resultSet.next()) {
				users.add(new User(resultSet.getInt("id"), resultSet
						.getString("first_name"), resultSet
						.getString("last_name"), resultSet
						.getString("username"), resultSet.getInt("role")));
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

	public ArrayList<User> getUsersForTask(Task task) {
		ArrayList<User> users = null;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("SELECT users.id, users.first_name, users.last_name, users.username, users.role FROM users INNER JOIN user_tasks ON users.id = user_tasks.user_id WHERE user_tasks.task_id = ?");
			preparedStatement.setInt(1, task.getId());
			resultSet = preparedStatement.executeQuery();
			users = new ArrayList<User>();
			while (resultSet.next()) {
				users.add(new User(resultSet.getInt("id"), resultSet
						.getString("first_name"), resultSet
						.getString("last_name"), resultSet
						.getString("username"), resultSet.getInt("role")));
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

	public ArrayList<Task> getTaskRequirements(Task task) {
		ArrayList<Task> tasks = null;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("SELECT * FROM tasks WHERE  id in (SELECT task_req FROM task_reqs WHERE task_id = ?)");
			preparedStatement.setInt(1, task.getId());
			resultSet = preparedStatement.executeQuery();
			tasks = new ArrayList<Task>();
			while (resultSet.next()) {
				tasks.add(new Task(
						resultSet.getInt("id"),
						resultSet.getInt("project_id"),
						resultSet.getString("name"),
						resultSet.getDate("projected_start") != null ? new java.sql.Date(
								resultSet.getDate("projected_start").getTime())
								: null,
						resultSet.getDate("actual_start") != null ? new java.sql.Date(
								resultSet.getDate("actual_start").getTime())
								: null,
						resultSet.getDate("projected_end") != null ? new java.sql.Date(
								resultSet.getDate("projected_end").getTime())
								: null,
						resultSet.getDate("actual_end") != null ? new java.sql.Date(
								resultSet.getDate("actual_end").getTime())
								: null, resultSet.getInt("value")));
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
	public boolean updateUser(User user, String password) {
		boolean valid = true;
		PasswordUtil util = new PasswordUtil(password);
		try {
			String update = "UPDATE users SET first_name= '"
				+ user.getFirstName() + "', last_name = '"
				+ user.getLastName() + "', username = '"
				+ user.getUsername() + "', role = " 
				+ user.getRole() + ", password = '" 
				+ util.getHash() + "', salt = '" 
				+ util.getSalt() + "' "
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
			String update = "UPDATE projects SET name= '" + project.getName()
					+ "', start_date = ?" + ", projected_end = ?"
					+ ", end_date = ?" + " WHERE id= ?";
			connect();

			preparedStatement = connection.prepareStatement(update);
			preparedStatement.setDate(1,
					project.getStartDate() != null ? new java.sql.Date(project
							.getStartDate().getTime()) : null);
			preparedStatement.setDate(2,
					project.getProjectedEndDate() != null ? new java.sql.Date(
							project.getProjectedEndDate().getTime()) : null);
			preparedStatement.setDate(3,
					project.getEndDate() != null ? new java.sql.Date(project
							.getEndDate().getTime()) : null);
			preparedStatement.setInt(4, project.getId());

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

	public boolean updateProjectUser(ProjectUser projectUser) {
		boolean valid = true;
		try {
			String update = "UPDATE project_users SET project_id = ?, user_id = ?, project_role = ? WHERE id = ?";
			connect();

			preparedStatement = connection.prepareStatement(update);
			preparedStatement.setInt(1, projectUser.getProjectId());
			preparedStatement.setInt(2, projectUser.getUserId());
			preparedStatement.setInt(3, projectUser.getProjectRole());
			preparedStatement.setInt(4, projectUser.getId());

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
			String update = "UPDATE tasks SET project_id= ?" + ", name = ?"
					+ ", projected_start = ?" + ", actual_start = ?"
					+ ", projected_end = ?" + ", actual_end = ?" + ", value = "
					+ task.getValue() + " WHERE id= ?";
			connect();
			preparedStatement = connection.prepareStatement(update);
			preparedStatement.setInt(1, task.getProjectId());
			preparedStatement.setString(2, task.getName());
			preparedStatement.setDate(3,
					(task.getProjectedStartDate() != null ? new java.sql.Date(
							task.getProjectedStartDate().getTime()) : null));
			preparedStatement.setDate(4,
					(task.getStartDate() != null ? new java.sql.Date(task
							.getStartDate().getTime()) : null));
			preparedStatement.setDate(5,
					(task.getProjectedEndDate() != null ? new java.sql.Date(
							task.getProjectedEndDate().getTime()) : null));
			preparedStatement.setDate(6,
					(task.getEndDate() != null ? new java.sql.Date(task
							.getEndDate().getTime()) : null));
			preparedStatement.setInt(7, task.getId());

			int records = preparedStatement.executeUpdate();
			if (records != 1)
				valid = false;
		} catch (Exception e) {
			valid = false;
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

	public boolean removeProjectUser(ProjectUser projectUser) {
		boolean removed = true;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("DELETE FROM project_users WHERE project_id = ? AND user_id = ?");
			preparedStatement.setInt(1, projectUser.getProjectId());
			preparedStatement.setInt(2, projectUser.getUserId());
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

	public boolean removeTaskRequirement(TaskRequirement tr) {
		boolean removed = true;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("DELETE FROM task_reqs WHERE task_id = ? AND task_req = ?");
			preparedStatement.setInt(1, tr.getTaskId());
			preparedStatement.setInt(2, tr.getTaskReq());
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

	public boolean removeUserTask(UserTask ut) {
		boolean removed = true;
		try {
			connect();
			preparedStatement = connection
					.prepareStatement("DELETE FROM user_tasks WHERE user_id = ? AND task_id = ? AND project_users = ?");
			preparedStatement.setInt(1, ut.getUserId());
			preparedStatement.setInt(2, ut.getTaskId());
			preparedStatement.setInt(3, ut.getProjectUsers());
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
}