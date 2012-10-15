package com.github.qingtian.gamestudy;

public class GameAction {

	public static final int NORMAL = 0;
	public static final int DETECT_INITAL_PRESS_ONLY = 1;

	private static final int STATE_RELEASED = 0;
	private static final int STATE_PRESSED = 1;
	private static final int STATE_WAITING_FOR_RELEASE = 2;

	private String name;// 动作名
	private int behavior;// 行为
	private int count;// 按下的次数
	private int state;// 状态

	public GameAction(String name) {
		this(name, NORMAL);
	}

	public GameAction(String name, int behavior) {
		this.name = name;
		this.behavior = behavior;
		reset();
	}

	public String getName() {
		return name;
	}

	public void reset() {
		state = STATE_RELEASED;
		count = 0;
	}

	public void tap() {
		press();
		release();
	}

	public void press() {
		press(1);
	}

	public void press(int amount) {
		if (state != STATE_WAITING_FOR_RELEASE) {
			this.count += amount;
			state = STATE_PRESSED;
		}
	}

	public void release() {
		state = STATE_RELEASED;
	}

	public int getCount() {
		int s = count;
		if (s != 0) {
			if (state == STATE_PRESSED) {
				count = 0;
			} else if (behavior == DETECT_INITAL_PRESS_ONLY) {
				state = STATE_WAITING_FOR_RELEASE;
				count = 0;
			}
		}
		return s;
	}

	public boolean isPressed() {
		return (getCount() != 0);
	}
}
