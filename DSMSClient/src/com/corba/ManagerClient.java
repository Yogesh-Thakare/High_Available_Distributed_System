package com.corba;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.omg.CORBA.ORB;

import FrontEnd.FrontEndInterface;
import FrontEnd.FrontEndInterfaceHelper;


public class ManagerClient extends Thread{

	private int testMode;
	private String managerID;
	private String recordID;
	private String firstName;
	private String lastName;
	private String description;
	private String status;
	private String phone;
	private String location;
	private String address;
	private String remoteServerName;
	private static int userChoice;
	
	/**
	 * constructor for initializing new object
	 * @param testMode
	 * @param managerID
	 * @param recordID
	 * @param firstName
	 * @param lastName
	 * @param designation
	 * @param status
	 * @param phone
	 * @param location
	 * @param address
	 * @param remoteServerName
	 */
	public ManagerClient(int testMode, String managerID, String recordID,
			String firstName, String lastName, String designation,
			String status, String phone, String location, String address, String remoteServerName) {
		super();
		this.testMode = testMode;
		this.managerID = managerID;
		this.recordID = recordID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.description = designation;
		this.status = status;
		this.phone = phone;
		this.location = location;
		this.address = address;
		this.remoteServerName =remoteServerName;
		start();
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			
			// display client menu
			showMenu();
			while(true){
				Boolean valid = false;
				
				// get the user choice
				while (!valid) {
					try {
						System.out.print("Enter your choice: ");
						userChoice = Integer.parseInt(br.readLine());
						valid = true;
					} catch (Exception e) {
						System.out.println("Invalid Input, please enter an integer: ");
						valid = false;
					}
				}
				
				// start the test cased based on user choice
				switch(userChoice){
					case 1:
                        new ManagerClient(userChoice, "MTL1111", "", "YOGESH", "THAKARE", "JUNIOR", "ACTIVE", "514-979-6766", "MTL", "123,st marc", "");
                        new ManagerClient(userChoice, "LVL1112", "", "YOGESH", "THAKARE", "JUNIOR", "ACTIVE", "514-979-6766", "LVL", "123,st marc", "");
                        new ManagerClient(userChoice, "DDO1113", "", "YOGESH", "THAKARE", "JUNIOR", "ACTIVE", "514-979-6766", "DDO", "123,st marc", "");
						break;
					case 2:
                        new ManagerClient(userChoice, "MTL1111", "", "NARESH", "PATIL", "JUNIOR", "SURGEON", "514-979-6766", "MTL", "143,st marc", "");
                        new ManagerClient(userChoice, "LVL1111", "", "NARESH", "PATIL", "JUNIOR", "SURGEON", "514-979-6766", "LVL", "143,st marc", "");
                        new ManagerClient(userChoice, "DDO1111", "", "NARESH", "PATIL", "JUNIOR", "SURGEON", "514-979-6766", "DDO", "143,st marc", "");
						break;
					case 3: 
						new ManagerClient(userChoice, "MTL1111", "","", "", "", "","","","","");
						new ManagerClient(userChoice, "LVL1111", "","", "", "", "","","","","");
						new ManagerClient(userChoice, "DDO1111", "","", "", "", "","","","","");
						break;
					case 4:	
                        new ManagerClient(userChoice, "MTL1111", "NR11111", "", "THAKARE", "", "TERMINATED", "", "", "", "");
                        new ManagerClient(userChoice, "LVL1111", "DR11111", "", "PATIL", "", "SURGEON", "", "", "", "");
                        new ManagerClient(userChoice, "DDO1111", "DR11111", "", "PATIL", "", "SURGEON", "", "", "", "");
						break;
					case 5:
						new ManagerClient(userChoice, "MTL1111", "NR11111", "", "", "", "", "", "", "","lvl");
						break;
					case 6:
                    	new ManagerClient(userChoice, "MTL2222", "", "GAURAV", "AMRUTKAR", "JUNIOR", "ACTIVE", "514-979-6766", "MTL", "123,st marc", "");
                        new ManagerClient(userChoice, "LVL2222", "", "MAHESH", "NARE", "JUNIOR", "ACTIVE", "514-979-6766", "LVL", "123,st marc", "");
                        new ManagerClient(userChoice, "DDO2222", "", "GAGAN", "SINGH", "JUNIOR", "ACTIVE", "514-979-6766", "DDO", "123,st marc", "");                   
                        new ManagerClient(userChoice, "MTL3333", "", "JOHN", "BURNEY", "JUNIOR", "SURGEON", "514-979-6766", "MTL", "143,st marc", "");
                        new ManagerClient(userChoice, "LVL3333", "", "ABHINOOR", "PANNU", "JUNIOR", "SURGEON", "514-979-6766", "LVL", "143,st marc", "");
                        new ManagerClient(userChoice, "DDO3333", "", "JACOB", "SCOTT", "JUNIOR", "SURGEON", "514-979-6766", "DDO", "143,st marc", "");
                        new ManagerClient(userChoice, "MTL4444", "", "", "", "", "", "", "", "", "");
                        new ManagerClient(userChoice, "LVL4444", "", "", "", "", "", "", "", "", "");
                        new ManagerClient(userChoice, "DDO4444", "", "", "", "", "", "", "", "", "");
                        new ManagerClient(userChoice, "MTL5555", "NR11112", "", "THAKARE", "", "TERMINATED", "", "", "", "");
                        new ManagerClient(userChoice, "LVL5555", "DR11112", "", "PATIL", "", "SURGEON", "", "", "", "");
                        new ManagerClient(userChoice, "DDO5555", "DR11112", "", "PATIL", "", "SURGEON", "", "", "", "");
                        new ManagerClient(userChoice, "LVL6666", "NR11111", "", "", "", "", "", "", "", "MTL");
						break;
					default:
						System.out.println("Invalid Input, please try again.");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	@Override
	public void run() {
		try{
			String response ="";
			String clinicName = managerID.replaceAll("[0-9]", "").trim();
			FrontEndInterface serverObj;
			
			// get the remote object reference based on station name by using IOR contact details
			if ((serverObj = getRemoteObjectStub(clinicName)) != null){ 
				
				// invoke remote methods using remote object reference based on user choice
				switch (testMode) {
					case 1:
						response = serverObj.executeOperation("NR", managerID, firstName, lastName, description, status, address, phone, location, recordID, remoteServerName); 
						System.out.println(response);
						updateClientLog(managerID,"Create Nurse Record", response);
						break;
					case 2:
						response = serverObj.executeOperation("DR", managerID, firstName, lastName, description, status, address, phone, location, recordID, remoteServerName); 
						updateClientLog(managerID,"Create Doctor Record", response);
						break;
					case 3:
						response = serverObj.executeOperation("GR", managerID, firstName, lastName, description, status, address, phone, location, recordID, remoteServerName); 
						updateClientLog(managerID,"Get Record Count", response);
						break;
					case 4:
						response = serverObj.executeOperation("ER", managerID, firstName, lastName, description, status, address, phone, location, recordID, remoteServerName); 
						updateClientLog(managerID,"Edit Record Status", response);
						break;
					case 5:
						response = serverObj.executeOperation("TR", managerID, firstName, lastName, description, status, address, phone, location, recordID, remoteServerName); 
						updateClientLog(managerID,"Edit Record Status", response);
						break;
					case 6:
						String bid = managerID.replaceAll("[A-Z]", "");
						if(bid.equalsIgnoreCase("2222")){
							response = serverObj.executeOperation("NR", managerID, firstName, lastName, description, status, address, phone, location, recordID, remoteServerName); 
							updateClientLog(managerID,"Create Nurse Record", response);
						}else if(bid.equalsIgnoreCase("3333")){
							response = serverObj.executeOperation("DR", managerID, firstName, lastName, description, status, address, phone, location, recordID, remoteServerName); 
							updateClientLog(managerID,"Create Doctor Record", response);
					    }else if(bid.equalsIgnoreCase("4444")){
					    	response = serverObj.executeOperation("GR", managerID, firstName, lastName, description, status, address, phone, location, recordID, remoteServerName); 
							updateClientLog(managerID,"Get Record Count", response);
					    }else if(bid.equalsIgnoreCase("5555")){
					    	response = serverObj.executeOperation("ER", managerID, firstName, lastName, description, status, address, phone, location, recordID, remoteServerName); 
							updateClientLog(managerID,"Edit Record Status", response);
					    }else if(bid.equalsIgnoreCase("6666")){
					    	response = serverObj.executeOperation("TR", managerID, firstName, lastName, description, status, address, phone, location, recordID, remoteServerName); 
							updateClientLog(managerID,"Edit Record Status", response);
					    }	
						break;
					default:
						System.out.println("Invalid Input, please try again.");
					}
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * get the remote object reference
	 * @param clinicName
	 * @return
	 * @throws IOException
	 */
	private static FrontEndInterface getRemoteObjectStub(String clinicName) throws IOException {
		String[] args = null;
		
		// initialize ORB
		ORB orb = ORB.init(args, null);
		
		String property = System.getProperty("user.dir");
		property = property.substring(0, property.lastIndexOf(File.separator));
		
		// get the IOR string from contact file based on station 
		BufferedReader br = new BufferedReader(new FileReader(property+File.separator+"DSMSFrontEnd"+File.separator+"FE_contact.txt"));
		String ior = br.readLine();
		br.close();
		
		// Get the CORBA object from IOR
		org.omg.CORBA.Object obj = orb.string_to_object(ior);
		
		// Convert CORBA object to JAVA object
		FrontEndInterface serverObj = FrontEndInterfaceHelper.narrow(obj);
		
		return serverObj;
	}
	
	
	/**
	 * method for displaying menu
	 */
	public static void showMenu() {
		System.out.println("\n****Welcome to DPIS****\n");
		System.out.println("Test cases(1-6)");
		System.out.println("1. Create Nurse Record");
		System.out.println("2. Create Doctor Record");
		System.out.println("3. Get Records Count");
		System.out.println("4. Edit Record");
		System.out.println("5. Transfer Record");
		System.out.println("6. Test Concurrency");
	}
	
	/**
	 * method for logging the client operations and status of the performed operations
	 * @param managerID
	 * @param operation
	 * @param status
	 */
	private synchronized void updateClientLog(String managerID, String operation, String status) {
		File file;
		BufferedWriter bw =null;
		String header="";
		LineNumberReader lnr = null;
		int lineNumber = 0;
		try{
			file = new File(managerID+"_log.txt");
			
			// check for the files exists, if not create new with header
			if(!file.exists()){
				file.createNewFile();
				FileWriter fw = new FileWriter(file.getName());
				bw = new BufferedWriter(fw);
				header = "No.\t" + "OPERATION\t\t" + "TIME\t\t" + "STATUS";
				bw.write(header);
				bw.close();
			}
			
			// if exists, add rows to exising file
			FileWriter fw = new FileWriter(file.getName(), true);
			bw = new BufferedWriter(fw);
			lnr = new LineNumberReader(new FileReader(file));
			while (null != lnr.readLine())
				lineNumber = lnr.getLineNumber();
			lnr.close();

			bw.newLine();
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss a");
			Date date = new Date();
			
			// writing the performed operation to respective client file
			String data = lineNumber+"\t"+ operation+"\t"+dateFormat.format(date)+"\t"+status;
			bw.write(data);
			bw.close();
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
			if(bw != null)
				bw.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}
