package com.sss.tank;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;


public class Baozha {
	private int x,y;
	private boolean live = true;	//爆炸的生命
	private TankClient tc;
	private boolean baozhaInit = false;		//第一个爆炸因不能正常显示，故添加此变量，先产生一个爆炸，
	
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
		if(!baozhaInit){		//若为false，说明没有产生过爆炸，因此先产生一个，并随后设置为true,
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

		g.drawImage(imgs[step], x, y, null);	//绘制爆炸图案
		step++;
	}
}
