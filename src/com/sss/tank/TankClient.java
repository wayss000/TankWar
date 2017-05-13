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
 * ̹�˴�ս����
 * @author sss
 *
 */
public class TankClient extends Frame implements ActionListener{
	
	public static final int Game_width = 800;	//�Զ����800*600�Ľ���ߴ�
	public static final int Game_high = 600;
	
	NetClient nc = new NetClient(this);
	ConnDialog dialog = new ConnDialog();
	
	Image backImage = null;
	
	private int tankNum = 5;	//�з�̹������
	private boolean canShowPaint = true;		//�Ƿ���Ի滭������
	
	//����˵�
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
	
	//�������
	Tank myTank =new Tank(50,500,true,this);
//	Wall []wall = new Wall[10];

	//���ڵз�̹�ˡ���ը���ӵ���ͬʱ���ڶ���ģ�����List�����࣬
	List<Tank> tanks = new ArrayList<Tank>();
	List<Baozha> Baozhas = new ArrayList<Baozha>();
	List<Zidan> zi_s = new ArrayList<Zidan>();
	List<Wall> wall = new ArrayList<Wall>();
	
	/*
	 * �����������÷����� paint ת��������һ��������������������������
	 * �������ʵ�ִ˷�������ôӦ�õ��� super.paint(g) ������
	 * �Ӷ�������ȷ�س��������������
	 * ���ͨ�� g �еĵ�ǰ����������ȫ����ĳ����������򲻻Ὣ paint() ת�������������� 
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
//			t.hitTanks(tanks);		//̹�˽�ֹ�໥��ײ
		}
/*ȥ���˲��֣�����û�е���̹�ˣ�
		if(tanks.size() == 0){
//			for(int i = 0; i<tankNum; i++){		//�з�̹���������������tankNum���з�̹�ˣ�
//				tanks.add(new Tank(100 + i*55, 100,false,this));
//			}
			if(Baozhas.size() == 0){
				if(canShowPaint == true){
					canShowPaint = false;
					JOptionPane.showMessageDialog(null, "��ϲ���ѻ������ео���ʤ��������","ʤ����", JOptionPane.INFORMATION_MESSAGE);
					this.setVisible(true);
					canShowPaint = true;
//					new Thread(new PaintThread()).start(); // �߳�������  //�˴� ���������̣߳�������ѭ��
				}
			}
		}*/
		
		/*if(myTank.getLife() == 0){		//ȥ����Σ���ȥ����̹������ʱ��������Ϸ�����Ի���
			if(canShowPaint == true){
				canShowPaint = false;
				JOptionPane.showMessageDialog(null, "��Ǹ���������ѱ��о����ܣ�����","ʧ����", JOptionPane.INFORMATION_MESSAGE);
				this.setVisible(true);
				canShowPaint = true;
//				new Thread(new PaintThread()).start(); // �߳�������  //�˴� ���������̣߳�������ѭ��
			}
		}*/
		
		
		g.drawString("�ӵ�������"+zi_s.size(), 10, 75);
		g.drawString("��ը������"+Baozhas.size(), 10, 95);
		g.drawString("̹��������"+tanks.size(), 10, 115);
	}
	
	/*update(Graphics g)	��������˫���崦����ֹ������˸,�˷�����ȥ�������ǣ�����
	 * �����������÷����� update ����ת��������һ��������������������������
	 * �������ʵ�ִ˷�������ôӦ�õ��� super.update(g) ������
	 * �Ӷ�������ȷ�س��������������
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
	
	public void initWall(){					//��ʼ����ǽ
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
		menu1 = new Menu("��Ϸ");
		menu2 = new Menu("����");
		menupause = new Menu("��ͣ/��ʼ");
		menuitem_stop = new MenuItem("��ͣ");
		menuitem_start = new MenuItem("��ʼ");
		menuitemplay = new MenuItem("�ؿ�");
		menuitemexit = new MenuItem("�˳�");
		menuitem_use_info = new MenuItem("����˵��");
		menuitem_owner_info = new MenuItem("������Ϣ");
		
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
		
//		initWall();							//���ô˷�����ʼ����ǽ��
		tanks.add(myTank);					//����棬�����Լ�̹�˵�������
		//��ʼ���з�̹��
//		tanks.add(new Tank(500, 100,false,this));
//		tanks.add(new Tank(600, 100,false,this));
//		tanks.add(new Tank(700, 100,false,this));
//		tanks.add(new Tank(700, 200,false,this));
//		tanks.add(new Tank(700, 300,false,this));
		
		this.setLocation(280, 50);
		this.setTitle("С̹�˴�ս");
		this.setSize(Game_width, Game_high);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setMenuBar(menubar);		//��Ӳ˵�����
		this.setResizable(false);
		this.setBackground(Color.gray);
		this.addKeyListener(new Keymonitor());//��Ӽ��̴����¼�
		this.setVisible(true);
		
		new Thread(new PaintThread()).start();
		
//		nc.connect("127.0.0.1", TankServer.TCP_PORT);
	}

	private class PaintThread implements Runnable{
		public void run(){
			while(canShowPaint){
				repaint();//�ػ������������������������������˷����ᾡ����ô������ paint ����������˷����ᾡ����ô������ update ������
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
			Object[] options = { "ȷ��", "ȡ��" };
			int response = JOptionPane.showOptionDialog(this, "��ȷ��Ҫ��ʼ����Ϸ��", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				canShowPaint = true;
				this.dispose();
				new TankClient();
			} else {
				canShowPaint = true;
				new Thread(new PaintThread()).start(); // �߳�����
			}

		} else if (e.getActionCommand().endsWith("Stop")) {
			canShowPaint = false;
		} else if (e.getActionCommand().equals("Continue")) {

			if (!canShowPaint) {
				canShowPaint = true;
				new Thread(new PaintThread()).start(); // �߳�����
			}
		} else if (e.getActionCommand().equals("Exit")) {
			canShowPaint = false;
			Object[] options = { "ȷ��", "ȡ��" };
			int response = JOptionPane.showOptionDialog(this, "��ȷ��Ҫ�˳���", "",
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
					options, options[0]);
			if (response == 0) {
				System.exit(0);
			} else {
				canShowPaint = true;
				new Thread(new PaintThread()).start(); // �߳�����
	
			}

		} else if (e.getActionCommand().equals("use_info")) {
			canShowPaint = false;
			JOptionPane.showMessageDialog(null, "�á� �� �� �����Ʒ���Ctrl���̷��䣬A���У�R��Ѫ��",
					"������Ϣ", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(true);
			canShowPaint = true;
			new Thread(new PaintThread()).start(); // �߳�����
		} else if(e.getActionCommand().equals("owner_info")){
			canShowPaint = false;
			JOptionPane.showMessageDialog(null, "���ߣ�ı��",
					"������Ϣ",JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(true);
			new Thread(new PaintThread()).start();
		}
	}
	
	class ConnDialog extends Dialog{
		Button b = new Button("ȷ��");
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