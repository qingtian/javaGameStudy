package com.github.qingtian.gamestudy;

import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

/**
 * 双缓存，自适应分辨率的屏幕管理器
 * 
 * @author qingtian
 * 
 *         2012-10-11 下午09:56:21
 */
public class ScreenManagerOfPageFlipping {

	private GraphicsDevice device;

	public ScreenManagerOfPageFlipping() {
		GraphicsEnvironment environment = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		device = environment.getDefaultScreenDevice();
	}

	public DisplayMode findFirstCompatibleMode(DisplayMode modes[]) {
		DisplayMode[] goodModes = device.getDisplayModes();
		for (int i = 0; i < modes.length; i++) {
			for (int j = 0; j < goodModes.length; j++) {
				if (displayModesMatch(modes[i], goodModes[j])) {
					return modes[i];
				}
			}
		}
		return null;
	}

	public DisplayMode getCurrentDisplayMode() {
		return device.getDisplayMode();
	}

	public boolean displayModesMatch(DisplayMode provide, DisplayMode accept) {
		if (provide.getWidth() != accept.getWidth()
				|| provide.getHeight() != accept.getHeight())
			return false;
		if (provide.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI
				&& accept.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI
				&& provide.getBitDepth() != accept.getBitDepth())
			return false;
		if (provide.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN
				&& accept.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN
				&& provide.getRefreshRate() != accept.getRefreshRate())
			return false;
		return true;
	}

	public void setFullScreen(DisplayMode displayMode, final JFrame window) {
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setUndecorated(true);
		window.setIgnoreRepaint(true);
		window.setResizable(false);

		device.setFullScreenWindow(window);

		if (displayMode != null && device.isDisplayChangeSupported()) {
			try {
				device.setDisplayMode(displayMode);
			} catch (IllegalArgumentException e) {
				// 处理不支持的显示属性
				window.setSize(displayMode.getWidth(), displayMode.getHeight());
			}
		}
		// 创建双缓存
		try {
			EventQueue.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					window.createBufferStrategy(2);
				}

			});
		} catch (InterruptedException e) {

		} catch (InvocationTargetException e) {

		}

	}

	public Graphics2D getGraphics() {
		Window window = device.getFullScreenWindow();
		if (window != null) {
			BufferStrategy strategy = window.getBufferStrategy();
			return (Graphics2D) strategy.getDrawGraphics();
		} else
			return null;
	}

	public void update() {
		Window window = device.getFullScreenWindow();
		if (window != null) {
			BufferStrategy strategy = window.getBufferStrategy();
			if (!strategy.contentsLost())
				strategy.show();
		}
	}

	public int getWidth() {
		Window window = device.getFullScreenWindow();
		if (window != null) {
			return window.getWidth();
		} else
			return 0;
	}

	public int getHeight() {
		Window window = device.getFullScreenWindow();
		if (window != null) {
			return window.getHeight();
		} else
			return 0;
	}

	public Window getFullScreenWindow() {
		return device.getFullScreenWindow();
	}

	/**
	 * 生成与当前显示器兼容的图像，就是与显示器有相同位深度和颜色模型的图像<br/>
	 * 生成的图像为bufferedImage，是系统内存中存放 的非加速图像
	 * 
	 * @param w
	 * @param h
	 * @param transparancy
	 * @return
	 */
	public BufferedImage createCompatibleImage(int w, int h, int transparancy) {
		Window window = device.getFullScreenWindow();
		if (window != null) {
			GraphicsConfiguration gc = window.getGraphicsConfiguration();
			return gc.createCompatibleImage(w, h, transparancy);
		} else
			return null;
	}

	public void disposeScreen() {
		Window window = device.getFullScreenWindow();
		if (window != null)
			window.dispose();
		device.setFullScreenWindow(null);
	}
}
