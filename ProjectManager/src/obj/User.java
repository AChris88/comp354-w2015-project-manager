package obj;

/**
 * 
 * @author Christian Allard 7026188
 *
 */
public class User {
	private int id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String salt;
	private int role;

	public User(int id, String firstName, String lastName, String username,
			String password, String salt, int role) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.salt = salt;
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public String getSalt() {
		return salt;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", username=" + username + ", password="
				+ password + ", salt=" + salt + ", role=" + role + "]";
	}
}
