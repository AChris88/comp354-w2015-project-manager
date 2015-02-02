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
	private int role;

	public User() {
		id = 0;
		firstName = "";
		lastName = "";
		username = "";
		role = 0;
	}

	// The id is useless for inserts!
	public User(int id, String firstName, String lastName, String username,
			int role) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
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
				+ lastName + ", username=" + username + ", role=" + role + "]";
	}
}
