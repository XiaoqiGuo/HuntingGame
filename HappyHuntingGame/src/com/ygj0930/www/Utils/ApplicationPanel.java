package com.ygj0930.www.Utils;

import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*;
import com.ygj0930.www.Bean.HuntingPoints; 


public class ApplicationPanel extends JPanel{ 
    private HuntingPoints points; 
    
    public ApplicationPanel(HuntingPoints p) { 
        points = p;    
        setPreferredSize(new Dimension(points.getWidth(), points.getHeight()));  //设置面板的最佳大小
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.BLACK,5)); 
    } 
	
    public void paintComponent(Graphics aPen) {
        super.paintComponent(aPen); 
        points.drawWith(aPen); //绘制面板元素
    } 

    private void update() {
    	repaint(); //重新绘制面板
    }
}

 