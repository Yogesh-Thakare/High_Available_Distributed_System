package com.Replica3.impl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * This is the listener class and listens to incoming requests on specific ports
 * @author Gagandeep
 */
public class UDPThread extends Thread 
{
	int port;
	ClinicServerImpl objImpl;

	/**
	 * Constructor to initialize values
	 * @param pn port number
	 * @param o object
	 */
	UDPThread(int pn, ClinicServerImpl o) 
	{
		this.port = pn;
		this.objImpl = o;
	}

	/**
	 * Method to enable the listener
	 */
	public void run() 
	{
		DatagramSocket aSocket = null;
		
		String operation = "";
		String response = "";	
		
		try 
		{	
			aSocket = new DatagramSocket(port);
			System.out.println("UDP Server started on Port number: " + port);
			byte[] buffer = new byte[32];
			byte[] replyBuffer = new byte[32];
			
			while (true) 
			{
				DatagramPacket request = new DatagramPacket(buffer,buffer.length);
				aSocket.receive(request);
				operation = new String(request.getData()).trim();

				if(operation.length() <= 4)
				{
					response = objImpl.getRecordCount(operation);
				}
				else
				{
					response = objImpl.transfer(operation);
				}
				
				replyBuffer = response.getBytes();
				
				DatagramPacket reply = new DatagramPacket(replyBuffer,response.length(), request.getAddress(),request.getPort());
				aSocket.send(reply);
			}
		} 
		catch (SocketException e) 
		{
			System.out.println("SocketException: " + e.getMessage());
		} 
		catch (Exception e) 
		{
			System.out.println("Exception: " + e.getMessage());
		} 
		finally 
		{
			if (aSocket != null)
			{
				aSocket.close();
			}
		}
	}
}
