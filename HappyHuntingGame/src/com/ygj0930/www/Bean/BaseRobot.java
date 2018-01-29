package com.ygj0930.www.Bean;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.lang.Math;

//机器人抽象类
public abstract class BaseRobot {

	public String type = "BaseRobot";
	public static final int RADIUS = 10; // 半径
	public static final int TURN_UNIT = 1; // 以1度为转向单位，角度越小，机器人越灵活
	protected Point location; // 机器人位置
	protected int direction; // 朝向
	protected int speed; // 速度(步长)

	// 绘制机器人
	public void draw(Graphics aPen) {
		aPen.setColor(this.getColor());// 机器人颜色
		aPen.fillOval(location.x - RADIUS, location.y - RADIUS, 2 * RADIUS, 2 * RADIUS); // 填充圆形：圆心x、圆心y、宽、高
		aPen.setColor(Color.BLACK); // 画笔颜色
		aPen.drawOval(location.x - RADIUS, location.y - RADIUS, 2 * RADIUS, 2 * RADIUS); // 绘制圆形

		// 绘制朝向线条
		int endX = location.x + (int) (RADIUS * Math.cos(direction * Math.PI / 180.0));
		int endY = location.y - (int) (RADIUS * Math.sin(direction * Math.PI / 180.0));
		aPen.setColor(Color.BLACK);
		aPen.drawLine(location.x, location.y, endX, endY);

	}

	// 每次刷新位置，警察都要找到新的目标点
	abstract protected Point findTarget(HuntingPoints world);

	// 计算出方向:根据需要前往的目标点，结合警察现在的坐标，计算出需要前往的方向（角度）【小偷是随机转向、警察则根据要前往的目标点坐标来计算】
	abstract protected int computeDesiredDirection(Point target);

	// 移动：路线规划与避障
	protected void moveOneStep(HuntingPoints p) {
		Point target = this.findTarget(p); // 寻找下一步前往的目标点
		direction = this.computeDesiredDirection(target); // 根据目标点，计算出下一步的方向

		// 移动：避障
		// 下一步走到的点
		Point middle = new Point();
		middle.x = (int) (location.x + speed * Math.cos((double) direction * Math.PI / 180.0));
		middle.y = (int) (location.y - speed * Math.sin((double) direction * Math.PI / 180.0));

		// 循环检测壁障位置是否有障碍/墙/其他机器人 by GXQ
		int count = 4; // 转向次数
		while ((checkForCrahingWall(middle.x, middle.y, p) == true || checkForCollision(middle.x, middle.y, p) == true)
				&& count > 0) {
			if (checkForCrahingWall(middle.x, middle.y, p) == true)
			{
				direction -= 90;
				if (direction > 180)
					direction -= 360;
				if (direction <= -180)
					direction += 360;
			}
			// 若碰撞人或障碍物:在检测函数中计算出避障方向。
			// 在避障方向上走一步
			middle.x = (int) (location.x + speed * Math.cos((double) direction * Math.PI / 180.0));
			middle.y = (int) (location.y - speed * Math.sin((double) direction * Math.PI / 180.0));
			count--;
		}
		if (count == 0) {
			return;
		} else
			setLocation(middle); // 下一步的点无碰撞，则更新点坐标：让当前点移动到下一个点，

	}

	// 定时器每次刷新式更新机器人位置：走一步
	public void update(HuntingPoints world) {
		moveOneStep(world);
	}

	// 避障算法：遇到障碍物时怎么走(参数为障碍点坐标)
	abstract protected void obstacleAvoiding(int x, int y, HuntingPoints p, boolean is_obstacle);

	// 撞墙检测
	private boolean checkForCrahingWall(int x, int y, HuntingPoints points) {
		if (x <= BaseRobot.RADIUS || y <= BaseRobot.RADIUS || x >= points.getWidth() - BaseRobot.RADIUS
				|| y >= points.getHeight() - BaseRobot.RADIUS) {// 点的合法性判断：下一步在当前点内或者超出了面板范围
			return true;
		}
		return false;
	}

	// 碰撞检查：对下一个点坐标进行检测
	private boolean checkForCollision(int x, int y, HuntingPoints world) {

		// ========障碍物碰撞检测与避障=====
		for (int i = 0; i < world.getObstacles().size(); i++) {
			int x1 = world.getObstacles().get(i).getLocation().x;
			int y1 = world.getObstacles().get(i).getLocation().y;
			if (Point.distance(x1, y1, x, y) < Obstacle.RADIUS + BaseRobot.RADIUS) { // 两点距离小于障碍物半径+机器人半径
				this.obstacleAvoiding(x1, y1, world, true); //// 计算避障方向
				return true;
			}
		}

		// ========机器人之间碰撞检测与避障=====
		int otherRobotIndex = 0;
		if (this.getColor() == Color.CYAN) { // 检测警察机器人的碰撞
			while (otherRobotIndex < world.getPolices().size()) {
				if (location == world.getPolice(otherRobotIndex).getLocation())
					otherRobotIndex++;
				else {
					if (Point.distanceSq((double) x, (double) y,
							(double) world.getPolice(otherRobotIndex).getLocation().x,
							(double) world.getPolice(otherRobotIndex).getLocation().y) <= 4 * RADIUS * RADIUS) { // 和某个警察碰撞，则避开
						this.obstacleAvoiding(world.getPolice(otherRobotIndex).getLocation().x,
								world.getPolice(otherRobotIndex).getLocation().y, world, false);
						return true;
					} else
						otherRobotIndex++;
				}
			}
			// 机器人和小偷碰撞：不避开
			if (Point.distanceSq((double) x, (double) y, (double) world.getThief().getLocation().x,
					(double) world.getThief().getLocation().y) <= 4 * RADIUS * RADIUS) {
				return true;
			}
		} else { // 检查小偷机器人与4个警察机器人的碰撞
			while (otherRobotIndex < world.getPolices().size()) {
				if (Point.distanceSq((double) x, (double) y, (double) world.getPolice(otherRobotIndex).getLocation().x,
						(double) world.getPolice(otherRobotIndex).getLocation().y) <= 4 * RADIUS * RADIUS) {
					this.obstacleAvoiding(x, y, world, false); // 计算避障方向
					return true;
				} else
					otherRobotIndex++;
			}

		}
		return false;
	}

	public BaseRobot(Point loc) {
		location = loc;
		direction = 0;
		speed = 15; // 步长等于半径，则避障时转弯走一步即可避开障碍物
	}

	public Color getColor() {
		return Color.yellow;
	}

	public Point getLocation() {
		return location;
	}

	public int getDirection() {
		return direction;
	}

	public int getSpeed() {
		return speed;
	}

	public void setLocation(Point p) {
		location = p;
	}

	public void setSpeed(int s) {
		speed = s;
	}
}