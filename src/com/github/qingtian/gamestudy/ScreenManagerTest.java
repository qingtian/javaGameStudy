package com.github.qingtian.gamestudy;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ScreenManagerTest extends JFrame {

	public static void main(String[] args) {
		DisplayMode displayMode = new DisplayMode(1024, 768, 16,
				DisplayMode.REFRESH_RATE_UNKNOWN);
		new ScreenManagerTest().run(displayMode);
	}

	public void run(DisplayMode displayMode) {
		setBackground(Color.black);
		setForeground(Color.white);
		setFont(new Font("Dialog", 0, 24));
		ScreenManager screen = new ScreenManager();
		try {
			screen.setFullScreen(displayMode, this);
			Thread.sleep(4000);
		} catch (InterruptedException e) {

		} finally {
			screen.disposeScreen();
		}
	}

	public void paint(Graphics g) {
		if (g instanceof Graphics2D) {
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			g.drawString("这是一个简单的全屏测试...", 20, 50);
		}
	}
}
