package taskEditor;

import java.util.Date;
import java.util.Observable;

import obj.Task;

public class TaskEditorModel extends Observable{

	private Task t;

	public void setTask(Task t) {
		this.t = t;
		setChanged();
		notifyObservers();
	}

	/**
	 * @return
	 */
	public String getTaskName() {
		return t.getName();
	}

	/**
	 * @return
	 */
	public Date getTaskProjectedStartDate() {
		return t.getProjectedStartDate();
	}
	
	/**
	 * @return
	 */
	public Date getTaskStartDate() {
		return t.getStartDate();
	}

	/**
	 * @return
	 */
	public Date getTaskProjectedEndDate() {
		return t.getProjectedEndDate();
	}

	/**
	 * @return
	 */
	public Date getTaskEndDate() {
		return t.getEndDate();
	}
}