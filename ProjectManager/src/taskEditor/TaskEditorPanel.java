package taskEditor;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JTable;

import java.awt.Color;
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
	private JButton btnAddRemoveUser;
	private JTable list;
	private UserListModel listModel;
	private JLabel errorMessageLabel;

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

		errorMessageLabel = new JLabel("");
		errorMessageLabel.setForeground(Color.RED);
		GridBagConstraints gbc_errorMessageLabel = new GridBagConstraints();
		gbc_errorMessageLabel.anchor = GridBagConstraints.EAST;
		gbc_errorMessageLabel.insets = new Insets(0, 0, 5, 5);
		gbc_errorMessageLabel.gridx = 3;
		gbc_errorMessageLabel.gridy = 1;
		add(errorMessageLabel, gbc_errorMessageLabel);

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

		btnAddRemoveUser = new JButton("Add/Remove Users");
		btnAddRemoveUser.addActionListener(clickListener);

		btnReset = new JButton("Reset");
		GridBagConstraints gbc_btnReset = new GridBagConstraints();
		gbc_btnReset.insets = new Insets(0, 0, 5, 0);
		gbc_btnReset.gridx = 5;
		gbc_btnReset.gridy = 6;
		// TODO add button action listener to reset fields
		add(btnReset, gbc_btnReset);

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
		add(btnAddRemoveUser, gbc_btnAddUser);

		GridBagConstraints gbc_btnCloseTab = new GridBagConstraints();
		gbc_btnCloseTab.insets = new Insets(0, 0, 5, 0);
		gbc_btnCloseTab.gridx = 5;
		gbc_btnCloseTab.gridy = 9;
		add(btnCloseTab, gbc_btnCloseTab);

		taskModel.setTask(task);

		if (manager.projectUser.getProjectRole() != 1) {
			btnChangePrerequisites.setVisible(false);
			btnAddRemoveUser.setVisible(false);
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

		if (taskModel.getTask().getId() != -1) {
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

				Project p = new Project(taskModel.getTask().getProjectId(), "",
						new Date(0), new Date(0), new Date(0));

				Calendar c = Calendar.getInstance();
				Calendar expectedStart = Calendar.getInstance(), start = Calendar
						.getInstance(), expectedEnd = Calendar.getInstance(), end = Calendar
						.getInstance();
				String[] dateComponents = new String[3];

				if (expectedStartTextField.getText().equals("YYYY-MM-DD")
						|| expectedStartTextField.getText().equals("")) {
					t.setProjectedStartDate(null);
				} else {
					dateComponents = expectedStartTextField.getText()
							.split("-");
					expectedStart.set(Integer.parseInt(dateComponents[0]),
							Integer.parseInt(dateComponents[1]) - 1,
							Integer.parseInt(dateComponents[2]));
					t.setProjectedStartDate(expectedStart.getTime());
				}

				if (startTextField.getText().equals("YYYY-MM-DD")
						|| startTextField.getText().equals("")) {
					t.setStartDate(null);
				} else {
					dateComponents = startTextField.getText().split("-");

					start.set(Integer.parseInt(dateComponents[0]),
							Integer.parseInt(dateComponents[1]) - 1,
							Integer.parseInt(dateComponents[2]));
					t.setStartDate(start.getTime());
				}

				if (expectedEndTextField.getText().equals("YYYY-MM-DD")
						|| expectedEndTextField.getText().equals("")) {
					t.setProjectedEndDate(null);
				} else {
					dateComponents = expectedEndTextField.getText().split("-");
					expectedEnd.set(Integer.parseInt(dateComponents[0]),
							Integer.parseInt(dateComponents[1]) - 1,
							Integer.parseInt(dateComponents[2]));
					t.setProjectedEndDate(expectedEnd.getTime());
				}

				if (endTextField.getText().equals("YYYY-MM-DD")
						|| endTextField.getText().equals("")) {
					t.setEndDate(null);
				} else {
					dateComponents = endTextField.getText().split("-");
					end.set(Integer.parseInt(dateComponents[0]),
							Integer.parseInt(dateComponents[1]) - 1,
							Integer.parseInt(dateComponents[2]));
					t.setEndDate(end.getTime());
				}

				if (t.getName() != "") {
					if (validDates(expectedStart, start, expectedEnd, end)) {
						errorMessageLabel.setText("");
						boolean success = true;

						if (t.getId() == -1) {
							success = manager.db.insertTask(t);
						} else {
							success = manager.db.updateTask(t);
						}

						if (success) {
							errorMessageLabel.setText("");
						} else {
							errorMessageLabel
									.setText("Task with this name already exists.");
						}
					} else {
						errorMessageLabel
								.setText("Date conflict exists. Task not saved.");
					}
				} else {
					errorMessageLabel.setText("Need a name. Task not saved.");
				}
				taskModel.setTask(t);

			} else if (source == btnChangePrerequisites) {
				manager.addTab(
						new AlterPrerequisitesPanel(manager, taskModel
								.getTask(), tableModel), "Prerequisites: "
								+ taskModel.getTaskName());
			} else if (source == btnAddRemoveUser) {
				manager.addTab(
						new AddUserTaskPanel(manager, taskModel.getTask(),
								listModel), "Users: " + taskModel.getTaskName());
			} else if (source == btnCloseTab) {
				manager.closeTab(TaskEditorPanel.this);
			}
		}

		/**
		 * @param end
		 * @param projectedEnd
		 * @param start
		 * @param projectedStart
		 * @return
		 */
		private boolean validDates(Calendar projectedStart, Calendar start,
				Calendar projectedEnd, Calendar end) {
			boolean valid = true;
			if (end != Calendar.getInstance()) {
				valid &= projectedStart != Calendar.getInstance();
				valid &= start != Calendar.getInstance();
				valid &= projectedEnd != Calendar.getInstance();
				valid &= start.getTimeInMillis() < projectedEnd
						.getTimeInMillis();
				valid &= start.getTimeInMillis() < end.getTimeInMillis();
				valid &= projectedStart.getTimeInMillis() < projectedEnd
						.getTimeInMillis();
				valid &= projectedStart.getTimeInMillis() < end
						.getTimeInMillis();
			} else if (projectedEnd != Calendar.getInstance()) {
				valid &= projectedStart != Calendar.getInstance();
				valid &= projectedStart.getTimeInMillis() < projectedEnd
						.getTimeInMillis();
				if (start != Calendar.getInstance()) {
					valid &= start.getTimeInMillis() < projectedEnd
							.getTimeInMillis();
				}
			} else if (start != Calendar.getInstance()) {
				valid &= projectedStart != Calendar.getInstance();
			}
			return valid;
		}
	}
}