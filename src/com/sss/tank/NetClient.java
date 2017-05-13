package com.sss.tank;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public class NetClient {
	
	TankClient tc;
	
	private int udpPort;
	
	public int getUdpPort() {
		return udpPort;
	}
	public void setUdpPort(int udpPort) {
		this.udpPort = udpPort;
	}

	DatagramSocket ds = null;
	
	public NetClient(TankClient tc){
		this.tc = tc;
		
	}
	public void connect(String IP,int port){
		try {
			ds = new DatagramSocket(udpPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		Socket s = null;
		try{
			s = new Socket(IP,port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			DataInputStream dis = new DataInputStream(s.getInputStream());
			int id = dis.readInt();
			tc.myTank.id = id;
			
			/*if(id%2 == 0){
				tc.myTank.good = false;
			}else{
				tc.myTank.good = true;
			}*/
			
//System.out.println("succes  Server give me ID: "+id);
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(s != null){
				try {
					s.close();
					s = null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		TankNewMsg tnm = new TankNewMsg(tc.myTank);
		send(tnm);
		
		new Thread(new UDPRecThread()).start();
	}
	
	public void send(Msg tnm){
		tnm.send(ds,"127.0.0.1",TankServer.UDP_PORT);
	}
	
	private class UDPRecThread implements Runnable{

		byte[] buf	=new byte[1024];
		public void run() {
			
			while(ds != null){
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				try {
					ds.receive(dp);
					parse(dp);
//System.out.println("A udppacket received  from Server!");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void parse(DatagramPacket dp){
			ByteArrayInputStream bais = new ByteArrayInputStream(buf,0,dp.getLength());
			DataInputStream dis = new DataInputStream(bais);
			int msgType = 0;
			try {
				msgType = dis.readInt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Msg msg = null;
			switch(msgType){
			case Msg.Tank_New_Msg:
				msg = new TankNewMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.Tank_Move_Msg:
				msg = new TankMoveMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			case Msg.Zidan_New_Msg:
				msg = new ZidanNewMsg(NetClient.this.tc);
				msg.parse(dis);
				break;
			}
		}
		
	}
	
}
