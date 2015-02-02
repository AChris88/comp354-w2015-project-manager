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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import projectEditor.ProjectEditorPanel;
import customComponents.ProjectTableModel;

/**
 * @author George Lambadas 7077076
 * 
 */
public class DashboardPanel extends JPanel {

	private ProjectManager manager;
	private JButton btnNewUser;
	private JButton btnNewProject;
	private JTable table;
	private ProjectTableModel model;

	public DashboardPanel(ProjectManager manager) {
		this.manager = manager;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		// only allow managers to add new users
		if (this.manager.currentUser.getRole() == 1) {
			btnNewUser = new JButton("New User");
			GridBagConstraints gbc_btnNewUser = new GridBagConstraints();
			gbc_btnNewUser.insets = new Insets(0, 0, 5, 0);
			gbc_btnNewUser.gridx = 6;
			gbc_btnNewUser.gridy = 2;
			add(btnNewUser, gbc_btnNewUser);
		}

		btnNewProject = new JButton("New Project");
		GridBagConstraints gbc_btnNewProject = new GridBagConstraints();
		gbc_btnNewProject.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewProject.gridx = 6;
		gbc_btnNewProject.gridy = 4;
		add(btnNewProject, gbc_btnNewProject);

		JLabel lblCurrentProjects = new JLabel("Current Projects: ");
		GridBagConstraints gbc_lblCurrentProjects = new GridBagConstraints();
		gbc_lblCurrentProjects.insets = new Insets(0, 0, 5, 0);
		gbc_lblCurrentProjects.gridx = 6;
		gbc_lblCurrentProjects.gridy = 6;
		add(lblCurrentProjects, gbc_lblCurrentProjects);

		model = new ProjectTableModel();

		model.populateModel(manager.db.getProjects());

		table = new JTable(model);
		table.setFillsViewportHeight(true);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoCreateRowSorter(true);

		table.addMouseListener(((MouseListener) new DoubleClickListener()));

		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 6;
		gbc_table.gridy = 7;

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, gbc_table);

		this.setBounds(100, 100, 500, 200);
	}

	private class DoubleClickListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				JTable target = (JTable) e.getSource();
				int row = target.getSelectedRow();
				if (model.getProjectAt(row) != null)
					manager.setActivePanel(new ProjectEditorPanel(manager,
							model.getProjectAt(row)), "Project: " + model.getProjectAt(row).getName());

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
