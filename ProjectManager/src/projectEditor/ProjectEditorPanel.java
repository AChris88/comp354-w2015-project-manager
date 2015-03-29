/**
 * 
 */
package projectEditor;

import javax.swing.JPanel;

import obj.Project;
import obj.Task;
import obj.User;
import taskEditor.TaskEditorPanel;
import taskEditor.ViewTaskPanel;
import userEditor.AddProjectUserPanel;
import application.ProjectManager;

import java.awt.Color;
import java.awt.GridBagLayout;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;

import customComponents.TaskTableModel;
import customComponents.UserListModel;

import javax.swing.JButton;

import dashboard.DashboardPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

/**
 * @author George Lambadas 7077076
 * 
 */
public class ProjectEditorPanel extends JPanel implements Observer {

	private static final long serialVersionUID = 4103549863558485657L;
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
	private JButton btnAddRemoveUser;
	private JButton btnViewTask;
	private JButton btnCreateGANTTChart;
	private JTable list;
	private ArrayList<User> projectUsers;

	private UserListModel listModel;
	public JLabel errorMessageLabel;

	public ProjectEditorPanel(ProjectManager manager) {
		this(manager, null);
	}

	/**
	 * @wbp.parser.constructor
	 */
	public ProjectEditorPanel(ProjectManager manager, Project project) {
		this.manager = manager;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 1.0,
				0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 1.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		listModel = new UserListModel();
		projectUsers = manager.db.getUsersForProject(project);
		listModel.populateModel(projectUsers);
		
		list = new JTable(listModel);

		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.gridheight = 5;
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 1;
		gbc_list.gridy = 1;
		add(new JScrollPane(list), gbc_list);

		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 4;
		gbc_lblName.gridy = 1;
		add(lblName, gbc_lblName);

		txtProjectName = new JTextField();
		txtProjectName.setText("Project Name");
		GridBagConstraints gbc_txtProjectName = new GridBagConstraints();
		gbc_txtProjectName.insets = new Insets(0, 0, 5, 0);
		gbc_txtProjectName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtProjectName.gridx = 6;
		gbc_txtProjectName.gridy = 1;
		add(txtProjectName, gbc_txtProjectName);
		txtProjectName.setColumns(10);

		JLabel lblStartDate = new JLabel("Start Date");
		GridBagConstraints gbc_lblStartDate = new GridBagConstraints();
		gbc_lblStartDate.anchor = GridBagConstraints.EAST;
		gbc_lblStartDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblStartDate.gridx = 4;
		gbc_lblStartDate.gridy = 3;
		add(lblStartDate, gbc_lblStartDate);

		txtStartDate = new JTextField();
		txtStartDate.setText("YYYY-MM-DD");
		GridBagConstraints gbc_txtYyyymmdd = new GridBagConstraints();
		gbc_txtYyyymmdd.insets = new Insets(0, 0, 5, 0);
		gbc_txtYyyymmdd.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtYyyymmdd.gridx = 6;
		gbc_txtYyyymmdd.gridy = 3;
		add(txtStartDate, gbc_txtYyyymmdd);
		txtStartDate.setColumns(10);

		JLabel lblProjectedEndDate = new JLabel("Projected End Date");
		GridBagConstraints gbc_lblProjectedEndDate = new GridBagConstraints();
		gbc_lblProjectedEndDate.anchor = GridBagConstraints.EAST;
		gbc_lblProjectedEndDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblProjectedEndDate.gridx = 4;
		gbc_lblProjectedEndDate.gridy = 4;
		add(lblProjectedEndDate, gbc_lblProjectedEndDate);

		txtProjectedEndDate = new JTextField();
		txtProjectedEndDate.setText("YYYY-MM-DD");
		GridBagConstraints gbc_txtYyyymmdd_1 = new GridBagConstraints();
		gbc_txtYyyymmdd_1.insets = new Insets(0, 0, 5, 0);
		gbc_txtYyyymmdd_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtYyyymmdd_1.gridx = 6;
		gbc_txtYyyymmdd_1.gridy = 4;
		add(txtProjectedEndDate, gbc_txtYyyymmdd_1);
		txtProjectedEndDate.setColumns(10);

		JLabel lblEndDate = new JLabel("End Date");
		GridBagConstraints gbc_lblEndDate = new GridBagConstraints();
		gbc_lblEndDate.anchor = GridBagConstraints.EAST;
		gbc_lblEndDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblEndDate.gridx = 4;
		gbc_lblEndDate.gridy = 5;
		add(lblEndDate, gbc_lblEndDate);

		txtActualEndDate = new JTextField();
		txtActualEndDate.setText("YYYY-MM-DD");
		GridBagConstraints gbc_txtYyyymmdd_2 = new GridBagConstraints();
		gbc_txtYyyymmdd_2.insets = new Insets(0, 0, 5, 0);
		gbc_txtYyyymmdd_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtYyyymmdd_2.gridx = 6;
		gbc_txtYyyymmdd_2.gridy = 5;
		add(txtActualEndDate, gbc_txtYyyymmdd_2);
		txtActualEndDate.setColumns(10);

		this.setBounds(100, 100, 500, 450);

		lblTaskList = new JLabel("Task List:");
		GridBagConstraints gbc_lblTaskList = new GridBagConstraints();
		gbc_lblTaskList.insets = new Insets(0, 0, 5, 5);
		gbc_lblTaskList.gridx = 4;
		gbc_lblTaskList.gridy = 6;
		add(lblTaskList, gbc_lblTaskList);

		tableModel = new TaskTableModel();
		tableModel.populateModel(manager.db.getTasksForProject(project));
		table = new JTable(tableModel);

		table.addMouseListener(((MouseListener) new DoubleClickListener()));
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.insets = new Insets(0, 0, 5, 0);
		gbc_table.gridwidth = 5;
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

		btnAddRemoveUser = new JButton("Add/Remove Users");
		btnAddRemoveUser.addActionListener(clickListener);

		btnCloseTab = new JButton("Close Tab");
		btnCloseTab.addActionListener(clickListener);

		btnViewTask = new JButton("View my tasks");
		btnViewTask.addActionListener(clickListener);

		btnCreateGANTTChart = new JButton("Create GANTT chart");
		btnCreateGANTTChart.addActionListener(clickListener);

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

		GridBagConstraints gbc_btnAddUser = new GridBagConstraints();
		gbc_btnAddUser.insets = new Insets(0, 0, 0, 5);
		gbc_btnAddUser.gridx = 3;
		gbc_btnAddUser.gridy = 8;
		add(btnAddRemoveUser, gbc_btnAddUser);

		GridBagConstraints gbc_btnAddTask = new GridBagConstraints();
		gbc_btnAddTask.insets = new Insets(0, 0, 0, 5);
		gbc_btnAddTask.gridx = 4;
		gbc_btnAddTask.gridy = 8;
		add(btnAddTask, gbc_btnAddTask);
		btnAddTask.setVisible(false);

		GridBagConstraints gbc_btnViewTask = new GridBagConstraints();
		gbc_btnViewTask.insets = new Insets(0, 0, 0, 5);
		gbc_btnViewTask.gridx = 5;
		gbc_btnViewTask.gridy = 8;
		add(btnViewTask, gbc_btnViewTask);

		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.gridx = 6;
		gbc_btnSave.gridy = 8;
		add(btnSave, gbc_btnSave);

		GridBagConstraints gbc_btnCreateGANTTChart = new GridBagConstraints();
		gbc_btnCreateGANTTChart.insets = new Insets(0, 0, 5, 5);
		gbc_btnCreateGANTTChart.gridx = 1;
		gbc_btnCreateGANTTChart.gridy = 0;
		add(btnCreateGANTTChart, gbc_btnCreateGANTTChart);
		btnCreateGANTTChart.setVisible(true);
		
		errorMessageLabel = new JLabel();
		errorMessageLabel.setForeground(Color.RED);
		GridBagConstraints gbc_errorMessageLabel = new GridBagConstraints();
		gbc_errorMessageLabel.insets = new Insets(0, 0, 5, 5);
		gbc_errorMessageLabel.gridwidth = 3;
		gbc_errorMessageLabel.gridx = 2;
		gbc_errorMessageLabel.gridy = 0;
		add(errorMessageLabel, gbc_errorMessageLabel);

		projectModel.setProject(project);

		if (project.getId() != -1 && manager.projectUser.getProjectRole() != 1) {
			btnAddTask.setVisible(false);
			btnAddRemoveUser.setVisible(false);
			btnCreateGANTTChart.setVisible(false);
			btnDeleteProject.setVisible(false);
			btnSave.setVisible(false);
			table.setEnabled(false);
			txtActualEndDate.setEditable(false);
			txtProjectedEndDate.setEditable(false);
			txtProjectName.setEditable(false);
			txtStartDate.setEditable(false);
			btnCreateGANTTChart.setVisible(false);
			list.setEnabled(false);
		}
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

		if (projectModel.getProject().getId() != -1) {
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

				Calendar start = Calendar.getInstance(), projectedEnd = Calendar
						.getInstance(), end = Calendar.getInstance();

				String[] dateComponents = new String[3];

				if (txtStartDate.getText().equals("YYYY-MM-DD") || txtStartDate.getText().equals("") ) {
					p.setStartDate(null);
				} else {
					dateComponents = txtStartDate.getText().split("-");
					start.set(Integer.parseInt(dateComponents[0]),
							Integer.parseInt(dateComponents[1]) - 1,
							Integer.parseInt(dateComponents[2]));
					p.setStartDate(start.getTime());
				}

				if (txtProjectedEndDate.getText().equals("YYYY-MM-DD") || txtProjectedEndDate.getText().equals("")) {
					p.setProjectedEndDate(null);
				} else {
					dateComponents = txtProjectedEndDate.getText().split("-");
					projectedEnd.set(Integer.parseInt(dateComponents[0]),
							Integer.parseInt(dateComponents[1]) - 1,
							Integer.parseInt(dateComponents[2]));
					p.setProjectedEndDate(projectedEnd.getTime());
				}

				if (txtActualEndDate.getText().equals("YYYY-MM-DD") || txtActualEndDate.getText().equals("")) {
					p.setEndDate(null);
				} else {
					dateComponents = txtActualEndDate.getText().split("-");
					end.set(Integer.parseInt(dateComponents[0]),
							Integer.parseInt(dateComponents[1]) - 1,
							Integer.parseInt(dateComponents[2]));
					p.setEndDate(end.getTime());
				}

				projectModel.setProject(p);
				if (validDates(start, projectedEnd, end)) {
					errorMessageLabel.setText("");
					if (p.getId() == -1) {
						manager.db.insertProject(p, manager.currentUser);
					} else {
						manager.db.updateProject(p);
					}
				} else {
					errorMessageLabel.setText("Date conflict exists. Project not saved.");
				}
				// add task button case
			} else if (source == btnAddTask) {
				Task t = new Task(-1, projectModel.getProject().getId(), null,
						null, null, null, null, null);
				// add tab for new task
				manager.addTab(new TaskEditorPanel(manager, t), "New Task");

				// close tab case
			} else if (source == btnAddRemoveUser) {
				manager.addTab(
						new AddProjectUserPanel(manager, projectModel
								.getProject(), listModel), "Add User to Project "
								+ projectModel.getProject().getName());

				// close tab case
			} else if (source == btnCloseTab) {
				manager.setActivePanel(new DashboardPanel(manager),
						manager.currentUser.getFirstName() + "'s Dashboard");

				// delete project case
			} else if (source == btnDeleteProject) {
				ArrayList<Task> tasks = manager.db
						.getTasksForProject(projectModel.getProject());
				for (int i = 0; i < tasks.size(); i++) {
					manager.db.removeTask(tasks.get(i));
				}
				manager.db.removeProject(projectModel.getProject());
				manager.setActivePanel(new DashboardPanel(manager),
						manager.currentUser.getFirstName() + "'s Dashboard");
			} else if (source == btnViewTask) {
				manager.addTab(new ViewTaskPanel(manager, manager.currentUser),
						"Assigned tasks");
			} else if (source == btnCreateGANTTChart) {
				final IntervalCategoryDataset dataSet = createDataset();

				createGANTTChart(dataSet);
				IntervalCategoryDataset dataset = createDataset();
				JFreeChart chart = createGANTTChart(dataset);

				JPanel ganttPanel = new JPanel();
				ganttPanel.setLayout(new java.awt.BorderLayout());
				ChartPanel CP = new ChartPanel(chart);
				ganttPanel.add(btnCloseTab);
				ganttPanel.add(CP);
				ganttPanel.validate();

				manager.addTab(ganttPanel, "GANTT Chart");
			}
		}

		/**
		 * @param end
		 * @param projectedEnd
		 * @param start
		 * @return
		 */
		private boolean validDates(Calendar start, Calendar projectedEnd,
				Calendar end) {
			boolean valid = true;

			// start date is set
			if (start != Calendar.getInstance()) {
				// projectedEnd date is set
				if (projectedEnd != Calendar.getInstance()) {
					// start date must be before projectedEnd
					valid &= (start.getTimeInMillis() < projectedEnd
							.getTimeInMillis());
				}
				// end date is set
				if (end != Calendar.getInstance()) {
					// projectedEnd must be set and start must be before end
					valid &= (projectedEnd != Calendar.getInstance() && start
							.getTimeInMillis() < end.getTimeInMillis());
				}
			} else {
				// no other dates should be set
				valid &= (projectedEnd == Calendar.getInstance() && projectedEnd == end);
			}

			return valid;
		}
	}

	// This method is related to the creation of a dataset used for GANTT chart
	// creation
	public IntervalCategoryDataset createDataset() {

		// A task series with the planned tasks dates on the series.
		TaskSeries plannedSeries = new TaskSeries("Planned Implementation");

		// Gain access to the project's tasks in order to iterate through it
		ArrayList<Task> currentTasks = manager.db
				.getTasksForProject(projectModel.getProject());
		Task currentTask = null;
		for (int i = 0; i < currentTasks.size(); i++) {
			// Access the current task
			currentTask = manager.db.getTaskByName(currentTasks.get(i)
					.getName());

			// Add each individual task with its name, projected start date and
			// end date

			// int id, int projectId, String name, Date projectedStartDate,
			// Date startDate, Date projectedEndDate, Date endDate,
			// String toDo

			plannedSeries.add(new org.jfree.data.gantt.Task(currentTask
					.getName(),
					(currentTask.getProjectedStartDate() == null ? new Date()
							: currentTask.getProjectedStartDate()),
					(currentTask.getProjectedEndDate() == null ? new Date()
							: currentTask.getProjectedEndDate())));
		}

		// A task series with the actual tasks dates on the series.
		TaskSeries actualSeries = new TaskSeries("Actual Implementation");
		for (int i = 0; i < currentTasks.size(); i++) {
			// Access the current task
			currentTask = manager.db.getTaskByName(currentTasks.get(i)
					.getName());

			// Add each individual task with its name, projected start date and
			// end date
			actualSeries.add(new org.jfree.data.gantt.Task(currentTask
					.getName(),
					(currentTask.getStartDate() == null ? new Date()
							: currentTask.getStartDate()), (currentTask
							.getEndDate() == null ? new Date() : currentTask
							.getEndDate())));
		}

		// Create a final object of type task series collection
		final TaskSeriesCollection collection = new TaskSeriesCollection();

		// Add both series to the collection
		collection.add(plannedSeries);
		collection.add(actualSeries);

		return collection;
	}

	// Creates a GANTT chart based on both the planned and actual series
	private JFreeChart createGANTTChart(final IntervalCategoryDataset dataset) {
		final JFreeChart chart = ChartFactory.createGanttChart(
				"Gantt Chart for Project", // chart title
				"Task", // domain axis label
				"Date", // range axis label
				dataset, // data
				true, // include legend
				true, // tooltips
				false // urls
				);

		return chart;

	}

	// This utility saves the JFreeChart as a JPEG First Parameter:FileName,
	// Second Parameter: Chart To Save, Third Parameter: Height Of Picture,
	// Fourth Parameter: Width Of Picture
	public void saveChart(JFreeChart chart, String fileLocation) {
		String fileName = fileLocation;

		try {
			ChartUtilities.saveChartAsJPEG(new File(fileName), chart, 800, 600);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Problem occurred creating chart.");
		}
	}

	private class DoubleClickListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				JTable target = (JTable) e.getSource();
				int row = target.getSelectedRow();
				if (table.isEnabled() && tableModel.getTaskAt(row) != null)
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