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
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import obj.Project;
import obj.Task;
import obj.User;
import application.ProjectManager;
import customComponents.TaskTableModel;

import javax.swing.JButton;

import userEditor.AddUserTaskPanel;

/**
 * 
 * @author Christian Allard 7026188
 * 
 */
public class ViewTaskPanel extends JPanel implements Observer {
	private TaskTableModel tableModel;
	private JTable table;
	private JTabbedPane tabbedPane;
	private int tabCtr;
	private ProjectManager manager;
	private JButton btnCloseTab;
	private ButtonClickListener clickListener;

	/**
	 * @wbp.parser.constructor
	 */
	public ViewTaskPanel(ProjectManager manager) {
		this(manager, null);
	}

	public ViewTaskPanel(ProjectManager manager, User u) {
		this.manager = manager;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0,
				1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		tabbedPane = new JTabbedPane();
		tabCtr = 0;
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		tabbedPane.setVisible(false);
		add(tabbedPane, gbc_tabbedPane);
		
		clickListener = new ButtonClickListener();

		tableModel = new TaskTableModel();
		tableModel.populateModel(manager.db.getTasksForUser(u));

		table = new JTable(tableModel);
		table.addMouseListener(((MouseListener) new DoubleClickListener()));
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.insets = new Insets(0, 0, 5, 0);
		gbc_table.gridwidth = 6;
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 1;
		add(new JScrollPane(table), gbc_table);

		btnCloseTab = new JButton("Close Tab");
		btnCloseTab.addActionListener(clickListener);

		GridBagConstraints gbc_btnCloseTab = new GridBagConstraints();
		gbc_btnCloseTab.insets = new Insets(0, 0, 5, 0);
		gbc_btnCloseTab.gridx = 5;
		gbc_btnCloseTab.gridy = 2;
		add(btnCloseTab, gbc_btnCloseTab);
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

			if (source == btnCloseTab) {
				manager.closeTab(ViewTaskPanel.this);
			}
		}
	}
}