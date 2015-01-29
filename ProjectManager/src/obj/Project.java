package obj;

import java.util.Date;

/**
 * 
 * @author Christian Allard 7026188
 *
 */
public class Project {
	private int id;
	private String name;
	private Date startDate;
	private Date projectedEndDate;
	private Date endDate;

	public Project() {
		id = 0;
		name = "";
		startDate = null;
		projectedEndDate = null;
		endDate = null;
	}

	public Project(int id, String name, Date startDate, Date projectedEndDate,
			Date endDate) {
		super();
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.projectedEndDate = projectedEndDate;
		this.endDate = endDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", startDate="
				+ startDate + ", projectedEndDate=" + projectedEndDate
				+ ", endDate=" + endDate + "]";
	}

}
