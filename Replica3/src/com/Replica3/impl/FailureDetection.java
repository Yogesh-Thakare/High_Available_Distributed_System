package com.Replica3.impl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Failure detection class
 * @author Gagandeep
 */
public class FailureDetection extends Thread 
{
	/**
	 * run method to start failure detection thread 
	 */
	@Override
	public void run() 
	{
		new MulticastMessageReceiver(Constant.MC_FDS_PORT2);
		new MulticastMessageReceiver(Constant.MC_FDS_PORT3);
		
		while(true)
		{
			List<String> activeProcesses = new MulticastMessageSender().multicastRequest("FDS", Constant.MC_FDS_PORT1);
			int currentGroupLeader = new ClinicServer().getGroupLeaderProcessID();
	
			for(String str:activeProcesses)
				System.out.println(str);
			
			if (activeProcesses.size() == 0) 
			{
				try 
				{
					notifyFrontEnd(InetAddress.getLocalHost().getHostAddress());
				} 
				catch (UnknownHostException e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				List<Integer> processes =  new ArrayList<Integer>();
				
				for(String process: activeProcesses)
				{
					processes.add(Integer.parseInt(process.split(":")[0]));
				}
				
				if(!processes.contains(currentGroupLeader))
				{
					System.out.println("Leader Election System started...");
					new LeaderElection(activeProcesses).start();
				}
			}
			
			try 
			{
				Thread.currentThread().sleep(5000);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}	
	}

	/**
	 * Method to notify front end
	 */
	private void notifyFrontEnd(String add) 
	{
		DatagramSocket socket = null;
		DatagramPacket packet = null;
		
		try
		{
			socket = new DatagramSocket();
			byte[] bytes = add.getBytes();
			packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(new ClinicServer().getFrontEndAddress()), Constant.GL_NOTIFY_PORT);
			socket.send(packet);
		}
		catch(Exception e)
		{
			System.out.println("IO:"+e.getMessage());
		}
		finally
		{
			if(null != socket)
			{
				socket.close();
			}
		}
	}
}
