/**
 * 
 */
package projectEditor;

import javax.swing.JPanel;

import obj.Project;
import obj.Task;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;

import taskEditor.TaskEditorPanel;
import taskEditor.ViewTaskPanel;
import userEditor.AddProjectUserPanel;
import dashboard.DashboardPanel;

import application.ProjectManager;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author George Lambadas 7077076
 * 
 */
public class GanttPanel extends JPanel {

	private JButton btnCloseTab;
	public ProjectManager manager;

	public GanttPanel(ProjectManager manager, ChartPanel chartPanel) {
		this.manager = manager;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(chartPanel, gbc_panel);

		btnCloseTab = new JButton("Close Tab");
		btnCloseTab.addActionListener(new ButtonClickListener());
		GridBagConstraints gbc_btnCloseTab = new GridBagConstraints();
		gbc_btnCloseTab.gridx = 0;
		gbc_btnCloseTab.gridy = 2;
		add(btnCloseTab, gbc_btnCloseTab);
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

			// save button case
			if (source == btnCloseTab) {
				manager.closeTab(GanttPanel.this);
			}
		}
	}
}
