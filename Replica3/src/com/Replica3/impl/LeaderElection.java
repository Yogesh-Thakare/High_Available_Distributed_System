package com.Replica3.impl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

/**
 * Leader election class
 * @author Gagandeep
 */
public class LeaderElection extends Thread
{
	private List<String> activeProcesses = null; 
	
	/**
	 * Constructor
	 */
	public LeaderElection(List<String> activeProcesses)
	{
		this.activeProcesses = activeProcesses;
	}
	
	@Override
	public void run()
	{		
		electLeader();		
	}
	
	/**
	 * Method to get active processes
	 */
	public List<String> getActiveProcesses() 
	{
		return activeProcesses;
	}

	/**
	 * Method to set active processes
	 */
	public void setActiveProcesses(List<String> activeProcesses) 
	{
		this.activeProcesses = activeProcesses;
	}

	/**
	 * Method to get address
	 */
	private void getAddress(int process) 
	{
		String address = "LCHANGED";
		
		if(activeProcesses.size() != 0)
		{
			for(String proc: activeProcesses)
			{
				 String[] leaderInfo = proc.split(":");
				 if(process == Integer.parseInt(leaderInfo[0]))
				 {
					 address += ":"+leaderInfo[1];
					 break;
				 }	 
			}
		}
		notifyFrontEnd(address);
		notifyActiveReplicas(process);
	}
	
	/**
	 * Method to elect leader
	 */
	private void electLeader() 
	{
		int minProcessID = Constant.MAX_REPLICA;
		int processId;
		
		if(activeProcesses.size() != 0)
		{
			for(String process: activeProcesses)
			{
				 processId = Integer.parseInt(process.split(":")[0]);
				 
				 if(processId < minProcessID)
				 {
					minProcessID = processId;
				 }
			}
		}
		getAddress(minProcessID);
	}

	/**
	 * Method to notify servers which are up
	 */
	private void notifyActiveReplicas(int process) 
	{
		new MulticastMessageSender().multicastRequest("LES:"+process, Constant.MC_LES_PORT);
		System.out.println("New Group Leader Replica: "+process);
	}

	/**
	 * Method to notify Front End of new leader
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
