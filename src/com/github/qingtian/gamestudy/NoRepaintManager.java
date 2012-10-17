package com.github.qingtian.gamestudy;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

public class NoRepaintManager extends RepaintManager {

	public static void install() {
		RepaintManager repaintManager = new NoRepaintManager();
		repaintManager.setDoubleBufferingEnabled(false);
		RepaintManager.setCurrentManager(repaintManager);
	}

	@Override
	public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
	}

	@Override
	public synchronized void addInvalidComponent(JComponent invalidComponent) {
	}

	@Override
	public void markCompletelyDirty(JComponent aComponent) {
	}

	@Override
	public void paintDirtyRegions() {
	}

}
