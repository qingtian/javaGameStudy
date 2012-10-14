package com.github.qingtian.gamestudy;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class FlyTestOfPageFlipping extends JFrame {

	private ScreenManagerOfPageFlipping screenManager;

	private Image bgImage;
	private Fly fly;

	public static void main(String[] args) {

		DisplayMode displayMode = new DisplayMode(1024, 768, 16,
				DisplayMode.REFRESH_RATE_UNKNOWN);
		new FlyTestOfPageFlipping().run(displayMode);
	}

	public void loadImages() {
		bgImage = loadImage("images/bg.jpg");
		Image frame1 = loadImage("images/1.jpg");
		Image frame2 = loadImage("images/2.jpg");
		Image frame3 = loadImage("images/3.jpg");
		Animation animation = new Animation();
		animation.addFrame(frame1, 150);
		animation.addFrame(frame2, 250);
		animation.addFrame(frame1, 150);
		animation.addFrame(frame2, 150);
		animation.addFrame(frame3, 200);
		animation.addFrame(frame2, 150);

		fly = new Fly(animation);

		fly.setDx(0.2f);
		fly.setDy(0.2f);

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
			update(elapsedTime);
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

	public void update(long elapsedTime) {
		if (fly.getX() < 0)
			fly.setDx(Math.abs(fly.getDx()));
		else if (fly.getX() + fly.getWidth() >= screenManager.getWidth())
			fly.setDx(-Math.abs(fly.getDx()));

		if (fly.getY() < 0)
			fly.setDy(Math.abs(fly.getDy()));
		else if (fly.getY() + fly.getHeight() >= screenManager.getHeight())
			fly.setDy(-Math.abs(fly.getDy()));
		fly.update(elapsedTime);
	}

	public void draw(Graphics g) {
		// 通过画背景将背景重置，避免重影
		g.drawImage(bgImage, 0, 0, null);
		g.drawImage(fly.getImage(), Math.round(fly.getX()),
				Math.round(fly.getY()), null);
	}
}
