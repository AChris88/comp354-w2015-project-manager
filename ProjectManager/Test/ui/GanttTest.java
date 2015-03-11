package ui;

import java.awt.AWTException;
import application.ProjectManager;

/**
 * 
 * @author Christian Allard 27026188
 *
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
			
			Thread.sleep(500);
			
			bot.clickGanttButton();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}