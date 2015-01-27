package dataAccess;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import util.PasswordUtil;

/**
 * 
 * @author Christian Allard 7026188
 *
 */
public class DatabaseManager {
	private Connection connection;
	private ResultSet resultSet;
	private Statement statement;

	public DatabaseManager(){
		connection = null;
		resultSet = null;
		statement = null;
		createTables();
	}
	
	private void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:C:\\testdb.db");
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

	private void createTables(){
		try{
			connect();
			//checking if tables are created
			DatabaseMetaData data = connection.getMetaData();
			statement = connection.createStatement();
			
			resultSet = data.getTables(null, null, "users", null);
			if(!resultSet.next()) {
				statement.execute("CREATE TABLE users(	id INTEGER PRIMARY KEY AUTOINCREMENT, first_name TEXT, last_name TEXT, username TEXT UNIQUE, password TEXT, salt TEXT, role INTEGER(1));");
			}
			
			resultSet = data.getTables(null, null, "tasks", null);
			if(!resultSet.next()) {
				statement.execute("CREATE TABLE tasks(	id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE, projected_start DATE, actual_start DATE, projected_end DATE, actual_end DATE, to_do TEXT);");
			}
			
			resultSet = data.getTables(null, null, "projects", null);
			if(!resultSet.next()) {
				statement.execute("CREATE TABLE projects(	id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, start_date DATE, projected_end DATE, end_date DATE);");
			}
			
			resultSet = data.getTables(null, null, "project_tasks", null);
			if(!resultSet.next()) {
				statement.execute("CREATE TABLE project_tasks(	id INTEGER PRIMARY KEY AUTOINCREMENT, project_id INTEGER, task_id INTEGER, FOREIGN KEY (project_id) REFERENCES projects(id), FOREIGN KEY (task_id) REFERENCES tasks(id));");
			}
			
			resultSet = data.getTables(null, null, "task_reqs", null);
			if(!resultSet.next()) {
				statement.execute("CREATE TABLE task_reqs(	id INTEGER PRIMARY KEY AUTOINCREMENT, task_id INTEGER, task_req INTEGER, FOREIGN KEY (task_id) REFERENCES tasks(id), FOREIGN KEY (task_req) REFERENCES tasks(id), UNIQUE (task_id,  task_req));");
			}
			
			resultSet = data.getTables(null, null, "project_users", null);
			if(!resultSet.next()) {
				statement.execute("CREATE TABLE project_users(	id INTEGER PRIMARY KEY AUTOINCREMENT, project_id INTEGER, user_id INTEGER, project_role INTEGER, FOREIGN KEY (project_id) REFERENCES projects(id), FOREIGN KEY (user_id) REFERENCES users(id), UNIQUE (project_id,  user_id));");
			}
			
			resultSet = data.getTables(null, null, "user_tasks", null);
			if(!resultSet.next()) {
				statement.execute("CREATE TABLE user_tasks(	id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, task_id INTEGER, project_users INTEGER, FOREIGN KEY (user_id) REFERENCES users(id), FOREIGN KEY (task_id) REFERENCES tasks(id), FOREIGN KEY (project_users) REFERENCES project_users(id), UNIQUE(user_id,  task_id));");
			}
			
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			close();
			try{
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean insertUser(String firstName, String lastName, String username, String password, String salt, int role){
		boolean success = true;
		 connect();
		 PasswordUtil util = new PasswordUtil(password);
		 try{
			 statement = connection.createStatement();
			 statement.execute("INSERT INTO users VALUES(null, '" + firstName + "', '" + lastName + "', '" + username + "', '" + util.getHash() + "', '" + util.getSalt() + "', " + role +");");
		 } catch (Exception e){
			 success = false;
			 e.printStackTrace();
		 } finally{
			 close();
			 try{
				 statement.close();
			 } catch (Exception e){
				 e.printStackTrace();
			 }
		 }
		return success;
	}
	
	public void getUsers(){
		connect();
		try{
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM users");
			System.out.println("User:");
			while(resultSet.next()){
				System.out.println(resultSet.getString("first_name") + ", " + resultSet.getString("last_name"));
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally{
			close();
			try{
				statement.close();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void removeUser(int id){
		connect();
		try{
			statement = connection.createStatement();
			statement.execute("DELETE FROM users WHERE id = " + id + ";");
		} catch (Exception e){
			e.printStackTrace();
		} finally{
			close();
			try{
				statement.close();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public boolean login(String username, String password){
		boolean valid = false;
		String salt;
		String hash;
		String userHash;
		PasswordUtil util;
		
		connect();
		try{
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM users WHERE username = '" + username + "';");
			salt = resultSet.getString("salt");
			hash = resultSet.getString("password");
			util = new PasswordUtil(password, salt);
			userHash = util.getHash();
			if(hash.equals(userHash))
				valid = true;
		} catch (Exception e){
			e.printStackTrace();
		} finally{
			close();
			try{
				statement.close();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return valid;
	}
}