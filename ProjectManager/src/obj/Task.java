package obj;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Task {
	private int id;
	private String name;
	private Date projectedStartDate;
	private Date startDate;
	private Date projectEndDate;
	private Date endDate;
	private Task[] prereqs;
	private User[] workers;
	private List<Task> toDo;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getProjectedStartDate() {
		return projectedStartDate;
	}
	public void setProjectedStartDate(Date projectedStartDate) {
		this.projectedStartDate = projectedStartDate;
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
	public Task[] getPrereqs() {
		return prereqs;
	}
	public void setPrereqs(Task[] prereqs) {
		this.prereqs = prereqs;
	}
	public User[] getWorkers() {
		return workers;
	}
	public void setWorkers(User[] workers) {
		this.workers = workers;
	}
	public List<Task> getToDo() {
		return toDo;
	}
	public void setToDo(List<Task> toDo) {
		this.toDo = toDo;
	}
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "Task [id=" + id + ", name=" + name + ", projectedStartDate="
				+ projectedStartDate + ", startDate=" + startDate
				+ ", projectEndDate=" + projectEndDate + ", endDate=" + endDate
				+ ", prereqs=" + Arrays.toString(prereqs) + ", workers="
				+ Arrays.toString(workers) + ", toDo=" + toDo + "]";
	}
}
