package com.ygj0930.www.Bean;

import java.awt.Point;
import java.text.DateFormat;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class HuntingPoints {

	public static final int UPDATE_RATE = 20; // ����ˢ��Ƶ��

	private Random random = new Random();
	private int width = 500, height = 500; // ���Ŀ���
	private Thief thief; // С͵
	private ArrayList<Point> targets = new ArrayList<Point>(); // ��ΧС͵�����������ĸ��㣺����Ҫ�����Χ����
	private ArrayList<BaseRobot> polices; // ���������
	private ArrayList<Obstacle> Obstacles; // �ϰ���

	private Date startTime;
	private Date endTime;
	boolean flag = true;

	public ArrayList<BaseRobot> getPolices() {
		return polices;
	}

	public BaseRobot getPolice(int i) {
		return (BaseRobot) polices.get(i);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ArrayList<Obstacle> getObstacles() {
		return Obstacles;
	}

	public Obstacle getObstacle(int i) {
		return (Obstacle) Obstacles.get(i);
	}

	public void addPolice(Police r) {
		polices.add(r);
	}

	public BaseRobot getThief() {
		return thief;
	}

	public void addObstacle(Obstacle r) {
		Obstacles.add(r);
	}

	public HuntingPoints() {
		startTime = new Date();
		System.out.println(startTime + "��Χ����ʼ��");
		polices = new ArrayList<BaseRobot>();
		Obstacles = new ArrayList<Obstacle>();

		// �����ϰ���
		for (int i = 1; i <= 4; i++) {
			for (int j = 1; j <= 4; j++) {
				addObstacle(new Obstacle(new Point(100 * i, 100 * j)));
			}
		}
		for (int i = 1; i <= 3; i++) {
			addObstacle(new Obstacle(new Point(150, 100 * i + 50)));
			addObstacle(new Obstacle(new Point(250, 100 * i + 50)));
			addObstacle(new Obstacle(new Point(350, 100 * i + 50)));
		}

		// ����������֣��㲻�����ϰ���λ���غ�
		for (int i = 0; i < 4; i++) {
			Point newPoint = createPoint(400, 400);
			while (checkPoint(newPoint)) {
				newPoint = createPoint(400, 400);
			}
			addPolice(new Police(newPoint));
		}
		// С͵������֣��㲻�����ϰ���λ���غ�
		Point newPoint = createPoint(350, 350);
		while (checkPoint(newPoint)) {
			newPoint = createPoint(350, 350);
		}
		thief = new Thief(newPoint);
		// ����С͵λ�ã��ó����������ĸ�Χ����
		targets.add(new Point(thief.location.x, thief.location.y + 2 * BaseRobot.RADIUS));
		targets.add(new Point(thief.location.x, thief.location.y - 2 * BaseRobot.RADIUS));
		targets.add(new Point(thief.location.x - 2 * BaseRobot.RADIUS, thief.location.y));
		targets.add(new Point(thief.location.x + 2 * BaseRobot.RADIUS, thief.location.y));
	}

	// ���������
	private Point createPoint(int x, int y) {
		return new Point(random.nextInt(x) + 25, random.nextInt(y) + 25);
	}

	// �����Ƿ����غ�
	private boolean checkPoint(Point p) {
		return Utils.isCrahingObstacle(p.x, p.y, this);
	}

	// ��������Ԫ�أ��ϰ�����졢С͵
	public void draw(Graphics aPen) {
		for (int i = 0; i < Obstacles.size(); i++) {
			((Obstacle) Obstacles.get(i)).draw(aPen);
		}
		for (int i = 0; i < polices.size(); i++) {
			((BaseRobot) polices.get(i)).draw(aPen);
		}
		((BaseRobot) thief).draw(aPen);
	}

	// ��������Ԫ�أ���һ��
	public void update() {
		if (catchThief(this)) { // ��ֹ��ͳ��ʱ����ӡ
			if (flag) {
				endTime = new Date();
				System.out.println(endTime + "��Χ��������");
				System.out.println("��ʱ��" + (endTime.getTime() - startTime.getTime()) + "ms");
				flag = false;
			}
			// System.exit(0);
			return;
		} else {
			for (int i = 0; i < polices.size(); i++) {
				polices.get(i).update(this); // ������һ��
			}
			thief.update(this); // С͵��һ��
			updateTarget(); // ����Χ����
		}
	}

	// ��1:���±���䷨��С͵λ�ñ仯�󣬾���Ҫ�ﵽ�µ�Χ����
//	private void updateTarget() {
//		targets.clear();
//		targets.add(new Point(thief.location.x, thief.location.y + 2 * BaseRobot.RADIUS));
//		targets.add(new Point(thief.location.x, thief.location.y - 2 * BaseRobot.RADIUS));
//		targets.add(new Point(thief.location.x - 2 * BaseRobot.RADIUS, thief.location.y));
//		targets.add(new Point(thief.location.x + 2 * BaseRobot.RADIUS, thief.location.y));
//	}
	// ��2���������Ϊ���δ�����Χ���㣺����Χ����֮�����λ�ã���ÿ����������ľ������ӳ��
//	private void updateTarget() {
//		targets.clear();
//		targets.add(new Point(thief.location.x, thief.location.y + 2 * BaseRobot.RADIUS));
//		targets.add(new Point(thief.location.x, thief.location.y - 2 * BaseRobot.RADIUS));
//		targets.add(new Point(thief.location.x - 2 * BaseRobot.RADIUS, thief.location.y));
//		targets.add(new Point(thief.location.x + 2 * BaseRobot.RADIUS, thief.location.y));
//		// java�޸�targets����λ��Ԫ��ʱi��size֮�������ȼ���4��Χ����
//		ArrayList<Point> t = (ArrayList<Point>) targets.clone();
//		for (int i = 0; i < polices.size(); i++) {
//			BaseRobot police = polices.get(i);
//			double min = Double.MAX_VALUE; // ��¼�������
//			int index = -1;
//			for (int count = 0; count < t.size(); count++) {
//				Point curr = t.get(count);
//				double currDis = Point.distance(police.getLocation().x, police.getLocation().y, curr.x, curr.y);
//				if (currDis <= min) {
//					min = currDis;
//					index = count;
//				}
//			}
//			targets.set(i, t.get(index));
//			t.remove(index);// ɾ���Ѿ����ɵ�Ŀ���(�ȷ�2����һ��,ʹ�û������±겻ͬ)
//		}
//	}
//	//��3������Э�̷��䣺����Χ����֮�����λ�ã���ÿ������Э�̷���������ľ������ӳ�����
	private void updateTarget() {
		//java�޸�targets����λ��Ԫ��ʱi��size֮�������ȼ���4��Χ����
		targets.clear();
		targets.add(new Point(thief.location.x, thief.location.y + 2 * BaseRobot.RADIUS));
		targets.add(new Point(thief.location.x, thief.location.y - 2 * BaseRobot.RADIUS));
		targets.add(new Point(thief.location.x - 2 * BaseRobot.RADIUS, thief.location.y));
		targets.add(new Point(thief.location.x + 2 * BaseRobot.RADIUS, thief.location.y));


		Map <Point,ArrayList<BaseRobot>> target2police=new HashMap <Point,ArrayList<BaseRobot>>();
		ArrayList<Point> t=(ArrayList<Point>) targets.clone();
		ArrayList<BaseRobot> p=(ArrayList<BaseRobot>) polices.clone();
		while(t.size()>0||p.size()>0)
		{
			double dis[][]=new double [p.size()][t.size()];
			target2police.clear();
			for(int i=0;i<p.size();i++ )
			{
				BaseRobot police=p.get(i);
				double min = Double.MAX_VALUE; // ��¼�������	
				int index=-1;
				for (int j=0; j < t.size();j++) {
					Point curr = t.get(j);
					dis[i][j] = Point.distance(police.getLocation().x, police.getLocation().y, curr.x, curr.y);
					if (dis[i][j]<= min) {
						min = dis[i][j];
						index = j;
					} 					
				}
				Point temp=t.get(index);
				ArrayList<BaseRobot> pList=null;
				if(!target2police.containsKey(temp))
				{
					pList=new ArrayList<BaseRobot>();
					target2police.put(temp,pList);
				}
				pList=target2police.get((Object)temp);
				pList.add(police);
				target2police.put(temp,pList);
				
			}
			Iterator<Map.Entry<Point, ArrayList<BaseRobot>>> it= target2police.entrySet().iterator();
			while (it.hasNext()) 
			{  
				Map.Entry<Point, ArrayList<BaseRobot>> entry=it.next();
				ArrayList<BaseRobot> pList=(ArrayList<BaseRobot>)entry.getValue();
				Point target=(Point)entry.getKey();
				int tindex=t.indexOf(target);
				if(pList.size()==1)
				{
					targets.set(polices.indexOf(pList.get(0)),target);
					t.remove(tindex);
					p.remove(p.indexOf(pList.get(0)));
				}
				else
				{
					double max=0;
					BaseRobot worstp = null;
					for (BaseRobot police:pList)
					{
						int pindex=p.indexOf(police);
						if(dis[pindex][tindex]>max)
						{
							max=dis[pindex][tindex];
							worstp=police;
						}
					}
					targets.set(polices.indexOf(worstp),target);
					t.remove(tindex);
					p.remove(p.indexOf(worstp));
					
				}
			}
		}  
	}
	// ���ػ�����Χ����
	public Point getTarget(BaseRobot police) {
		return targets.get(polices.indexOf(police));
	}

	
	// // Χ���������ԣ�ÿ�����쵽������Լ�����ĵ�
	// public Point getTarget(BaseRobot police) {
	// double min = Double.MAX_VALUE; // ��¼�������
	// int count = 0;
	// int index = -1; // �������Χ������±�
	// while (count < targets.size()) {
	// Point curr = targets.get(count);
	// double currDis = Point.distance(police.getLocation().x,
	// police.getLocation().y, curr.x, curr.y);
	// if (currDis <= min) {
	// min = currDis;
	// index = count;
	// count++;
	// } else {
	// count++;
	// }
	// }
	// return index >= 0 ? targets.get(index) :
	// targets.get(polices.indexOf(police));
	// }

	// Χ���ɹ����жϣ����ݵ�ǰ����λ�����ж�

	// ��һ�����ݾ����Ƿ��ѵִ���Ը����Χ�������ж�
	// private boolean catchThief(HuntingPoints p) {
	// boolean catched = true;
	//
	// int size = polices.size();
	// int j;
	// for (j = 0; j < size; j++) {
	// int x = polices.get(j).findTarget(this).x;
	// int y = polices.get(j).findTarget(this).y;
	//
	// // ����Ŀ���λ�õĺϷ����жϣ���ǽ�غϣ���ת��
	// if (x <= BaseRobot.RADIUS || y <= BaseRobot.RADIUS || x >=
	// this.getWidth() - BaseRobot.RADIUS
	// || y >= this.getHeight() - BaseRobot.RADIUS) {
	// polices.get(j).obstacleAvoiding(x, y, p, true);
	// }
	//
	// // ����Ŀ���λ�õĺϷ����жϣ����ϰ����غϣ���ת��
	// int i;
	// for (i = 0; i < getObstacles().size(); i++) {
	// int x1 = getObstacles().get(i).getLocation().x;
	// int y1 = getObstacles().get(i).getLocation().y;
	// if (Point.distance(x1, y1, x, y) < Obstacle.RADIUS + BaseRobot.RADIUS) {
	// polices.get(j).obstacleAvoiding(x, y, p, true);
	// }
	// }
	// if (i < size)
	// break;
	// }
	//
	// // 4����������Ը����Χ������غ��ж�
	// for (int i = 0; i < polices.size(); i++) {
	// if (i == j)
	// continue;
	// int x1 = polices.get(i).location.x;
	// int y1 = polices.get(i).location.y;
	// if (Point.distance(x1, y1, polices.get(i).findTarget(this).getX(),
	// polices.get(i).findTarget(this).getY()) <= BaseRobot.RADIUS)
	// continue; // ��ǰ���쵽��Ŀ���
	// else {
	// catched = false; // ����һ������δ����Ŀ��㣺��δ����
	// return catched;
	// }
	// }
	// return catched;
	// }

	// ��2����ֹ״̬�жϣ�С͵��4������Χס���߱����������ǽ���߱����������ǽ�ǣ���Χ���ɹ������ϰ��ﲻ�㣩
	private boolean catchThief(HuntingPoints p) {
		boolean catched = true;
		for (int i = 0; i < targets.size(); i++) { // �����ĸ���,ֻҪһ��������״̬�������ϼ�û׽��
			Point currPoint = targets.get(i);
			int x = currPoint.x;
			int y = currPoint.y;
			// �ж�Χ����״̬
			// if (!Utils.isCrahingWall(x, y, p) && !Utils.isCollision(x, y, p)
			// && !Utils.isCrahingObstacle(x, y, p))
			if (!Utils.isCrahingWall(x, y, p) && !Utils.isCollision(x, y, p)) {
				catched = false;
				return catched;
			}
		}
		return catched;
	}

}