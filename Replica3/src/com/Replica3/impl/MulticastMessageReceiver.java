package com.Replica3.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Message receiver class 
 * @author Gagandeep
 */
public class MulticastMessageReceiver extends Thread
{
	private int port;
	
	/**
	 * Constructor
	 */
	public MulticastMessageReceiver(int port) 
	{
		this.port = port;
		start();
	}
	
	/**
	 * run method to start message receiver thread
	 */
	@Override
	public void run()
	{
		MulticastSocket socket = null;
		DatagramPacket inPacket = null;
		
		try
		{
			byte[] receiveData = new byte[1024];
			socket = new MulticastSocket(port);
			InetAddress address = InetAddress.getByName("224.2.2.3");
			socket.joinGroup(address);
			
			while(true)
			{
				inPacket = new DatagramPacket(receiveData, receiveData.length);
				socket.receive(inPacket);
				Message msg = null; String request = "";
				
				if(inPacket.getLength() >50)
				{
					msg = deserializeMessge(inPacket);
					
					if("operation".equalsIgnoreCase(msg.getMessageType()))
					{
						Message msgg = new UDPReceiver().getResult(msg);
						new ClinicServer().setFrontEndAddress(msg.getMessage());
						DatagramSocket socket2 = new DatagramSocket();
						byte[] serializeMessge = serializeMessge(msgg);
						DatagramPacket packet = new DatagramPacket(serializeMessge, serializeMessge.length,inPacket.getAddress(),inPacket.getPort());
						socket2.send(packet);
						System.out.println("response sent");
					}	
				}
				else
				{
					 request = new String(inPacket.getData());
					 
					 if(request.startsWith("FDS") || request.startsWith("LES"))
					 {
						     String reply = ""+Constant.PROCESS_ID;
						     byte[] replyBuf = reply.getBytes();
                             inPacket = new DatagramPacket(replyBuf, replyBuf.length,inPacket.getAddress(),inPacket.getPort());
                             socket.send(inPacket);
                             System.out.println("packet sent");
                     }
					 else
					 {
                    	 new ClinicServer().setGroupLeaderProcessID(Integer.parseInt(request.split(":")[1]));
                     }
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("SOCKET Exception:" + e.getMessage());
		}
		finally
		{
			if(null!= socket)
			{
				socket.close();
			}
		}
	}

	/**
	 * Method to serialize message object
	 */
	private byte[] serializeMessge(Message msg) throws IOException, ClassNotFoundException 
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput oo = new ObjectOutputStream(bos);
		oo.writeObject(msg);
		oo.close();
		return bos.toByteArray();
	}
	
	/**
	 * Method to get message stream form object
	 */
	private Message deserializeMessge(DatagramPacket inPacket) throws IOException, ClassNotFoundException 
	{
		ObjectInputStream inStream = new ObjectInputStream(new ByteArrayInputStream(inPacket.getData()));
		Message msg = (Message)inStream.readObject();
		inStream.close();
		return msg;
	}
}
