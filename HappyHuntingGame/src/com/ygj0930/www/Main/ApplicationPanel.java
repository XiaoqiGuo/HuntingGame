package com.ygj0930.www.Main;

import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*;
import com.ygj0930.www.Bean.HuntingPoints; 


public class ApplicationPanel extends JPanel{ 
    private HuntingPoints points; 
    
    public ApplicationPanel(HuntingPoints p) { 
        points = p;    
        setPreferredSize(new Dimension(points.getWidth(), points.getHeight()));  
        setBackground(Color.white);
        setBorder(BorderFactory.createLineBorder(Color.BLACK,5)); 
    } 
	
    public void paintComponent(Graphics aPen) {
        super.paintComponent(aPen); 
        points.draw(aPen); //»æÖÆÃæ°åÔªËØ
    }
}

 