package com.github.qingtian.gamestudy;

import java.awt.Image;
import java.util.ArrayList;

public class Animation {

	private ArrayList<AnimationFrame> frames;// 帧
	private int currentFrameIndex;// 当前帧
	private long animationTime;// 时间
	private long total;// 总时间

	public Animation() {
		frames = new ArrayList<AnimationFrame>();
		total = 0;
	}

	public void addFrame(Image image, long time) {
		total += time;
		frames.add(new AnimationFrame(image, total));
	}

	public void update(long passedTime) {
		if (frames.size() > 1) {
			animationTime += passedTime;
			if (animationTime >= total) {
				animationTime = animationTime % total;
				currentFrameIndex = 0;
			}
			while (animationTime > getFrame(currentFrameIndex).endTime) {
				currentFrameIndex++;
			}
		}

	}

	public Image getImage() {
		if (frames.size() == 0)
			return null;
		else
			return getFrame(currentFrameIndex).image;
	}

	private AnimationFrame getFrame(int i) {
		return frames.get(i);
	}
}
