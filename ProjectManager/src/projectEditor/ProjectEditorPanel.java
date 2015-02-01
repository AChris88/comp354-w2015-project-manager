/**
 * 
 */
package projectEditor;

import javax.swing.JPanel;

import obj.Project;

import application.ProjectManager;

/**
 * @author George Lambadas 7077076
 *
 */
public class ProjectEditorPanel extends JPanel {

	private ProjectManager manager;
	private Project project;

	public ProjectEditorPanel(ProjectManager manager) {
		this(manager, null);
	}
	
	public ProjectEditorPanel(ProjectManager manager, Project project) {
		this.manager = manager;
		this.project = project;
	}
}
