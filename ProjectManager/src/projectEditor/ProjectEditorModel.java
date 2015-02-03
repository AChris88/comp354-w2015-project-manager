/**
 * 
 */
package projectEditor;

import java.util.Date;
import java.util.Observable;

import obj.Project;

/**
 * @author George Lambadas 7077076
 * 
 */
public class ProjectEditorModel extends Observable {

	private Project p;

	public void setProject(Project p) {
		this.p = p;
		setChanged();
		notifyObservers();
	}

	public Project getProject() {
		return p;
	}

	public void setName(String name) {
		p.setName(name);
	}

	public void setStartDate(Date startDate) {
		p.setStartDate(startDate);
	}

	public void setProjectedEndDate(Date projectedEndDate) {
		p.setProjectedEndDate(projectedEndDate);
	}

	public void setEndDate(Date endDate) {
		p.setEndDate(endDate);
	}
}
