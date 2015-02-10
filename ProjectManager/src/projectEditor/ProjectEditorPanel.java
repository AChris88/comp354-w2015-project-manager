/**
 * 
 */
package projectEditor;

import javax.swing.JPanel;

import obj.Project;
import obj.Task;
import taskEditor.TaskEditorPanel;
import application.ProjectManager;

import java.awt.GridBagLayout;

import javax.swing.JList;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;

import customComponents.ProjectTableModel;
import customComponents.TaskTableModel;

import javax.swing.JSplitPane;
import javax.swing.JButton;

import dashboard.DashboardPanel;

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
	private TaskTableModel tableModel;
	private JButton btnCloseTab;
	private JButton btnSave;
	private JButton btnAddTask;
	private JButton btnDeleteProject;
	private ButtonClickListener clickListener;

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
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 1.0, 0.0, Double.MIN_VALUE };
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

		this.setBounds(100, 100, 500, 450);

		lblTaskList = new JLabel("Task List:");
		GridBagConstraints gbc_lblTaskList = new GridBagConstraints();
		gbc_lblTaskList.insets = new Insets(0, 0, 5, 5);
		gbc_lblTaskList.gridx = 3;
		gbc_lblTaskList.gridy = 6;
		add(lblTaskList, gbc_lblTaskList);

		tableModel = new TaskTableModel();
		tableModel.populateModel(manager.db.getTasksForProject(project));
		table = new JTable(tableModel);

		table.addMouseListener(((MouseListener) new DoubleClickListener()));
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.insets = new Insets(0, 0, 5, 0);
		gbc_table.gridwidth = 3;
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 2;
		gbc_table.gridy = 7;
		add(new JScrollPane(table), gbc_table);

		projectModel = new ProjectEditorModel();
		projectModel.addObserver(this);

		clickListener = new ButtonClickListener();

		btnSave = new JButton("Save");
		btnSave.addActionListener(clickListener);

		btnAddTask = new JButton("Add Task");
		btnAddTask.addActionListener(clickListener);

		btnCloseTab = new JButton("Close Tab");
		btnCloseTab.addActionListener(clickListener);
		
		btnDeleteProject = new JButton("Delete Project");
		btnDeleteProject.addActionListener(clickListener);
		GridBagConstraints gbc_btnDeleteProject = new GridBagConstraints();
		gbc_btnDeleteProject.insets = new Insets(0, 0, 0, 5);
		gbc_btnDeleteProject.gridx = 1;
		gbc_btnDeleteProject.gridy = 8;
		add(btnDeleteProject, gbc_btnDeleteProject);
		btnDeleteProject.setVisible(false);
		
		GridBagConstraints gbc_btnCloseTab = new GridBagConstraints();
		gbc_btnCloseTab.insets = new Insets(0, 0, 0, 5);
		gbc_btnCloseTab.gridx = 2;
		gbc_btnCloseTab.gridy = 8;
		add(btnCloseTab, gbc_btnCloseTab);

		GridBagConstraints gbc_btnAddTask = new GridBagConstraints();
		gbc_btnAddTask.insets = new Insets(0, 0, 0, 5);
		gbc_btnAddTask.gridx = 3;
		gbc_btnAddTask.gridy = 8;
		add(btnAddTask, gbc_btnAddTask);
		btnAddTask.setVisible(false);
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.gridx = 4;
		gbc_btnSave.gridy = 8;
		add(btnSave, gbc_btnSave);

		projectModel.setProject(project);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		txtProjectName.setText(projectModel.getProject().getName());

		Calendar calendar = Calendar.getInstance();

		if (projectModel.getProject().getStartDate() != null) {
			calendar.setTime(projectModel.getProject().getStartDate());
			txtStartDate.setText(calendar.get(Calendar.YEAR) + "-"
					+ (calendar.get(Calendar.MONTH) + 1) + "-"
					+ calendar.get(Calendar.DATE));
		} else {
			txtStartDate.setText("YYYY-MM-DD");
		}

		// set projected end field
		if (projectModel.getProject().getProjectedEndDate() != null) {
			calendar.setTime(projectModel.getProject().getProjectedEndDate());
			txtProjectedEndDate.setText(calendar.get(Calendar.YEAR) + "-"
					+ (calendar.get(Calendar.MONTH) + 1) + "-"
					+ calendar.get(Calendar.DATE));
		} else {
			txtProjectedEndDate.setText("YYYY-MM-DD");
		}

		// set actual end date if not null
		if (projectModel.getProject().getEndDate() != null) {
			calendar.setTime(projectModel.getProject().getEndDate());
			txtActualEndDate.setText(calendar.get(Calendar.YEAR) + "-"
					+ (calendar.get(Calendar.MONTH) + 1) + "-"
					+ calendar.get(Calendar.DATE));
		} else {
			txtActualEndDate.setText("YYYY-MM-DD");
		}
		
		if ( projectModel.getProject().getId() != -1) {
			btnAddTask.setVisible(true);
			btnDeleteProject.setVisible(true);
		}
	}

	/**
	 * Listener class for button clicking
	 * 
	 * @author George Lambadas 7077076
	 * 
	 */
	private class ButtonClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();

			// save button case
			if (source == btnSave) {
				Project p = projectModel.getProject();
				p.setName(txtProjectName.getText());

				Calendar c = Calendar.getInstance();
				String[] dateComponents = new String[3];

				if (txtStartDate.getText().equals("YYYY-MM-DD")) {
					p.setStartDate(null);
				} else {
					dateComponents = txtStartDate.getText().split("-");
					c.set(Integer.parseInt(dateComponents[0]),
							Integer.parseInt(dateComponents[1]) - 1,
							Integer.parseInt(dateComponents[2]));
					p.setStartDate(c.getTime());
				}

				if (txtProjectedEndDate.getText().equals("YYYY-MM-DD")) {
					p.setProjectedEndDate(null);
				} else {
					dateComponents = txtProjectedEndDate.getText().split("-");
					c.set(Integer.parseInt(dateComponents[0]),
							Integer.parseInt(dateComponents[1]) - 1,
							Integer.parseInt(dateComponents[2]));
					p.setProjectedEndDate(c.getTime());
				}

				if (txtActualEndDate.getText().equals("YYYY-MM-DD")) {
					p.setEndDate(null);
				} else {
					dateComponents = txtActualEndDate.getText().split("-");
					c.set(Integer.parseInt(dateComponents[0]),
							Integer.parseInt(dateComponents[1]) - 1,
							Integer.parseInt(dateComponents[2]));
					p.setEndDate(c.getTime());
				}

				projectModel.setProject(p);

				if (p.getId() == -1) {
					manager.db.insertProject(p, manager.currentUser);
				} else {
					manager.db.updateProject(p);
				}

				//add task button case
			} else if (source == btnAddTask) {
				Task t = new Task(-1, projectModel.getProject().getId(), null,
						null, null, null, null, null);
				//add tab for new task
				manager.addTab(new TaskEditorPanel(manager, t), "New Task");

				//close tab case
			} else if (source == btnCloseTab) {
				manager.setActivePanel(new DashboardPanel(manager),
						manager.currentUser.getFirstName() + "'s Dashboard");
				
				//delete project case
			} else if (source == btnDeleteProject) {
				ArrayList<Task> tasks = manager.db.getTasksForProject(projectModel.getProject());
				for(int i = 0; i < tasks.size(); i++) {
					manager.db.removeTask(tasks.get(i));
				}
				manager.db.removeProject(projectModel.getProject());
				manager.setActivePanel(new DashboardPanel(manager), manager.currentUser.getFirstName() + "'s Dashboard");
			}
		}
	}

	private class DoubleClickListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				JTable target = (JTable) e.getSource();
				int row = target.getSelectedRow();
				if (tableModel.getTaskAt(row) != null)
					manager.addTab(
							new TaskEditorPanel(manager, tableModel
									.getTaskAt(row)), "Task: "
									+ tableModel.getTaskAt(row).getName());

			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

	}
}
