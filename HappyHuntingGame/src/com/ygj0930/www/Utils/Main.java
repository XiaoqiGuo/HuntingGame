package com.ygj0930.www.Utils;

import javax.swing.JFrame;
import com.ygj0930.www.Bean.HuntingPoints;

public class Main {
	  public static void main(String args[]) { 
	        JFrame frame = new ApplicationFrame(new HuntingPoints()); 
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	        frame.setVisible(true); 
	    } 
}
