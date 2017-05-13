package com.sss.tank;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * 坦克大战主类
 * @author sss
 *
 */
public class TankClient extends Frame implements ActionListener{
	
	public static final int Game_width = 800;	//自定义的800*600的界面尺寸
	public static final int Game_high = 600;
	
	NetClient nc = new NetClient(this);
	ConnDialog dialog = new ConnDialog();
	
	Image backImage = null;
	
	private int tankNum = 5;	//敌方坦克数量
	private boolean canShowPaint = true;		//是否可以绘画主窗口
	
	//定义菜单
	MenuBar menubar = null;
	
	Menu menu1 = null;
	Menu menu2 = null;
	Menu menupause = null;
	
	MenuItem menuitem_stop = null;
	MenuItem menuitem_start = null;
	MenuItem menuitemplay = null;
	MenuItem menuitemexit = null;
	MenuItem menuitem_use_info = null;
	MenuItem menuitem_owner_info = null;
	
	//定义对象
	Tank myTank =new Tank(50,500,true,this);
//	Wall []wall = new Wall[10];

	//由于敌方坦克、爆炸及子弹是同时存在多个的，所以List集合类，
	List<Tank> tanks = new ArrayList<Tank>();
	List<Baozha> Baozhas = new ArrayList<Baozha>();
	List<Zidan> zi_s = new ArrayList<Zidan>();
	List<Wall> wall = new ArrayList<Wall>();
	
	/*
	 * 绘制容器。该方法将 paint 转发给任意一个此容器子组件的轻量级组件。
	 * 如果重新实现此方法，那么应该调用 super.paint(g) 方法，
	 * 从而可以正确地呈现轻量级组件。
	 * 如果通过 g 中的当前剪切设置完全剪切某个子组件，则不会将 paint() 转发给这个子组件。 
	 */
	public void paint(Graphics g) {
		myTank.draw(g);
		
		for(int i = 0 ; i< wall.size() ; i++){
				myTank.hitWall(wall.get(i));
		}
		for(int i = 0; i < wall.size(); i++){
			if(wall.get(i).isLive())
				wall.get(i).draw(g);
		}
		
		for(int i = 0; i < zi_s.size(); i++){
			Zidan zi = zi_s.get(i);
			zi.hitTank(myTank);
			zi.hitTanks(tanks);
			for(int j = 0 ; j < wall.size(); j++){
				zi.hitWall(wall.get(j));
			}
			zi.draw(g);
		}
		
		for(int i = 0; i < Baozhas.size(); i++){
			Baozha b = Baozhas.get(i);
			b.draw(g);
		}
		
		for(int i = 0; i < tanks.size(); i++){
			Tank t = tanks.get(i);
			t.draw(g);
			for(int j = 0; j < wall.size(); j++){
				t.hitWall(wall.get(j));
			}
//			t.hitTanks(tanks);		//坦克禁止相互碰撞
		}
/*去掉此部分，即，没有电脑坦克，
		if(tanks.size() == 0){
//			for(int i = 0; i<tankNum; i++){		//敌方坦克死后，则重新添加tankNum辆敌方坦克；
//				tanks.add(new Tank(100 + i*55, 100,false,this));
//			}
			if(Baozhas.size() == 0){
				if(canShowPaint == true){
					canShowPaint = false;
					JOptionPane.showMessageDialog(null, "恭喜你已击败所有敌军，胜利！！！","胜利啦", JOptionPane.INFORMATION_MESSAGE);
					this.setVisible(true);
					canShowPaint = true;
//					new Thread(new PaintThread()).start(); // 线程启动；  //此处 不能启动线程，否则将死循环
				}
			}
		}*/
		
		/*if(myTank.getLife() == 0){		//去掉这段，即去掉好坦克死亡时弹出的游戏结束对话框
			if(canShowPaint == true){
				canShowPaint = false;
				JOptionPane.showMessageDialog(null, "抱歉！！！你已被敌军击败！！！","失败了", JOptionPane.INFORMATION_MESSAGE);
				this.setVisible(true);
				canShowPaint = true;
//				new Thread(new PaintThread()).start(); // 线程启动；  //此处 不能启动线程，否则将死循环
			}
		}*/
		
		
		g.drawString("子弹数量："+zi_s.size(), 10, 75);
		g.drawString("爆炸数量："+Baozhas.size(), 10, 95);
		g.drawString("坦克数量："+tanks.size(), 10, 115);
	}
	
	/*update(Graphics g)	在这里用双缓冲处理，防止画面闪烁,此方法可去掉，但是，，，
	 * 更新容器。该方法将 update 方法转发给任意一个此容器子组件的轻量级组件。
	 * 如果重新实现此方法，那么应该调用 super.update(g) 方法，
	 * 从而可以正确地呈现轻量级组件。
	 */
	public void update(Graphics g) {
		if(backImage == null){
			backImage = this.createImage(Game_width, Game_high);
		}
		Graphics back = backImage.getGraphics();
		Color c = back.getColor();
		back.setColor(Color.gray);
		back.fillRect(0, 0, Game_width, Game_high);
		back.setColor(c);
		paint(back);
		g.drawImage(backImage, 0, 0, null);
	}
	
	public void initWall(){					//初始化城墙
		for(int i = 0 ; i < 10; i++){
			wall.add(new Wall(150+i*50, 200,true,this));
		}
		for(int i = 0 ; i < 10; i++){
			wall.add(new Wall(200, 250+20*i,true,this));
		}
		for(int i = 0 ; i < 10; i++){
			wall.add(new Wall(550, 250+20*i,true,this));
		}
	}

	public TankClient(){
		
		menubar = new MenuBar();
		menu1 = new Menu("游戏");
		menu2 = new Menu("帮助");
		menupause = new Menu("暂停/开始");
		menuitem_stop = new MenuItem("暂停");
		menuitem_start = new MenuItem("开始");
		menuitemplay = new MenuItem("重开");
		menuitemexit = new MenuItem("退出");
		menuitem_use_info = new MenuItem("操作说明");
		menuitem_owner_info = new MenuItem("作者信息");
		
		menu1.add(menuitemplay);
		menu1.add(menuitemexit);
		menu2.add(menuitem_use_info);
		menu2.add(menuitem_owner_info);
		menupause.add(menuitem_stop);
		menupause.add(menuitem_start);
		
		menubar.add(menu1);
		menubar.add(menu2);
		menubar.add(menupause);
		
		menuitemplay.addActionListener(this);
		menuitemplay.setActionCommand("NewGame");
		menuitemexit.addActionListener(this);
		menuitemexit.setActionCommand("Exit");
		menuitem_stop.addActionListener(this);
		menuitem_stop.setActionCommand("Stop");
		menuitem_start.addActionListener(this);
		menuitem_start.setActionCommand("Continue");
		menuitem_use_info.addActionListener(this);
		menuitem_use_info.setActionCommand("use_info");
		menuitem_owner_info.addActionListener(this);
		menuitem_owner_info.setActionCommand("owner_info");
		
//		initWall();							//调用此方法初始化城墙，
		tanks.add(myTank);					//网络版，加入自己坦克到集合中
		//初始化敌方坦克
//		tanks.add(new Tank(500, 100,false,this));
//		tanks.add(new Tank(600, 100,false,this));
//		tanks.add(new Tank(700, 100,false,this));
//		tanks.add(new Tank(700, 200,false,this));
//		tanks.add(new Tank(700, 300,false,this));
		
		this.setLocation(280, 50);
		this.setTitle("小坦克大战");
		this.setSize(Game_width, Game_high);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setMenuBar(menubar);		//添加菜单窗口
		this.setResizable(false);
		this.setBackground(Color.gray);
		this.addKeyListener(new Keymonitor());//添加键盘处理事件
		this.setVisible(true);
		
		new Thread(new PaintThread()).start();
		
//		nc.connect("127.0.0.1", TankServer.TCP_PORT);
	}

	private class PaintThread implements Runnable{
		public void run(){
			while(canShowPaint){
				repaint();//重绘此组件。如果此组件是轻量级组件，则此方法会尽快调用此组件的 paint 方法。否则此方法会尽快调用此组件的 update 方法。
				try{
					Thread.sleep(50);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}

	private class Keymonitor extends KeyAdapter{

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if(key == KeyEvent.VK_ENTER){
				dialog.setVisible(true);
			}else{
				myTank.keyPressed(e);
			}
		}
		
		public void keyReleased(KeyEvent e){
			myTank.keyReleased(e);
		}
	}
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
//		tc.TankClient();
	}
	
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("NewGame")) {
			canShowPaint = false;
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "您确认要开始新游戏！", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				canShowPaint = true;
				this.dispose();
				new TankClient();
			} else {
				canShowPaint = true;
				new Thread(new PaintThread()).start(); // 线程启动
			}

		} else if (e.getActionCommand().endsWith("Stop")) {
			canShowPaint = false;
		} else if (e.getActionCommand().equals("Continue")) {

			if (!canShowPaint) {
				canShowPaint = true;
				new Thread(new PaintThread()).start(); // 线程启动
			}
		} else if (e.getActionCommand().equals("Exit")) {
			canShowPaint = false;
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "您确认要退出吗", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				System.exit(0);
			} else {
				canShowPaint = true;
				new Thread(new PaintThread()).start(); // 线程启动
	
			}

		} else if (e.getActionCommand().equals("use_info")) {
			canShowPaint = false;
			JOptionPane.showMessageDialog(null, "用→ ← ↑ ↓控制方向，Ctrl键盘发射，A大招，R回血！",
					"操作信息", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(true);
			canShowPaint = true;
			new Thread(new PaintThread()).start(); // 线程启动
		} else if(e.getActionCommand().equals("owner_info")){
			canShowPaint = false;
			JOptionPane.showMessageDialog(null, "作者：谋哥",
					"作者信息",JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(true);
			new Thread(new PaintThread()).start();
		}
	}
	
	class ConnDialog extends Dialog{
		Button b = new Button("确定");
		TextField tfIP = new TextField("127.0.0.1", 12);
		TextField tfPort = new TextField(""+TankServer.TCP_PORT, 4);
		TextField tfRB = new TextField(4);
		TextField tfMyUDPPort = new TextField("5556",4);
		
		public ConnDialog(){
			super(TankClient.this,true);
			this.setLayout(new FlowLayout());
			this.add(new Label("IP:"));
			this.add(tfIP);
			this.add(new Label("Port:"));
			this.add(tfPort);
			this.add(new Label("Red or Blue:"));
			this.add(tfRB);
			this.add(new Label("MyUDPPort:"));
			this.add(tfMyUDPPort);
			this.add(b);
			this.setLocation(300,300);
			this.pack();
			this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowAdapter e){
					setVisible(false);
				}
			});
			
			b.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					setVisible(false);

					String IP = tfIP.getText();
					int Port = Integer.parseInt(tfPort.getText().trim());
					int myUDPPort = Integer.parseInt(tfMyUDPPort.getText());
					String color = tfRB.getText();
					if(color.equals("red") || color.equals("Red")){
						myTank.good = true;
					}else{
						myTank.good = false;
					}
					nc.setUdpPort(myUDPPort);
					nc.connect(IP, Port);
				}
			});
		}
	}
}