/**
 * 
 */
package taskEditor;

import javax.swing.JPanel;

import javax.swing.JScrollPane;

import java.awt.GridBagLayout;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;

import customComponents.TaskTableModel;

import application.ProjectManager;

import obj.Project;
import obj.Task;
import obj.TaskRequirement;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;

/**
 * @author George Lambadas 7077076
 * 
 */
public class AlterPrerequisitesPanel extends JPanel {
	private static final long serialVersionUID = 6195901282771479175L;
	private JTable allTasksTable;
	private JTable prerequisitesTable;
	private TaskTableModel prereqTableModel;
	private TaskTableModel allTaskTableModel;
	private JButton btnSave;
	private JButton btnCancel;
	private Task task;
	private ProjectManager manager;
	private ButtonClickListener clickListener;

	public AlterPrerequisitesPanel(ProjectManager manager, Task task) {
		this.manager = manager;
		this.task = task;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.5, 0.0, 0.5,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblAllTasks = new JLabel("All Tasks");
		GridBagConstraints gbc_lblAllTasks = new GridBagConstraints();
		gbc_lblAllTasks.insets = new Insets(0, 0, 5, 5);
		gbc_lblAllTasks.gridx = 2;
		gbc_lblAllTasks.gridy = 2;
		add(lblAllTasks, gbc_lblAllTasks);

		JLabel lblPrerequisiteTasks = new JLabel("Prerequisite Tasks:");
		GridBagConstraints gbc_lblPrerequisiteTasks = new GridBagConstraints();
		gbc_lblPrerequisiteTasks.insets = new Insets(0, 0, 5, 0);
		gbc_lblPrerequisiteTasks.gridx = 4;
		gbc_lblPrerequisiteTasks.gridy = 2;
		add(lblPrerequisiteTasks, gbc_lblPrerequisiteTasks);

		allTaskTableModel = new TaskTableModel();
		allTaskTableModel.populateModel(manager.db
				.getPotentialPrerequisites(task));

		allTasksTable = new JTable(allTaskTableModel);
		allTasksTable
				.addMouseListener(((MouseListener) new DoubleClickListener()));
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.insets = new Insets(0, 0, 5, 5);
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 2;
		gbc_table.gridy = 3;
		add(new JScrollPane(allTasksTable), gbc_table);

		prereqTableModel = new TaskTableModel();
		prereqTableModel.populateModel(manager.db.getTaskRequirements(task));

		prerequisitesTable = new JTable(prereqTableModel);
		prerequisitesTable
				.addMouseListener(((MouseListener) new DoubleClickListener()));
		GridBagConstraints gbc_prerequisitesTable = new GridBagConstraints();
		gbc_prerequisitesTable.insets = new Insets(0, 0, 5, 0);
		gbc_prerequisitesTable.fill = GridBagConstraints.BOTH;
		gbc_prerequisitesTable.gridx = 4;
		gbc_prerequisitesTable.gridy = 3;
		add(new JScrollPane(prerequisitesTable), gbc_prerequisitesTable);

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
				// insert all task_reqs in the prerequisites table model
				ArrayList<Task> allPrereq = prereqTableModel.getAllTasks();
				for (int i = 0; i < allPrereq.size(); i++) {
					// potentially does not insert if already exists
					manager.db.insertTaskRequirement(new TaskRequirement(-1,
							task.getId(), allPrereq.get(i).getId()));

				}

				// delete all task_reqs in the allTasks table model
				ArrayList<Task> allNonPrereq = allTaskTableModel.getAllTasks();
				for (int i = 0; i < allNonPrereq.size(); i++) {
					// potentially doesn't delete if doesn't already exist
					manager.db.removeTaskRequirement(new TaskRequirement(-1,
							task.getId(), allNonPrereq.get(i).getId()));

				}
			} else if (source == btnCancel) {
				// close tab
				manager.closeTab(AlterPrerequisitesPanel.this);
			}
		}

	}

	private class DoubleClickListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				JTable target = (JTable) e.getSource();
				if (target == allTasksTable) {
					int row = target.getSelectedRow();
					Task toMove = allTaskTableModel.removeTaskAt(row);

					allTaskTableModel.fireTableRowsDeleted(row, row);
					prereqTableModel.addTask(toMove);
					prereqTableModel.fireTableRowsInserted(
							prereqTableModel.getRowCount() - 2,
							prereqTableModel.getRowCount() - 2);
				} else if (target == prerequisitesTable) {
					int row = target.getSelectedRow();
					Task toMove = prereqTableModel.removeTaskAt(row);

					prereqTableModel.fireTableRowsDeleted(row, row);
					allTaskTableModel.addTask(toMove);
					allTaskTableModel.fireTableRowsInserted(
							allTaskTableModel.getRowCount() - 2,
							allTaskTableModel.getRowCount() - 2);

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

}
