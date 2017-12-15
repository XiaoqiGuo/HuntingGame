package com.ygj0930.www.Bean;

import java.awt.Point;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class HuntingPoints {

	public static final int UPDATE_RATE = 15; // ����ˢ��Ƶ��

	private ArrayList<Point> targets = new ArrayList<Point>(); // ��ΧС͵�����������ĸ��㣺����Ҫ����ĵ�
	private ArrayList<BaseRobot> polices; // ���������
	private ArrayList<Obstacle> recs; // �ϰ���

	private Random random = new Random();
	private int width, height; // ���Ŀ���
	private BaseRobot thief; // С͵

	public ArrayList<BaseRobot> getRobots() {
		return polices;
	}

	public BaseRobot getRobot(int i) {
		return (BaseRobot) polices.get(i);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ArrayList<Obstacle> getRecs() {
		return recs;
	}

	public Obstacle getRec(int i) {
		return (Obstacle) recs.get(i);
	}

	public void addRobot(Police r) {
		polices.add(r);
	}

	public BaseRobot getThief() {
		return thief;
	}

	public void addRec(Obstacle r) {
		recs.add(r);
	}

	public HuntingPoints() {
		polices = new ArrayList<BaseRobot>();
		recs = new ArrayList<Obstacle>();

		// С͵�������
		thief = new Thief(new Point(random.nextInt(900) + 50, random.nextInt(900) + 50));
		// ����С͵λ�ã��ó����������ĸ�Ŀ���
		targets.add(new Point(thief.location.x, thief.location.y + 2 * BaseRobot.RADIUS));
		targets.add(new Point(thief.location.x, thief.location.y - 2 * BaseRobot.RADIUS));
		targets.add(new Point(thief.location.x - 2 * BaseRobot.RADIUS, thief.location.y));
		targets.add(new Point(thief.location.x + 2 * BaseRobot.RADIUS, thief.location.y));

		// ����������֣�0~1000��
		addRobot(new Police(new Point(random.nextInt(500), random.nextInt(500))));
		addRobot(new Police(new Point(random.nextInt(400) + 500, random.nextInt(500))));
		addRobot(new Police(new Point(random.nextInt(500), random.nextInt(400) + 500)));
		addRobot(new Police(new Point(random.nextInt(400) + 500, random.nextInt(400) + 500)));

		// �����ϰ���
		addRec(new Obstacle(new Point(50, 50)));
		addRec(new Obstacle(new Point(50, 300)));
		addRec(new Obstacle(new Point(50, 600)));
		addRec(new Obstacle(new Point(50, 900)));
		addRec(new Obstacle(new Point(300, 50)));
		addRec(new Obstacle(new Point(300, 300)));
		addRec(new Obstacle(new Point(300, 600)));
		addRec(new Obstacle(new Point(300, 900)));
		addRec(new Obstacle(new Point(600, 50)));
		addRec(new Obstacle(new Point(600, 300)));
		addRec(new Obstacle(new Point(600, 600)));
		addRec(new Obstacle(new Point(600, 900)));
		addRec(new Obstacle(new Point(900, 50)));
		addRec(new Obstacle(new Point(900, 300)));
		addRec(new Obstacle(new Point(900, 600)));
		addRec(new Obstacle(new Point(900, 900)));
		for (int i = 0; i <= 15; i++) {
			addRec(new Obstacle(new Point(random.nextInt(900) + 70, random.nextInt(900) + 70)));
		}

		width = 1000;
		height = 1000;
	}

	// ��ֹ�ж�
	private boolean reachGoal() {
		boolean goal = true;

		int size = polices.size();
		int j;
		for (j = 0; j < size; j++) {
			int x = polices.get(j).findTarget(this).x;
			int y = polices.get(j).findTarget(this).y;
			
			//����λ�õĺϷ����жϣ����Ľ������ܳ�ǽ
			if (x <= BaseRobot.RADIUS || y <= BaseRobot.RADIUS || x >= this.getWidth() - BaseRobot.RADIUS
					|| y >= this.getHeight() - BaseRobot.RADIUS) {
				break;
			}
			//�������ϰ�����ײ
			int i;
			for (i = 0; i < getRecs().size(); i++) {
				int x1 = getRecs().get(i).getLocation().x;
				int y1 = getRecs().get(i).getLocation().y;
				if (Point.distance(x1, y1, x, y) < Obstacle.RADIUS + BaseRobot.RADIUS) {
					break;
				}
			}
			if (i < size)
				break;
		}
		//4�����������Ŀ�����غ��ж�
		for (int i = 0; i < polices.size(); i++) {
			if (i == j)
				continue;
			int x1 = polices.get(i).location.x;
			int y1 = polices.get(i).location.y;
			if (Point.distance(x1, y1, polices.get(i).findTarget(this).getX(),
					polices.get(i).findTarget(this).getY()) <= BaseRobot.RADIUS)
				continue;
			else {
				goal = false;
				return goal;
			}
		}
		//���Ľ�������Χ�µ�ǽ�ǵ�����ж�
		//TODO����������ж�
		
		return goal;
	}

	// ��������Ԫ�أ��ϰ�����졢С͵
	public void drawWith(Graphics aPen) {
		for (int i = 0; i < recs.size(); i++) {
			((Obstacle) recs.get(i)).drawWith(aPen);
		}
		for (int i = 0; i < polices.size(); i++) {
			((BaseRobot) polices.get(i)).drawWith(aPen);
		}
		((BaseRobot) thief).drawWith(aPen);
	}
    
	//��������Ԫ�أ���һ��
	public void update() {
		if (reachGoal()){  //��ֹ�жϣ�ץ������ֹͣ����
			return;
		}
		for (int i = 0; i < polices.size(); i++)
			polices.get(i).update(this); //������һ��
		thief.update(this); //С͵��һ��
		updateTarget(); //����С͵��ߵ�Ŀ���
	}
	
	// ����Ŀ���:С͵λ�ñ仯�󣬾���Ҫ�ﵽ�µ�Ŀ���
		private void updateTarget() {
			targets.clear();
			targets.add(new Point(thief.location.x, thief.location.y + 2 * BaseRobot.RADIUS));
			targets.add(new Point(thief.location.x, thief.location.y - 2 * BaseRobot.RADIUS));
			targets.add(new Point(thief.location.x - 2 * BaseRobot.RADIUS, thief.location.y));
			targets.add(new Point(thief.location.x + 2 * BaseRobot.RADIUS, thief.location.y));
		}

		// 4������ֱ���ִ�һ��Ŀ��㣬�����±����Ŀ������
		public Point getTarget(BaseRobot police) {
			return targets.get(polices.indexOf(police));
		}
}