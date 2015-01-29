package obj;

/**
 * 
 * @author Christian Allard 7026188
 *
 */
public class TaskRequirement {

	private int id;
	private int taskId;
	private int taskReq;

	public TaskRequirement() {
		id = 0;
		taskId = 0;
		taskReq = 0;
	}

	public TaskRequirement(int id, int taskId, int taskReq) {
		super();
		this.id = id;
		this.taskId = taskId;
		this.taskReq = taskReq;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getTaskReq() {
		return taskReq;
	}

	public void setTaskReq(int taskReq) {
		this.taskReq = taskReq;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "TaskRequirement [id=" + id + ", taskId=" + taskId
				+ ", taskReq=" + taskReq + "]";
	}

}
