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

	public static final int UPDATE_RATE = 20; // 界面刷新频率

	private Random random = new Random();
	private int width = 500, height = 500; // 面板的宽、高
	private Thief thief; // 小偷
	private ArrayList<Point> targets = new ArrayList<Point>(); // 包围小偷的上下左右四个点：警察要到达的围捕点
	private ArrayList<BaseRobot> polices; // 警察机器人
	private ArrayList<Obstacle> Obstacles; // 障碍物

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
		System.out.println(startTime + "：围捕开始！");
		polices = new ArrayList<BaseRobot>();
		Obstacles = new ArrayList<Obstacle>();

		// 设置障碍物
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

		// 警察随机出现：点不能与障碍物位置重合
		for (int i = 0; i < 4; i++) {
			Point newPoint = createPoint(400, 400);
			while (checkPoint(newPoint)) {
				newPoint = createPoint(400, 400);
			}
			addPolice(new Police(newPoint));
		}
		// 小偷随机出现：点不能与障碍物位置重合
		Point newPoint = createPoint(350, 350);
		while (checkPoint(newPoint)) {
			newPoint = createPoint(350, 350);
		}
		thief = new Thief(newPoint);
		// 根据小偷位置，得出上下左右四个围捕点
		targets.add(new Point(thief.location.x, thief.location.y + 2 * BaseRobot.RADIUS));
		targets.add(new Point(thief.location.x, thief.location.y - 2 * BaseRobot.RADIUS));
		targets.add(new Point(thief.location.x - 2 * BaseRobot.RADIUS, thief.location.y));
		targets.add(new Point(thief.location.x + 2 * BaseRobot.RADIUS, thief.location.y));
	}

	// 生成随机点
	private Point createPoint(int x, int y) {
		return new Point(random.nextInt(x) + 25, random.nextInt(y) + 25);
	}

	// 检查点是否有重合
	private boolean checkPoint(Point p) {
		return Utils.isCrahingObstacle(p.x, p.y, this);
	}

	// 绘制所有元素：障碍物、警察、小偷
	public void draw(Graphics aPen) {
		for (int i = 0; i < Obstacles.size(); i++) {
			((Obstacle) Obstacles.get(i)).draw(aPen);
		}
		for (int i = 0; i < polices.size(); i++) {
			((BaseRobot) polices.get(i)).draw(aPen);
		}
		((BaseRobot) thief).draw(aPen);
	}

	// 更新所有元素：走一步
	public void update() {
		if (catchThief(this)) { // 终止，统计时间差并打印
			if (flag) {
				endTime = new Date();
				System.out.println(endTime + "：围捕结束！");
				System.out.println("耗时：" + (endTime.getTime() - startTime.getTime()) + "ms");
				flag = false;
			}
			// System.exit(0);
			return;
		} else {
			for (int i = 0; i < polices.size(); i++) {
				polices.get(i).update(this); // 警察走一步
			}
			thief.update(this); // 小偷走一步
			updateTarget(); // 更新围捕点
		}
	}

	// 法1:简单下标分配法，小偷位置变化后，警察要达到新的围捕点
//	private void updateTarget() {
//		targets.clear();
//		targets.add(new Point(thief.location.x, thief.location.y + 2 * BaseRobot.RADIUS));
//		targets.add(new Point(thief.location.x, thief.location.y - 2 * BaseRobot.RADIUS));
//		targets.add(new Point(thief.location.x - 2 * BaseRobot.RADIUS, thief.location.y));
//		targets.add(new Point(thief.location.x + 2 * BaseRobot.RADIUS, thief.location.y));
//	}
	// 法2：警察分配为最近未分配的围捕点：更新围捕点之后调整位置，将每个点与最近的警察进行映射
//	private void updateTarget() {
//		targets.clear();
//		targets.add(new Point(thief.location.x, thief.location.y + 2 * BaseRobot.RADIUS));
//		targets.add(new Point(thief.location.x, thief.location.y - 2 * BaseRobot.RADIUS));
//		targets.add(new Point(thief.location.x - 2 * BaseRobot.RADIUS, thief.location.y));
//		targets.add(new Point(thief.location.x + 2 * BaseRobot.RADIUS, thief.location.y));
//		// java修改targets具体位置元素时i在size之间所以先加入4个围捕点
//		ArrayList<Point> t = (ArrayList<Point>) targets.clone();
//		for (int i = 0; i < polices.size(); i++) {
//			BaseRobot police = polices.get(i);
//			double min = Double.MAX_VALUE; // 记录最近距离
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
//			t.remove(index);// 删除已经分派的目标点(比法2多这一行,使得机器人下标不同)
//		}
//	}
//	//法3：警察协商分配：更新围捕点之后调整位置，将每个点与协商法计算出来的警察进行映射分配
	private void updateTarget() {
		//java修改targets具体位置元素时i在size之间所以先加入4个围捕点
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
				double min = Double.MAX_VALUE; // 记录最近距离	
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
	// 返回机器人围捕点
	public Point getTarget(BaseRobot police) {
		return targets.get(polices.indexOf(police));
	}

	
	// // 围捕点分配策略：每个警察到达距离自己最近的点
	// public Point getTarget(BaseRobot police) {
	// double min = Double.MAX_VALUE; // 记录最近距离
	// int count = 0;
	// int index = -1; // 最近距离围捕点的下标
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

	// 围捕成功的判断：根据当前各点位置来判断

	// 法一：根据警察是否已抵达各自负责的围捕点来判断
	// private boolean catchThief(HuntingPoints p) {
	// boolean catched = true;
	//
	// int size = polices.size();
	// int j;
	// for (j = 0; j < size; j++) {
	// int x = polices.get(j).findTarget(this).x;
	// int y = polices.get(j).findTarget(this).y;
	//
	// // 警察目标点位置的合法性判断：与墙重合，则转弯
	// if (x <= BaseRobot.RADIUS || y <= BaseRobot.RADIUS || x >=
	// this.getWidth() - BaseRobot.RADIUS
	// || y >= this.getHeight() - BaseRobot.RADIUS) {
	// polices.get(j).obstacleAvoiding(x, y, p, true);
	// }
	//
	// // 警察目标点位置的合法性判断：与障碍物重合，则转弯
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
	// // 4个警察与各自负责的围捕点的重合判断
	// for (int i = 0; i < polices.size(); i++) {
	// if (i == j)
	// continue;
	// int x1 = polices.get(i).location.x;
	// int y1 = polices.get(i).location.y;
	// if (Point.distance(x1, y1, polices.get(i).findTarget(this).getX(),
	// polices.get(i).findTarget(this).getY()) <= BaseRobot.RADIUS)
	// continue; // 当前警察到达目标点
	// else {
	// catched = false; // 当有一个警察未到达目标点：则未捕获
	// return catched;
	// }
	// }
	// return catched;
	// }

	// 法2：终止状态判断：小偷被4个警察围住或者被三个警察堵墙或者被两个警察堵墙角，则围捕成功。（障碍物不算）
	private boolean catchThief(HuntingPoints p) {
		boolean catched = true;
		for (int i = 0; i < targets.size(); i++) { // 遍历四个点,只要一个点三种状态都不符合即没捉到
			Point currPoint = targets.get(i);
			int x = currPoint.x;
			int y = currPoint.y;
			// 判断围捕点状态
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