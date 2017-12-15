package com.ygj0930.www.Bean;

import java.awt.Point;

public class Thief extends BaseRobot {
	public String type = "Thief";

	public Thief(Point loc) {
		super(loc);
	}

	public int computeRandomDirectionChange() {
		int decision = (int) (Math.random() * 10); // 随机生成一个转向抉择：左转or右转
		int amount = (int) (Math.random() * 90 / TURN_UNIT); // 随机生成转多少个单位
		if (decision >= 0) {
			if (decision == 1 || decision == 3 || decision == 5 || decision == 7 || decision == 9) // 奇数右转（减角度）
				direction = direction - TURN_UNIT * amount;
			else
				direction = direction + TURN_UNIT * amount; // 偶数左转（加角度）
		}
		// 将朝向与x轴的角度控制在180以内，用于绘制朝向线的末端点时确定x\y坐标
		if (direction > 180)
			direction -= 360;
		if (direction <= -180)
			direction += 360;
		return direction; // 返回朝向
	}

	// 避障策略：待改进
	protected void obstacleAvoiding(int x, int y) {
		this.direction += 90;
		if (this.direction > 180)
			this.direction -= 360;
		if (this.direction <= -180)
			this.direction += 360;
	}

	// 寻找目标点:小偷不需要实现，警察才需要寻找目标点
	@Override
	protected Point findTarget(HuntingPoints world) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int computeDesiredDirection(Point target) {
		return computeRandomDirectionChange();
	}
}
