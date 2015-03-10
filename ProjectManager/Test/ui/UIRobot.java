package ui;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class UIRobot implements Runnable {

	private Robot bot = null;
	private int projectX = 0;
	private int projectY = 0;
	
	private int ganttX = 0;
	private int ganttY = 0;
	
	public UIRobot() {
		// 1366 x 768
//		projectX = 500;
//		projectY = 400;
		
		// 800 x 600
		
		
		
		// 1920 X 1080
		run();
	}

	@Override
	public void run() {
		try {
			bot = new Robot();
		} catch (Exception e) {

		}
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
	
	public void selectProject(){
		bot.mouseMove(500, 400);
		
		bot.mousePress(InputEvent.BUTTON1_MASK);
		System.out.println("click press 1");
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		System.out.println("click release 1");
		bot.mousePress(InputEvent.BUTTON1_MASK);
		System.out.println("click press 2");
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		System.out.println("click release 2");
	}
}
