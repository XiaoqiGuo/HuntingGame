package com.ygj0930.www.Bean;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.lang.Math;

//机器人抽象类
public abstract class BaseRobot {
	public String type = "BaseRobot";
	public static final int RADIUS = 15; // 半径
	public static final int SQUARE_OF_RADIUS = RADIUS * RADIUS; // 半径的平方
	public static final int TURN_UNIT = 1; // 以1度为转向单位，角度越小，机器人越灵活

	protected Point location; // 机器人位置
	protected int direction; // 朝向
	protected int speed; // 速度

	public boolean collisionObstacle; // 是否撞到障碍物
	public boolean collisionRobot; // 是否撞到机器人
	public boolean collisionWall; // 是否撞到墙

	public BaseRobot(Point loc) {
		location = loc;
		direction = 0;
		speed = RADIUS; // 步长等于半径
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

	// 绘制机器人
	public void drawWith(Graphics aPen) {
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
	protected void moveOneStep(HuntingPoints world) {
		// 转向
		Point target = this.findTarget(world);
		direction = this.computeDesiredDirection(target); // 计算出下一步的方向

		// 移动：避障

		// 下一步走到的点
		Point middle = new Point();
		middle.x = (int) (location.x + speed * Math.cos((double) direction * Math.PI / 180.0));
		middle.y = (int) (location.y - speed * Math.sin((double) direction * Math.PI / 180.0));

		// 撞墙检查：若下一步小偷撞墙，则掉头【待解决：若警察也掉头，会出现出去的警察再也回不来...】
		if (checkForCrahingWall(middle.x, middle.y, world) == false) {
			if (this.getColor() != Color.CYAN) {
				direction += 180;
				if (direction > 180)
					direction -= 360;
				if (direction <= -180)
					direction += 360;
				middle.x = (int) (location.x + speed * Math.cos((double) direction * Math.PI / 180.0));
				middle.y = (int) (location.y - speed * Math.sin((double) direction * Math.PI / 180.0));
			}
		}

		// 碰撞检测：下一个点是否会碰撞
		if (checkForCollision(middle.x, middle.y, world) == false) {
			setLocation(middle); // 下一步的点无碰撞，则更新点坐标：让当前点移动到下一个点
		} else {
			// 若碰撞，则再前一步
			middle.x = (int) (location.x + speed * Math.cos((double) direction * Math.PI / 180.0));
			middle.y = (int) (location.y - speed * Math.sin((double) direction * Math.PI / 180.0));
			setLocation(middle);
		}
	}

	// 定时器每次刷新式更新机器人位置：走一步
	public void update(HuntingPoints world) {
		moveOneStep(world);
	}

	// 避障算法：遇到障碍物时怎么走(参数为障碍点坐标)
	abstract protected void obstacleAvoiding(int x, int y);

	// 撞墙检测
	private boolean checkForCrahingWall(int x, int y, HuntingPoints world) {
		if (x <= BaseRobot.RADIUS || y <= BaseRobot.RADIUS || x >= world.getWidth() - BaseRobot.RADIUS
				|| y >= world.getHeight() - BaseRobot.RADIUS) {// 点的合法性判断：下一步在当前点内或者超出了面板范围
			collisionWall = true;
			return false;
		}
		return true;
	}

	// 碰撞检查：对下一个点坐标进行检测
	private boolean checkForCollision(int x, int y, HuntingPoints world) {
		for (int i = 0; i < world.getRecs().size(); i++) { // 障碍物碰撞
			int x1 = world.getRecs().get(i).getLocation().x;
			int y1 = world.getRecs().get(i).getLocation().y;
			if (Point.distance(x1, y1, x, y) < Obstacle.RADIUS + BaseRobot.RADIUS) { // 两点距离小于障碍物半径+机器人半径
				this.obstacleAvoiding(x1, y1); // 避障（避开参数坐标点）
				collisionObstacle = true;
				return true;
			}
		}

		// 检测机器人之间的碰撞
		int otherRobotIndex = 0;
		if (this.getColor() == Color.CYAN) { // 检测警察机器人的碰撞
			while (otherRobotIndex < world.getRobots().size()) {
				if (location == world.getRobot(otherRobotIndex).getLocation())
					otherRobotIndex++;
				else {
					if (Point.distanceSq((double) x, (double) y,
							(double) world.getRobot(otherRobotIndex).getLocation().x,
							(double) world.getRobot(otherRobotIndex).getLocation().y) <= 4 * RADIUS * RADIUS) { // 和某个警察碰撞，则避开
						this.obstacleAvoiding(world.getRobot(otherRobotIndex).getLocation().x,
								world.getRobot(otherRobotIndex).getLocation().y);
						collisionRobot = true;
						return true;
					} else
						otherRobotIndex++;
				}
			}
			// 机器人和小偷碰撞：不避开
			if (Point.distanceSq((double) x, (double) y, (double) world.getThief().getLocation().x,
					(double) world.getThief().getLocation().y) <= 4 * RADIUS * RADIUS) {
				collisionObstacle = false;
				return true;
			}
		} else { // 检查小偷机器人与4个警察机器人的碰撞
			while (otherRobotIndex < world.getRobots().size()) {
				if (Point.distanceSq((double) x, (double) y, (double) world.getRobot(otherRobotIndex).getLocation().x,
						(double) world.getRobot(otherRobotIndex).getLocation().y) <= 4 * RADIUS * RADIUS) {
					this.obstacleAvoiding(x, y); // 避开警察（掉头走）
					collisionObstacle = false;
					return true;
				} else
					otherRobotIndex++;
			}

		}
		return false;
	}
}