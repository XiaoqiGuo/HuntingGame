package com.ygj0930.www.Bean;

import java.awt.Color;
import java.awt.Point;

public class Thief extends BaseRobot {
	public String type = "Thief";

	public Thief(Point loc) {
		super(loc);
	}

	public Color getColor() {
		return Color.yellow; // 小偷是黄色
	}

	// 小偷逃跑策略：往最近的警察的反方向跑，或随机左右转向跑
	// 实验显示：单纯往最近警察的反方向跑，小偷的路线随机性不明显；全程随机，则小偷前期在原地乱转而位移不明显；
	//因此综合采用两种方式：每一步，要么反向跑，要么随机转向
	@Override
	protected int computeDesiredDirection(Point target) {
		int decision = (int) (Math.random() * 10); // 随机生成一个转向抉择：反向逃跑 or 随机转向
		if (decision >= 0) {
			if (decision == 2 || decision == 6) 
				return antiTurns(target);
			else
				return randomTurns();
		}
		return randomTurns();
	}

	// 往最近警察的反方向跑
	public int antiTurns(Point target) {
		int xx, yy;
		double hh;
		int degree;
		if (target != null) {
			xx = target.x - location.x;
			yy = target.y - location.y;
			hh = Math.sqrt((double) (xx * xx + yy * yy));
			degree = (int) Math.acos((xx / hh) * 180 / Math.PI);
			if (yy >= 0) {
				degree = -degree;
			}
			return degree;
		} else {
			return randomTurns();
		}
	}

	// 随机转向
	public int randomTurns() {
		int decision = (int) (Math.random() * 10); // 随机生成一个转向抉择：左转or右转
		int amount = (int) (Math.random() * 90 / TURN_UNIT); // 随机生成转多少个单位
		if (decision >= 0) {
			if (decision == 1 || decision == 3 || decision == 5 || decision == 7 || decision == 9) // 随机右转（减角度）
				direction = direction - TURN_UNIT * amount;
			else
				direction = direction + TURN_UNIT * amount; // 随机左转（加角度）
		}
		// 将朝向与x轴的角度控制在180以内，用于绘制朝向线的末端点时确定x\y坐标
		if (direction > 180)
			direction -= 360;
		if (direction <= -180)
			direction += 360;
		return direction; // 返回朝向
	}

	// 避障：转向90度绕开障碍物
	protected void obstacleAvoiding(int x, int y,HuntingPoints p,boolean is_obstacle) {
		this.direction += 90;
		if (this.direction > 180)
			this.direction -= 360;
		if (this.direction <= -180)
			this.direction += 360;
	}

	// 寻找距离最近的警察
	@Override
	protected Point findTarget(HuntingPoints p) {
		double min = Double.MAX_VALUE; // 记录警察与小偷的最近距离
		int count = 0;
		int index = -1; // 最近距离警察的下标
		while (count < p.getPolices().size()) {
			Police curr = (Police) p.getPolice(count);
			// 遍历四个警察，分别计算与小偷的距离，找出最近者
			double currDis = Point.distance(location.x, location.y, curr.getLocation().x, getLocation().y);
			if (currDis <= min) {
				min = currDis;
				index = count;
				count++;
			} else {
				count++;
			}
		}
		return index >= 0 ? p.getPolice(index).location : null;
	}
}
