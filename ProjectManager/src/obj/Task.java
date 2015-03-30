package obj;

import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * @author Christian Allard 7026188
 *
 */
public class Task {
	private int id;
	private int projectId;
	private String name;
	private Date projectedStartDate;
	private Date startDate;
	private Date projectedEndDate;
	private Date endDate;
	private int value;

	public Task() {
		id = 0;
		projectId = 0;
		name = "";
		projectedStartDate = null;
		startDate = null;
		projectedEndDate = null;
		endDate = null;
		value = 0;
	}

	public Task(int id, int projectId, String name, Date projectedStartDate,
			Date startDate, Date projectedEndDate, Date endDate,
			int value) {
		super();
		this.id = id;
		this.projectId = projectId;
		this.name = name;
		this.projectedStartDate = projectedStartDate;
		this.startDate = startDate;
		this.projectedEndDate = projectedEndDate;
		this.endDate = endDate;
		this.value = value;
	}

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

	public Date getProjectedEndDate() {
		return projectedEndDate;
	}

	public void setProjectedEndDate(Date projectedEndDate) {
		this.projectedEndDate = projectedEndDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getId() {
		return id;
	}

    public void setId(int id) { this.id= id;}

	public int getProjectId() {
		return projectId;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", projectId=" + projectId + ", name=" + name
				+ ", projectedStartDate=" + projectedStartDate + ", startDate="
				+ startDate + ", projectedEndDate=" + projectedEndDate
				+ ", endDate=" + endDate + ", value=" + value + "]";
	}
}