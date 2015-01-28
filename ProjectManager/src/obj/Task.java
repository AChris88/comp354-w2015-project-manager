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
	private String name;
	private Date projectedStartDate;
	private Date startDate;
	private Date projectedEndDate;
	private Date endDate;
	private ArrayList<Task> toDo;

	public Task(int id, String name, Date projectedStartDate, Date startDate,
			Date projectedEndDate, Date endDate, ArrayList<Task> toDo) {
		super();
		this.id = id;
		this.name = name;
		this.projectedStartDate = projectedStartDate;
		this.startDate = startDate;
		this.projectedEndDate = projectedEndDate;
		this.endDate = endDate;
		this.toDo = toDo;
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

	public ArrayList<Task> getToDo() {
		return toDo;
	}

	public void setToDo(ArrayList<Task> toDo) {
		this.toDo = toDo;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", name=" + name + ", projectedStartDate="
				+ projectedStartDate + ", startDate=" + startDate
				+ ", projectEndDate=" + projectedEndDate + ", endDate="
				+ endDate + ", toDo=" + toDo + "]";
	}
}
