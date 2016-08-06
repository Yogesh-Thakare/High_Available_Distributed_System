package com.replica2.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

/**
 * 
 * @author jaiminpatel
 * main server class
 */
public class ClinicServers{

	private static int groupLeaderProcessID = 1;
	private static String frontEndAddress;
	/**
	 * 
	 * @param args
	 * @throws InvalidName
	 * @throws ServantAlreadyActive
	 * @throws WrongPolicy
	 * @throws ObjectNotActive
	 * @throws AdapterInactive
	 * @throws IOException
	 */
	public static void main(String args[]) throws InvalidName, ServantAlreadyActive, WrongPolicy, ObjectNotActive, AdapterInactive, IOException{
			
			//ORB initialization
			ORB orb = ORB.init(args, null);
			POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		
			//initialize the server objects and UDP servers
			ServerClinicImpl spvmObj = new ServerClinicImpl(Constants.MTL_UDP_PORT);
			ServerClinicImpl splObj = new ServerClinicImpl(Constants.LVL_UDP_PORT);
			ServerClinicImpl spbObj = new ServerClinicImpl(Constants.DDO_UDP_PORT);
			
			//translate object reference to ior and write to file
			writeContactDetailsToFile(orb, rootPOA, spvmObj, "MTL");
			writeContactDetailsToFile(orb, rootPOA, splObj, "LVL");
			writeContactDetailsToFile(orb, rootPOA, spbObj, "DDO");

			new UDPReceiver(7002);
			new MulticastMessageReceiver(Constants.MC_OPERATION_PORT);
			
			new FailureDetection().start();;
			
			//run the ORB
			rootPOA.the_POAManager().activate();
			orb.run();	
	}

	/**
	 * method for writing IOR to text file
	 * @param orb
	 * @param rootPOA
	 * @param object
	 * @param station
	 * @throws ServantAlreadyActive
	 * @throws WrongPolicy
	 * @throws ObjectNotActive
	 * @throws IOException
	 */
	private static void writeContactDetailsToFile(ORB orb, POA rootPOA,
			ServerClinicImpl object, String station) throws ServantAlreadyActive,
			WrongPolicy, ObjectNotActive, IOException {
				
				//get the object reference 
				byte[] id = rootPOA.activate_object(object);
				org.omg.CORBA.Object ref = rootPOA.id_to_reference(id);
				
				String ior = orb.object_to_string(ref);
				System.out.println(ior);
				
				//get the current path to write file
				String property = System.getProperty("user.dir");
				property += File.separator+"contact"+File.separator+station+"_contact.txt";
				
				FileWriter file = new FileWriter(property);
				file.write(ior);
				file.close();
	}	
	
	public int getProcessID(){
		return Constants.PROCESS_ID;
	}

	/**
	 * @return the groupLeaderProcessID
	 */
	public int getGroupLeaderProcessID() {
		return groupLeaderProcessID;
	}

	/**
	 * @param groupLeaderProcessID the groupLeaderProcessID to set
	 */
	public void setGroupLeaderProcessID(int groupLeaderProcessID) {
		this.groupLeaderProcessID = groupLeaderProcessID;
	}

	/**
	 * @return the froentEndAddress
	 */
	public String getFrontEndAddress() {
		return frontEndAddress;
	}

	/**
	 * @param froentEndAddress the froentEndAddress to set
	 */
	public void setFrontEndAddress(String frontEndAddress) {
		this.frontEndAddress = frontEndAddress;
	}
	
	
}
