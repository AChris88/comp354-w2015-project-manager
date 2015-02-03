/**
 * 
 */
package taskEditor;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;

import application.ProjectManager;

import obj.Task;

import java.awt.Insets;

/**
 * @author George Lambadas 7077076
 *
 */
public class AlterPrerequisitesPanel extends JPanel{
	private JTable allTasksTable;
	private JTable prerequisitesTable;
	
	public AlterPrerequisitesPanel(ProjectManager manager, Task task) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.5, 0.0, 0.5, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblAllTasks = new JLabel("All Tasks");
		GridBagConstraints gbc_lblAllTasks = new GridBagConstraints();
		gbc_lblAllTasks.insets = new Insets(0, 0, 5, 5);
		gbc_lblAllTasks.gridx = 2;
		gbc_lblAllTasks.gridy = 2;
		add(lblAllTasks, gbc_lblAllTasks);
		
		JLabel lblPrerequisiteTasks = new JLabel("Prerequisite Tasks:");
		GridBagConstraints gbc_lblPrerequisiteTasks = new GridBagConstraints();
		gbc_lblPrerequisiteTasks.insets = new Insets(0, 0, 5, 5);
		gbc_lblPrerequisiteTasks.gridx = 4;
		gbc_lblPrerequisiteTasks.gridy = 2;
		add(lblPrerequisiteTasks, gbc_lblPrerequisiteTasks);
		
		allTasksTable = new JTable();
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.insets = new Insets(0, 0, 0, 5);
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 2;
		gbc_table.gridy = 3;
		add(allTasksTable, gbc_table);
		
		prerequisitesTable = new JTable();
		GridBagConstraints gbc_prerequisitesTable = new GridBagConstraints();
		gbc_prerequisitesTable.insets = new Insets(0, 0, 5, 5);
		gbc_prerequisitesTable.gridx = 0;
		gbc_prerequisitesTable.gridy = 0;
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 4;
		gbc_table.gridy = 3;
		add(prerequisitesTable, gbc_prerequisitesTable);
	}

}
