/**
 * 
 */
package userEditor;

import javax.swing.JPanel;

import obj.Project;
import obj.ProjectUser;
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
import javax.swing.JCheckBox;
import javax.swing.JPasswordField;

import projectEditor.ProjectEditorModel;

/**
 * @author Philippe GENOIS 6527426
 * 
 */
public class UserChangeProjectRole extends JPanel implements Observer {

	private ProjectManager manager;
	private UserEditorModel userModel;
	private ProjectEditorModel projectModel;
	private TaskTableModel tableModel;
	private JButton btnCloseTab;
	private JButton btnSave;
	private JButton btnDeleteUser;
	private ButtonClickListener clickListener;
	private JCheckBox chckbxRole;

	private ProjectUser projectUser;

	public UserChangeProjectRole(ProjectManager manager) {
		this(manager, null, null);
	}

	/**
	 * @wbp.parser.constructor
	 */
	public UserChangeProjectRole(ProjectManager manager, User user, Project p) {
		this.manager = manager;

		projectUser = manager.db.getProjectUser(p, user);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0,
				1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblRole = new JLabel("Project Manager");
		GridBagConstraints gbc_lblRole = new GridBagConstraints();
		gbc_lblRole.insets = new Insets(0, 0, 5, 5);
		gbc_lblRole.gridx = 2;
		gbc_lblRole.gridy = 1;
		add(lblRole, gbc_lblRole);

		this.setBounds(100, 100, 500, 450);

		userModel = new UserEditorModel();
		userModel.addObserver(this);

		clickListener = new ButtonClickListener();

		chckbxRole = new JCheckBox("");
		GridBagConstraints gbc_chckbxRole = new GridBagConstraints();
		gbc_chckbxRole.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxRole.gridx = 4;
		gbc_chckbxRole.gridy = 1;
		add(chckbxRole, gbc_chckbxRole);

		btnDeleteUser = new JButton("Delete Project");
		btnDeleteUser.setEnabled(false);
		btnDeleteUser.addActionListener(clickListener);
		btnDeleteUser.setVisible(false);
		GridBagConstraints gbc_btnDeleteUser = new GridBagConstraints();
		gbc_btnDeleteUser.insets = new Insets(0, 0, 0, 5);
		gbc_btnDeleteUser.gridx = 1;
		gbc_btnDeleteUser.gridy = 3;
		add(btnDeleteUser, gbc_btnDeleteUser);

		GridBagConstraints gbc_btnAddTask = new GridBagConstraints();
		gbc_btnAddTask.insets = new Insets(0, 0, 0, 5);
		gbc_btnAddTask.gridx = 4;
		gbc_btnAddTask.gridy = 8;

		btnCloseTab = new JButton("Close Tab");
		btnCloseTab.addActionListener(clickListener);

		GridBagConstraints gbc_btnCloseTab = new GridBagConstraints();
		gbc_btnCloseTab.insets = new Insets(0, 0, 0, 5);
		gbc_btnCloseTab.gridx = 2;
		gbc_btnCloseTab.gridy = 3;
		add(btnCloseTab, gbc_btnCloseTab);

		btnSave = new JButton("Save");
		btnSave.addActionListener(clickListener);
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.insets = new Insets(0, 0, 0, 5);
		gbc_btnSave.gridx = 4;
		gbc_btnSave.gridy = 3;
		add(btnSave, gbc_btnSave);

		userModel.setUser(user);

		if (projectUser.getProjectRole() == 1) {
			chckbxRole.setSelected(true);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {

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

			if (source == btnSave) {
				if (chckbxRole.isSelected()) {
					projectUser.setProjectRole(1);
				} else {
					projectUser.setProjectRole(0);
				}

				manager.db.updateProjectUser(projectUser);

				JOptionPane.showMessageDialog(null,
						"User was updated with success.");

				manager.closeTab(UserChangeProjectRole.this);
			} else if (source == btnCloseTab) {
				manager.closeTab(UserChangeProjectRole.this);
			}
		}
	}
}
