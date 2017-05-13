package com.sss.tank;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class TankNewMsg implements Msg{
	int msgType = Msg.Tank_New_Msg;
	Tank tank;
	TankClient tc;

	public TankNewMsg(Tank tank) {
		this.tank = tank;
	}

	public TankNewMsg(TankClient tc) {
		this.tc = tc;
	}

	public void send(DatagramSocket ds,String IP, int udpPort) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		
		try {
			dos.writeInt(msgType);
			dos.writeInt(tank.id);
			dos.writeInt(tank.x);
			dos.writeInt(tank.y);
			dos.writeInt(tank.dir.ordinal());
			dos.writeInt(tank.ptdir.ordinal()); 	//图片版，炮筒方向，确定调用的图片
			dos.writeBoolean(tank.good);
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
		
			int x = dis.readInt();
			int y = dis.readInt();
			Direction dir = Direction.values()[dis.readInt()];
			Direction ptdir = Direction.values()[dis.readInt()];	//炮筒方向
			boolean good = dis.readBoolean();
//System.out.println("id:"+id+"--x:"+x+"--y:"+y+"--dir"+dir+"--ptdir:"+ptdir+"--good:"+good);
	
			boolean exist = false;
			for(int i = 0; i < tc.tanks.size(); i++){
				Tank t = tc.tanks.get(i);
				if(t.id == id){
					exist = true;
					break;
				}
			}
			if(!exist){
				
				TankNewMsg tnMsg = new TankNewMsg(tc.myTank);
				tc.nc.send(tnMsg);
				
				Tank t = new Tank(x, y, good, tc);
				t.id = id;
				tc.tanks.add(t);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
