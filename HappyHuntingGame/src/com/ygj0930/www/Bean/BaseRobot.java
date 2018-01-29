package com.ygj0930.www.Bean;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.lang.Math;

//�����˳�����
public abstract class BaseRobot {

	public String type = "BaseRobot";
	public static final int RADIUS = 10; // �뾶
	public static final int TURN_UNIT = 1; // ��1��Ϊת��λ���Ƕ�ԽС��������Խ���
	protected Point location; // ������λ��
	protected int direction; // ����
	protected int speed; // �ٶ�(����)

	// ���ƻ�����
	public void draw(Graphics aPen) {
		aPen.setColor(this.getColor());// ��������ɫ
		aPen.fillOval(location.x - RADIUS, location.y - RADIUS, 2 * RADIUS, 2 * RADIUS); // ���Բ�Σ�Բ��x��Բ��y������
		aPen.setColor(Color.BLACK); // ������ɫ
		aPen.drawOval(location.x - RADIUS, location.y - RADIUS, 2 * RADIUS, 2 * RADIUS); // ����Բ��

		// ���Ƴ�������
		int endX = location.x + (int) (RADIUS * Math.cos(direction * Math.PI / 180.0));
		int endY = location.y - (int) (RADIUS * Math.sin(direction * Math.PI / 180.0));
		aPen.setColor(Color.BLACK);
		aPen.drawLine(location.x, location.y, endX, endY);

	}

	// ÿ��ˢ��λ�ã����춼Ҫ�ҵ��µ�Ŀ���
	abstract protected Point findTarget(HuntingPoints world);

	// ���������:������Ҫǰ����Ŀ��㣬��Ͼ������ڵ����꣬�������Ҫǰ���ķ��򣨽Ƕȣ���С͵�����ת�򡢾��������Ҫǰ����Ŀ������������㡿
	abstract protected int computeDesiredDirection(Point target);

	// �ƶ���·�߹滮�����
	protected void moveOneStep(HuntingPoints p) {
		Point target = this.findTarget(p); // Ѱ����һ��ǰ����Ŀ���
		direction = this.computeDesiredDirection(target); // ����Ŀ��㣬�������һ���ķ���

		// �ƶ�������
		// ��һ���ߵ��ĵ�
		Point middle = new Point();
		middle.x = (int) (location.x + speed * Math.cos((double) direction * Math.PI / 180.0));
		middle.y = (int) (location.y - speed * Math.sin((double) direction * Math.PI / 180.0));

		// ѭ��������λ���Ƿ����ϰ�/ǽ/���������� by GXQ
		int count = 4; // ת�����
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
			// ����ײ�˻��ϰ���:�ڼ�⺯���м�������Ϸ���
			// �ڱ��Ϸ�������һ��
			middle.x = (int) (location.x + speed * Math.cos((double) direction * Math.PI / 180.0));
			middle.y = (int) (location.y - speed * Math.sin((double) direction * Math.PI / 180.0));
			count--;
		}
		if (count == 0) {
			return;
		} else
			setLocation(middle); // ��һ���ĵ�����ײ������µ����꣺�õ�ǰ���ƶ�����һ���㣬

	}

	// ��ʱ��ÿ��ˢ��ʽ���»�����λ�ã���һ��
	public void update(HuntingPoints world) {
		moveOneStep(world);
	}

	// �����㷨�������ϰ���ʱ��ô��(����Ϊ�ϰ�������)
	abstract protected void obstacleAvoiding(int x, int y, HuntingPoints p, boolean is_obstacle);

	// ײǽ���
	private boolean checkForCrahingWall(int x, int y, HuntingPoints points) {
		if (x <= BaseRobot.RADIUS || y <= BaseRobot.RADIUS || x >= points.getWidth() - BaseRobot.RADIUS
				|| y >= points.getHeight() - BaseRobot.RADIUS) {// ��ĺϷ����жϣ���һ���ڵ�ǰ���ڻ��߳�������巶Χ
			return true;
		}
		return false;
	}

	// ��ײ��飺����һ����������м��
	private boolean checkForCollision(int x, int y, HuntingPoints world) {

		// ========�ϰ�����ײ��������=====
		for (int i = 0; i < world.getObstacles().size(); i++) {
			int x1 = world.getObstacles().get(i).getLocation().x;
			int y1 = world.getObstacles().get(i).getLocation().y;
			if (Point.distance(x1, y1, x, y) < Obstacle.RADIUS + BaseRobot.RADIUS) { // �������С���ϰ���뾶+�����˰뾶
				this.obstacleAvoiding(x1, y1, world, true); //// ������Ϸ���
				return true;
			}
		}

		// ========������֮����ײ��������=====
		int otherRobotIndex = 0;
		if (this.getColor() == Color.CYAN) { // ��⾯������˵���ײ
			while (otherRobotIndex < world.getPolices().size()) {
				if (location == world.getPolice(otherRobotIndex).getLocation())
					otherRobotIndex++;
				else {
					if (Point.distanceSq((double) x, (double) y,
							(double) world.getPolice(otherRobotIndex).getLocation().x,
							(double) world.getPolice(otherRobotIndex).getLocation().y) <= 4 * RADIUS * RADIUS) { // ��ĳ��������ײ����ܿ�
						this.obstacleAvoiding(world.getPolice(otherRobotIndex).getLocation().x,
								world.getPolice(otherRobotIndex).getLocation().y, world, false);
						return true;
					} else
						otherRobotIndex++;
				}
			}
			// �����˺�С͵��ײ�����ܿ�
			if (Point.distanceSq((double) x, (double) y, (double) world.getThief().getLocation().x,
					(double) world.getThief().getLocation().y) <= 4 * RADIUS * RADIUS) {
				return true;
			}
		} else { // ���С͵��������4����������˵���ײ
			while (otherRobotIndex < world.getPolices().size()) {
				if (Point.distanceSq((double) x, (double) y, (double) world.getPolice(otherRobotIndex).getLocation().x,
						(double) world.getPolice(otherRobotIndex).getLocation().y) <= 4 * RADIUS * RADIUS) {
					this.obstacleAvoiding(x, y, world, false); // ������Ϸ���
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
		speed = 15; // �������ڰ뾶�������ʱת����һ�����ɱܿ��ϰ���
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