package com.sss.tank;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class ZidanNewMsg implements Msg {
	
	Zidan zd;
	TankClient tc;
	int msgType = Msg.Zidan_New_Msg;
	
	public ZidanNewMsg(Zidan zd){
		this.zd = zd;
	}
	
	public ZidanNewMsg(TankClient tc){
		this.tc = tc;
	}

	public void send(DatagramSocket ds, String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		try {
			dos.writeInt(msgType);
			dos.writeInt(zd.tankId);
			dos.writeInt(zd.x);
			dos.writeInt(zd.y);
			dos.writeInt(zd.dir.ordinal()); 	//图片版，炮筒方向，确定调用的图片
			dos.writeBoolean(zd.good);
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
			int tankId = dis.readInt();
			if(tankId == tc.myTank.id){
				return;
			}
			
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			boolean good = dis.readBoolean();

			
			Zidan zd = new Zidan(tankId,x, y, dir, good, tc);
			tc.zi_s.add(zd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
