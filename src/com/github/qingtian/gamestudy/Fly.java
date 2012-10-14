package com.github.qingtian.gamestudy;

import java.awt.Image;

public class Fly {

	private Animation animation;

	private float x;
	private float y;

	private float dx;
	private float dy;

	public Fly(Animation animation) {
		this.animation = animation;
	}

	public void update(long passedTime) {
		x += dx * passedTime;
		y += dy * passedTime;
		animation.update(passedTime);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}

	public Image getImage() {
		return animation.getImage();
	}

	public int getWidth() {
		return animation.getImage().getWidth(null);
	}

	public int getHeight() {
		return animation.getImage().getHeight(null);
	}
}
