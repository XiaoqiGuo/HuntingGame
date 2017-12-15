package com.ygj0930.www.Bean;

import java.awt.Point;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class HuntingPoints {

	public static final int UPDATE_RATE = 15; // 界面刷新频率

	private ArrayList<Point> targets = new ArrayList<Point>(); // 包围小偷的上下左右四个点：警察要到达的点
	private ArrayList<BaseRobot> polices; // 警察机器人
	private ArrayList<Obstacle> recs; // 障碍物

	private Random random = new Random();
	private int width, height; // 面板的宽、高
	private BaseRobot thief; // 小偷

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

		// 小偷随机出现
		thief = new Thief(new Point(random.nextInt(900) + 50, random.nextInt(900) + 50));
		// 根据小偷位置，得出上下左右四个目标点
		targets.add(new Point(thief.location.x, thief.location.y + 2 * BaseRobot.RADIUS));
		targets.add(new Point(thief.location.x, thief.location.y - 2 * BaseRobot.RADIUS));
		targets.add(new Point(thief.location.x - 2 * BaseRobot.RADIUS, thief.location.y));
		targets.add(new Point(thief.location.x + 2 * BaseRobot.RADIUS, thief.location.y));

		// 警察随机出现（0~1000）
		addRobot(new Police(new Point(random.nextInt(500), random.nextInt(500))));
		addRobot(new Police(new Point(random.nextInt(400) + 500, random.nextInt(500))));
		addRobot(new Police(new Point(random.nextInt(500), random.nextInt(400) + 500)));
		addRobot(new Police(new Point(random.nextInt(400) + 500, random.nextInt(400) + 500)));

		// 设置障碍物
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

	// 终止判断
	private boolean reachGoal() {
		boolean goal = true;

		int size = polices.size();
		int j;
		for (j = 0; j < size; j++) {
			int x = polices.get(j).findTarget(this).x;
			int y = polices.get(j).findTarget(this).y;
			
			//警察位置的合法性判断：待改进，不能出墙
			if (x <= BaseRobot.RADIUS || y <= BaseRobot.RADIUS || x >= this.getWidth() - BaseRobot.RADIUS
					|| y >= this.getHeight() - BaseRobot.RADIUS) {
				break;
			}
			//警察与障碍物碰撞
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
		//4个警察与各自目标点的重合判断
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
		//待改进：增加围堵到墙角的情况判断
		//TODO：壁咚情况判断
		
		return goal;
	}

	// 绘制所有元素：障碍物、警察、小偷
	public void drawWith(Graphics aPen) {
		for (int i = 0; i < recs.size(); i++) {
			((Obstacle) recs.get(i)).drawWith(aPen);
		}
		for (int i = 0; i < polices.size(); i++) {
			((BaseRobot) polices.get(i)).drawWith(aPen);
		}
		((BaseRobot) thief).drawWith(aPen);
	}
    
	//更新所有元素：走一步
	public void update() {
		if (reachGoal()){  //终止判断：抓到了则停止程序
			return;
		}
		for (int i = 0; i < polices.size(); i++)
			polices.get(i).update(this); //警察走一步
		thief.update(this); //小偷走一步
		updateTarget(); //更新小偷身边的目标点
	}
	
	// 更新目标点:小偷位置变化后，警察要达到新的目标点
		private void updateTarget() {
			targets.clear();
			targets.add(new Point(thief.location.x, thief.location.y + 2 * BaseRobot.RADIUS));
			targets.add(new Point(thief.location.x, thief.location.y - 2 * BaseRobot.RADIUS));
			targets.add(new Point(thief.location.x - 2 * BaseRobot.RADIUS, thief.location.y));
			targets.add(new Point(thief.location.x + 2 * BaseRobot.RADIUS, thief.location.y));
		}

		// 4个警察分别负责抵达一个目标点，根据下标进行目标点分配
		public Point getTarget(BaseRobot police) {
			return targets.get(polices.indexOf(police));
		}
}