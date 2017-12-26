package com.ygj0930.www.Bean;

import java.awt.Color;
import java.awt.Point;

public class Police extends BaseRobot {
	public String type = "Police";

	// ���ݵ����괴��������
	public Police(Point loc) {
		super(loc);
	}

	// ��������ɫ
	public Color getColor() {
		return Color.CYAN;
	}

	// �ҳ�����������Ҫǰ����Ŀ��㣨С͵�������ҵ�ĳ�����򣩡����ݾ�����±�������Ŀ��㡿
	protected Point findTarget(HuntingPoints p) {
		return p.getTarget(this);
	}

	// ·�߹滮�����������������һ��Ҫȥ�ķ��򣨽Ƕȣ�
	protected int computeDesiredDirection(Point target) {
		return Bug2Direction(target);
	}

	// Bug2�㷨���Ծ��쵱ǰλ���������������Χ���㣬���������
	private int Bug2Direction(Point target) {
		int xdiff, ydiff, d;
		double hdiff;
		xdiff = target.x - location.x;
		ydiff = target.y - location.y;
		hdiff = Math.sqrt((double) (xdiff * xdiff + ydiff * ydiff)); // ��������
		d = (int) (Math.acos(xdiff / hdiff) * 180 / Math.PI); // ���쵽Ŀ���ֱ����x��н�
		// У���Ƕ�
		if (ydiff > 0)
			d = -d;
		if (d > 180)
			d -= 360;
		if (d <= -180)
			d += 360;
		return d;
	}

	// ����:�����ϰ���ʱ��ô��
	@Override
	protected void obstacleAvoiding(int x, int y, HuntingPoints p,boolean is_obstacle) {
		if(is_obstacle){
			//vectorDirection(x, y, p); //�ϰ�����ϣ��˹��Ƴ���
			turnLeftDirection(x, y); //�ϰ�����ϣ����з��������ϰ����Ե��
		}else{
			turnLeftDirection(x, y); //������֮����ϣ�ת�����м���
		}
	}

	// �����㷨1��ֱ����ת90,�����ϰ���
	private void turnLeftDirection(int x, int y) {
		int xdiff, ydiff, d;
		double hdiff;
		xdiff = x - this.location.x;
		ydiff = y - this.location.y;
		hdiff = Math.sqrt((double) (xdiff * xdiff + ydiff * ydiff));
		d = (int) (Math.acos(xdiff / hdiff) * 180 / Math.PI);
		if (ydiff > 0)
			d = -d;
		d += 90;
		if (d > 180)
			d -= 360;
		if (d <= -180)
			d += 360;
		this.direction = d;
	}

	// �����㷨2���˹��Ƴ���
	private void vectorDirection(int x, int y, HuntingPoints p) {
		Point targetPoint = p.getTarget(this);

		//====����������ƽ�ƣ��������ϰ���Ϊ����ԭ�㣬������ϰ���ָ������ˡ�Ŀ������������ֵ
		// �ϰ���ָ������˵�����
		double x1 = this.location.x - x;
		double y1 = this.location.y - y;
		// �ϰ���ָ��Ŀ��������
		double x2 = targetPoint.x - x;
		double y2 = targetPoint.y - y;
		// ���������
		double vecX, vecY;
		vecX = 10 * x1 + x2 + x;
		vecY = 10 * y1 + y2 + y;
        Vector2D resultVector2d = new Vector2D(vecX,vecY);
        int d = (int) resultVector2d.getAngle();
		this.direction = d;
	}
}