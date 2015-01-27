/**
 * 
 */
package application;

import util.PasswordUtil;
import dataAccess.DatabaseManager;


/**
 * @author George Lambadas 7077076
 *
 */
public class ProjectManagerApp {

	public static void main(String[] args) {
		//test insert
		/*
		DatabaseManager dbm = new DatabaseManager();
		dbm.insertUser("Chris", "Allard", "herp", "daDerp", "salt", 0);
		dbm.getUsers();
		dbm.removeUser(1);
		dbm.getUsers();
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
		dbm.insertUser("Chris", "Allard", "slaiy", "password", "", 0);
		System.out.println("Login successful: " + dbm.login("slaiy", "password"));
		*/
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ProjectManager();
				
			}
		});
	}
}
