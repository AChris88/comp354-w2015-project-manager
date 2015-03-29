package taskEditor;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JTable;

import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import obj.Project;
import obj.ProjectUser;
import obj.Task;
import obj.User;
import obj.UserTask;
import application.ProjectManager;
import customComponents.TaskTableModel;
import customComponents.UserListModel;

import javax.swing.JButton;

import userEditor.AddUserTaskPanel;

import javax.swing.JList;


/**
 * 
 * @author Christian Allard 7026188
 * 
 */
public class TaskEditorPanel extends JPanel implements Observer {
	private TaskTableModel tableModel;
	private TaskEditorModel taskModel;
	private JTable table;
	private JTextField nameTextField;
	private JLabel lblExpectedStart;
	private JTextField expectedStartTextField;
	private JLabel lblActualStart;
	private JTextField startTextField;
	private JLabel lblExpectedEnd;
	private JTextField expectedEndTextField;
	private JLabel lblActualEnd;
	private JTextField endTextField;
	private JTabbedPane tabbedPane;
	private int tabCtr;
	private JLabel lblPrerequisites;
	private JButton btnSave;
	private JButton btnReset;
	private ProjectManager manager;
	private JButton btnChangePrerequisites;
	private JButton btnCloseTab;
	private ButtonClickListener clickListener;
	private JButton btnAddUser;
	private JTable list;
	private JButton btnRemoveUser;
	private UserListModel listModel;
	private ListSelectionModel listSelectionModel;

	/**
	 * @wbp.parser.constructor
	 */
	public TaskEditorPanel(ProjectManager manager) {
		this(manager, null);
	}

	public TaskEditorPanel(ProjectManager manager, Task task) {
		this.manager = manager;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 1.0, 0.0,
				1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		tabbedPane = new JTabbedPane();
		tabCtr = 0;
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		tabbedPane.setVisible(false);
		add(tabbedPane, gbc_tabbedPane);
		
		listModel = new UserListModel();

		listModel.populateModel(manager.db.getUsersForTask(task));

		list = new JTable(listModel);

		
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.gridheight = 6;
		gbc_list.gridwidth = 2;
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 1;
		gbc_list.gridy = 1;
		add(new JScrollPane(list), gbc_list);

		JLabel lblTakeName = new JLabel("Task Name:");
		GridBagConstraints gbc_lblTakeName = new GridBagConstraints();
		gbc_lblTakeName.anchor = GridBagConstraints.EAST;
		gbc_lblTakeName.insets = new Insets(0, 0, 5, 5);
		gbc_lblTakeName.gridx = 4;
		gbc_lblTakeName.gridy = 1;
		add(lblTakeName, gbc_lblTakeName);

		nameTextField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 5;
		gbc_textField.gridy = 1;
		add(nameTextField, gbc_textField);
		nameTextField.setColumns(10);

		lblExpectedStart = new JLabel("Expected Start:");
		GridBagConstraints gbc_lblExpectedStart = new GridBagConstraints();
		gbc_lblExpectedStart.anchor = GridBagConstraints.EAST;
		gbc_lblExpectedStart.insets = new Insets(0, 0, 5, 5);
		gbc_lblExpectedStart.gridx = 4;
		gbc_lblExpectedStart.gridy = 2;
		add(lblExpectedStart, gbc_lblExpectedStart);

		expectedStartTextField = new JTextField();
		GridBagConstraints gbc_expectedStartTextField = new GridBagConstraints();
		gbc_expectedStartTextField.insets = new Insets(0, 0, 5, 0);
		gbc_expectedStartTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_expectedStartTextField.gridx = 5;
		gbc_expectedStartTextField.gridy = 2;
		add(expectedStartTextField, gbc_expectedStartTextField);
		expectedStartTextField.setColumns(10);

		lblActualStart = new JLabel("Actual Start:");
		GridBagConstraints gbc_lblActualStart = new GridBagConstraints();
		gbc_lblActualStart.anchor = GridBagConstraints.EAST;
		gbc_lblActualStart.insets = new Insets(0, 0, 5, 5);
		gbc_lblActualStart.gridx = 4;
		gbc_lblActualStart.gridy = 3;
		add(lblActualStart, gbc_lblActualStart);

		startTextField = new JTextField();
		GridBagConstraints gbc_startTextField = new GridBagConstraints();
		gbc_startTextField.insets = new Insets(0, 0, 5, 0);
		gbc_startTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_startTextField.gridx = 5;
		gbc_startTextField.gridy = 3;
		add(startTextField, gbc_startTextField);
		startTextField.setColumns(10);

		lblExpectedEnd = new JLabel("Expected End:");
		GridBagConstraints gbc_lblExpectedEnd = new GridBagConstraints();
		gbc_lblExpectedEnd.anchor = GridBagConstraints.EAST;
		gbc_lblExpectedEnd.insets = new Insets(0, 0, 5, 5);
		gbc_lblExpectedEnd.gridx = 4;
		gbc_lblExpectedEnd.gridy = 4;
		add(lblExpectedEnd, gbc_lblExpectedEnd);

		expectedEndTextField = new JTextField();
		GridBagConstraints gbc_expectedEndTextField = new GridBagConstraints();
		gbc_expectedEndTextField.insets = new Insets(0, 0, 5, 0);
		gbc_expectedEndTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_expectedEndTextField.gridx = 5;
		gbc_expectedEndTextField.gridy = 4;
		add(expectedEndTextField, gbc_expectedEndTextField);
		expectedEndTextField.setColumns(10);

		lblActualEnd = new JLabel("Actual End:");
		GridBagConstraints gbc_lblActualEnd = new GridBagConstraints();
		gbc_lblActualEnd.anchor = GridBagConstraints.EAST;
		gbc_lblActualEnd.insets = new Insets(0, 0, 5, 5);
		gbc_lblActualEnd.gridx = 4;
		gbc_lblActualEnd.gridy = 5;
		add(lblActualEnd, gbc_lblActualEnd);

		endTextField = new JTextField();
		GridBagConstraints gbc_endTextField = new GridBagConstraints();
		gbc_endTextField.insets = new Insets(0, 0, 5, 0);
		gbc_endTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_endTextField.gridx = 5;
		gbc_endTextField.gridy = 5;
		add(endTextField, gbc_endTextField);
		endTextField.setColumns(10);

		btnSave = new JButton("Save");
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.insets = new Insets(0, 0, 5, 5);
		gbc_btnSave.gridx = 4;
		gbc_btnSave.gridy = 6;
		
		clickListener = new ButtonClickListener();
		
		btnSave.addActionListener(clickListener);
		add(btnSave, gbc_btnSave);
		
		btnAddUser = new JButton("Add Users");
		btnAddUser.addActionListener(clickListener);

		btnReset = new JButton("Reset");
		GridBagConstraints gbc_btnReset = new GridBagConstraints();
		gbc_btnReset.insets = new Insets(0, 0, 5, 0);
		gbc_btnReset.gridx = 5;
		gbc_btnReset.gridy = 6;
		// TODO add button action listener to reset fields
		add(btnReset, gbc_btnReset);
		
		btnRemoveUser = new JButton("Remove user");
		GridBagConstraints gbc_btnRemoveUser = new GridBagConstraints();
		gbc_btnRemoveUser.gridwidth = 2;
		gbc_btnRemoveUser.insets = new Insets(0, 0, 5, 5);
		gbc_btnRemoveUser.gridx = 1;
		gbc_btnRemoveUser.gridy = 7;
		add(btnRemoveUser, gbc_btnRemoveUser);
		
		btnRemoveUser.addActionListener(clickListener);

		lblPrerequisites = new JLabel("Prerequisites:");
		GridBagConstraints gbc_lblPrerequisites = new GridBagConstraints();
		gbc_lblPrerequisites.insets = new Insets(0, 0, 5, 5);
		gbc_lblPrerequisites.gridx = 3;
		gbc_lblPrerequisites.gridy = 7;
		add(lblPrerequisites, gbc_lblPrerequisites);

		tableModel = new TaskTableModel();
		tableModel.populateModel(manager.db.getTaskRequirements(task));

		table = new JTable(tableModel);
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.insets = new Insets(0, 0, 5, 0);
		gbc_table.gridwidth = 6;
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 8;
		add(new JScrollPane(table), gbc_table);

		taskModel = new TaskEditorModel();
		taskModel.addObserver(this);

		btnChangePrerequisites = new JButton("Change Prerequisites");
		btnChangePrerequisites.addActionListener(clickListener);

		GridBagConstraints gbc_btnChangePrerequisites = new GridBagConstraints();
		gbc_btnChangePrerequisites.insets = new Insets(0, 0, 5, 5);
		gbc_btnChangePrerequisites.gridx = 3;
		gbc_btnChangePrerequisites.gridy = 9;
		add(btnChangePrerequisites, gbc_btnChangePrerequisites);
		btnChangePrerequisites.setVisible(false);

		btnCloseTab = new JButton("Close Tab");
		btnCloseTab.addActionListener(clickListener);
		
		GridBagConstraints gbc_btnAddUser = new GridBagConstraints();
		gbc_btnAddUser.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddUser.gridx = 4;
		gbc_btnAddUser.gridy = 9;
		add(btnAddUser, gbc_btnAddUser);


		GridBagConstraints gbc_btnCloseTab = new GridBagConstraints();
		gbc_btnCloseTab.insets = new Insets(0, 0, 5, 0);
		gbc_btnCloseTab.gridx = 5;
		gbc_btnCloseTab.gridy = 9;
		add(btnCloseTab, gbc_btnCloseTab);

		taskModel.setTask(task);

		if (manager.projectUser.getProjectRole() != 1) {
			btnRemoveUser.setVisible(false);
			btnChangePrerequisites.setVisible(false);
			btnAddUser.setVisible(false);
			btnSave.setVisible(false);
			btnReset.setVisible(false);
			nameTextField.setEditable(false);
			endTextField.setEditable(false);
			startTextField.setEditable(false);
			expectedEndTextField.setEditable(false);
			expectedStartTextField.setEditable(false);
			list.setEnabled(false);
			
		} else {
			table.addMouseListener(((MouseListener) new DoubleClickListener()));
		}
	}

	private void addTab(Task task) {
		// ImageIcon icon = createImageIcon("images/middle.gif");

		JComponent panel1 = makeTextPanel(task.getName());
		tabbedPane.addTab(task.getName(), null, panel1, task.getName());

		int keyEvent = 0;

		switch (tabCtr) {
		case 0:
			keyEvent = KeyEvent.VK_1;
			break;
		case 1:
			keyEvent = KeyEvent.VK_2;
			break;
		case 2:
			keyEvent = KeyEvent.VK_3;
			break;
		case 3:
			keyEvent = KeyEvent.VK_4;
			break;
		case 4:
			keyEvent = KeyEvent.VK_5;
			break;
		case 5:
			keyEvent = KeyEvent.VK_6;
			break;
		}

		if (tabCtr == 0)
			tabbedPane.setVisible(true);

		// keyEvent could also be represented as (tabCtr + 49)
		tabbedPane.setMnemonicAt(tabCtr++, keyEvent);
	}

	private JComponent makeTextPanel(String text) {
		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);
		filler.setHorizontalAlignment(JLabel.CENTER);
		panel.setLayout(new GridLayout(1, 1));
		panel.add(filler);
		return panel;
	}

	private ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = ProjectManager.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		nameTextField.setText(taskModel.getTaskName());

		Calendar calendar = Calendar.getInstance();

		if (taskModel.getTaskProjectedStartDate() != null) {
			calendar.setTime(taskModel.getTaskProjectedStartDate());
			expectedStartTextField.setText(calendar.get(Calendar.YEAR) + "-"
					+ (calendar.get(Calendar.MONTH) + 1) + "-"
					+ calendar.get(Calendar.DATE));
		} else {
			expectedStartTextField.setText("YYYY-MM-DD");
		}

		if (taskModel.getTaskStartDate() != null) {
			calendar.setTime(taskModel.getTaskStartDate());
			startTextField.setText(calendar.get(Calendar.YEAR) + "-"
					+ (calendar.get(Calendar.MONTH) + 1) + "-"
					+ calendar.get(Calendar.DATE));
		} else {
			startTextField.setText("YYYY-MM-DD");
		}

		if (taskModel.getTaskProjectedEndDate() != null) {
			calendar.setTime(taskModel.getTaskProjectedEndDate());
			expectedEndTextField.setText(calendar.get(Calendar.YEAR) + "-"
					+ (calendar.get(Calendar.MONTH) + 1) + "-"
					+ calendar.get(Calendar.DATE));
		} else {
			expectedEndTextField.setText("YYYY-MM-DD");
		}

		if (taskModel.getTaskEndDate() != null) {
			calendar.setTime(taskModel.getTaskEndDate());
			endTextField.setText(calendar.get(Calendar.YEAR) + "-"
					+ (calendar.get(Calendar.MONTH) + 1) + "-"
					+ calendar.get(Calendar.DATE));
		} else {
			endTextField.setText("YYYY-MM-DD");
		}
		
		if(taskModel.getTask().getId() != -1 ) {
			btnChangePrerequisites.setVisible(true);
		}

	}

	private class DoubleClickListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				JTable target = (JTable) e.getSource();
				int row = target.getSelectedRow();
				if (tableModel.getTaskAt(row) != null) {
					manager.addTab(
							new TaskEditorPanel(manager, tableModel
									.getTaskAt(row)), "Task: "
									+ tableModel.getTaskAt(row).getName());
				}
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

			if (source == btnSave) {
				Task t = taskModel.getTask();
				t.setName(nameTextField.getText());
				
				Project p = new Project(taskModel.getTask().getProjectId(), "", new Date(0), new Date(0), new Date(0));
				ArrayList<Task> allTasks = manager.db.getTasksForProject(p);
				
//				boolean taskNameFound = false;
//				for(int i = 0; i < allTasks.size(); ++i)
//				{
//					if(allTasks.get(i).getName().equals(t.getName()))
//					{
//						taskNameFound = true;
//					}
//				}
				
//				if(!taskNameFound)
//				{
					Calendar c = Calendar.getInstance();
					String[] dateComponents = new String[3];
	
					if (expectedStartTextField.getText().equals("YYYY-MM-DD")) {
						t.setStartDate(null);
					} else {
						dateComponents = expectedStartTextField.getText()
								.split("-");
						c.set(Integer.parseInt(dateComponents[0]),
								Integer.parseInt(dateComponents[1]) - 1,
								Integer.parseInt(dateComponents[2]));
						t.setProjectedStartDate(c.getTime());
					}
	
					if (startTextField.getText().equals("YYYY-MM-DD")) {
						t.setStartDate(null);
					} else {
						dateComponents = startTextField.getText().split("-");
	
						c.set(Integer.parseInt(dateComponents[0]),
								Integer.parseInt(dateComponents[1]) - 1,
								Integer.parseInt(dateComponents[2]));
						t.setStartDate(c.getTime());
					}
	
					if (expectedEndTextField.getText().equals("YYYY-MM-DD")) {
						t.setStartDate(null);
					} else {
						dateComponents = expectedEndTextField.getText().split("-");
						c.set(Integer.parseInt(dateComponents[0]),
								Integer.parseInt(dateComponents[1]) - 1,
								Integer.parseInt(dateComponents[2]));
						t.setProjectedEndDate(c.getTime());
					}
	
					if (endTextField.getText().equals("YYYY-MM-DD")) {
						t.setStartDate(null);
					} else {
						dateComponents = endTextField.getText().split("-");
						c.set(Integer.parseInt(dateComponents[0]),
								Integer.parseInt(dateComponents[1]) - 1,
								Integer.parseInt(dateComponents[2]));
						t.setEndDate(c.getTime());
					}
	
					if (t.getId() == -1) {
						manager.db.insertTask(t);
					} else {
						manager.db.updateTask(t);
					}
	
					t = manager.db.getTaskByName(t.getName());
					taskModel.setTask(t);

//				}
			} else if (source == btnChangePrerequisites) {
				manager.addTab(
						new AlterPrerequisitesPanel(manager, taskModel
								.getTask()),
						"Prerequisites: " + taskModel.getTaskName());
			} else if (source == btnRemoveUser) {
				//TODO removeUser();
			} else if (source == btnAddUser) {
				manager.addTab(
						new AddUserTaskPanel(manager, taskModel.getTask()),
						"Users: " + taskModel.getTaskName());
			} else if (source == btnCloseTab) {
				manager.closeTab(TaskEditorPanel.this);
			}
		}
		
		/*private void removeUser()
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
		    
		    ArrayList<ProjectUser> temp = manager.db.getProjectUsers();
		    ProjectUser pu = new ProjectUser();

		    for(int j = 0; j < temp.size(); ++j)
		    {
		    	if(temp.get(j).getUserId() == Integer.parseInt(split) && temp.get(j).getProjectId() == taskModel.getTask().getProjectId())
		    	{
		    		pu = temp.get(j);
		    		break;
		    	}
		    }
		    
		    // need user id, task id, project_user id
		    UserTask ut = new UserTask(0, Integer.parseInt(split), taskModel.getTask().getId(), pu.getId());
		    manager.db.removeUserTask(ut);

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
		}*/
	}
}