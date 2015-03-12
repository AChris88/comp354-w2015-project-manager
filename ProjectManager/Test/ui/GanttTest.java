package ui;

import java.awt.AWTException;

import application.ProjectManager;

/**
 * 
 * @author Christian Allard 27026188
 * 
 *         The use of JUnit in that case would simply render the task of
 *         sychronizing the UI thread too hard. The goal of the test is to show
 *         the proper integration of the external charting module. By accessing
 *         it through normal UI inputs, we show that the integration does not
 *         harm the functionality of the core application
 */
public class GanttTest {
	public static void main(String[] args) throws AWTException {
		UIRobot bot = new UIRobot();
		Runnable app = (Runnable) new ProjectManager();

		Thread botThread = new Thread(bot);
		Thread appThread = new Thread(app);

		try {
			appThread.start();
			botThread.start();

			bot.login();

			Thread.sleep(1500);

			bot.selectProject();

			Thread.sleep(1500);

			bot.clickGanttButton();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}