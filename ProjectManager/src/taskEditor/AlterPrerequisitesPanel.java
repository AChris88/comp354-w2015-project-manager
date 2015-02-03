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

import obj.Task;

import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author George Lambadas 7077076
 * 
 */
public class AlterPrerequisitesPanel extends JPanel {
	private JTable allTasksTable;
	private JTable prerequisitesTable;
	private TaskTableModel prereqTableModel;
	private TaskTableModel allTaskTableModel;

	public AlterPrerequisitesPanel(ProjectManager manager, Task task) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
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

		TaskTableModel allTaskTableModel = new TaskTableModel();
		allTaskTableModel.populateModel(manager.db
				.getPotentialPrerequisites(task));

		allTasksTable = new JTable(allTaskTableModel);
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.insets = new Insets(0, 0, 0, 5);
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 2;
		gbc_table.gridy = 3;
		add(new JScrollPane(allTasksTable), gbc_table);

		prereqTableModel = new TaskTableModel();
		prereqTableModel.populateModel(manager.db.getTaskRequirements(task));

		prerequisitesTable = new JTable(prereqTableModel);
		GridBagConstraints gbc_prerequisitesTable = new GridBagConstraints();
		gbc_prerequisitesTable.insets = new Insets(0, 0, 0, 5);
		gbc_prerequisitesTable.fill = GridBagConstraints.BOTH;
		gbc_prerequisitesTable.gridx = 4;
		gbc_prerequisitesTable.gridy = 3;
		add(new JScrollPane(prerequisitesTable), gbc_prerequisitesTable);
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

				}
				/*
				 * int row = target.getSelectedRow(); if
				 * (tableModel.getTaskAt(row) != null) manager.addTab( new
				 * TaskEditorPanel(manager, tableModel .getTaskAt(row)),
				 * "Task: " + tableModel.getTaskAt(row).getName());
				 */

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