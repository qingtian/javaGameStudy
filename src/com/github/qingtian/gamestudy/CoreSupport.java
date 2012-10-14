package com.github.qingtian.gamestudy;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;

import javax.swing.ImageIcon;

public abstract class CoreSupport {

	protected static final int FONT_SIZE = 24;

	private static final DisplayMode POSSIBLE_MODES[] = {
			new DisplayMode(1024, 768, 24, 0),
			new DisplayMode(1024, 768, 16, 0),
			new DisplayMode(800, 600, 32, 0), new DisplayMode(800, 600, 24, 0),
			new DisplayMode(800, 600, 16, 0) };

	private boolean isRunning;
	protected ScreenManagerOfPageFlipping screen;

	public void stop() {
		isRunning = false;
	}

	public void run() {
		try {
			init();
			gameLoop();
		} finally {
			screen.disposeScreen();
		}
	}

	public void init() {
		screen = new ScreenManagerOfPageFlipping();
		DisplayMode displayMode = screen
				.findFirstCompatibleMode(POSSIBLE_MODES);
		screen.setFullScreen(displayMode);
		Window window = screen.getFullScreenWindow();
		window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
		window.setBackground(Color.black);
		window.setForeground(Color.white);

		isRunning = true;
	}

	public Image loadImage(String filename) {
		return new ImageIcon(filename).getImage();
	}

	public void gameLoop() {
		long startTime = System.currentTimeMillis();
		long currTime = startTime;

		while (isRunning) {
			long elapsedTime = System.currentTimeMillis() - currTime;// 经过时间
			currTime += elapsedTime;
			update(elapsedTime);
			// 双缓存
			Graphics2D g = screen.getGraphics();
			draw(g);
			g.dispose();
			screen.update();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {

			}
		}
	}

	public void update(long elapsedTime) {

	}

	public abstract void draw(Graphics2D g);
}
