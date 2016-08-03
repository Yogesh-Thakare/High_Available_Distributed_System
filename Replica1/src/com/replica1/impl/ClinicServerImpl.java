package com.replica1.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import dsms.ClinicServicePOA;



/**
 * 
 * @author jaiminpatel Implementation class of the server interface
 */
public class ClinicServerImpl extends ClinicServicePOA {

	private HashMap<String, List<Practitioner>> practitionerRecords = new HashMap<String, List<Practitioner>>();
	private static int drRecordID = 11111;
	private static int nrRecordID = 11111;
	//private final Object lock = new java.lang.Object();
	
	private UDPThread udpThread;

	/**
	 * Constructor for initializing server object
	 * 
	 * @param portNum
	 */
	protected ClinicServerImpl(int portNum) {
		super();
		udpThread = new UDPThread(portNum, this);
		udpThread.start();
	}
	
	@Override
	public String createDRecord(String firstName, String lastName,
			String address, String phone, String specialization,
			String location, String managerID) {
		String record = "DR", response = "";
		String key = lastName.substring(0, 1);

		// set the details received from client
		DoctorRecord dr = new DoctorRecord();
		int id = getDRId();
		dr.setRecordID(record + id);
		dr.setFirstName(firstName);
		dr.setLastName(lastName);
		dr.setAddress(address);
		dr.setSpecialization(specialization);
		dr.setLocation(location);

		// adding record to list and then to Hashtable
		addRecord(key, dr);

		response = "SUCCESS: Doctor Record Created";
		updateServerLog(managerID, record + id, "Created Doctor Record",
				response, location.toString());
		return response;
	}

	/**
	 * interface method implementation for creating new criminal record (CR)
	 *//*
	@Override
	public String createCRecord(String firstName, String lastName,
			String description, String status, String badgeID) {

		String record = "CR", response = "";
		String key = lastName.substring(0, 1);

		// set the details received from client
		CriminalRecords cd = new CriminalRecords();
		int id = getCRId();
		cd.setRecordID(record + id);
		cd.setFirstName(firstName);
		cd.setLastName(lastName);
		cd.setCrimeDescription(description);
		cd.setStatus(status);

		// adding record to list and then to Hashtable
		addRecord(key, cd);

		response = "SUCCESS: Criminal Record Created";
		updateServerLog(badgeID, record + id, "Created Criminal Record",
				response, status.toString());
		return response;
	}
*/
	
	@Override
	public String createNRecord(String firstName, String lastName,
			String designation, String status, String statusDate,
			String managerID) {
		String record = "NR", response = "";
		String key = lastName.substring(0, 1);
		
		// set the details received from client
		NurseRecord nr = new NurseRecord();
		int id = getNRId();
		nr.setRecordID(record + id);
		nr.setFirstName(firstName);
		nr.setLastName(lastName);
		nr.setDesignation(designation);
		nr.setStatus(statusDate);
		nr.setStatusDate(statusDate);
		

		// adding record to list and then to Hashtable
		addRecord(key, nr);

		response = "SUCCESS: Nurse Record Created";
		updateServerLog(managerID, record + id, "Created Nurse Record",
				response, status.toString());
		return response;
	}
	
	/*
	*//**
	 * interface method implementation for creating new missing record (MR)
	 *//*
	@Override
	public String createMRecord(String firstName, String lastName,
			String address, String lastDate, String lastLocation,
			String status, String badgeID) {

		String record = "MR", response = "";
		String key = lastName.substring(0, 1);
		
		// set the details received from client
		MissingRecords cd = new MissingRecords();
		int id = getMRId();
		cd.setRecordID(record + id);
		cd.setFirstName(firstName);
		cd.setLastName(lastName);
		cd.setAddress(address);
		cd.setLastDate(lastDate);
		cd.setLastLocation(lastLocation);
		cd.setStatus(status);

		// adding record to list and then to Hashtable
		addRecord(key, cd);

		response = "SUCCESS: Missing Record Created";
		updateServerLog(badgeID, record + id, "Created Missing Record",
				response, status.toString());
		return response;
	}*/
	
	@Override
	public String getRecordCounts(String recordType, String managerID) {
		// extracting clinic name from badgeId
				String clinicName = managerID.replaceAll("[0-9]", "");
				String result = "";

				// getting local clinic record count
				result += this.getRecordCount(clinicName);

				// getting record count from other remote clinics
				result += " "
						+ getRecordFromOtherServers(getRemoteServerPorts(clinicName, "getcount", null));
				updateServerLog(managerID, null, "Get Record Count", result, null);
				return result;
	}

	/**
	 * interface method implementation for getting records count from own and
	 * other servers
	 *//*
	@Override
	public String getRecordCounts(String badgeID) {
		// extracting station name from badgeId
		String stationName = badgeID.replaceAll("[0-9]", "");
		String result = "";

		// getting local station record count
		result += this.getRecordCount(stationName);

		// getting record count from other remote servers
		result += " "
				+ getRecordFromOtherServers(getRemoteServerPorts(stationName, "getcount", null));
		updateServerLog(badgeID, null, "Get Record Count", result, null);
		return result;
	}*/
	
	@Override
	public String editRecord(String recordID, String fieldName,
			String newValue, String managerID) 
	{
		String result = "";
		if(recordID.startsWith("DR")&&fieldName.equalsIgnoreCase("location") && !(newValue.equalsIgnoreCase("mtl")||newValue.equalsIgnoreCase("lvl")||newValue.equalsIgnoreCase("ddo")))
		{
			result = "FAILED: Because of Invalid data for field name: "+fieldName;
			//logger.info(" could not update doctor record with record ID: "+recordID+" Because of Invalid data for field name: "+fieldName);			
		}
		else if(recordID.startsWith("NR")&& fieldName.equalsIgnoreCase("designation") && !(newValue.equalsIgnoreCase("junior")|| newValue.equalsIgnoreCase("senior")))
		{
			result = "FAILED: Because of Invalid data for field name: "+fieldName;

		}
		else if(recordID.startsWith("NR")&& fieldName.equalsIgnoreCase("status") && !(newValue.equalsIgnoreCase("terminated")|| newValue.equalsIgnoreCase("active")))
		{
			result = "FAILED: Because of Invalid data for field name: "+fieldName;

		}
		else if(fieldName.equalsIgnoreCase("address")||fieldName.equalsIgnoreCase("phone")||fieldName.equalsIgnoreCase("location")||
				fieldName.equalsIgnoreCase("designation")||fieldName.equalsIgnoreCase("status")||fieldName.equalsIgnoreCase("statusDate"))
		{
			Practitioner practitionerUpdate=null;
			/*synchronized(practitionerRecords) 
			{*/
			boolean recordFound=false;
			for (Map.Entry<String,List<Practitioner>> entry : practitionerRecords.entrySet())
		    {
			    List<Practitioner> practitionerList = entry.getValue();
			    synchronized(practitionerList) 
			    {
			    	for(Practitioner practitioner:practitionerList)
			    	{
			    		if(recordID.startsWith("DR")&& practitioner instanceof DoctorRecord)
			    		{
			    			practitionerUpdate=practitioner;
			    			if(recordID.equals(practitioner.getRecordID()))
			    			{
			    				if(fieldName.equalsIgnoreCase("address")){((DoctorRecord)practitioner).setAddress(newValue); recordFound=true; break;}
			    				if(fieldName.equalsIgnoreCase("phone")){((DoctorRecord)practitioner).setPhone(newValue);recordFound=true; break;}
			    				if(fieldName.equalsIgnoreCase("location")){((DoctorRecord)practitioner).setLocation(newValue);recordFound=true; break;}
			    			}
			    		}
			    		else if(recordID.startsWith("NR")&& practitioner instanceof NurseRecord)
			    		{
			    			practitionerUpdate=practitioner;
			    			if(recordID.equals(practitioner.getRecordID()))
			    			{
			    				if(fieldName.equalsIgnoreCase("designation")){((NurseRecord)practitioner).setDesignation(newValue);recordFound=true; break;}
			    				if(fieldName.equalsIgnoreCase("status")){((NurseRecord)practitioner).setStatus(newValue);recordFound=true; break;}
			    				if(fieldName.equalsIgnoreCase("statusDate")){((NurseRecord)practitioner).setStatusDate(newValue);recordFound=true; break;}
			    			}
			    		}
			    	}
		        }
			    	if(recordFound) break;
		        }
			if(practitionerUpdate instanceof DoctorRecord)
				result = "SUCCESS: Doctor Record updated";
			if(practitionerUpdate instanceof NurseRecord)
				result = "SUCCESS: Nurse Record updated";
			
		}
		else
		{
			result = "FAILED:: could not update record with record ID: "+recordID+" Invalid field name: "+fieldName;
		
		}
		updateServerLog(managerID, recordID, "Edit Record Status", result,
				newValue);
		return result;
	}

	/**
	 * interface method implementation for editing the existing record (criminal
	 * or missing record)
	 */
	/*@Override
	public String editCRecord(String lastName, String recordID,
			String newStatus, String badgeID) {
		String key = lastName.substring(0, 1);
		String result = "", newstatus = "";

		if (recordtable.containsKey(key)) {

			// extracting list from the Hashtable
			List<Records> list = recordtable.get(key);

			for (Records cd : list) {
				// synchronizing at record level so multiple thread can't update
				// the record same time
				synchronized (lock) {

					// checking whether the record already exist or not
					if (recordID.equalsIgnoreCase(cd.getRecordID())
							&& lastName.equalsIgnoreCase(cd.getLastName())) {
						
						// check for the same record status
						if (newStatus.toString().equalsIgnoreCase(
								cd.getStatus().toString())) {
							newstatus = cd.getStatus().toString();
							result = "FAILED: Record already has the same status";
						} else {
							// updating the record with new status
							cd.setStatus(newStatus);
							int indexOf = list.indexOf(cd);
							list.set(indexOf, cd);
							newstatus = newStatus.toString();
							result = "SUCCESS: Record updated";
						}
					} else {
						result = "FAILED:: Record does not exist";
					}
				}
			}
			// putting updated list back to Hashtable
			recordtable.put(key, list);
		} else {
			result = "FAILED: Record does not exist";
		}
		updateServerLog(badgeID, recordID, "Edit Record Status", result,
				newstatus);
		return result;
	}*/

	/**
	 * interface method implementation for transferring the existing record to
	 * another station
	 */
	@Override
	public String transferRecord(String managerId, String recordId,
			String remoteServerName) {
		String response = "";
		Boolean found = false;
		Set<String> keySet = practitionerRecords.keySet();
		
		// iterating each key then checking in respective list for record exist or not
		// if exist the transferring to specified remote server
		for (String key : keySet) {
			List<Practitioner> list = practitionerRecords.get(key);
			synchronized (list) {
			for (Practitioner r : list) {
			
					if (recordId.equalsIgnoreCase(r.getRecordID())) {
						String record = constructRecord(r, remoteServerName);
						if (record.length() != 0) {
							getRecordFromOtherServers(getRemoteServerPorts(remoteServerName,"transfer",record));
							deleteRecord(recordId, keySet);
						}
						found = true;
						break;
					} else {
						found = false;
					}
				}
			}
			}
		

		// if record found in local station then after transferring deleting record from local station
		if (!found)
			response += "Record Not Found!";
		else {
			//deleteRecord(recordId, keySet);
			response += "Record deleted and transferred!";
		}
		updateServerLog(managerId, recordId, "Transfer Record", response,
				"-------");
		return response;
	}

	/**
	 * method for deleting the existing record after transferring the record to
	 * another station
	 * 
	 * @param recordId
	 * @param keySet
	 */
	private void deleteRecord(String recordId, Set<String> keySet) {
		
		// checking for record exist in HashMap of local server
		for (String key : keySet) {
			List<Practitioner> list = practitionerRecords.get(key);
			Iterator<Practitioner> iterator = list.iterator();
			while (iterator.hasNext()) {
				Practitioner next = iterator.next();
				if (recordId.equalsIgnoreCase(next.getRecordID())) {
					iterator.remove();
					break;
				}
			}
			
			// after deleting record from list, putting updated list to HashMap
			practitionerRecords.put(key, list);
		}
	}

	/**
	 * 
	 * method for converting record into array of strings
	 * @param r
	 * @param managerId
	 * @return array of strings
	 */
	private String constructRecord(Practitioner r, String managerId) {
		String recordID = r.getRecordID();
		StringBuilder record = new StringBuilder();
		record.append(recordID).append("|").append(managerId).append("|").append(r.getFirstName()).append("|").append(r.getLastName()).append("|");
		if (recordID.contains("DR")) {
			DoctorRecord dr = (DoctorRecord) r;
			record.append(dr.getAddress()).append("|").append(dr.getPhone()).append("|").append(dr.getSpecialization()).append("|").append(dr.getLocation());
		}
		if (recordID.contains("NR")) {
			NurseRecord nr = (NurseRecord) r;
			record.append(nr.getDesignation()).append("|").append(nr.getStatus()).append("|").append(nr.getStatusDate());
		}
		return record.toString();

	}

/*	*//**
	 * method for getting remote object reference of specified station based on
	 * contact details
	 * 
	 * @param stationName
	 * @return object reference of specified station name
	 * @throws IOException
	 *//*
	private ClinicService getRemoteServerObject(String stationName)
			throws IOException {
		String[] args = null;
		
		// initializing ORB
		ORB orb = ORB.init(args, null);

		// get the current filepath
		String property = System.getProperty("user.dir");
		property += File.separator + "contact" + File.separator + stationName
				+ "_contact.txt";

		// read the contact details from respective IOR file based on station
		BufferedReader br = new BufferedReader(new FileReader(property));
		String ior = br.readLine();
		br.close();

		//  fetch the IOR from file and convert it to CORBA object
		org.omg.CORBA.Object obj = orb.string_to_object(ior);

		// convert CORBA object to Java Object
		ClinicService serverObj = ClinicServiceHelper.narrow(obj);

		return serverObj;

	}*/

	/**
	 * Synchronized method for getting unique criminal record ID
	 * 
	 * @return
	 */
	private synchronized int getDRId() {
		int id = drRecordID;
		drRecordID++;
		return id;
	}

	/**
	 * Synchronized method for getting unique missing record ID
	 * 
	 * @return
	 */
	private synchronized int getNRId() {
		int id = nrRecordID;
		nrRecordID++;
		return id;
	}

	/**
	 * method for adding record (criminal or missing) to Hashtable
	 * 
	 * @param key
	 * @param record
	 */
	private void addRecord(String key, Practitioner record) {
		List<Practitioner> list = new ArrayList<Practitioner>();
		
		// check for the record, if exist fetch the list and then add otherwise create new list an dadd
		synchronized (list) {
			if (practitionerRecords.containsKey(key)) {
				list = practitionerRecords.get(key);
				list.add(record);
			} else {
				list.add(record);
			}
			practitionerRecords.put(key, list);
		}
	}

	/**
	 * method for getting remote server's UDP port numbers
	 * 
	 * @param stationName
	 * @return list of other servers port numbers
	 */
	private List<String> getRemoteServerPorts(String stationName, String operation, String record) {
		List<String> list = new ArrayList<String>();
		if("transfer".equalsIgnoreCase(operation)){
			if ("MTL".equalsIgnoreCase(stationName)) {
				list.add(Constants.MTL_UDP_PORT + ":"+record);
			} else if ("LVL".equalsIgnoreCase(stationName)) {
				list.add(Constants.LVL_UDP_PORT + ":"+record);
			} else if ("DDO".equalsIgnoreCase(stationName)) {
				list.add(Constants.DDO_UDP_PORT + ":"+record);
			}
		}else if("getcount".equalsIgnoreCase(operation)){
			// set the UDP port number for other servers expect own for UDP communication between servers
			if ("MTL".equalsIgnoreCase(stationName)) {
				list.add(Constants.DDO_UDP_PORT + ":DDO");
				list.add(Constants.LVL_UDP_PORT + ":LVL");
			} else if ("LVL".equalsIgnoreCase(stationName)) {
				list.add(Constants.MTL_UDP_PORT + ":MTL");
				list.add(Constants.DDO_UDP_PORT + ":DDO");
			} else if ("DDO".equalsIgnoreCase(stationName)) {
				list.add(Constants.MTL_UDP_PORT + ":MTL");
				list.add(Constants.LVL_UDP_PORT + ":LVL");
			}
		}
		return list;
	}

	/**
	 * method for generating and updating station wise log files
	 * 
	 * @param badgeID
	 * @param recordID
	 * @param operation
	 * @param response
	 * @param newStatus
	 */
	private synchronized void updateServerLog(String badgeID, String recordID,
			String operation, String response, String newStatus) {
		File file;
		BufferedWriter bw = null;
		LineNumberReader lnr = null;
		int lineNumber = 0;
		String stationName = badgeID.replaceAll("[0-9]", "").trim();
		try {
			file = new File(stationName + "_log.txt");
			
			// check for file exists, if not create new file and header
			if (!file.exists()) {
				file.createNewFile();
				FileWriter fw = new FileWriter(file.getName());
				bw = new BufferedWriter(fw);
				String header = "No.\t" + "Manager ID\t" + "Record ID\t"
						+ "Status\t" + "Operation\t\t" + "Time\t\t"
						+ "Transaction Status";
				bw.write(header);
				bw.close();
			}

			// if exist, get line number and add rows
			FileWriter fw = new FileWriter(file.getName(), true);
			bw = new BufferedWriter(fw);
			lnr = new LineNumberReader(new FileReader(file));
			while (null != lnr.readLine())
				lineNumber = lnr.getLineNumber();
			lnr.close();

			bw.newLine();
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss a");
			Date date = new Date();
			
			// write the transaction details of performed operation on server
			String data = lineNumber + "\t" + badgeID + "\t"
					+ ((null == recordID) ? "-------" : recordID) + "\t\t"
					+ ((null == newStatus) ? "-------" : newStatus) + "\t"
					+ operation + "\t" + dateFormat.format(date) + "\t"
					+ response;
			bw.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (lnr != null)
					lnr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * method to count number of record exist in Hashtable at particular time
	 * 
	 * @param task
	 * @return
	 */
	public synchronized String getRecordCount(String task) {
		int size = 0;
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss a");
		Date date = new Date();
		
		// get the count of all record stored in list for all keys
		Set<String> keySet = practitionerRecords.keySet();
		if (!keySet.isEmpty()) {
			for (String key : keySet)
				size += practitionerRecords.get(key).size();
		}
		task += ":" + size;
		task += " (" + dateFormat.format(date) + ")";
		return task;
	}

	/**
	 * method for getting record count from remote servers through UDP
	 * communication
	 * 
	 * @param list
	 * @return
	 */
	private String getRecordFromOtherServers(List<String> list) {
		DatagramSocket aSocket = null;
		String result = "";
		try {
			// create socket
			aSocket = new DatagramSocket();
			InetAddress aHost = InetAddress.getByName("localhost");

			String msg;
			
			// send UDP message to remote servers and get the record count
			for (String port : list) {
				String[] split = port.split(":");
				String taskMessage = split[1].trim();
				byte[] m = taskMessage.getBytes();
				DatagramPacket request = new DatagramPacket(m,
						taskMessage.length(), aHost, Integer.parseInt(split[0]));
				aSocket.send(request);

				byte[] buffer = new byte[32];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(reply);
				
				msg = new String(reply.getData());
				result += " " + msg.trim();
			}
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null)
				aSocket.close();
		}
		return result;
	}
	
	/**
	 * 
	 * @param rec
	 * @return
	 */
	public String transfer(String record) {
		String response = "";
		String[] rec = record.split("\\|");
		// creating respective record into remote server
		if (rec[0].contains("DR"))
			response += createDRecord(rec[2], rec[3], rec[4], rec[5],rec[6],rec[7] ,rec[1]);
		else if(rec[0].contains("NR"))
			response += createNRecord(rec[2], rec[3], rec[4], rec[5],rec[6],rec[1]);

		return response;
	}

	



	

	
}
