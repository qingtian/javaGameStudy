package com.github.qingtian.gamestudy;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

import javax.swing.JFrame;

/**
 * 屏幕管理器
 * 
 * @author qingtian
 * 
 *         2012-10-11 下午09:56:21
 */
public class ScreenManager {

	private GraphicsDevice device;

	public ScreenManager() {
		GraphicsEnvironment environment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		device = environment.getDefaultScreenDevice();
	}

	public void setFullScreen(DisplayMode displayMode, JFrame window) {
		window.setUndecorated(true);
		window.setResizable(false);

		device.setFullScreenWindow(window);
		if (displayMode != null && device.isDisplayChangeSupported()) {
			try {
				device.setDisplayMode(displayMode);
			} catch (IllegalArgumentException e) {
				// 处理不支持的显示属性
			}
		}

	}

	public Window getFullScreenWindow() {
		return device.getFullScreenWindow();
	}

	public void disposeScreen() {
		Window window = device.getFullScreenWindow();
		if (window != null)
			window.dispose();
		device.setFullScreenWindow(null);
	}
}
