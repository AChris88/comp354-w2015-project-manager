/**
 * 
 */
package authentication;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;

import dashboard.DashboardPanel;

import obj.User;

import application.ProjectManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author George Lambadas 7077076
 * 
 */
public class AuthenticationPanel extends JPanel {
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton cancelButton;
	private JButton loginButton;
	private ProjectManager manager;
	private JLabel errorMessageLabel;

	public AuthenticationPanel(ProjectManager manager) {
		this.manager = manager;

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 3 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 3 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		errorMessageLabel = new JLabel();
		errorMessageLabel.setForeground(Color.RED);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 3;
		gbc_lblNewLabel.gridy = 2;
		add(errorMessageLabel, gbc_lblNewLabel);

		JLabel lblUsername = new JLabel("Username: ");
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.anchor = GridBagConstraints.EAST;
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.gridx = 3;
		gbc_lblUsername.gridy = 3;
		add(lblUsername, gbc_lblUsername);

		usernameField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 4;
		gbc_textField.gridy = 3;
		add(usernameField, gbc_textField);
		usernameField.setColumns(10);

		JLabel lblPassword = new JLabel("Password: ");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 3;
		gbc_lblPassword.gridy = 4;
		add(lblPassword, gbc_lblPassword);

		passwordField = new JPasswordField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 4;
		gbc_textField_1.gridy = 4;
		add(passwordField, gbc_textField_1);
		passwordField.setColumns(10);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ButtonClickListener());

		GridBagConstraints gbc_cancelButton = new GridBagConstraints();
		gbc_cancelButton.insets = new Insets(0, 0, 0, 5);
		gbc_cancelButton.gridx = 3;
		gbc_cancelButton.gridy = 6;
		add(cancelButton, gbc_cancelButton);

		loginButton = new JButton("Login");
		loginButton.addActionListener(new ButtonClickListener());

		GridBagConstraints gbc_loginButton = new GridBagConstraints();
		gbc_loginButton.gridx = 4;
		gbc_loginButton.gridy = 6;
		add(loginButton, gbc_loginButton);
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

			if (source == loginButton) {
				// authenticate here
				manager.currentUser = manager.db.login(usernameField.getText(),
						new String(passwordField.getPassword()));
				if (manager.currentUser == null) {
					errorMessageLabel.setText("Invalid username or password.");
				} else {
					System.out.println("hello");
					manager.setActivePanel(new DashboardPanel(manager),
							manager.currentUser.getFirstName() + "'s Dashboard");
				}
			} else if (source == cancelButton) {
				manager.exit();
			}

		}
	}

}
