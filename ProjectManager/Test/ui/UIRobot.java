package ui;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * 
 * @author Christian Allard 27026188
 *
 */
public class UIRobot implements Runnable {
	private Robot bot = null;
	private double height, width;
	
	public UIRobot() throws AWTException {
		bot = new Robot();
		setMonitorDimensions();
	}

	@Override
	public void run() {
	}

	private void setMonitorDimensions(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		height = screenSize.getHeight();
		width = screenSize.getWidth();
	}
	
	public void login() {
		bot.keyPress(KeyEvent.VK_T);
		bot.keyPress(KeyEvent.VK_E);
		bot.keyPress(KeyEvent.VK_S);
		bot.keyPress(KeyEvent.VK_T);
		bot.keyPress(KeyEvent.VK_U);
		bot.keyPress(KeyEvent.VK_S);
		bot.keyPress(KeyEvent.VK_E);
		bot.keyPress(KeyEvent.VK_R);
		bot.keyPress(KeyEvent.VK_TAB);
		bot.delay(500);
		bot.keyPress(KeyEvent.VK_P);
		bot.keyPress(KeyEvent.VK_A);
		bot.keyPress(KeyEvent.VK_S);
		bot.delay(500);
		bot.keyPress(KeyEvent.VK_S);
		bot.keyPress(KeyEvent.VK_W);
		bot.keyPress(KeyEvent.VK_O);
		bot.keyPress(KeyEvent.VK_R);
		bot.keyPress(KeyEvent.VK_D);
		bot.delay(500);
		bot.keyPress(KeyEvent.VK_TAB);
		bot.delay(500);
		bot.keyPress(KeyEvent.VK_TAB);
		bot.delay(500);
		bot.keyPress(KeyEvent.VK_SPACE);
		bot.keyRelease(KeyEvent.VK_SPACE);
	}

	public void selectProject() {
		int projectX = 0, projectY = 0;
		
		if (width == 1366 && height == 768) {
			projectX = 500;
			projectY = 400;
		} else if (width == 800 && height == 600) {
			projectX = 300;
			projectY = 315;
		} else if (width == 1920 && height == 1080) {
			projectX = 900;
			projectY = 560;
		} else if (width == 1600 && height == 900){
			projectX = 600;
			projectY = 470;
		} else if (width == 1280 && height == 720){
			projectX = 500;
			projectY = 375;
		}
	
		bot.mouseMove(projectX, projectY);

		bot.mousePress(InputEvent.BUTTON1_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	
	public void clickGanttButton(){
		int ganttX = 0, ganttY = 0;
		
		if (width == 1366 && height == 768) {
			ganttX = 325;
			ganttY = 250;
		} else if (width == 800 && height == 600) {
			ganttX = 300;
			ganttY = 315;
		} else if (width == 1920 && height == 1080) {
			ganttX = 900;
			ganttY = 560;
		} else if (width == 1600 && height == 900){
			ganttX = 600;
			ganttY = 470;
		} else if (width == 1280 && height == 720){
			ganttX = 500;
			ganttY = 375;
		}
		
		bot.mouseMove(ganttX, ganttY);

		bot.mousePress(InputEvent.BUTTON1_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		bot.mousePress(InputEvent.BUTTON1_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
}
