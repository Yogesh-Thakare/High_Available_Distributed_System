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
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Message sender class 
 * @author Gagandeep
 */
public class MulticastMessageSender 
{

	/**
	 * Method to multi cast message to other servers
	 */
	public List<Message> multicastMessage(Message msg) 
	{
		DatagramSocket aSocket = null;
		DatagramPacket request = null;
		byte[] buffer;
		
		try 
		{
			aSocket = new DatagramSocket();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			ObjectOutput oo = new ObjectOutputStream(outStream);
			oo.writeObject(msg);
			oo.close();

			buffer = outStream.toByteArray();
			InetAddress address = InetAddress.getByName("224.2.2.3");
			request = new DatagramPacket(buffer, buffer.length, address, Constant.MC_OPERATION_PORT);
			aSocket.send(request);

		} 
		catch (SocketException e) 
		{
			System.out.println("SOCKET Exception:" + e.getMessage());
		} 
		catch (Exception e) 
		{
			System.out.println("Exception:" + e.getMessage());
		}
		return getResults(aSocket);
	}

	/**
	 * Method to receive output from other servers
	 */
	private List<Message> getResults(DatagramSocket soc) 
	{
		List<Message> list = new ArrayList<Message>();
		byte[] buffer = null;
		
		try 
		{
			soc.setSoTimeout(2000);
			while (true) 
			{
				try 
				{
					buffer = new byte[1024];
					DatagramPacket res = new DatagramPacket(buffer,	buffer.length);
					soc.receive(res);
					list.add(deserializeMessge(res));
				} 
				catch (SocketTimeoutException e) 
				{
					break;
				}
			}
		} 
		catch (Exception e) 
		{
			System.out.println("IO:" + e.getMessage());
		}
		finally 
		{
			if (null != soc)
			{
				soc.close();
			}
		}
		
		for (Message msg : list)
		{
			System.out.println(msg.getMessage());
		}

		return list;
	}

	/**
	 * Method for multi cast requests to other servers
	 */
	public List<String> multicastRequest(String request, int port) 
	{
		DatagramSocket socket = null;
		DatagramPacket packet = null;
		
		try 
		{
			InetAddress address = InetAddress.getByName("224.2.2.3");
			socket = new DatagramSocket();
			byte[] reqBytes = request.getBytes();
			packet = new DatagramPacket(reqBytes, reqBytes.length, address,	port);
			socket.send(packet);
		} 
		catch (Exception e) 
		{
			System.out.println("Exception:" + e.getMessage());
		}
		return getResponses(socket);
	}

	/**
	 * Method to get response from servers
	 */
	private List<String> getResponses(DatagramSocket soc) 
	{
		List<String> list = new ArrayList<String>();
		byte[] buffer = null;
		
		try 
		{
			soc.setSoTimeout(2000);
			while (true) 
			{
				try 
				{
					buffer = new byte[10];
					DatagramPacket res = new DatagramPacket(buffer,buffer.length);
					soc.receive(res);
					list.add(new String(res.getData()).trim()+":"+res.getAddress().getHostAddress());
				} 
				catch (SocketTimeoutException e) 
				{
					break;
				}
			}
		} 
		catch (Exception e) 
		{
			System.out.println("Exception:" + e.getMessage());
		} 
		finally 
		{
			if (null != soc)
			{
				soc.close();
			}
		}
		
		try 
		{
			list.add(Constant.PROCESS_ID+":"+InetAddress.getLocalHost().getHostAddress());
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Method to deserialize message from input stream
	 */
	private Message deserializeMessge(DatagramPacket inPacket) throws IOException, ClassNotFoundException 
	{
		ObjectInputStream inStream = new ObjectInputStream(new ByteArrayInputStream(inPacket.getData()));
		Message msg = (Message) inStream.readObject();
		inStream.close();
		return msg;
	}
}
