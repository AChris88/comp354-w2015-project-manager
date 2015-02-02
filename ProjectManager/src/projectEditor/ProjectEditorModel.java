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
		notifyObservers();
	}

	/**
	 * @return
	 */
	public String getProjectName() {
		return p.getName();
	}

	/**
	 * @return
	 */
	public Date getProjectStartDate() {
		return p.getStartDate();
	}

	/**
	 * @return
	 */
	public Date getProjectProjectedEndDate() {
		return p.getProjectedEndDate();
	}

	/**
	 * @return
	 */
	public Date getProjectEndDate() {
		return p.getEndDate();
	}

}
