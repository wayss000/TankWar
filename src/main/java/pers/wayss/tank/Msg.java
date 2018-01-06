package pers.wayss.tank;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface Msg {
	public static final int Tank_New_Msg = 1;
	public static final int Tank_Move_Msg = 2;
	public static final int Zidan_New_Msg = 3;
	
	public void send(DatagramSocket ds, String IP, int udpPort);
	public void parse(DataInputStream dis);
}
