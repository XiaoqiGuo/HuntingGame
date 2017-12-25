package com.ygj0930.www.Main;
import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*;

import com.ygj0930.www.Bean.HuntingPoints; 

public class ApplicationFrame extends JFrame { 
    private HuntingPoints points;   //面板上的所有元素
    private Timer timer;  //定时器：按照频率刷新面板
    private ApplicationPanel panel;  //面板

    public ApplicationFrame(HuntingPoints p) {
    	super("多机器人围捕实验——叶国坚、郭晓琦、付小桐");
        points = p;
        panel = new ApplicationPanel(p); //把元素传进面板，进行绘制  
        getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        getContentPane().add(panel); 
        pack();
        setResizable(false);  //禁止窗口调整大小：因为障碍物等绘制坐标与窗口大小有关
        
        //开启定时器，刷新窗口
        timer = new Timer(1000/HuntingPoints.UPDATE_RATE, new Ticker());
        timer.start();
    } 

    //自定义定时器
    private class Ticker implements ActionListener { 
        public void actionPerformed(ActionEvent e) { 
            points.update(); //更新小偷、目标点、机器人的位置
            panel.repaint(); //重新绘制窗口
        } 
    } 
} 