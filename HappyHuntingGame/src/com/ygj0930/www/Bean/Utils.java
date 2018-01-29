package com.ygj0930.www.Bean;

import java.awt.Color;
import java.awt.Point;

public class Utils {

	// ײǽ���
	public static boolean isCrahingWall(int x, int y, HuntingPoints points) {
		if (x <= BaseRobot.RADIUS || y <= BaseRobot.RADIUS || x >= points.getWidth() - BaseRobot.RADIUS
				|| y >= points.getHeight() - BaseRobot.RADIUS) {// ��ĺϷ����жϣ���һ���ڵ�ǰ���ڻ��߳�������巶Χ
			return true;
		}
		return false;
	}

	//�ϰ�����ײ���
	public static boolean isCrahingObstacle(int x, int y, HuntingPoints points){
		boolean isCrash = false;
		for (int i = 0; i < points.getObstacles().size(); i++) { // �ϰ�����ײ
			int x1 = points.getObstacles().get(i).getLocation().x;
			int y1 = points.getObstacles().get(i).getLocation().y;
			if (Point.distance(x1, y1, x, y) < Obstacle.RADIUS + BaseRobot.RADIUS) { // �������С���ϰ���뾶+�����˰뾶
				isCrash = true;
				return isCrash;
			}
		}
		return isCrash;
	}
	
	// ���쵽���⣺���쵽����Χ����
	public static boolean isCollision(int x, int y, HuntingPoints points) {
		boolean isCrash = false;
		for (int i = 0; i < points.getPolices().size(); i++) {
			int x1 = points.getPolice(i).getLocation().x;
			int y1 = points.getPolice(i).getLocation().y;
			if (Point.distance(x1, y1, x, y) < 2 * BaseRobot.RADIUS) { // �������С��С͵�������뾯������˰뾶֮��
				isCrash = true;
				return isCrash;
			}
		}
		return isCrash;
	}
}
