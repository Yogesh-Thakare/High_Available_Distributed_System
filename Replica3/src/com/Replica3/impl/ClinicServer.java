package com.Replica3.impl;

import java.io.File;
import java.io.FileWriter;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class ClinicServer 
{
	private static int groupLeaderProcessID = 3;
	private static String frontEndAddress;

	/**
	 * Method to get the Process ID
	 */
	public int getProcessID()
	{
		return Constant.PROCESS_ID;
	}

	/**
	 * Method to get the leader ID
	 */
	public int getGroupLeaderProcessID() 
	{
		return groupLeaderProcessID;
	}

	/**
	 * Method to set the leader ID
	 */
	public void setGroupLeaderProcessID(int ID) 
	{
		this.groupLeaderProcessID = ID;
	}

	/**
	 * Method to get the Front End address
	 */
	public String getFrontEndAddress() 
	{
		return frontEndAddress;
	}

	/**
	 * Method to set the Front End address
	 */
	public void setFrontEndAddress(String add) 
	{
		this.frontEndAddress = add;
	}
	
	/**
	 * Save ORB in a text file
	 */
	private static void writeORB(ORB orb, POA rootPOA, ClinicServerImpl obj, String server)
	{			
		try
		{
			byte[] id = rootPOA.activate_object(obj);
			org.omg.CORBA.Object ref = rootPOA.id_to_reference(id);
				
			String ior = orb.object_to_string(ref);
			System.out.println(ior);
				
			String property = System.getProperty("user.dir");
			property += File.separator + "contact"+File.separator + server+"_contact.txt";
				
			FileWriter file = new FileWriter(property);
			file.write(ior);
			file.close();
		}
		catch(Exception ex)
		{
			System.out.println("Exception in ORB");
		}		
	}	
	
	/**
	 * @param args
	 */
	public static void main(String args[]) 
	{		
		try
		{
			//Initialize the ORB
			ORB orb = ORB.init(args, null);
			POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		
			//Initialize the UDP as well as server objects
			ClinicServerImpl objMTL = new ClinicServerImpl(Constant.MTL_UDP_PORT);
			ClinicServerImpl objLVL = new ClinicServerImpl(Constant.LVL_UDP_PORT);
			ClinicServerImpl objDDO = new ClinicServerImpl(Constant.DDO_UDP_PORT);
			
			//Write IOR to text file
			writeORB(orb, rootPOA, objMTL, "MTL");
			writeORB(orb, rootPOA, objLVL, "LVL");
			writeORB(orb, rootPOA, objDDO, "DDO");

			new UDPReceiver(7003);
			new MulticastMessageReceiver(Constant.MC_OPERATION_PORT);
			
			new FailureDetection().start();;
			
			//Run the ORB
			rootPOA.the_POAManager().activate();
			orb.run();	
		}
		catch(Exception ex)
		{
			System.out.println("Exception in ORB");
		}
	}
}
