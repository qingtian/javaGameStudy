package com.github.qingtian.gamestudy;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class KeyTest extends CoreSupport implements KeyListener {

	public static void main(String[] args) {
		new KeyTest().run();
	}

	private LinkedList<String> messages = new LinkedList<String>();

	public void init() {
		super.init();
		Window window = screen.getFullScreenWindow();
		window.setFocusTraversalKeysEnabled(false);
		window.addKeyListener(this);
		addMessage("键盘测试，按下ESC退出");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ESCAPE)
			stop();
		else
			addMessage("Pressed: " + KeyEvent.getKeyText(keyCode));
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		addMessage("Released: " + KeyEvent.getKeyText(keyCode));
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public synchronized void addMessage(String message) {
		messages.add(message);
		// 消息提示过长，清除首个消息
		if (messages.size() >= screen.getHeight() / FONT_SIZE)
			messages.remove(0);
	}

	@Override
	public void draw(Graphics2D g) {
		Window window = screen.getFullScreenWindow();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(window.getBackground());
		g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
		g.setColor(window.getForeground());
		// 提示消息显示纵向起始位置
		int y = FONT_SIZE;
		for (int i = 0; i < messages.size(); i++) {
			g.drawString(messages.get(i), 5, y);
			y += FONT_SIZE;
		}
	}

}
