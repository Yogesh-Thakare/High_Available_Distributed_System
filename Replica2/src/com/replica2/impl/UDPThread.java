package com.replica2.impl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
/**
 * 
 * @author jaiminpatel
 * 
 */
public class UDPThread extends Thread {

	int portNumber;
	ServerClinicImpl object;

	UDPThread(int portNo, ServerClinicImpl Obj) {
		this.portNumber = portNo;
		this.object = Obj;
	}

	/**
	 * called by each station server for UDP server startup
	 */
	public void run() {
		String task = "", response = "";
		DatagramSocket aSocket = null;
		try {
			
			// 
			aSocket = new DatagramSocket(portNumber);
			System.out.println("UDP server started at port: " + portNumber);
			byte[] buffer = new byte[32];
			byte[] replyBuffer = new byte[32];
			
			// Continuously listening for client requests
			while (true) {
				DatagramPacket request = new DatagramPacket(buffer,
						buffer.length);
				aSocket.receive(request);
				task = new String(request.getData()).trim();

				// get response based on above request and reply to
				// corresponding requested server
				if(task.length() <= 4)
					response = object.getRecordCount(task);
				else
					response = object.transfer(task);
				replyBuffer = response.getBytes();
				DatagramPacket reply = new DatagramPacket(replyBuffer,
						response.length(), request.getAddress(),
						request.getPort());
				aSocket.send(reply);
			}
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
	}
}
