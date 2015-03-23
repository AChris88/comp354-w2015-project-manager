/**
 * 
 */
package userEditor;

import javax.swing.JPanel;

import obj.Project;
import obj.Task;
import obj.User;
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

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;

import customComponents.ProjectTableModel;
import customComponents.TaskTableModel;

import javax.swing.JSplitPane;
import javax.swing.JButton;

import dashboard.DashboardPanel;

import javax.swing.SwingConstants;

/**
 * @author Philippe GENOIS 6527426
 * 
 */
public class UserEditorPanel extends JPanel implements Observer {

	private ProjectManager manager;
	private UserEditorModel userModel;
	private JTextField txtFirstName;
	private JTextField txtUserName;
	private JTextField txtRole;
	private TaskTableModel tableModel;
	private JButton btnCloseTab;
	private JButton btnSave;
	private JButton btnDeleteUser;
	private ButtonClickListener clickListener;
	private JLabel lblLastName;
	private JTextField txtLastName;
	private JLabel lblPassword;
	private JTextField txtPassword;

	public UserEditorPanel(ProjectManager manager) {
		this(manager, null);
	}

	/**
	 * @wbp.parser.constructor
	 */
	public UserEditorPanel(ProjectManager manager, User user) {
		this.manager = manager;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		
				JLabel lblName = new JLabel("First Name");
				GridBagConstraints gbc_lblName = new GridBagConstraints();
				gbc_lblName.anchor = GridBagConstraints.EAST;
				gbc_lblName.insets = new Insets(0, 0, 5, 5);
				gbc_lblName.gridx = 2;
				gbc_lblName.gridy = 1;
				add(lblName, gbc_lblName);
		
				txtFirstName = new JTextField();
				GridBagConstraints gbc_txtFirstName = new GridBagConstraints();
				gbc_txtFirstName.insets = new Insets(0, 0, 5, 5);
				gbc_txtFirstName.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtFirstName.gridx = 4;
				gbc_txtFirstName.gridy = 1;
				add(txtFirstName, gbc_txtFirstName);
				txtFirstName.setColumns(10);
		
		lblLastName = new JLabel("Last Name");
		GridBagConstraints gbc_lblLastName = new GridBagConstraints();
		gbc_lblLastName.anchor = GridBagConstraints.EAST;
		gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastName.gridx = 2;
		gbc_lblLastName.gridy = 2;
		add(lblLastName, gbc_lblLastName);
				
				txtLastName = new JTextField();
				txtLastName.setColumns(10);
				GridBagConstraints gbc_txtLastName = new GridBagConstraints();
				gbc_txtLastName.insets = new Insets(0, 0, 5, 5);
				gbc_txtLastName.fill = GridBagConstraints.HORIZONTAL;
				gbc_txtLastName.gridx = 4;
				gbc_txtLastName.gridy = 2;
				add(txtLastName, gbc_txtLastName);
		
				JLabel lblUserName = new JLabel("User Name");
				GridBagConstraints gbc_lblUserName = new GridBagConstraints();
				gbc_lblUserName.anchor = GridBagConstraints.EAST;
				gbc_lblUserName.insets = new Insets(0, 0, 5, 5);
				gbc_lblUserName.gridx = 2;
				gbc_lblUserName.gridy = 3;
				add(lblUserName, gbc_lblUserName);
				
						txtUserName = new JTextField();
						txtUserName.setHorizontalAlignment(SwingConstants.LEFT);
						GridBagConstraints gbc_txtUserName = new GridBagConstraints();
						gbc_txtUserName.insets = new Insets(0, 0, 5, 5);
						gbc_txtUserName.fill = GridBagConstraints.HORIZONTAL;
						gbc_txtUserName.gridx = 4;
						gbc_txtUserName.gridy = 3;
						add(txtUserName, gbc_txtUserName);
						txtUserName.setColumns(10);
		
				JLabel lblRole = new JLabel("Role");
				GridBagConstraints gbc_lblRole = new GridBagConstraints();
				gbc_lblRole.insets = new Insets(0, 0, 5, 5);
				gbc_lblRole.gridx = 2;
				gbc_lblRole.gridy = 4;
				add(lblRole, gbc_lblRole);
				
						txtRole = new JTextField();
						txtRole.setText("1");
						GridBagConstraints gbc_txtRole = new GridBagConstraints();
						gbc_txtRole.insets = new Insets(0, 0, 5, 5);
						gbc_txtRole.fill = GridBagConstraints.HORIZONTAL;
						gbc_txtRole.gridx = 4;
						gbc_txtRole.gridy = 4;
						add(txtRole, gbc_txtRole);
						txtRole.setColumns(10);

		this.setBounds(100, 100, 500, 450);

		tableModel = new TaskTableModel();

		userModel = new UserEditorModel();
		userModel.addObserver(this);

		clickListener = new ButtonClickListener();
		
		lblPassword = new JLabel("Password");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 2;
		gbc_lblPassword.gridy = 5;
		add(lblPassword, gbc_lblPassword);
		
		txtPassword = new JTextField();
		txtPassword.setColumns(10);
		GridBagConstraints gbc_txtPassword = new GridBagConstraints();
		gbc_txtPassword.insets = new Insets(0, 0, 5, 5);
		gbc_txtPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPassword.gridx = 4;
		gbc_txtPassword.gridy = 5;
		add(txtPassword, gbc_txtPassword);
				
				btnDeleteUser = new JButton("Delete Project");
				btnDeleteUser.addActionListener(clickListener);
				GridBagConstraints gbc_btnDeleteUser = new GridBagConstraints();
				gbc_btnDeleteUser.insets = new Insets(0, 0, 0, 5);
				gbc_btnDeleteUser.gridx = 1;
				gbc_btnDeleteUser.gridy = 7;
				add(btnDeleteUser, gbc_btnDeleteUser);
				btnDeleteUser.setVisible(false);

		GridBagConstraints gbc_btnAddTask = new GridBagConstraints();
		gbc_btnAddTask.insets = new Insets(0, 0, 0, 5);
		gbc_btnAddTask.gridx = 4;
		gbc_btnAddTask.gridy = 8;
				
						btnCloseTab = new JButton("Close Tab");
						btnCloseTab.addActionListener(clickListener);
						
						GridBagConstraints gbc_btnCloseTab = new GridBagConstraints();
						gbc_btnCloseTab.insets = new Insets(0, 0, 0, 5);
						gbc_btnCloseTab.gridx = 2;
						gbc_btnCloseTab.gridy = 7;
						add(btnCloseTab, gbc_btnCloseTab);
				
						btnSave = new JButton("Save");
						btnSave.addActionListener(clickListener);
						GridBagConstraints gbc_btnSave = new GridBagConstraints();
						gbc_btnSave.insets = new Insets(0, 0, 0, 5);
						gbc_btnSave.gridx = 4;
						gbc_btnSave.gridy = 7;
						add(btnSave, gbc_btnSave);

		userModel.setUser(user);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		txtFirstName.setText(userModel.getFirstName());

		Calendar calendar = Calendar.getInstance();
/*
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
		}*/
	}

	/**
	 * Listener class for button clicking
	 * 
	 * @author Philippe GENOIS 6527426
	 * 
	 */
	private class ButtonClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();

			if (source == btnSave) 
			{
				User u = userModel.getUser();
				u.setFirstName(txtFirstName.getText());
				u.setLastName(txtLastName.getText());
				u.setUsername(txtUserName.getText());
				
				try 
				{
					u.setRole(Integer.parseInt(txtRole.getText()));
				}
				catch(Exception ex) 
				{
					u.setRole(0);
				}

				userModel.setUser(u);

				if (u.getId() == -1) 
				{
					manager.db.insertUser(u, txtPassword.getText());
				} else 
				{
					manager.db.updateUser(u);
				}
				
				JOptionPane.showMessageDialog(null, "User was added with success.");
				
				manager.setActivePanel(new DashboardPanel(manager),
						manager.currentUser.getFirstName() + "'s Dashboard");
			} 
			else if (source == btnCloseTab) 
			{
				manager.setActivePanel(new DashboardPanel(manager),
						manager.currentUser.getFirstName() + "'s Dashboard");
			} 
			else if (source == btnDeleteUser) 
			{
				
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
