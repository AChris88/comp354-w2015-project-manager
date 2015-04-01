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
import java.awt.event.MouseAdapter;
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
import customComponents.UserListModel;

import javax.swing.JSplitPane;
import javax.swing.JButton;

import dashboard.DashboardPanel;

import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JPasswordField;

/**
 * @author Philippe GENOIS 6527426
 * 
 */
public class ListOfUsers extends JPanel implements Observer {

	private ProjectManager manager;
	private JButton btnCloseTab;
	private ButtonClickListener clickListener;
	
	private JTable table;
	private UserListModel listModel;
	private JTable list;
	private JButton btnDeleteUser;

	/**
	 * @wbp.parser.constructor
	 */
	public ListOfUsers(final ProjectManager manager) 
	{
		this.manager = manager;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 1.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		this.setBounds(100, 100, 500, 450);

		listModel = new UserListModel();
		ArrayList<User> users = manager.db.getUsers();
		listModel.populateModel(users);

		list = new JTable(listModel);

		list.addMouseListener(new MouseAdapter() 
		{
			public void mouseClicked(MouseEvent evt) 
			{
				JTable list = (JTable) evt.getSource();

				if (evt.getClickCount() == 2) 
				{
					User u = listModel.getUserAt(list.getSelectedRow());
					
					if(u.getId() == manager.currentUser.getId())
					{
						manager.addTab(
								new UserEditorPanel(manager, manager.currentUser, false, true),
								"User: " + u.getFirstName() + " "  + u.getLastName());	
					}
					else
					{
						manager.addTab(
								new UserEditorPanel(manager, u, true, true),
								"User: " + u.getFirstName() + " "  + u.getLastName());	
					}
				}
			}
		});

		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.gridwidth = 2;
		gbc_list.gridheight = 5;
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 3;
		gbc_list.gridy = 1;
		add(new JScrollPane(list), gbc_list);

		clickListener = new ButtonClickListener();
		
		GridBagConstraints gbc_btnAddTask = new GridBagConstraints();
		gbc_btnAddTask.insets = new Insets(0, 0, 0, 5);
		gbc_btnAddTask.gridx = 4;
		gbc_btnAddTask.gridy = 8;
						
		btnCloseTab = new JButton("Close Tab");
		btnCloseTab.addActionListener(clickListener);
		
		GridBagConstraints gbc_btnCloseTab = new GridBagConstraints();
		gbc_btnCloseTab.insets = new Insets(0, 0, 5, 5);
		gbc_btnCloseTab.gridx = 4;
		gbc_btnCloseTab.gridy = 7;
		add(btnCloseTab, gbc_btnCloseTab);
	}

	@Override
	public void update(Observable arg0, Object arg1) 
	{
		
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

			if (source == btnCloseTab) 
			{
				manager.setActivePanel(new DashboardPanel(manager),
						manager.currentUser.getFirstName() + "'s Dashboard");
			} 
		}
	}

	private class DoubleClickListener implements MouseListener 
	{
		public void mouseClicked(MouseEvent e) 
		{
			if (e.getClickCount() == 2) 
			{


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
