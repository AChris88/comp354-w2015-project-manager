/**
 * 
 */
package projectEditor;

import javax.swing.JPanel;

import obj.Project;

import application.ProjectManager;
import java.awt.GridBagLayout;
import javax.swing.JList;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;

import customComponents.ProjectTableModel;
import customComponents.TaskTableModel;
import javax.swing.JSplitPane;
import javax.swing.JButton;

/**
 * @author George Lambadas 7077076
 * 
 */
public class ProjectEditorPanel extends JPanel implements Observer {

	private ProjectManager manager;
	private ProjectEditorModel projectModel;
	private JTextField txtProjectName;
	private JTextField txtStartDate;
	private JTextField txtProjectedEndDate;
	private JTextField txtActualEndDate;
	private JLabel lblTaskList;
	private JTable table;
	private JSplitPane splitPane;
	private JButton btnCancel;
	private JButton btnSave;

	public ProjectEditorPanel(ProjectManager manager) {
		this(manager, null);
	}

	/**
	 * @wbp.parser.constructor
	 */
	public ProjectEditorPanel(ProjectManager manager, Project project) {
		this.manager = manager;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 3;
		gbc_lblName.gridy = 1;
		add(lblName, gbc_lblName);

		txtProjectName = new JTextField();
		txtProjectName.setText("Project Name");
		GridBagConstraints gbc_txtProjectName = new GridBagConstraints();
		gbc_txtProjectName.insets = new Insets(0, 0, 5, 0);
		gbc_txtProjectName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtProjectName.gridx = 4;
		gbc_txtProjectName.gridy = 1;
		add(txtProjectName, gbc_txtProjectName);
		txtProjectName.setColumns(10);

		JLabel lblStartDate = new JLabel("Start Date");
		GridBagConstraints gbc_lblStartDate = new GridBagConstraints();
		gbc_lblStartDate.anchor = GridBagConstraints.EAST;
		gbc_lblStartDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblStartDate.gridx = 3;
		gbc_lblStartDate.gridy = 3;
		add(lblStartDate, gbc_lblStartDate);

		txtStartDate = new JTextField();
		txtStartDate.setText("YYYY-MM-DD");
		GridBagConstraints gbc_txtYyyymmdd = new GridBagConstraints();
		gbc_txtYyyymmdd.insets = new Insets(0, 0, 5, 0);
		gbc_txtYyyymmdd.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtYyyymmdd.gridx = 4;
		gbc_txtYyyymmdd.gridy = 3;
		add(txtStartDate, gbc_txtYyyymmdd);
		txtStartDate.setColumns(10);

		JLabel lblProjectedEndDate = new JLabel("Projected End Date");
		GridBagConstraints gbc_lblProjectedEndDate = new GridBagConstraints();
		gbc_lblProjectedEndDate.anchor = GridBagConstraints.EAST;
		gbc_lblProjectedEndDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblProjectedEndDate.gridx = 3;
		gbc_lblProjectedEndDate.gridy = 4;
		add(lblProjectedEndDate, gbc_lblProjectedEndDate);

		txtProjectedEndDate = new JTextField();
		txtProjectedEndDate.setText("YYYY-MM-DD");
		GridBagConstraints gbc_txtYyyymmdd_1 = new GridBagConstraints();
		gbc_txtYyyymmdd_1.insets = new Insets(0, 0, 5, 0);
		gbc_txtYyyymmdd_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtYyyymmdd_1.gridx = 4;
		gbc_txtYyyymmdd_1.gridy = 4;
		add(txtProjectedEndDate, gbc_txtYyyymmdd_1);
		txtProjectedEndDate.setColumns(10);

		JLabel lblEndDate = new JLabel("End Date");
		GridBagConstraints gbc_lblEndDate = new GridBagConstraints();
		gbc_lblEndDate.anchor = GridBagConstraints.EAST;
		gbc_lblEndDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblEndDate.gridx = 3;
		gbc_lblEndDate.gridy = 5;
		add(lblEndDate, gbc_lblEndDate);

		txtActualEndDate = new JTextField();
		txtActualEndDate.setText("YYYY-MM-DD");
		GridBagConstraints gbc_txtYyyymmdd_2 = new GridBagConstraints();
		gbc_txtYyyymmdd_2.insets = new Insets(0, 0, 5, 0);
		gbc_txtYyyymmdd_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtYyyymmdd_2.gridx = 4;
		gbc_txtYyyymmdd_2.gridy = 5;
		add(txtActualEndDate, gbc_txtYyyymmdd_2);
		txtActualEndDate.setColumns(10);
		
		this.setBounds(100, 100, 500, 500);
		
		lblTaskList = new JLabel("Task List:");
		GridBagConstraints gbc_lblTaskList = new GridBagConstraints();
		gbc_lblTaskList.insets = new Insets(0, 0, 5, 5);
		gbc_lblTaskList.gridx = 3;
		gbc_lblTaskList.gridy = 6;
		add(lblTaskList, gbc_lblTaskList);
		
		
		TaskTableModel tableModel = new TaskTableModel();
		tableModel.populateModel(manager.db.getTasks()); //TODO get tasks for project

		table = new JTable();//TODO new JTable(tableModel);
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.gridwidth = 3;
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 2;
		gbc_table.gridy = 7;
		add(new JScrollPane(table), gbc_table);

		//projectModel = new ProjectEditorModel();
		//projectModel.setProject(project);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		txtProjectName.setText(projectModel.getProjectName());
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(projectModel.getProjectStartDate());
		txtStartDate.setText(calendar.get(Calendar.YEAR) + "-"
				+ calendar.get(Calendar.MONTH) + "-"
				+ calendar.get(Calendar.DATE));

		//set projected end field
		calendar.setTime(projectModel.getProjectProjectedEndDate());
		txtProjectedEndDate.setText(calendar.get(Calendar.YEAR) + "-"
				+ calendar.get(Calendar.MONTH) + "-"
				+ calendar.get(Calendar.DATE));

		//set actual end date if not null
		if (projectModel.getProjectEndDate() != null) {
			calendar.setTime(projectModel.getProjectEndDate());
			txtActualEndDate.setText(calendar.get(Calendar.YEAR) + "-"
					+ calendar.get(Calendar.MONTH) + "-"
					+ calendar.get(Calendar.DATE));
		}
	}
}