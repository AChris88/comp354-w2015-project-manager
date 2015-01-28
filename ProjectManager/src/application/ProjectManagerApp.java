/**
 * 
 */
package application;

import java.util.ArrayList;
import java.util.Date;
import obj.Project;
import obj.Task;
import obj.User;
import util.PasswordUtil;
import dataAccess.DatabaseManager;


/**
 * @author George Lambadas 7077076
 *
 */
public class ProjectManagerApp {

	public static void main(String[] args) {
		//test user insertion
		/*
		DatabaseManager dbm = new DatabaseManager();
		User user = new User(1, "Chris", "Allard", "herp", "daDerp", "salt", 0)
		dbm.insertUser(user);
		dbm.getUsers();
		dbm.removeUser(user);
		dbm.getUsers();
		*/
		
		//test task insertion
		/*
		DatabaseManager dbm = new DatabaseManager();
		Task task = new Task(1, "Super important task", new Date(), new Date(), new Date(), new Date(), new ArrayList<Task>());
		dbm.insertTask(task);
		dbm.getTasks();
		dbm.removeTask(task);
		dbm.getTasks();
		*/
		
		//test project insertion
		/*
		DatabaseManager dbm = new DatabaseManager();
		Project project = new Project(1, "Super important project", new Date(), new Date(), new Date());
		dbm.insertProject(project);
		dbm.getProjects();
		dbm.removeProject(project);
		dbm.getProjects();
		*/
		
		//test password salting and hashing
		/*
		PasswordUtil util = new PasswordUtil("test");
		System.out.println(util.getSalt());
		System.out.println(util.getHash());
		*/
		
		//test user login
		/*
		DatabaseManager dbm = new DatabaseManager();
		dbm.insertUser(new User(0, "Chris", "Allard", "slaiy", "password", "salt", 0));
		System.out.println("Login successful: " + dbm.login("slaiy", "password"));
		*/
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ProjectManager();
				
			}
		});
	}
}
