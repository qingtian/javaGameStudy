package com.github.qingtian.gamestudy;

public class Player extends Fly {

	public static final int STATE_NORMAL = 0;
	public static final int STATE_JUMPING = 1;

	public static final float SPEED = .3f;
	public static final float GRAVITY = .002f;

	private int floorY;
	private int state;

	public Player(Animation animation) {
		super(animation);
		state = STATE_NORMAL;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setFloorY(int floorY) {
		this.floorY = floorY;
		setY(floorY);
	}

	public void jump() {
		setDy(-1);
		state = STATE_JUMPING;
	}

	public void update(long elapsedTime) {
		if (getState() == STATE_JUMPING) {
			setDy(getDy() + GRAVITY * elapsedTime);
		}
		super.update(elapsedTime);
		if (getState() == STATE_JUMPING && getY() >= floorY) {
			setDy(0);
			setY(floorY);
			setState(STATE_NORMAL);
		}
	}
}
