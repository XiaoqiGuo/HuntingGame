package com.ygj0930.www.Bean;

import java.awt.Color;
import java.awt.Point;

public class Thief extends BaseRobot {
	public String type = "Thief";

	public Thief(Point loc) {
		super(loc);
	}

	public Color getColor() {
		return Color.yellow; // С͵�ǻ�ɫ
	}

	// С͵���ܲ��ԣ�������ľ���ķ������ܣ����������ת����
	// ʵ����ʾ���������������ķ������ܣ�С͵��·������Բ����ԣ�ȫ���������С͵ǰ����ԭ����ת��λ�Ʋ����ԣ�
	//����ۺϲ������ַ�ʽ��ÿһ����Ҫô�����ܣ�Ҫô���ת��
	@Override
	protected int computeDesiredDirection(Point target) {
		int decision = (int) (Math.random() * 10); // �������һ��ת����񣺷������� or ���ת��
		if (decision >= 0) {
			if (decision == 2 || decision == 6) 
				return antiTurns(target);
			else
				return randomTurns();
		}
		return randomTurns();
	}

	// ���������ķ�������
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

	// ���ת��
	public int randomTurns() {
		int decision = (int) (Math.random() * 10); // �������һ��ת�������תor��ת
		int amount = (int) (Math.random() * 90 / TURN_UNIT); // �������ת���ٸ���λ
		if (decision >= 0) {
			if (decision == 1 || decision == 3 || decision == 5 || decision == 7 || decision == 9) // �����ת�����Ƕȣ�
				direction = direction - TURN_UNIT * amount;
			else
				direction = direction + TURN_UNIT * amount; // �����ת���ӽǶȣ�
		}
		// ��������x��ĽǶȿ�����180���ڣ����ڻ��Ƴ����ߵ�ĩ�˵�ʱȷ��x\y����
		if (direction > 180)
			direction -= 360;
		if (direction <= -180)
			direction += 360;
		return direction; // ���س���
	}

	// ���ϣ�ת��90���ƿ��ϰ���
	protected void obstacleAvoiding(int x, int y,HuntingPoints p,boolean is_obstacle) {
		this.direction += 90;
		if (this.direction > 180)
			this.direction -= 360;
		if (this.direction <= -180)
			this.direction += 360;
	}

	// Ѱ�Ҿ�������ľ���
	@Override
	protected Point findTarget(HuntingPoints p) {
		double min = Double.MAX_VALUE; // ��¼������С͵���������
		int count = 0;
		int index = -1; // ������뾯����±�
		while (count < p.getPolices().size()) {
			Police curr = (Police) p.getPolice(count);
			// �����ĸ����죬�ֱ������С͵�ľ��룬�ҳ������
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
