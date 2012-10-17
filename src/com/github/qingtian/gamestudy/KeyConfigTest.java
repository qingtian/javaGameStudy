package com.github.qingtian.gamestudy;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class KeyConfigTest extends InputManagerTest implements ActionListener {

	protected GameAction config;
	private JButton playButton;
	private JButton configButton;
	private JButton quitButton;
	private JButton pauseButton;
	private JPanel playButtonSpace;

	public static void main(String[] args) {
		new KeyConfigTest().run();
	}

	private static final String message = "键盘配置";

	private JPanel dialog;
	private JButton okButton;
	private List inputs;

	public void init() {
		super.init();
		NoRepaintManager.install();
		config = new GameAction("config");

		quitButton = createButton("quit", "退出");
		playButton = createButton("play", "继续");
		pauseButton = createButton("pause", "暂停");
		configButton = createButton("config", "配置");

		playButtonSpace = new JPanel();
		playButtonSpace.setOpaque(false);
		playButtonSpace.add(pauseButton);

		JFrame frame = (JFrame) super.screen.getFullScreenWindow();
		Container contentPane = frame.getContentPane();

		if (contentPane instanceof JComponent) {
			((JComponent) contentPane).setOpaque(false);
		}

		contentPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		contentPane.add(playButtonSpace);
		contentPane.add(configButton);
		contentPane.add(quitButton);
		inputs = new ArrayList();

		JPanel configPanel = new JPanel(new GridLayout(5, 2, 2, 2));
		addActionConfig(configPanel, moveLeft);
		addActionConfig(configPanel, moveRight);
		addActionConfig(configPanel, jump);
		addActionConfig(configPanel, pause);
		addActionConfig(configPanel, exit);

		// 创建按钮
		JPanel bottomPanel = new JPanel(new FlowLayout());
		okButton = new JButton("OK");
		okButton.setFocusable(false);
		okButton.addActionListener(this);
		bottomPanel.add(okButton);

		JPanel topPanel = new JPanel(new FlowLayout());
		topPanel.add(new JLabel(message));
		Border border = BorderFactory.createLineBorder(Color.black);

		dialog = new JPanel(new BorderLayout());
		dialog.add(topPanel, BorderLayout.NORTH);
		dialog.add(configPanel, BorderLayout.CENTER);
		dialog.add(bottomPanel, BorderLayout.SOUTH);
		dialog.setBorder(border);
		dialog.setVisible(false);
		dialog.setSize(dialog.getPreferredSize());

		dialog.setLocation((screen.getWidth() - dialog.getWidth()) / 2,
				(screen.getHeight() - dialog.getHeight()) / 2);

		((JFrame) screen.getFullScreenWindow()).getLayeredPane().add(dialog,
				JLayeredPane.MODAL_LAYER);
	}

	private void addActionConfig(JPanel configPanel, GameAction action) {
		JLabel label = new JLabel(action.getName(), JLabel.RIGHT);
		InputComponent input = new InputComponent(action);
		configPanel.add(label);
		configPanel.add(input);
		inputs.add(input);
	}

	public void setPaused(boolean p) {
		super.setPaused(p);
		playButtonSpace.removeAll();
		if (isPaused()) {
			playButtonSpace.add(playButton);
		} else {
			playButtonSpace.add(pauseButton);
		}
	}

	public void draw(Graphics2D g) {
		super.draw(g);
		JFrame frame = (JFrame) super.screen.getFullScreenWindow();
		frame.getLayeredPane().paintComponents(g);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == okButton) {
			config.tap();
			return;
		}
		Object src = event.getSource();
		if (src == quitButton) {
			super.exit.tap();
		} else if (src == configButton) {
			config.tap();
		} else if (src == playButton || src == pauseButton) {
			super.pause.tap();
		}
	}

	public void checkSystemInput() {
		super.checkSystemInput();
		if (config.isPressed()) {
			System.out.println("点击配置键");
			boolean show = !dialog.isVisible();
			dialog.setVisible(show);
			setPaused(show);
		}
	}

	private void resetInputs() {
		for (int i = 0; i < inputs.size(); i++) {
			((InputComponent) inputs.get(i)).setText();
		}
	}

	public JButton createButton(String name, String toolTip) {
		String imagePath = "images/menu/" + name + ".png";
		ImageIcon iconRollover = new ImageIcon(imagePath);
		int w = iconRollover.getIconWidth();
		int h = iconRollover.getIconHeight();
		Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

		Image image = screen.createCompatibleImage(w, h,
				Transparency.TRANSLUCENT);
		Graphics2D g = (Graphics2D) image.getGraphics();
		Composite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				.5f);
		g.setComposite(alpha);
		g.drawImage(iconRollover.getImage(), 0, 0, null);
		g.dispose();

		ImageIcon iconDefault = new ImageIcon(image);
		image = screen.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
		g = (Graphics2D) image.getGraphics();
		g.drawImage(iconRollover.getImage(), 2, 2, null);
		g.dispose();

		ImageIcon iconPressed = new ImageIcon(image);
		JButton button = new JButton();
		button.addActionListener(this);
		button.setIgnoreRepaint(true);
		button.setFocusable(false);
		button.setToolTipText(toolTip);
		button.setBorder(null);
		button.setContentAreaFilled(false);
		button.setCursor(cursor);
		button.setIcon(iconDefault);
		button.setRolloverIcon(iconRollover);
		button.setPressedIcon(iconPressed);

		return button;

	}

	class InputComponent extends JTextField {

		private GameAction action;

		public InputComponent(GameAction action) {
			this.action = action;
			setText();
			enableEvents(KeyEvent.KEY_EVENT_MASK | MouseEvent.MOUSE_EVENT_MASK
					| MouseEvent.MOUSE_MOTION_EVENT_MASK
					| MouseEvent.MOUSE_WHEEL_EVENT_MASK);
		}

		private void setText() {
			String text = "";
			List list = inputManager.getMaps(action);
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					text += (String) list.get(i) + ", ";
				}
				text = text.substring(0, text.length() - 2);
			}

			synchronized (getTreeLock()) {
				setText(text);
			}
		}

		private void mapGameAction(int code, boolean isMouseMap) {
			if (inputManager.getMaps(action).size() >= 3) {
				inputManager.clearMap(action);
			}
			if (isMouseMap) {
				inputManager.mapToMouse(action, code);
			} else {
				inputManager.mapToKey(action, code);
			}
			resetInputs();
			screen.getFullScreenWindow().requestFocus();
		}

		protected void processKeyEvent(KeyEvent e) {
			if (e.getID() == e.KEY_PRESSED) {
				if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE
						&& inputManager.getMaps(action).size() > 0) {
					inputManager.clearMap(action);
					setText("");
					screen.getFullScreenWindow().requestFocus();
				} else {
					mapGameAction(e.getKeyCode(), false);
				}
			}
			e.consume();
		}

		protected void processMouseEvent(MouseEvent e) {
			if (e.getID() == e.MOUSE_PRESSED) {
				if (hasFocus()) {
					int code = InputManager.getMouseButtonCode(e);
					mapGameAction(code, true);
				} else {
					requestFocus();
				}
			}
			e.consume();
		}

		protected void processMouseMotionEvent(MouseEvent e) {
			e.consume();
		}

		protected void processMouseWheelEvent(MouseWheelEvent e) {
			if (hasFocus()) {
				int code = InputManager.MOUSE_WHEEL_DOWN;
				if (e.getWheelRotation() < 0) {
					code = InputManager.MOUSE_WHEEL_UP;
				}
				mapGameAction(code, true);
			}
			e.consume();
		}
	}
}
