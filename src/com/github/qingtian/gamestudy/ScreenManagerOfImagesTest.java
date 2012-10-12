package com.github.qingtian.gamestudy;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ScreenManagerOfImagesTest extends JFrame {

	private Image bgImage;
	private boolean imagesLoaded;

	public static void main(String[] args) {
		DisplayMode displayMode = new DisplayMode(1024, 768, 16,
				DisplayMode.REFRESH_RATE_UNKNOWN);
		new ScreenManagerOfImagesTest().run(displayMode);
	}

	public void loadImages() {
		bgImage = loadImage("images/bg.jpg");
		imagesLoaded = true;
		repaint();
	}

	private Image loadImage(String filename) {
		return new ImageIcon(filename).getImage();
	}

	public void run(DisplayMode displayMode) {
		setBackground(Color.black);
		setForeground(Color.white);
		setFont(new Font("Dialog", 0, 24));
		ScreenManager screen = new ScreenManager();
		try {
			screen.setFullScreen(displayMode, this);
			loadImages();
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

			if (imagesLoaded) {
				g2d.drawImage(bgImage, 0, 0, null);
				g.drawString("这是一个简单带背景图的全屏测试...", 200, 500);
			} else {
				g.drawString("正在读取图片...", 20, 50);
			}

		}
	}
}
