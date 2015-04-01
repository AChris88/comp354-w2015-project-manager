/**
 * 
 */
package projectEditor;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.GridBagLayout;
import javax.swing.JTable;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

import customComponents.PertTableModel;

import application.ProjectManager;

/**
 * @author George Lambadas 7077076
 *
 */
public class PertTablePanel extends JPanel {
	private JTable table;
	private PertTableModel tableModel;
	private ProjectManager manager;
	private JButton btnCloseTab;
	
	
	public PertTablePanel(ProjectManager manager, ArrayList<String[]> pertData) {
		this.manager = manager;
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		tableModel = new PertTableModel();
		tableModel.populateModel(pertData);
		
		table = new JTable(tableModel);
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.insets = new Insets(0, 0, 5, 0);
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 1;
		gbc_table.gridwidth = 3;
		add(new JScrollPane(table), gbc_table);
		
		btnCloseTab = new JButton("Close Tab");
		btnCloseTab.addActionListener(new ButtonClickListener());
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 2;
		add(btnCloseTab, gbc_btnNewButton);
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
				manager.closeTab(PertTablePanel.this);
			}
			
		}
	}

}
