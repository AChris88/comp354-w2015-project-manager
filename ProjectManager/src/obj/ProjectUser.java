package obj;

/**
 * 
 * @author Christian Allard 7026188
 *
 */
public class ProjectUser {

	private int id;
	private int projectId;
	private int userId;
	private int projectRole;

	public ProjectUser() {
		id = 0;
		projectId = 0;
		userId = 0;
		projectRole = 0;
	}

	public ProjectUser(int id, int projectId, int userId, int projectRole) {
		super();
		this.id = id;
		this.projectId = projectId;
		this.userId = userId;
		this.projectRole = projectRole;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getProjectRole() {
		return projectRole;
	}

	public void setProjectRole(int projectRole) {
		this.projectRole = projectRole;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "ProjectUser [id=" + id + ", projectId=" + projectId
				+ ", projectRole=" + projectRole + "]";
	}

}
