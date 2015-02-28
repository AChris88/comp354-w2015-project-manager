package userEditor;

import java.util.Observable;

import obj.User;

public class UserEditorModel extends Observable{

	private User user;

	public void setUser(User u) {
		this.user = u;
		setChanged();
		notifyObservers();
	}

	public String getFirstName() {
		return this.user.getFirstName();
	}
	
	public String getLastName(){
		return this.user.getLastName();
	}
	
	public String getUserName(){
		return this.user.getUsername();
	}
	
	public int getId(){
		return this.user.getId();
	}
	
	public int getRole(){
		return this.user.getRole();
	}

	public User getUser() {
		return this.user;
	}
}