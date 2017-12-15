package com.ygj0930.www.Bean;

import java.awt.Point;

public class Thief extends BaseRobot {
	public String type = "Thief";

	public Thief(Point loc) {
		super(loc);
	}

	public int computeRandomDirectionChange() {
		int decision = (int) (Math.random() * 10); // �������һ��ת�������תor��ת
		int amount = (int) (Math.random() * 90 / TURN_UNIT); // �������ת���ٸ���λ
		if (decision >= 0) {
			if (decision == 1 || decision == 3 || decision == 5 || decision == 7 || decision == 9) // ������ת�����Ƕȣ�
				direction = direction - TURN_UNIT * amount;
			else
				direction = direction + TURN_UNIT * amount; // ż����ת���ӽǶȣ�
		}
		// ��������x��ĽǶȿ�����180���ڣ����ڻ��Ƴ����ߵ�ĩ�˵�ʱȷ��x\y����
		if (direction > 180)
			direction -= 360;
		if (direction <= -180)
			direction += 360;
		return direction; // ���س���
	}

	// ���ϲ��ԣ����Ľ�
	protected void obstacleAvoiding(int x, int y) {
		this.direction += 90;
		if (this.direction > 180)
			this.direction -= 360;
		if (this.direction <= -180)
			this.direction += 360;
	}

	// Ѱ��Ŀ���:С͵����Ҫʵ�֣��������ҪѰ��Ŀ���
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
