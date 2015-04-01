/**
 * 
 */
package dashboard;

import javax.swing.JPanel;

import application.ProjectManager;

import java.awt.GridBagLayout;

import javax.swing.JButton;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import obj.Project;
import obj.User;
import projectEditor.ProjectEditorPanel;
import customComponents.ProjectTableModel;

/**
 * @author George Lambadas 7077076
 * 
 */
public class DashboardPanel extends JPanel {

	private static final long serialVersionUID = 2351909846353604219L;
	private ProjectManager manager;
	private JButton btnNewUser;
	private JButton btnEditMyProfile;
	private JButton btnEditSupUser;
	private JTable table;
	private ProjectTableModel model;
	private ButtonClickListener clickListener;
	private JButton btnNewProject;

	public DashboardPanel(ProjectManager manager) {
		this.manager = manager;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		// create a click listener object to listen on all buttons in the panel

		clickListener = new ButtonClickListener();

		// only allow managers to add new users
		if (this.manager.currentUser.getRole() == 1) {
			btnNewUser = new JButton("New User");
			GridBagConstraints gbc_btnNewUser = new GridBagConstraints();
			gbc_btnNewUser.insets = new Insets(0, 0, 5, 0);
			gbc_btnNewUser.gridx = 6;
			gbc_btnNewUser.gridy = 2;
			add(btnNewUser, gbc_btnNewUser);
			
			btnNewUser.addActionListener(clickListener);
			
			btnNewProject = new JButton("New Project");
			GridBagConstraints gbc_btnNewProject = new GridBagConstraints();
			gbc_btnNewProject.insets = new Insets(0, 0, 5, 0);
			gbc_btnNewProject.gridx = 6;
			gbc_btnNewProject.gridy = 4;
			add(btnNewProject, gbc_btnNewProject);
			
			btnNewProject.addActionListener(clickListener);
			
			btnEditSupUser = new JButton("Edit / Delete Users");
			GridBagConstraints gbc_btnEditSupUser = new GridBagConstraints();
			gbc_btnEditSupUser.anchor = GridBagConstraints.NORTH;
			gbc_btnEditSupUser.insets = new Insets(0, 0, 5, 0);
			gbc_btnEditSupUser.gridx = 6;
			gbc_btnEditSupUser.gridy = 5;
			add(btnEditSupUser, gbc_btnEditSupUser);
			
			btnEditSupUser.addActionListener(clickListener);
		}
		
		btnEditMyProfile = new JButton("Edit my profile");
		GridBagConstraints gbc_btnEditMyProfile = new GridBagConstraints();
		gbc_btnEditMyProfile.insets = new Insets(0, 0, 5, 0);
		gbc_btnEditMyProfile.gridx = 6;
		gbc_btnEditMyProfile.gridy = 1;
		add(btnEditMyProfile, gbc_btnEditMyProfile);
		
		btnEditMyProfile.addActionListener(clickListener);

		JLabel lblCurrentProjects = new JLabel("Current Projects: ");
		GridBagConstraints gbc_lblCurrentProjects = new GridBagConstraints();
		gbc_lblCurrentProjects.insets = new Insets(0, 0, 5, 0);
		gbc_lblCurrentProjects.gridx = 6;
		gbc_lblCurrentProjects.gridy = 7;
		add(lblCurrentProjects, gbc_lblCurrentProjects);

		// instantiate model for project table and populate it with projects for
		// the current user.
		model = new ProjectTableModel();
		model.populateModel(manager.db.getProjectForUsers(manager.currentUser));

		// instantiate Project Table using model
		table = new JTable(model);
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoCreateRowSorter(true);

//      table.setRowSelec

		// set listener on table for double clicks
		table.addMouseListener(((MouseListener) new DoubleClickListener()));

		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 6;
		gbc_table.gridy = 8;

		// place table in scroll pane to make headings visible and to allow for
		// more projects than what can be displayed given the current size
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, gbc_table);

		this.setBounds(100, 100, 500, 277);
	}
    //Method used for testing purpose only
    public void openCurrentSelectedProject()
    {
        int row = table.getSelectedRow();
        if (model.getProjectAt(row) != null)
            manager.openProject(model.getProjectAt(row));
    }

	/**
	 * Class used to listen for clicks on buttons within parent class
	 * @author George Lambadas 7077076
	 *
	 */
	private class ButtonClickListener implements ActionListener 
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			JButton source = (JButton) e.getSource();

			if (source == btnNewProject) 
			{
				manager.openProject(new Project(-1, null, null, null, null));
			}
			else if (source == btnNewUser) 
			{
				manager.openUser(new User(-1, null, null, null, -1));
			}
			else if (source == btnEditSupUser) 
			{
				manager.openList();
			}
			else if (source == btnEditMyProfile) 
			{
				manager.openUser(manager.currentUser, false, false);
			}
		}
	}

	private class DoubleClickListener implements MouseListener 
	{
		public void mouseClicked(MouseEvent e) 
		{
			if (e.getClickCount() == 2) 
			{
				JTable target = (JTable) e.getSource();
				int row = target.getSelectedRow();
				if (model.getProjectAt(row) != null)
					manager.openProject(model.getProjectAt(row));
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
