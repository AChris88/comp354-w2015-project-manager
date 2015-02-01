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
		//test user update
		
		DatabaseManager dbm = new DatabaseManager();
		User user = new User(1, "Chris", "Allard", "herp", "salt", 0);
		dbm.insertUser(user, "password");
		System.out.println(dbm.getUsers().get(0));
		user.setFirstName("George");
		user.setLastName("Lambadass");
		dbm.updateUser(user);
		System.out.println(dbm.getUsers().get(0));
		
		
		//test project update
		/*
		DatabaseManager dbm = new DatabaseManager();
		Project project = new Project(1, "Super important project", new Date(), new Date(), new Date());
		User user = new User(0, "Chris", "Allard", "slaiy", "salt", 0);
		dbm.insertProject(project, user);
		System.out.println(dbm.getProjects().get(0));
		project.setName("Even more important project");
		Date newEndDate = new Date();
		newEndDate.setTime(0);
		project.setProjectedEndDate(newEndDate);
		dbm.updateProject(project);
		System.out.println(dbm.getProjects().get(0));
		*/
		
		//test task update
		/*
		DatabaseManager dbm = new DatabaseManager();
		Task task = new Task(1, 1, "Super important task", new Date(), new Date(), new Date(), new Date(), new ArrayList<Task>());
		dbm.insertTask(task);
		System.out.println(dbm.getTasks().get(0));
		task.setName("even more important task");
		Date newDate = new Date();
		newDate.setTime(0);
		task.setEndDate(newDate);
		dbm.updateTask(task);
		System.out.println(dbm.getTasks().get(0));*/
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
		dbm.insertUser(new User(0, "Chris", "Allard", "slaiy", "salt", 0), "password");
		System.out.println("Login successful: " + dbm.login("slaiy", "password"));
		*/
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ProjectManager();
				
			}
		});
	}
}
