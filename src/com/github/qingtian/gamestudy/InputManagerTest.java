package com.github.qingtian.gamestudy;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.KeyEvent;

public class InputManagerTest extends CoreSupport {

	public static void main(String[] args) {
		new InputManagerTest().run();
	}

	protected GameAction jump;
	protected GameAction exit;
	protected GameAction moveLeft;
	protected GameAction moveRight;
	protected GameAction pause;

	protected InputManager inputManager;
	private Player player;
	private Image bgImage;
	private boolean paused;

	public void init() {
		super.init();
		Window window = screen.getFullScreenWindow();
		inputManager = new InputManager(window);
		createGameActions();
		createFly();
		paused = false;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		if (this.paused != paused) {
			this.paused = paused;
			inputManager.resetAllGameActions();
		}
	}

	@Override
	public void update(long elapsedTime) {
		checkSystemInput();

		if (!isPaused()) {
			checkGameInput();
			player.update(elapsedTime);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(bgImage, 0, 0, null);
		g.drawImage(player.getImage(), Math.round(player.getX()),
				Math.round(player.getY()), null);
	}

	private void createFly() {
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

		player = new Player(animation);
		player.setFloorY(screen.getHeight() - player.getHeight());
	}

	protected void checkSystemInput() {
		if (pause.isPressed()) {
			setPaused(!isPaused());
		}
		if (exit.isPressed()) {
			stop();
		}
	}

	private void checkGameInput() {
		float x = 0;
		if (moveLeft.isPressed()) {
			x -= Player.SPEED;
		}
		if (moveRight.isPressed()) {
			x += Player.SPEED;
		}
		player.setDx(x);
		if (jump.isPressed() && player.getState() != Player.STATE_JUMPING) {
			player.jump();
		}
	}

	private void createGameActions() {
		jump = new GameAction("jump", GameAction.DETECT_INITAL_PRESS_ONLY);
		exit = new GameAction("exit", GameAction.DETECT_INITAL_PRESS_ONLY);
		moveLeft = new GameAction("moveLeft");
		moveRight = new GameAction("moveRight");
		pause = new GameAction("pause", GameAction.DETECT_INITAL_PRESS_ONLY);

		inputManager.mapToKey(exit, KeyEvent.VK_ESCAPE);
		inputManager.mapToKey(pause, KeyEvent.VK_P);
		inputManager.mapToKey(jump, KeyEvent.VK_SPACE);
		inputManager.mapToMouse(jump, InputManager.MOUSE_BUTTON_1);

		inputManager.mapToKey(moveLeft, KeyEvent.VK_LEFT);
		inputManager.mapToKey(moveRight, KeyEvent.VK_RIGHT);

		inputManager.mapToKey(moveLeft, KeyEvent.VK_A);
		inputManager.mapToKey(moveRight, KeyEvent.VK_D);

		inputManager.mapToMouse(moveLeft, InputManager.MOUSE_MOVE_LEFT);
		inputManager.mapToMouse(moveRight, InputManager.MOUSE_MOVE_RIGHT);
	}

}
