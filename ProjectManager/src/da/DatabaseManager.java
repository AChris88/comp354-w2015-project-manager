package da;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseManager {
	/*
    private static final String CREATE_TABLE_USERS = "CREATE TABLE users(id INTEGER PRIMARY KEY, first_name TEXT, last_name TEXT);";
    private static final String CREATE_TABLE_TASKS = "CREATE TABLE tasks(id INTEGER PRIMARY KEY, name TEXT, projected_start DATE, actual_start DATE, projected_end DATE, actual_end DATE, tasks TEXT, users TEXT, to_do TEXT);";
    private static final String CREATE_TABLE_PROJECTS = "CREATE TABLE projects(id INTEGER PRIMARY KEY, name TEXT, tasks TEXT, managers TEXT, start_date DATE, projected_end DATE, end_date DATE);";
	*/	
	
	public void connect(){
		Connection connection = null;
		ResultSet resultSet = null;
		Statement statement = null;

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager
					.getConnection("jdbc:sqlite:C:\\testdb.db");
			
			
			statement = connection.createStatement();
			
			/*
			//tests table creation
			//find how to check if tables are already created
			statement.execute("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, first_name TEXT, last_name TEXT);");
			statement.execute("CREATE TABLE tasks(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, projected_start DATE, actual_start DATE, projected_end DATE, actual_end DATE, tasks TEXT, users TEXT, to_do TEXT);");
			statement.execute("CREATE TABLE projects(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, tasks TEXT, managers TEXT, start_date DATE, projected_end DATE, end_date DATE);");
			
			//tests user insertion
			statement.execute("INSERT INTO users VALUES(null,'Chris', 'Allard');");
			statement.execute("INSERT INTO users VALUES(null,'George', 'Lambadass');");
			statement.execute("INSERT INTO users VALUES(null,'Samuel', 'Leblanc');");
			*/
			
			//test table query
			resultSet = statement
					.executeQuery("SELECT * FROM users");
			
			//prints query result
			while (resultSet.next()) {
				System.out.println("id: " + resultSet.getInt("id") + " first_name:" + resultSet.getString("first_name") + " last_name:" + resultSet.getString("last_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void close(){
		
	}
}
