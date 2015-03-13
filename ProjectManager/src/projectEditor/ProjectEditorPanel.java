/**
 * 
 */
package projectEditor;

import javax.swing.JPanel;

import obj.Project;
import obj.ProjectUser;
import obj.Task;
import obj.User;
import taskEditor.TaskEditorPanel;
import taskEditor.ViewTaskPanel;
import userEditor.AddProjectUserPanel;
import application.ProjectManager;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JList;

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

import javax.swing.DefaultListModel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import customComponents.ProjectTableModel;
import customComponents.TaskTableModel;

import javax.swing.JSplitPane;
import javax.swing.JButton;

import dashboard.DashboardPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;

import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
	private JButton btnAddUser;
	private JButton btnViewTask;
	private JButton btnCreateGANTTChart;
	private JButton btnRemoveUser;
	private JList list;
	
	private ListSelectionModel listSelectionModel;
	private DefaultListModel listModel;

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
		
	    listModel = new DefaultListModel();
	    
	    ArrayList<User> temp = manager.db.getUsersForProject(project);
	    
	    for(int i = 0; i < temp.size(); ++i)
	    {
	    	if(temp.get(i).getId() != manager.currentUser.getId())
	    	{
	    		listModel.addElement(temp.get(i).getId() + "-" + temp.get(i).getFirstName() + " " + temp.get(i).getLastName());
	    	}
	    }

		list = new JList(listModel);
		JScrollPane listScroller = new JScrollPane(list);
		

		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.gridheight = 5;
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 1;
		gbc_list.gridy = 1;
		add(list, gbc_list);

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

		btnRemoveUser = new JButton("Remove user");
		GridBagConstraints gbc_btnRemoveUser = new GridBagConstraints();
		gbc_btnRemoveUser.insets = new Insets(0, 0, 5, 5);
		gbc_btnRemoveUser.gridx = 1;
		gbc_btnRemoveUser.gridy = 6;
		add(btnRemoveUser, gbc_btnRemoveUser);

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

		btnAddUser = new JButton("Add Users");
		btnAddUser.addActionListener(clickListener);

		btnCloseTab = new JButton("Close Tab");
		btnCloseTab.addActionListener(clickListener);

		btnViewTask = new JButton("View my tasks");
		btnViewTask.addActionListener(clickListener);
		
		btnRemoveUser.addActionListener(clickListener);

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
		add(btnAddUser, gbc_btnAddUser);

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

		projectModel.setProject(project);
	    
	    if (listModel.getSize() == 0) 
	    {
	        btnRemoveUser.setEnabled(false);
	    }

        if(manager.currentUser.getRole() == 0 ){
            btnAddTask.setVisible(false);
            btnAddUser.setVisible(false);
            btnCreateGANTTChart.setVisible(false);
            btnDeleteProject.setVisible(false);
            btnSave.setVisible(false);
            table.setVisible(false);
            lblEndDate.setVisible(false);
            lblName.setVisible(false);
            lblTaskList.setVisible(false);
            lblProjectedEndDate.setVisible(false);
            lblStartDate.setVisible(false);
            txtActualEndDate.setVisible(false);
            txtProjectedEndDate.setVisible(false);
            txtProjectName.setVisible(false);
            txtStartDate.setVisible(false);
            btnRemoveUser.setVisible(false);
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

		ProjectUser pu = null;
		ArrayList<ProjectUser> pus = manager.db.getProjectUsers();
		for (ProjectUser projUser : pus) {
			if (projUser.getProjectId() == projectModel.getProject().getId()
					&& projUser.getUserId() == manager.currentUser.getId()
					&& projUser.getProjectRole() == 1) {
				btnCreateGANTTChart.setVisible(true);
				break;
			}
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

				// add task button case
			} else if (source == btnAddTask) {
				Task t = new Task(-1, projectModel.getProject().getId(), null,
						null, null, null, null, null);
				// add tab for new task
				manager.addTab(new TaskEditorPanel(manager, t), "New Task");

				// close tab case
			} else if (source == btnAddUser) {
				manager.addTab(
						new AddProjectUserPanel(manager, projectModel
								.getProject()), "Add User to Project "
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
			} else if (source == btnRemoveUser) {
				removeUser();
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
		
		private void removeUser()
		{
			int index = list.getSelectedIndex();
			
			String name = listModel.get(index).toString();
			
			String split = new String();
			
			// manual split to character '-'
			int i = 0;
			while(name.charAt(i) != '-')
			{
				split += name.charAt(i++);
			}
			
		    listModel.remove(index);
		    
		    // only need ID to remove user
		   	ProjectUser projectUser = new ProjectUser(-1, projectModel.getProject().getId(), Integer.parseInt(split), 0);
		    manager.db.removeProjectUser(projectUser);

		    int size = listModel.getSize();

		    if (size == 0) { //Nobody's left, disable firing.
		        btnRemoveUser.setEnabled(false);

		    } else { //Select an index.
		        if (index == listModel.getSize()) {
		            //removed item in last position
		            index--;
		        }

		        list.setSelectedIndex(index);
		        list.ensureIndexIsVisible(index);
		    }
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
					.getName(), (currentTask.getProjectedStartDate() == null ? new Date()
					: currentTask.getProjectedStartDate()),
					(currentTask.getProjectedEndDate() == null ? new Date() : currentTask
							.getProjectedEndDate())));
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
					.getName(), (currentTask.getStartDate() == null ? new Date()
					: currentTask.getStartDate()),
					(currentTask.getEndDate() == null ? new Date() : currentTask
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