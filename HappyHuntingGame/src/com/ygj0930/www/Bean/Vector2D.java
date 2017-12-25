package com.ygj0930.www.Bean;

public class Vector2D {
	private double x;
	private double y;

	public Vector2D() {
		x = 0;
		y = 0;
	}

	public Vector2D(double _x, double _y) {
		x = _x;
		y = _y;
	}

	// 获取弧度
	public double getRadian() {
		return Math.atan2(y, x);
	}

	// 获取角度
	public double getAngle() {
		return getRadian() / Math.PI * 180;
	}
}
