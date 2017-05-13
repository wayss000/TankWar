package com.sss.tank;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Zidan {
	int x,y;
	Direction dir;
	
	boolean good;
	private boolean live = true;
	private TankClient tc;
	int tankId;
	
	private static final int XSPEED = 10;
	private static final int YSPEED = 10;

	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static Image[] zidanImages = null;
	private static Map<String, Image> imgsZidan = new HashMap<String , Image>();
	
	static {
		zidanImages = new Image[] {
			tk.getImage(Baozha.class.getClassLoader().getResource("images/missileL.gif")),
			tk.getImage(Baozha.class.getClassLoader().getResource("images/missileLU.gif")),
			tk.getImage(Baozha.class.getClassLoader().getResource("images/missileU.gif")),
			tk.getImage(Baozha.class.getClassLoader().getResource("images/missileRU.gif")),
			tk.getImage(Baozha.class.getClassLoader().getResource("images/missileR.gif")),
			tk.getImage(Baozha.class.getClassLoader().getResource("images/missileRD.gif")),
			tk.getImage(Baozha.class.getClassLoader().getResource("images/missileD.gif")),
			tk.getImage(Baozha.class.getClassLoader().getResource("images/missileLD.gif"))
		};
		imgsZidan.put("L", zidanImages[0]);
		imgsZidan.put("LU", zidanImages[1]);
		imgsZidan.put("U", zidanImages[2]);
		imgsZidan.put("RU", zidanImages[3]);
		imgsZidan.put("R", zidanImages[4]);
		imgsZidan.put("RD", zidanImages[5]);
		imgsZidan.put("D", zidanImages[6]);
		imgsZidan.put("LD", zidanImages[7]);
	}
	
	public Zidan(int tankId,int x, int y, Direction dir){
		this.tankId = tankId;
		this.x = x;
		this.y = y;
		this.dir = dir;
		
	}
	
	public Zidan(int tankId,int x, int y, Direction dir, boolean good, TankClient tc){
		this(tankId,x, y, dir);
		this.good = good;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if(!live){
			tc.zi_s.remove(this);
			return;
		}
					
		switch(dir){			//绘制出炮筒
		case L:
			g.drawImage(imgsZidan.get("L"), x, y, null);
			break;
		case LU:
			g.drawImage(imgsZidan.get("LU"), x, y, null);
			break;
		case U:
			g.drawImage(imgsZidan.get("U"), x, y, null);
			break;
		case RU:
			g.drawImage(imgsZidan.get("RU"), x, y, null);
			break;
		case R:
			g.drawImage(imgsZidan.get("R"), x, y, null);
			break;
		case RD:
			g.drawImage(imgsZidan.get("RD"), x, y, null);
			break;
		case D:
			g.drawImage(imgsZidan.get("D"), x, y, null);
			break;
		case LD:
			g.drawImage(imgsZidan.get("LD"), x, y, null);
			break;
		}
		
		move();
	}
	
	public void move(){
		switch(dir){
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		}
		if(x < 0 || y < 0 || x > TankClient.Game_width || y > TankClient.Game_high){//子弹出边界就死了
			live = false;
		}
	}
	
	public boolean isLive(){
		return live;
	}
	
	public Rectangle getRect(){
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean hitTank(Tank t){//子弹攻击坦克
		if(this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()){//两个矩形相交，并且坦克或者，并且“好坦克不打好坦克，坏不打坏”
			/*if(t.isGood()){			//若是true坦克，则判断生命值是否为0，若是false坦克，直接死
				t.setLife();		//我方坦克坦克生命-1
				if(t.getLife() < 0) t.setLive(false);
			}
			else	t.setLive(false);*/		//敌方直接坦克死
			t.setLive(false);	//去掉上面这段，所有坦克直接死亡
			
			this.live = false;		//子弹死

			Baozha b = new Baozha(x, y, tc);
			tc.Baozhas.add(b);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks){
		for(int i = 0; i< tanks.size(); i++){
			if(hitTank(tanks.get(i)))
				return true;
		}
		return false;
	}
	
	public boolean hitWall(Wall w){
		if(this.live && this.getRect().intersects(w.getRect()) && w.isLive()){
			live = false;
			w.setLive(false);
			return true;
		}
		return false;
	}
}
