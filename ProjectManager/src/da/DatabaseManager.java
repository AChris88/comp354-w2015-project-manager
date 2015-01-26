package da;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
				statement.execute("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, first_name TEXT, last_name TEXT);");
			}
			
			resultSet = data.getTables(null, null, "tasks", null);
			if(!resultSet.next()) {
				statement.execute("CREATE TABLE tasks(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, projected_start DATE, actual_start DATE, projected_end DATE, actual_end DATE, prereqs TEXT, users TEXT, to_do TEXT);");
			}
			
			resultSet = data.getTables(null, null, "projects", null);
			if(!resultSet.next()) {
				statement.execute("CREATE TABLE projects(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, tasks TEXT, managers TEXT, start_date DATE, projected_end DATE, end_date DATE);");
			}
			
			resultSet = data.getTables(null, null, "project_tasks", null);
			if(!resultSet.next()) {
				statement.execute("CREATE TABLE project_tasks(id INTEGER PRIMARY KEY AUTOINCREMENT, project_id INTEGER, task_id INTEGER);");
			}
			
			resultSet = data.getTables(null, null, "task_reqs", null);
			if(!resultSet.next()) {
				statement.execute("CREATE TABLE task_reqs(id INTEGER PRIMARY KEY AUTOINCREMENT, task_id INTEGER, task_req INTEGER);");
			}
			
			resultSet = data.getTables(null, null, "user_tasks", null);
			if(!resultSet.next()) {
				statement.execute("CREATE TABLE user_tasks(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, task_id INTEGER);");
			}
			
			resultSet = data.getTables(null, null, "project_managers", null);
			if(!resultSet.next()) {
				statement.execute("CREATE TABLE project_managers(id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, project_id INTEGER);");
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try{
				close();
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean insertUser(String firstName, String lastName){
		boolean success = true;
		 connect();
		 try{
			 statement = connection.createStatement();
			 statement.execute("INSERT INTO users VALUES(null, '" + firstName + "', '" + lastName + "');");
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
}