package com.ygj0930.www.Bean;
import java.awt.Color;
import java.awt.Point;

public class Police extends BaseRobot {
	public String type = "Police";
	//根据点坐标创建机器人
	public Police(Point loc) {
		super(loc);
	}
	
	//警察是青色
	public Color getColor() {
		return Color.CYAN;
	}
	
	//找出该名警察需要前往的目标点（小偷上下左右的某个方向）【根据警察的下标来分配目标点】
	protected Point findTarget(HuntingPoints p) {
		return  p.getTarget(this);
	}
	
	//===以下两个函数合成Bug2算法======
	//根据目标点，计算出该名警察要去的方向（角度）
	protected int computeDesiredDirection(Point target){
    	int xdiff,ydiff,d;
		double hdiff;
		xdiff = target.x - location.x;
		ydiff = target.y - location.y;
		hdiff = Math.sqrt((double )(xdiff * xdiff + ydiff * ydiff)); //两点间距离
		d = (int )(Math.acos(xdiff / hdiff)  * 180 / Math.PI); //警察到目标的直线与x轴夹角
        //校正角度
		if( ydiff > 0)
			d = -d;
		if (d > 180)
			d -= 360;
		if (d <= -180)
			d += 360;
		return d;			
    }
	
	//绕行障碍物：直接左转90,走一步，避开障碍
	@Override
	protected void obstacleAvoiding(int x, int y) {
		int xdiff,ydiff,d;
	    double hdiff;
	    xdiff = x - this.location.x;
	    ydiff = y - this.location.y;
	    hdiff = Math.sqrt((double )(xdiff * xdiff + ydiff * ydiff));
	    d = (int )(Math.acos(xdiff / hdiff)  * 180 / Math.PI);
	    if( ydiff > 0)
			d = -d;
	    d+=90;
	    if (d > 180)
			d -= 360;
		if (d <= -180)
			d += 360;
		this.direction =  d;
	}
}