/**
 * 
 */
package userEditor;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.GridBagLayout;

import javax.swing.JTable;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import customComponents.UserTableModel;
import application.ProjectManager;
import obj.Project;
import obj.ProjectUser;
import obj.Task;
import obj.User;
import obj.UserTask;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;

/**
 * @author Philippe GENOIS 6527426
 * 
 */
public class AddProjectUserPanel extends JPanel {
	private static final long serialVersionUID = 6195901282771479175L;
	private JTable allUsersTable;
	private JTable projectUsersTable;
	
	private UserTableModel allUsersTableModel;
	private UserTableModel projectUsersTableModel;
	
	private JButton btnSave;
	private JButton btnCancel;
	
	private Project project;
	private ProjectManager manager;
	private ButtonClickListener clickListener;

	public AddProjectUserPanel(ProjectManager manager, Project p) {
		this.manager = manager;
		this.project = p;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.5, 0.0, 0.5,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblAllTasks = new JLabel("All available Users");
		GridBagConstraints gbc_lblAllTasks = new GridBagConstraints();
		gbc_lblAllTasks.insets = new Insets(0, 0, 5, 5);
		gbc_lblAllTasks.gridx = 2;
		gbc_lblAllTasks.gridy = 2;
		add(lblAllTasks, gbc_lblAllTasks);

		JLabel lblPrerequisiteTasks = new JLabel("Users in Project");
		GridBagConstraints gbc_lblPrerequisiteTasks = new GridBagConstraints();
		gbc_lblPrerequisiteTasks.insets = new Insets(0, 0, 5, 0);
		gbc_lblPrerequisiteTasks.gridx = 4;
		gbc_lblPrerequisiteTasks.gridy = 2;
		add(lblPrerequisiteTasks, gbc_lblPrerequisiteTasks);

		allUsersTableModel = new UserTableModel();
		allUsersTableModel.populateModel(usersNotInCurrentProject());

		allUsersTable = new JTable(allUsersTableModel);
		allUsersTable.addMouseListener(((MouseListener) new DoubleClickListener()));
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.insets = new Insets(0, 0, 5, 5);
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 2;
		gbc_table.gridy = 3;
		add(new JScrollPane(allUsersTable), gbc_table);

		projectUsersTableModel = new UserTableModel();
		projectUsersTableModel.populateModel(allUserInProjectExceptCurrent());

		projectUsersTable = new JTable(projectUsersTableModel);
		projectUsersTable.addMouseListener(((MouseListener) new DoubleClickListener()));
		GridBagConstraints gbc_prerequisitesTable = new GridBagConstraints();
		gbc_prerequisitesTable.insets = new Insets(0, 0, 5, 0);
		gbc_prerequisitesTable.fill = GridBagConstraints.BOTH;
		gbc_prerequisitesTable.gridx = 4;
		gbc_prerequisitesTable.gridy = 3;
		add(new JScrollPane(projectUsersTable), gbc_prerequisitesTable);

		clickListener = new ButtonClickListener();
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(clickListener);
		GridBagConstraints gbc_btnCancel = new GridBagConstraints();
		gbc_btnCancel.insets = new Insets(0, 0, 5, 5);
		gbc_btnCancel.gridx = 2;
		gbc_btnCancel.gridy = 4;
		add(btnCancel, gbc_btnCancel);

		btnSave = new JButton("Save");
		btnSave.addActionListener(clickListener);
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.insets = new Insets(0, 0, 5, 0);
		gbc_btnSave.gridx = 4;
		gbc_btnSave.gridy = 4;
		add(btnSave, gbc_btnSave);
	}

	private class ButtonClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();

			if (source == btnSave) {
				ArrayList<User> allUserInProject = projectUsersTableModel.getAllUsers();
				
				for (int i = 0; i < allUserInProject.size(); i++) 
				{
					ArrayList<User> alreadyInTheDB = manager.db.getUsersForProject(project);
					
					if(!findUserIn(alreadyInTheDB, allUserInProject.get(i))) 
					{
						manager.db.insertProjectUser(new ProjectUser(-1, project.getId(), allUserInProject.get(i).getId(), 0));
					}
				}
					
				ArrayList<User> allUserNotInProject = allUsersTableModel.getAllUsers();
				for (int i = 0; i < allUserNotInProject.size(); i++) {
					ProjectUser pu = getProjectUser(allUserNotInProject.get(i), project);
					
					if(pu != null) {
						ArrayList<Task> tasksForUser = manager.db.getUserTasksForUser(allUserNotInProject.get(i), pu);
	
						for(int j = 0; j < tasksForUser.size(); ++j) {
							manager.db.removeUserTask(new UserTask(-1, allUserNotInProject.get(i).getId(), 
									tasksForUser.get(j).getId(), pu.getId()));
						}
					}
					
					//potentially doesn't delete if doesn't already exist
					manager.db.removeProjectUser(new ProjectUser(-1, project.getId(), allUserNotInProject.get(i).getId(), 0));
				}
			} else if (source == btnCancel) {
				manager.closeTab(AddProjectUserPanel.this);
			}
		}

	}

	private class DoubleClickListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				JTable target = (JTable) e.getSource();
				if (target == allUsersTable) {
					int row = target.getSelectedRow();
					User toMove = allUsersTableModel.removeUserAt(row);

					allUsersTableModel.fireTableRowsDeleted(row, row);
					projectUsersTableModel.addUser(toMove);
					projectUsersTableModel.fireTableRowsInserted(
							projectUsersTableModel.getRowCount() - 2,
							projectUsersTableModel.getRowCount() - 2);
				} else if (target == projectUsersTable) {
					int row = target.getSelectedRow();
					User toMove = projectUsersTableModel.removeUserAt(row);

					projectUsersTableModel.fireTableRowsDeleted(row, row);
					allUsersTableModel.addUser(toMove);
					allUsersTableModel.fireTableRowsInserted(
							allUsersTableModel.getRowCount() - 2,
							allUsersTableModel.getRowCount() - 2);
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) { }

		@Override
		public void mouseExited(MouseEvent arg0) { }

		@Override
		public void mousePressed(MouseEvent arg0) { }

		@Override
		public void mouseReleased(MouseEvent arg0) { }
	}
	
	private ArrayList<User> allUserInProjectExceptCurrent() {
		ArrayList<User> alreadyThere = manager.db.getUsersForProject(project);
		
		for(int i = 0; i < alreadyThere.size(); i++) {
			if(alreadyThere.get(i).getId() == manager.currentUser.getId()) {
				alreadyThere.remove(i);
			}
		}
		
		return alreadyThere;
	}
	
	private ArrayList<User> usersNotInCurrentProject() {
		ArrayList<User> allUsers = manager.db.getUsers();
		ArrayList<User> alreadyThere = manager.db.getUsersForProject(project);
		ArrayList<User> temp = new ArrayList<User>();
		
		for(int i = 0; i < allUsers.size(); i++) {
			if(!findUserIn(alreadyThere, allUsers.get(i))) {
				temp.add(allUsers.get(i));
			}
		}
		
		return temp;
	}
	
	private boolean findUserIn(ArrayList<User> array, User u) {
		for(int i = 0; i < array.size(); ++i) {
			if(array.get(i).getId() == u.getId()) {
				return true;
			}
		}
		
		return false;
	}
	
	private ProjectUser getProjectUser(User u, Project p) {
		ArrayList<ProjectUser> pu = manager.db.getProjectUsers();
		
		for(int i = 0; i < pu.size(); ++i) {
			if(pu.get(i).getProjectId() == p.getId() && pu.get(i).getUserId() == u.getId()) {
				return pu.get(i);
			}
		}
		
		return null; // Shouldn't be possible if we made till this panel... but better be careful
	}
}
