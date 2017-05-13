package com.sss.tank;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Wall {
	private boolean live = true;

	private int kuan = 20;
	private int chang = 20;
	private int x,y;
	private TankClient tc;
	
	public void setLive(boolean live) {
		this.live = live;
	}
	public boolean isLive(){
		return live;
	}
	
	public Wall(int x, int y, boolean live,TankClient tc) {
		this.x = x;
		this.y = y;
		this.live = live;
		this.tc = tc;
	}
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static Image imgs = tk.getImage(Baozha.class.getClassLoader().getResource("images/commonWall.gif"));
	
	public void draw(Graphics g){
//		g.fillRect(x, y, w, h);
		g.drawImage(imgs, x, y, null);
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,kuan,chang);
	}
}
