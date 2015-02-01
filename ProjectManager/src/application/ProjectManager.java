/**
 * 
 */
package application;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import obj.User;

import dataAccess.DatabaseManager;

import authentication.AuthenticationPanel;

/**
 * @author George Lambadas 7077076
 *
 */
public class ProjectManager extends JFrame {

	private static final long serialVersionUID = 8535188348113824366L;
	private GridBagConstraints activePanelConstraints;
	private JPanel activePanel;
	public DatabaseManager db;
	public User currentUser;
	
	public ProjectManager() {
		initialize();
		db = new DatabaseManager();
		setVisible(true);
		
	}
	
	/**
	 * Sets layout for frame.
	 */
	private void initialize() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		//TODO make help menu
		// makeHelpMenu();

		//TODO set menu bar
		//setJMenuBar(createMenuBar());

		// set up constraints that will be used for adding whatever panel is
		// active to the main part of the GUI
		activePanelConstraints = new GridBagConstraints();
		activePanelConstraints.gridx = 1;
		activePanelConstraints.gridy = 1;
		activePanelConstraints.weightx = 1;
		activePanelConstraints.weighty = 1;
		activePanelConstraints.fill = GridBagConstraints.BOTH;

		// set constraints and add toolbar
		/*GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.BOTH;
		add(createToolBar(), constraints);*/

		//TODO set up panel for folder tree and contact list
		/*JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new GridBagLayout());

		GridBagConstraints leftPanelConstraints = new GridBagConstraints();
		leftPanelConstraints.fill = GridBagConstraints.BOTH;
		leftPanelConstraints.anchor = GridBagConstraints.FIRST_LINE_START;

		this.folderTreeModule = new FolderTreeModule(dbc, this);
		leftPanel.add(folderTreeModule, leftPanelConstraints);
		leftPanelConstraints.gridx++;
		this.cls = new ContactSidebarModule(dbc, this);
		leftPanel.add(cls, leftPanelConstraints);

		constraints = new GridBagConstraints();
		constraints.gridx++;
		add(leftPanel, constraints);*/

		setActivePanel(new AuthenticationPanel(this), "Login");
		add(activePanel);

		pack();
	}

	public void exit() {
		this.dispose();
	}
	
	public void setActivePanel(JPanel activePanel, String title) {
		if(this.activePanel != null) {
			this.remove(this.activePanel);
		}
		this.activePanel = activePanel;
		this.add(activePanel);
		this.setBounds(activePanel.getBounds());
		
		this.setTitle(title);
	}
	
	
	/**
	 * @return JMenuBar 
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar;
		JMenu menu;

		menuBar = new JMenuBar();
		
		//TODO instantiate one menu listener
		ActionListener ml = null;

		// create file menu, add items and add it to the bar
		menu = createMenu("File", KeyEvent.VK_F, "This is the file menu.");
		
		menu.add(createMenuItem("Exit", KeyEvent.VK_E,
				KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK),
				"This is the exit menu item.", ml));
		
		menuBar.add(menu);

		// create Project menu, add items and add it to the bar
		menu = createMenu("Projects", KeyEvent.VK_P, "This is the projects menu.");
		
		menu.add(createMenuItem(
				"New",
				KeyEvent.VK_N,
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
			KeyStroke stroke, String accessibleAndToolTip, ActionListener listener) {
		JMenuItem menuItem = new JMenuItem(name, mnemonic);
		menuItem.setAccelerator(stroke);
		menuItem.getAccessibleContext().setAccessibleDescription(
				accessibleAndToolTip);
		menuItem.setToolTipText(accessibleAndToolTip);
		menuItem.addActionListener(listener);
		menuItem.setActionCommand(name);
		return menuItem;
	}
}
