package com.ygj0930.www.Bean;

import java.awt.Color;
import java.awt.Point;

public class Utils {

	// 撞墙检测
	public static boolean isCrahingWall(int x, int y, HuntingPoints points) {
		if (x <= BaseRobot.RADIUS || y <= BaseRobot.RADIUS || x >= points.getWidth() - BaseRobot.RADIUS
				|| y >= points.getHeight() - BaseRobot.RADIUS) {// 点的合法性判断：下一步在当前点内或者超出了面板范围
			return true;
		}
		return false;
	}

	//障碍物碰撞检测
	public static boolean isCrahingObstacle(int x, int y, HuntingPoints points){
		boolean isCrash = false;
		for (int i = 0; i < points.getObstacles().size(); i++) { // 障碍物碰撞
			int x1 = points.getObstacles().get(i).getLocation().x;
			int y1 = points.getObstacles().get(i).getLocation().y;
			if (Point.distance(x1, y1, x, y) < Obstacle.RADIUS + BaseRobot.RADIUS) { // 两点距离小于障碍物半径+机器人半径
				isCrash = true;
				return isCrash;
			}
		}
		return isCrash;
	}
	
	// 警察到达检测：警察到达了围捕点
	public static boolean isCollision(int x, int y, HuntingPoints points) {
		boolean isCrash = false;
		for (int i = 0; i < points.getPolices().size(); i++) {
			int x1 = points.getPolice(i).getLocation().x;
			int y1 = points.getPolice(i).getLocation().y;
			if (Point.distance(x1, y1, x, y) < 2 * BaseRobot.RADIUS) { // 两点距离小于小偷机器人与警察机器人半径之和
				isCrash = true;
				return isCrash;
			}
		}
		return isCrash;
	}
}
