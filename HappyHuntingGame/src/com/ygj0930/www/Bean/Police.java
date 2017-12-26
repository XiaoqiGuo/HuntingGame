package com.ygj0930.www.Bean;

import java.awt.Color;
import java.awt.Point;

public class Police extends BaseRobot {
	public String type = "Police";

	// 根据点坐标创建机器人
	public Police(Point loc) {
		super(loc);
	}

	// 警察是青色
	public Color getColor() {
		return Color.CYAN;
	}

	// 找出该名警察需要前往的目标点（小偷上下左右的某个方向）【根据警察的下标来分配目标点】
	protected Point findTarget(HuntingPoints p) {
		return p.getTarget(this);
	}

	// 路线规划：计算出该名警察下一步要去的方向（角度）
	protected int computeDesiredDirection(Point target) {
		return Bug2Direction(target);
	}

	// Bug2算法：以警察当前位置与所分配给他的围捕点，计算出朝向
	private int Bug2Direction(Point target) {
		int xdiff, ydiff, d;
		double hdiff;
		xdiff = target.x - location.x;
		ydiff = target.y - location.y;
		hdiff = Math.sqrt((double) (xdiff * xdiff + ydiff * ydiff)); // 两点间距离
		d = (int) (Math.acos(xdiff / hdiff) * 180 / Math.PI); // 警察到目标的直线与x轴夹角
		// 校正角度
		if (ydiff > 0)
			d = -d;
		if (d > 180)
			d -= 360;
		if (d <= -180)
			d += 360;
		return d;
	}

	// 避障:遇到障碍物时怎么走
	@Override
	protected void obstacleAvoiding(int x, int y, HuntingPoints p,boolean is_obstacle) {
		if(is_obstacle){
			//vectorDirection(x, y, p); //障碍物避障：人工势场法
			turnLeftDirection(x, y); //障碍物避障：绕行法，挨着障碍物边缘走
		}else{
			turnLeftDirection(x, y); //机器人之间避障：转弯绕行即可
		}
	}

	// 避障算法1：直接左转90,绕行障碍物
	private void turnLeftDirection(int x, int y) {
		int xdiff, ydiff, d;
		double hdiff;
		xdiff = x - this.location.x;
		ydiff = y - this.location.y;
		hdiff = Math.sqrt((double) (xdiff * xdiff + ydiff * ydiff));
		d = (int) (Math.acos(xdiff / hdiff) * 180 / Math.PI);
		if (ydiff > 0)
			d = -d;
		d += 90;
		if (d > 180)
			d -= 360;
		if (d <= -180)
			d += 360;
		this.direction = d;
	}

	// 避障算法2：人工势场法
	private void vectorDirection(int x, int y, HuntingPoints p) {
		Point targetPoint = p.getTarget(this);

		//====根据向量的平移，我们令障碍物为坐标原点，计算出障碍物指向机器人、目标点的向量坐标值
		// 障碍物指向机器人的向量
		double x1 = this.location.x - x;
		double y1 = this.location.y - y;
		// 障碍物指向目标点的向量
		double x2 = targetPoint.x - x;
		double y2 = targetPoint.y - y;
		// 计算合向量
		double vecX, vecY;
		vecX = 10 * x1 + x2 + x;
		vecY = 10 * y1 + y2 + y;
        Vector2D resultVector2d = new Vector2D(vecX,vecY);
        int d = (int) resultVector2d.getAngle();
		this.direction = d;
	}
}