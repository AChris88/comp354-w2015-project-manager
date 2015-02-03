package taskEditor;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JTable;

import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
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

import obj.Task;
import application.ProjectManager;
import customComponents.TaskTableModel;

import javax.swing.JButton;

/**
 * 
 * @author Christian Allard 7026188
 *
 */
public class TaskEditorPanel extends JPanel implements Observer{
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
	
	/**
	 * @wbp.parser.constructor
	 */
	public TaskEditorPanel(ProjectManager manager){
		this(manager, null);
	}
	
	public TaskEditorPanel(ProjectManager manager, Task task) {
		this.manager = manager;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);
		
		tabbedPane = new JTabbedPane();
		tabCtr = 0;
        GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
        gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
        gbc_tabbedPane.gridx = 0;
        gbc_tabbedPane.gridy = 0;
        tabbedPane.setVisible(false);
        add(tabbedPane, gbc_tabbedPane);
		
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
		//TODO add button action listener to save record
		add(btnSave, gbc_btnSave);
		
		btnReset = new JButton("Reset");
		GridBagConstraints gbc_btnReset = new GridBagConstraints();
		gbc_btnReset.insets = new Insets(0, 0, 5, 0);
		gbc_btnReset.gridx = 5;
		gbc_btnReset.gridy = 6;
		//TODO add button action listener to reset fields
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
		table.addMouseListener(((MouseListener) new DoubleClickListener()));
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.gridwidth = 6;
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 8;
		add(new JScrollPane(table), gbc_table);
		
		taskModel = new TaskEditorModel();
		taskModel.addObserver(this);
		taskModel.setTask(task);
	}
	
	private void addTab(Task task){
//		ImageIcon icon = createImageIcon("images/middle.gif");
		
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
        
        //keyEvent could also be represented as (tabCtr + 49)
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
		nameTextField.setText(taskModel.getTaskName());
		
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(taskModel.getTaskProjectedStartDate());
		expectedStartTextField.setText(calendar.get(Calendar.YEAR) + "-"
				+ calendar.get(Calendar.MONTH) + "-"
				+ calendar.get(Calendar.DATE));

		
		calendar.setTime(taskModel.getTaskStartDate());
		startTextField.setText(calendar.get(Calendar.YEAR) + "-"
				+ calendar.get(Calendar.MONTH) + "-"
				+ calendar.get(Calendar.DATE));

		//set projected end field
		calendar.setTime(taskModel.getTaskProjectedEndDate());
		expectedEndTextField.setText(calendar.get(Calendar.YEAR) + "-"
				+ calendar.get(Calendar.MONTH) + "-"
				+ calendar.get(Calendar.DATE));

		//set actual end date if not null
		if (taskModel.getTaskEndDate() != null) {
			calendar.setTime(taskModel.getTaskEndDate());
			endTextField.setText(calendar.get(Calendar.YEAR) + "-"
					+ calendar.get(Calendar.MONTH) + "-"
					+ calendar.get(Calendar.DATE));
		}
	}
	
	private class DoubleClickListener implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				JTable target = (JTable) e.getSource();
				int row = target.getSelectedRow();
				if (tableModel.getTaskAt(row) != null) {
					manager.addTab(new TaskEditorPanel(manager, tableModel.getTaskAt(row)), "Task: "+tableModel.getTaskAt(row).getName());
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