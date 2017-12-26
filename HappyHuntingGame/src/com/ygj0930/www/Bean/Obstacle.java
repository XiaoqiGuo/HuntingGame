package com.ygj0930.www.Bean;
import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
public class Obstacle {
	public static final int	RADIUS = 30;
	private Point 	location;
	
	public Obstacle(Point loc) {
		location = loc;
	}
	
	public Point getLocation() { return location; }

	public void setLocation(Point p) { location = p; }
	
	public void draw(Graphics aPen) {
		aPen.setColor(Color.GRAY);
		aPen.fillOval(location.x - RADIUS,location.y - RADIUS,2 * RADIUS,2 * RADIUS); //ÃÓ≥‰‘≤–Œ
	}		
}