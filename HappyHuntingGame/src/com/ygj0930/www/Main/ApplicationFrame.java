package com.ygj0930.www.Main;
import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*;

import com.ygj0930.www.Bean.HuntingPoints; 

public class ApplicationFrame extends JFrame { 
    private HuntingPoints points;   //����ϵ�����Ԫ��
    private Timer timer;  //��ʱ��������Ƶ��ˢ�����
    private ApplicationPanel panel;  //���

    public ApplicationFrame(HuntingPoints p) {
    	super("�������Χ��ʵ�顪��Ҷ���ᡢ����������Сͩ");
        points = p;
        panel = new ApplicationPanel(p); //��Ԫ�ش�����壬���л���  
        getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        getContentPane().add(panel); 
        pack();
        setResizable(false);  //��ֹ���ڵ�����С����Ϊ�ϰ���Ȼ��������봰�ڴ�С�й�
        
        //������ʱ����ˢ�´���
        timer = new Timer(1000/HuntingPoints.UPDATE_RATE, new Ticker());
        timer.start();
    } 

    //�Զ��嶨ʱ��
    private class Ticker implements ActionListener { 
        public void actionPerformed(ActionEvent e) { 
            points.update(); //����С͵��Ŀ��㡢�����˵�λ��
            panel.repaint(); //���»��ƴ���
        } 
    } 
} 