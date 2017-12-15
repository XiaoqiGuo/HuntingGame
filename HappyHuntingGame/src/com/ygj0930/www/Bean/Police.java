package com.ygj0930.www.Bean;
import java.awt.Color;
import java.awt.Point;
public class Police extends BaseRobot {
	private Point target;  //Ŀ���
	boolean reach;  //�Ƿ�ץס
	public String type = "Police";
	//���ݵ����괴��������
	public Police(Point loc) {
		super(loc);
	}
	
	public Color getColor() {
		return Color.CYAN;
	}
	
	//�ҳ�����������Ҫǰ����Ŀ��㣨С͵�������ҵ�ĳ�����򣩡����ݾ�����±�������Ŀ��㡿
	protected Point findTarget(HuntingPoints p) {
		target = p.getTarget(this);
		return target;
	}
	
	//����Ŀ��㣬�������������Ҫȥ�ķ��򣨽Ƕȣ�
	protected int computeDesiredDirection(Point target){
    	int xdiff,ydiff,d;
		double hdiff;
		xdiff = target.x - location.x;
		ydiff = target.y - location.y;
		hdiff = Math.sqrt((double )(xdiff * xdiff + ydiff * ydiff)); //��������
		d = (int )(Math.acos(xdiff / hdiff)  * 180 / Math.PI); //���쵽Ŀ���ֱ����x��н�
        //У���Ƕ�
		if( ydiff > 0)
			d = -d;
		if (d > 180)
			d -= 360;
		if (d <= -180)
			d += 360;
		return d;			
    }
	
	//���ϲ��ԣ�ת��90��
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