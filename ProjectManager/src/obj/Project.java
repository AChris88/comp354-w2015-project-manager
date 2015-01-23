package obj;

import java.util.Arrays;
import java.util.Date;

public class Project {
	private int id;
	private String name;
	private Task[] tasks;
	private User[] managers;
	private Date startDate;
	private Date projectEndDate;
	private Date endDate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Task[] getTasks() {
		return tasks;
	}
	public void setTasks(Task[] tasks) {
		this.tasks = tasks;
	}
	public User[] getManagers() {
		return managers;
	}
	public void setManagers(User[] managers) {
		this.managers = managers;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getProjectEndDate() {
		return projectEndDate;
	}
	public void setProjectEndDate(Date projectEndDate) {
		this.projectEndDate = projectEndDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", tasks="
				+ Arrays.toString(tasks) + ", managers="
				+ Arrays.toString(managers) + ", startDate=" + startDate
				+ ", projectEndDate=" + projectEndDate + ", endDate=" + endDate
				+ "]";
	}
}
