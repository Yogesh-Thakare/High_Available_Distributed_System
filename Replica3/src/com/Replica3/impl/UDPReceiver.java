package com.Replica3.impl;

import dsms.IClinic;
import dsms.IClinicHelper;
import org.omg.CORBA.ORB;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.io.ObjectInputStream;

/**
 * This class is used to receive and compare results
 * @author Gagandeep
 */
public class UDPReceiver extends Thread 
{
	private int port;

	/**
	 * Default Constructor for run method
	 */
	public UDPReceiver() 
	{
		
	}

	/**
	 * Constructor to start the thread
	 * @param p port number
	 */
	public UDPReceiver(int p) 
	{
		this.port = p;
		start();
	}

	/**
	 * Method run
	 */
	@Override
	public void run() 
	{
		DatagramSocket socket = null;
		
		try 
		{
			socket = new DatagramSocket(port);
			
			while(true)
			{
				byte[] receivedData = new byte[1024];
				DatagramPacket request = new DatagramPacket(receivedData,receivedData.length);
				socket.receive(request);
				
				ObjectInputStream inStream = new ObjectInputStream(new ByteArrayInputStream(request.getData()));
				Message m = (Message) inStream.readObject();
				m.setProcessID(new ClinicServer().getProcessID());
				inStream.close();
	
				MulticastMessageSender sender = new MulticastMessageSender();
				List<Message> results = sender.multicastMessage(m);
				
				results.add(getResult(m)); //Local server result
	
				byte[] outputresult = compareResults(results).getBytes();
	
				DatagramPacket response = new DatagramPacket(outputresult,outputresult.length, request.getAddress(), request.getPort());
				socket.send(response);
			}
		} 
		catch (SocketException e) 
		{
			System.out.println("Socket Exception:" + e.getMessage());
		} 
		catch (Exception e) 
		{
			System.out.println("Exception:" + e.getMessage());
		} 
		finally 
		{
			if (socket != null)
			{
				socket.close();
			}
		}
	}

	/**
	 * Method to get the result from local server
	 * @param msg result
	 */
	public Message getResult(Message msg) 
	{
		IClinic objIClinic = null;
		Message objMsg = new Message();
		String response = "";
		
		if (msg != null) 
		{
			String method = msg.getMethod();
			String managerId = msg.getManagerId();
			String stationName = managerId.replaceAll("[0-9]", "");

			if (null != (objIClinic = getRemoteObjectStub(stationName))) 
			{
				if ("DR".equalsIgnoreCase(method))
					response = objIClinic.createDRecord(msg.getFirstName(),
							msg.getLastName(), msg.getAddress(),
							msg.getPhone(), msg.getSpecialization(),msg.getLocation(),msg.getManagerId());
				else if ("NR".equalsIgnoreCase(method))
					response = objIClinic.createNRecord(msg.getFirstName(),
							msg.getLastName(), msg.getDesignation(),
							msg.getStatus(), msg.getStatusDate(),
							msg.getManagerId());
				else if ("GR".equalsIgnoreCase(method))
					response = objIClinic.getRecordCounts(msg.getRecordType(),msg.getManagerId());
				else if ("ER".equalsIgnoreCase(method))
					response = objIClinic.editRecord(msg.getRecordId(),
							msg.getFieldName(), msg.getNewValue(),
							msg.getManagerId());
				else if ("TR".equalsIgnoreCase(method))
					response = objIClinic.transferRecord(msg.getManagerId(),
							msg.getRecordId(), msg.getRemoteServerName());
			}
		}

		objMsg.setProcessID(new ClinicServer().getProcessID());
		objMsg.setMessage(response);
		objMsg.setMessageID(msg.getMessageID());
		objMsg.setMethod(msg.getMethod());
		return objMsg;
	}

	/**
	 * Method to get remote object
	 * @param msg result
	 */
	private static IClinic getRemoteObjectStub(String clinicName) 
	{
		String[] args = null;
		IClinic serverObj = null;
		
		ORB orb = ORB.init(args, null);

		String property = System.getProperty("user.dir");
		
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(property
					+ File.separator + "contact" + File.separator + clinicName
					+ "_contact.txt"));
			String ior = br.readLine();
			br.close();

			org.omg.CORBA.Object obj = orb.string_to_object(ior);

			serverObj = IClinicHelper.narrow(obj);
		} 
		catch (Exception e) 
		{
			System.out.println("IO:" + e.getMessage());
		}

		return serverObj;
	}

	/**
	 * Method to compare result of servers
	 * @param msg result
	 */
	public String compareResults(List<Message> results) 
	{
		String response = "", positiveRes = "", negResponse = "";
		int successCounter = 0;
		int majority = 0;
		
		if (results.size() != 0) 
		{
			majority = results.size() / 2 + 1;
			
			for (Message msg : results) 
			{
				if (!msg.getMethod().equalsIgnoreCase("GR")) 
				{
					if (msg.getMessage().split(":")[0].equalsIgnoreCase("SUCCESS")) 
					{
						positiveRes = msg.getMessage();
						successCounter++;
					}
					else
					{
						negResponse = msg.getMessage();
					}
				}
				else
				{
					response = msg.getMessage();
					break;
				}
			}
		}

		if (successCounter >= majority)
		{
			response = positiveRes;
		}
		else
		{
			response = negResponse;
		}

		return response;
	}
}
