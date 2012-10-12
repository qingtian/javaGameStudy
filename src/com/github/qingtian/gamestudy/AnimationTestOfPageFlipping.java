package com.github.qingtian.gamestudy;

import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class AnimationTestOfPageFlipping extends JFrame {

	private ScreenManagerOfPageFlipping screenManager;
	private boolean imagesLoaded;
	private Animation animation;

	public static void main(String[] args) {

		DisplayMode displayMode = new DisplayMode(1024, 768, 16,
				DisplayMode.REFRESH_RATE_UNKNOWN);
		new AnimationTestOfPageFlipping().run(displayMode);
	}

	public void loadImages() {
		Image frame1 = loadImage("images/1.jpg");
		Image frame2 = loadImage("images/2.jpg");
		Image frame3 = loadImage("images/3.jpg");
		animation = new Animation();
		animation.addFrame(frame1, 150);
		animation.addFrame(frame2, 250);
		animation.addFrame(frame1, 150);
		animation.addFrame(frame2, 150);
		animation.addFrame(frame3, 200);
		animation.addFrame(frame2, 150);
		imagesLoaded = true;
	}

	private Image loadImage(String filename) {
		return new ImageIcon(filename).getImage();
	}

	public void animationLoop() {
		long startTime = System.currentTimeMillis();
		long currTime = startTime;

		while (currTime - startTime < 10000) {
			long nowTime = System.currentTimeMillis();
			long elapsedTime = nowTime - currTime;// 经过时间
			currTime += elapsedTime;
			animation.update(elapsedTime);
			// 双缓存
			Graphics2D g = screenManager.getGraphics();
			draw(g);
			g.dispose();
			screenManager.update();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {

			}
		}
	}

	public void run(DisplayMode displayMode) {
		screenManager = new ScreenManagerOfPageFlipping();
		try {
			screenManager.setFullScreen(displayMode, this);
			loadImages();
			animationLoop();
		} finally {
			screenManager.disposeScreen();
		}
	}

	public void draw(Graphics g) {
		if (imagesLoaded) {
			g.drawImage(animation.getImage(), 0, 0, null);
		} else {
			g.drawString("正在读取图片", 5, 24);
		}
	}
}
