package com.sss.tank;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class TankMoveMsg implements Msg{
	int msgType = Msg.Tank_Move_Msg;
	int id;
	Direction dir;
	int x,y;
//	Direction ptdir;	//炮筒方向
	TankClient tc;

	public TankMoveMsg(int id, Direction dir,int x,int y,Direction ptdir) {
		this.id = id;
		this.dir = dir;
		this.x = x;
		this.y = y;
//		this.ptdir = ptdir;
	}

	public TankMoveMsg(TankClient tc) {
		this.tc = tc;
	}

	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		try {
			dos.writeInt(msgType);
			dos.writeInt(id);
			dos.writeInt(dir.ordinal());
			dos.writeInt(x);
			dos.writeInt(y);
//			dos.writeInt(ptdir.ordinal()); 	//图片版，炮筒方向，确定调用的图片
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte[] buf = baos.toByteArray();
		
		try {
			DatagramPacket dp = new DatagramPacket(buf,buf.length,new InetSocketAddress(IP, udpPort));
			ds.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void parse(DataInputStream dis) {
		try {
			int id = dis.readInt();
			if(this.tc.myTank.id == id){
				return;
			}
			Direction dir = Direction.values()[dis.readInt()];
			int x = dis.readInt();
			int y = dis.readInt();
//			Direction ptdir = Direction.values()[dis.readInt()];	//炮筒方向
//System.out.println("id:"+id+"--x:"+x+"--y:"+y+"--dir"+dir+"--ptdir:"+ptdir+"--good:"+good);

			for(int i = 0; i < tc.tanks.size(); i++){
				Tank t = tc.tanks.get(i);
				if(t.id == id){
					t.dir = dir;
					t.x = x;
					t.y = y;
					break;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
