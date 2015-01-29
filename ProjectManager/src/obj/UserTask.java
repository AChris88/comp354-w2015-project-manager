package obj;

/**
 * 
 * @author Christian Allard 7026188
 *
 */
public class UserTask {

	private int id;
	private int userId;
	private int taskId;
	private int projectUsers;

	public UserTask() {
		id = 0;
		userId = 0;
		taskId = 0;
		projectUsers = 0;
	}

	public UserTask(int id, int userId, int taskId, int projectUsers) {
		super();
		this.id = id;
		this.userId = userId;
		this.taskId = taskId;
		this.projectUsers = projectUsers;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getProjectUsers() {
		return projectUsers;
	}

	public void setProjectUsers(int projectUsers) {
		this.projectUsers = projectUsers;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "UserTask [id=" + id + ", userId=" + userId + ", taskId="
				+ taskId + ", projectUsers=" + projectUsers + "]";
	}

}
