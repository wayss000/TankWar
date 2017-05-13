package com.sss.tank;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;


public class Baozha {
	private int x,y;
	private boolean live = true;	//��ը������
	private TankClient tc;
	private boolean baozhaInit = false;		//��һ����ը����������ʾ������Ӵ˱������Ȳ���һ����ը��
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static Image[] imgs = {
		tk.getImage(Baozha.class.getClassLoader().getResource("images/0.gif")),
		tk.getImage(Baozha.class.getClassLoader().getResource("images/1.gif")),
		tk.getImage(Baozha.class.getClassLoader().getResource("images/2.gif")),
		tk.getImage(Baozha.class.getClassLoader().getResource("images/3.gif")),
		tk.getImage(Baozha.class.getClassLoader().getResource("images/4.gif")),
		tk.getImage(Baozha.class.getClassLoader().getResource("images/5.gif")),
		tk.getImage(Baozha.class.getClassLoader().getResource("images/6.gif")),
		tk.getImage(Baozha.class.getClassLoader().getResource("images/7.gif")),
		tk.getImage(Baozha.class.getClassLoader().getResource("images/8.gif")),
		tk.getImage(Baozha.class.getClassLoader().getResource("images/9.gif"))
	};
	
	int step = 0;
	
	public Baozha(int x, int y, TankClient tc){
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if(!baozhaInit){		//��Ϊfalse��˵��û�в�������ը������Ȳ���һ�������������Ϊtrue,
			for(int i = 0; i < imgs.length; i++)
				g.drawImage(imgs[i],-100 , -100, null);
			baozhaInit = true;
		}
		
		
		if(!live){
			tc.Baozhas.remove(this);
			return ;
		}
		
		if(step == imgs.length){
			live = false;
			step = 0;
			return;
		}

		g.drawImage(imgs[step], x, y, null);	//���Ʊ�ըͼ��
		step++;
	}
}
