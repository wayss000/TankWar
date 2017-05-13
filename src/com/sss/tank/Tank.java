package com.sss.tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * ̹����
 * @author sss
 *
 */
public class Tank {
	int id;		//��id��Ϊ���������ӵ�ID��
	
	//Ϊ�˷��������࣬�˴���һЩ������privateȥ����
	int x,y;
	private int oldX,oldY;	//��¼̹�˵���һ��λ�ã��Ա���ײ��ʱ�򷵻ص���һ��λ��
	
	private boolean live = true;
//	private int life = 5;		//ȥ��life���û�̹�˶���һǹ
	private BloodLine bl = new BloodLine();

	boolean good;
	
	TankClient tc;
	
//	private static Random r = new Random();
//	private int step = r.nextInt(12) + 3;
	
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	
	public static final int WIDTH = 50;
	public static final int HEIGHT = 50;
	
	public boolean zuo = false;
	public boolean shang = false;
	public boolean you = false;
	public boolean xia = false;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static Image[] p1tankImages = null;
	private static Image[] p2tankImages = null;
	
	private static Map<String, Image> p1imgs = new HashMap<String , Image>();
	private static Map<String, Image> p2imgs = new HashMap<String , Image>();
	
	static {
		p1tankImages = new Image[] {
			tk.getImage(Baozha.class.getClassLoader().getResource("images/p1tankL.gif")),
			tk.getImage(Baozha.class.getClassLoader().getResource("images/p1tankLU.gif")),
			tk.getImage(Baozha.class.getClassLoader().getResource("images/p1tankU.gif")),
			tk.getImage(Baozha.class.getClassLoader().getResource("images/p1tankRU.gif")),
			tk.getImage(Baozha.class.getClassLoader().getResource("images/p1tankR.gif")),
			tk.getImage(Baozha.class.getClassLoader().getResource("images/p1tankRD.gif")),
			tk.getImage(Baozha.class.getClassLoader().getResource("images/p1tankD.gif")),
			tk.getImage(Baozha.class.getClassLoader().getResource("images/p1tankLD.gif"))
		};
		
		p1imgs.put("L", p1tankImages[0]);
		p1imgs.put("LU", p1tankImages[1]);
		p1imgs.put("U", p1tankImages[2]);
		p1imgs.put("RU", p1tankImages[3]);
		p1imgs.put("R", p1tankImages[4]);
		p1imgs.put("RD", p1tankImages[5]);
		p1imgs.put("D", p1tankImages[6]);
		p1imgs.put("LD", p1tankImages[7]);
		
		p2tankImages = new Image[] {
				tk.getImage(Baozha.class.getClassLoader().getResource("images/p2tankL.gif")),
				tk.getImage(Baozha.class.getClassLoader().getResource("images/p2tankLU.gif")),
				tk.getImage(Baozha.class.getClassLoader().getResource("images/p2tankU.gif")),
				tk.getImage(Baozha.class.getClassLoader().getResource("images/p2tankRU.gif")),
				tk.getImage(Baozha.class.getClassLoader().getResource("images/p2tankR.gif")),
				tk.getImage(Baozha.class.getClassLoader().getResource("images/p2tankRD.gif")),
				tk.getImage(Baozha.class.getClassLoader().getResource("images/p2tankD.gif")),
				tk.getImage(Baozha.class.getClassLoader().getResource("images/p2tankLD.gif"))
			};
			
			p2imgs.put("L", p2tankImages[0]);
			p2imgs.put("LU", p2tankImages[1]);
			p2imgs.put("U", p2tankImages[2]);
			p2imgs.put("RU", p2tankImages[3]);
			p2imgs.put("R", p2tankImages[4]);
			p2imgs.put("RD", p2tankImages[5]);
			p2imgs.put("D", p2tankImages[6]);
			p2imgs.put("LD", p2tankImages[7]);
	}
	
	
	Direction dir = Direction.STOP;
	Direction ptdir = Direction.L;//��Ͳ�ķ���
	
	public boolean isLive(){
		return live;
	}
	
	public void setLive(boolean live) {
		this.live = live;
	}

	/*public int getLife() {
		return life;
	}*/

	/*public void setLife() {
		this.life -= 1;
	}*/
	
	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good = good;
		
	}
	
	public Tank(int x, int y, boolean good, TankClient tc){
		this(x,y,good);
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if(!live)	{
			/*if(!good){			//ȥ��if����˼Ϊ�����Ƿ�û�̹�ˣ��������Ƴ�
				tc.tanks.remove(this);		//̹������ʱ�����������Ƴ�
			}*/
			tc.tanks.remove(this);
			return;		//̹������ʱ��ֱ�ӷ��أ������ڻ�ͼ
		}
		
//		if(good)	bl.draw(g);
		bl.draw(g);		//Ѫ��ͳһ������
		g.drawString("ID:"+id, x, y-10);
		
		switch(ptdir){			//������Ͳ����ѡ��ͼƬ
		case L:
			if(good)
				g.drawImage(p1imgs.get("L"), x, y, null);
			else
				g.drawImage(p2imgs.get("L"), x, y, null);
			break;
		case LU:
			if(good)
				g.drawImage(p1imgs.get("LU"), x, y, null);
			else
				g.drawImage(p2imgs.get("LU"), x, y, null);
			break;
		case U:
			if(good)
				g.drawImage(p1imgs.get("U"), x, y, null);
			else
				g.drawImage(p2imgs.get("U"), x, y, null);
			break;
		case RU:
			if(good)
				g.drawImage(p1imgs.get("RU"), x, y, null);
			else
				g.drawImage(p2imgs.get("RU"), x, y, null);
			break;
		case R:
			if(good)
				g.drawImage(p1imgs.get("R"), x, y, null);
			else
				g.drawImage(p2imgs.get("R"), x, y, null);
			break;
		case RD:
			if(good)
				g.drawImage(p1imgs.get("RD"), x, y, null);
			else
				g.drawImage(p2imgs.get("RD"), x, y, null);
			break;
		case D:
			if(good)
				g.drawImage(p1imgs.get("D"), x, y, null);
			else
				g.drawImage(p2imgs.get("D"), x, y, null);
			break;
		case LD:
			if(good)
				g.drawImage(p1imgs.get("LD"), x, y, null);
			else
				g.drawImage(p2imgs.get("LD"), x, y, null);
			break;
		}
		
		move();
	}
	/**
	 * ����̹���ƶ�
	 */
	public void move(){
		this.oldX = x;	//	ÿ���ƶ�֮ǰ�ȱ���λ�ã�
		this.oldY = y;
		
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
		case STOP:
			break;
		}
		if(dir != Direction.STOP)	this.ptdir = this.dir;	//������Ͳ����
		
		if(x < 0)	x = 0;		//����������ֹ̹�˳��߽�
		if(y < 55)	y = 55;
		if(x + Tank.WIDTH > TankClient.Game_width)	x = TankClient.Game_width - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.Game_high)	y = TankClient.Game_high - Tank.HEIGHT;
		
		/*if(!good){									//��̹���ƶ�����
			Direction[] dirs = Direction.values();
			if(step == 0){
				step = r.nextInt(12) + 3;
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
			}
			step--;
			if(r.nextInt(50) > 48)	this.fire();	//��̹�˷����ӵ�
		}*/
	}

	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		switch(key){
		/*case KeyEvent.VK_R:
			if(this.good){
				this.live = true;
				this.life = 4;
			}
			break;*/		//ȥ����̹����������
		case KeyEvent.VK_LEFT :
			zuo = true;
			break;
		case KeyEvent.VK_UP :
			shang = true;
			break;
		case KeyEvent.VK_RIGHT :
			you = true;
			break;
		case KeyEvent.VK_DOWN :
			xia = true;
			break;
		}
		lacateDirection();
	}
	
	public void lacateDirection(){
		
		Direction oldDir = this.dir;
		
		if(zuo && !xia && !shang && !you)	dir = Direction.L;
		else if(zuo && !xia && shang && !you)	dir = Direction.LU;
		else if(!zuo && !xia && shang && !you)	dir = Direction.U;
		else if(!zuo && !xia && shang && you)	dir = Direction.RU;
		else if(!zuo && !xia && !shang && you)	dir = Direction.R;
		else if(!zuo && xia && !shang && you)	dir = Direction.RD;
		else if(!zuo && xia && !shang && !you)	dir = Direction.D;
		else if(zuo && xia && !shang && !you)	dir = Direction.LD;
		else if(!zuo && !xia && !shang && !you)	dir = Direction.STOP;
	
		if(dir != oldDir){
			TankMoveMsg tmm = new TankMoveMsg(id, dir,x,y,ptdir);
			tc.nc.send(tmm);
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_A:
			superFire();
			break;
		case KeyEvent.VK_CONTROL:
			fire();		//��fire()������ˣ����ⰴסCtrlһֱ���ڵ���
			if(isLive())
			break;
		case KeyEvent.VK_LEFT :
			zuo = false;
			break;
		case KeyEvent.VK_UP :
			shang = false;
			break;
		case KeyEvent.VK_RIGHT :
			you = false;
			break;
		case KeyEvent.VK_DOWN :
			xia = false;
			break;
		}
		lacateDirection();
	}
	
	public boolean isGood() {
		return good;
	}

	public Rectangle getRect(){		//��ײ��⣬��������ͼƬ�ľ��α߿�
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public boolean hitWall(Wall w){	//ײǽ
		if(this.live && this.getRect().intersects(w.getRect()) && w.isLive()){
			stay();		//�ص���һ��λ��
			return true;
		}
		return false;
	}
	
	public void stay(){
		this.x = oldX;
		this.y = oldY;
	}
	
	public boolean hitTanks(java.util.List<Tank> tanks){	//̹�˺�̹�˲�����ײ
		for(int i = 0; i < tanks.size(); i++){
			Tank t = tanks.get(i);
			if(this != t){
				if(this.live && t.isLive() && this.getRect().intersects(t.getRect())){
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	
	public Zidan fire(){
		if(!live)	return null;
		int x = this.x + Tank.WIDTH/2 - Zidan.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Zidan.HEIGHT/2;
		
		Zidan zi = new Zidan(id,x,y,ptdir,good,tc);
		tc.zi_s.add(zi);		//������������ӵ�
		
		ZidanNewMsg zdm = new ZidanNewMsg(zi);//�������ͻ��˷��Ͳ����ӵ�����Ϣ
		tc.nc.send(zdm);
		
		return zi;				//�Ͼ��Ѿ�����ˣ����return����ȥ����
	}

	public Zidan fire(Direction dir){//�˷�����Ҫ��ΪsuperFire()�ṩ������
		if(!live)	return null;
		int x = this.x + Tank.WIDTH/2 - Zidan.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Zidan.HEIGHT/2;
		
		Zidan zi = new Zidan(id,x, y, dir,good,tc);
		tc.zi_s.add(zi);
		return zi;
	}
	
	public void superFire(){//��8������ͬʱ����
		Direction dirs[] = Direction.values();
		for(int i = 0; i < dirs.length-1; i++){
			fire(dirs[i]);
		}
	}
	
	private class BloodLine{//Ѫ��
//		int w;
		public void draw(Graphics g){
			Color c = g.getColor();
			g.setColor(Color.red);
			g.drawRect(x, y-10, WIDTH, 10);
//			w = WIDTH * life/5;
			g.fillRect(x, y-10, WIDTH, 10);
			g.setColor(c);
		}
	}
}
