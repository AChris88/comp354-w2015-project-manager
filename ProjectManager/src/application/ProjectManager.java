/**
 * 
 */
package application;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


// Libraries necessary for GANNT chart creation
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;


// Other GANTT-related libraries
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
 


import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

import obj.Project;
import obj.ProjectUser;
//import obj.Task;
import obj.User;
import projectEditor.ProjectEditorPanel;
import userEditor.ListOfUsers;
import userEditor.UserEditorPanel;
import dataAccess.DatabaseManager;
import authentication.AuthenticationPanel;

/**
 * @author George Lambadas 7077076
 * 
 */
public class ProjectManager extends JFrame implements Runnable{

	private static final long serialVersionUID = 8535188348113824366L;
	private GridBagConstraints activePanelConstraints;
	private Component activePanel;
	public DatabaseManager db;
	public User currentUser;
	public ProjectUser projectUser;
	public Project currentProject;

    public Component getActivePanel(){
        return activePanel;
    }

	// Constructor for the Project Manager Window
	public ProjectManager() {
		initialize();
		db = new DatabaseManager();
		setVisible(true);
		centerScreen();
	}

    public ProjectManager(String dbFile) {
        initialize();
        db = new DatabaseManager(dbFile);
        setVisible(true);
        centerScreen();
    }

	/**
	 * Sets layout for frame.
	 */
	private void initialize() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// TODO set menu bar
		// setJMenuBar(createMenuBar());

		// set up constraints that will be used for adding whatever panel is
		// active to the main part of the GUI
		activePanelConstraints = new GridBagConstraints();
		activePanelConstraints.gridx = 1;
		activePanelConstraints.gridy = 1;
		activePanelConstraints.weightx = 1;
		activePanelConstraints.weighty = 1;
		activePanelConstraints.fill = GridBagConstraints.BOTH;
		setActivePanel(new AuthenticationPanel(this), "Login");

		pack();
	}

	/**
	 * Method to close the frame from outside the frame object.
	 */
	public void exit() {
		this.dispose();
	}

	/**
	 * Change the JPanel currently occupying the frame
	 * 
	 * @param activePanel
	 *            The new panel to be set
	 * @param title
	 *            The text to appear on the top of the frame
	 */
	public void setActivePanel(Component activePanel, String title) {
		if (this.activePanel != null) {
			this.remove(this.activePanel);
		}
		this.activePanel = activePanel;
		this.add(activePanel);
		this.setBounds(activePanel.getBounds());



		this.setTitle(title);
		this.centerScreen();
	}

	/**
	 * @return JMenuBar
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar;
		JMenu menu;

		menuBar = new JMenuBar();

		// TODO instantiate one menu listener
		ActionListener ml = null;

		// create file menu, add items and add it to the bar
		menu = createMenu("File", KeyEvent.VK_F, "This is the file menu.");

		menu.add(createMenuItem("Exit", KeyEvent.VK_E,
				KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK),
				"This is the exit menu item.", ml));

		menuBar.add(menu);

		// create Project menu, add items and add it to the bar
		menu = createMenu("Projects", KeyEvent.VK_P,
				"This is the projects menu.");

		menu.add(createMenuItem("New", KeyEvent.VK_N,
				KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK),
				"This is the menu item allowing you to create a new Project.",
				ml));
		menuBar.add(menu);

		return menuBar;
	}

	/**
	 * Helper method to create JMenus to add to a JMenuBar
	 * 
	 * @param name
	 *            Name to be displayed
	 * @param mnemonic
	 *            Alt-shortcut to menu
	 * @param accessibleAndToolTip
	 *            Text displayed on hover, or spoken if accessibility is turned
	 *            on.
	 * @return JMenu as specified by parameters
	 */
	private JMenu createMenu(String name, int mnemonic,
			String accessibleAndToolTip) {
		JMenu menu = new JMenu(name);
		menu.setMnemonic(mnemonic);
		menu.getAccessibleContext().setAccessibleDescription(
				accessibleAndToolTip);
		menu.setToolTipText(accessibleAndToolTip);
		return menu;
	}

	/**
	 * Helper method for creating JMenuItems.
	 * 
	 * @param name
	 *            Text to be displayed on item
	 * @param mnemonic
	 *            Shortcut key to access menu item when holding Alt
	 * @param stroke
	 *            keyStroke to access menu item
	 * @param accessibleAndToolTip
	 *            Text displayed on hover, or spoken if accessibility is turned
	 *            on.
	 * @param listener
	 *            ActionListener to listen for when then menu item is clicked.
	 * @return
	 */
	private JMenuItem createMenuItem(String name, int mnemonic,
			KeyStroke stroke, String accessibleAndToolTip,
			ActionListener listener) {
		JMenuItem menuItem = new JMenuItem(name, mnemonic);
		menuItem.setAccelerator(stroke);
		menuItem.getAccessibleContext().setAccessibleDescription(
				accessibleAndToolTip);
		menuItem.setToolTipText(accessibleAndToolTip);
		menuItem.addActionListener(listener);
		menuItem.setActionCommand(name);
		return menuItem;
	}

	private void centerScreen() {
		Dimension dim = getToolkit().getScreenSize();
		Rectangle abounds = getBounds();
		setLocation((dim.width - abounds.width) / 2,
				(dim.height - abounds.height) / 2);
	}

	/**
	 * Method to set the frame for project editing
	 * 
	 * @param p
	 *            the project to be edited
	 */
	public void openProject(Project p) {
		JTabbedPane tabbedPane = new JTabbedPane();

		currentProject = p;
		// check if the project is a new project or one pulled from the database
		String tabName;
		if (p.getId() == -1) {
			tabName = "New Project";
		} else {
			tabName = "Project: " + p.getName();
			projectUser = db.getProjectUser(p, currentUser);
		}
		
		tabbedPane.add(tabName,	new ProjectEditorPanel(this, p));
		
		tabbedPane.setBounds(new Rectangle(800, 400));
		this.setActivePanel(tabbedPane, p.getName() == null ? "New Project"
				: "Project: " + p.getName());
	}
	
	/**
	 * 
	 * 
	 */
	public void openUser(User u) {
		JTabbedPane tabbedPane = new JTabbedPane();


		// check if the project is a new project or one pulled from the database
		tabbedPane.add(
				u.getId() == -1 ? "New User" : "User: " + u.getFirstName() + " " + u.getLastName(),
				new UserEditorPanel(this, u, true, false));
		tabbedPane.setBounds(new Rectangle(800, 400));
		this.setActivePanel(tabbedPane, u.getFirstName() == null ? "New User"
				: "User: " + u.getFirstName() + " "  + u.getLastName());
	}
	
	public void openUser(User u, boolean roleActivated, boolean closeTab) {
		JTabbedPane tabbedPane = new JTabbedPane();


		// check if the project is a new project or one pulled from the database
		tabbedPane.add(
				u.getId() == -1 ? "New User" : "User: " + u.getFirstName() + " " + u.getLastName(),
				new UserEditorPanel(this, u, roleActivated, closeTab));
		tabbedPane.setBounds(new Rectangle(800, 400));
		this.setActivePanel(tabbedPane, u.getFirstName() == null ? "New User"
				: "User: " + u.getFirstName() + " "  + u.getLastName());
	}
	
	public void openList() {
		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.add(
				"List of registered Users",
				new ListOfUsers(this));
		tabbedPane.setBounds(new Rectangle(800, 400));
		this.setActivePanel(tabbedPane, "List of registered Users");
	}

	/**
	 * Add tab to current project editing view
	 * 
	 * @param panel
	 *            JPanel to be contained in the tab
	 * @param title
	 *            Text to appear at the top of the tab
	 */
	public void addTab(JPanel panel, String title) {
		((JTabbedPane) activePanel).add(title, panel);
		((JTabbedPane) activePanel).setSelectedComponent(panel);
	}

	public void closeTab(JPanel panel) {
		((JTabbedPane) activePanel).remove(panel);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
