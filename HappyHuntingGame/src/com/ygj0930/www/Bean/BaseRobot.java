package com.ygj0930.www.Bean;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.lang.Math;

//�����˳�����
public abstract class BaseRobot {
	public String type = "BaseRobot";
	public static final int RADIUS = 15; // �뾶
	public static final int SQUARE_OF_RADIUS = RADIUS * RADIUS; // �뾶��ƽ��
	public static final int TURN_UNIT = 1; // ��1��Ϊת��λ���Ƕ�ԽС��������Խ���

	protected Point location; // ������λ��
	protected int direction; // ����
	protected int speed; // �ٶ�

	public boolean collisionObstacle; // �Ƿ�ײ���ϰ���
	public boolean collisionRobot; // �Ƿ�ײ��������
	public boolean collisionWall; // �Ƿ�ײ��ǽ

	public BaseRobot(Point loc) {
		location = loc;
		direction = 0;
		speed = RADIUS; // �������ڰ뾶
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

	// ���ƻ�����
	public void drawWith(Graphics aPen) {
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
	protected void moveOneStep(HuntingPoints world) {
		// ת��
		Point target = this.findTarget(world);
		direction = this.computeDesiredDirection(target); // �������һ���ķ���

		// �ƶ�������

		// ��һ���ߵ��ĵ�
		Point middle = new Point();
		middle.x = (int) (location.x + speed * Math.cos((double) direction * Math.PI / 180.0));
		middle.y = (int) (location.y - speed * Math.sin((double) direction * Math.PI / 180.0));

		// ײǽ��飺����һ��С͵ײǽ�����ͷ���������������Ҳ��ͷ������ֳ�ȥ�ľ�����Ҳ�ز���...��
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

		// ��ײ��⣺��һ�����Ƿ����ײ
		if (checkForCollision(middle.x, middle.y, world) == false) {
			setLocation(middle); // ��һ���ĵ�����ײ������µ����꣺�õ�ǰ���ƶ�����һ����
		} else {
			// ����ײ������ǰһ��
			middle.x = (int) (location.x + speed * Math.cos((double) direction * Math.PI / 180.0));
			middle.y = (int) (location.y - speed * Math.sin((double) direction * Math.PI / 180.0));
			setLocation(middle);
		}
	}

	// ��ʱ��ÿ��ˢ��ʽ���»�����λ�ã���һ��
	public void update(HuntingPoints world) {
		moveOneStep(world);
	}

	// �����㷨�������ϰ���ʱ��ô��(����Ϊ�ϰ�������)
	abstract protected void obstacleAvoiding(int x, int y);

	// ײǽ���
	private boolean checkForCrahingWall(int x, int y, HuntingPoints world) {
		if (x <= BaseRobot.RADIUS || y <= BaseRobot.RADIUS || x >= world.getWidth() - BaseRobot.RADIUS
				|| y >= world.getHeight() - BaseRobot.RADIUS) {// ��ĺϷ����жϣ���һ���ڵ�ǰ���ڻ��߳�������巶Χ
			collisionWall = true;
			return false;
		}
		return true;
	}

	// ��ײ��飺����һ����������м��
	private boolean checkForCollision(int x, int y, HuntingPoints world) {
		for (int i = 0; i < world.getRecs().size(); i++) { // �ϰ�����ײ
			int x1 = world.getRecs().get(i).getLocation().x;
			int y1 = world.getRecs().get(i).getLocation().y;
			if (Point.distance(x1, y1, x, y) < Obstacle.RADIUS + BaseRobot.RADIUS) { // �������С���ϰ���뾶+�����˰뾶
				this.obstacleAvoiding(x1, y1); // ���ϣ��ܿ���������㣩
				collisionObstacle = true;
				return true;
			}
		}

		// ��������֮�����ײ
		int otherRobotIndex = 0;
		if (this.getColor() == Color.CYAN) { // ��⾯������˵���ײ
			while (otherRobotIndex < world.getRobots().size()) {
				if (location == world.getRobot(otherRobotIndex).getLocation())
					otherRobotIndex++;
				else {
					if (Point.distanceSq((double) x, (double) y,
							(double) world.getRobot(otherRobotIndex).getLocation().x,
							(double) world.getRobot(otherRobotIndex).getLocation().y) <= 4 * RADIUS * RADIUS) { // ��ĳ��������ײ����ܿ�
						this.obstacleAvoiding(world.getRobot(otherRobotIndex).getLocation().x,
								world.getRobot(otherRobotIndex).getLocation().y);
						collisionRobot = true;
						return true;
					} else
						otherRobotIndex++;
				}
			}
			// �����˺�С͵��ײ�����ܿ�
			if (Point.distanceSq((double) x, (double) y, (double) world.getThief().getLocation().x,
					(double) world.getThief().getLocation().y) <= 4 * RADIUS * RADIUS) {
				collisionObstacle = false;
				return true;
			}
		} else { // ���С͵��������4����������˵���ײ
			while (otherRobotIndex < world.getRobots().size()) {
				if (Point.distanceSq((double) x, (double) y, (double) world.getRobot(otherRobotIndex).getLocation().x,
						(double) world.getRobot(otherRobotIndex).getLocation().y) <= 4 * RADIUS * RADIUS) {
					this.obstacleAvoiding(x, y); // �ܿ����죨��ͷ�ߣ�
					collisionObstacle = false;
					return true;
				} else
					otherRobotIndex++;
			}

		}
		return false;
	}
}