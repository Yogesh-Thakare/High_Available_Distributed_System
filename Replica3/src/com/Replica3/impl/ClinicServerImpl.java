package com.Replica3.impl;

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
import dsms.IClinicPOA;

/**
 * Main Server class with all the method implementation
 * @author Gagandeep
 */
public class ClinicServerImpl extends IClinicPOA 
{
	private HashMap<String, List<DNRecord>> DNRecordHashMap = new HashMap<String, List<DNRecord>>();
	
	private static int drRecordID = 11111;
	private static int nrRecordID = 11111;
	//private final Object lock = new java.lang.Object();
	
	private UDPThread udpThread;

	/**
	 * Constructor to start UDP
	 */
	protected ClinicServerImpl(int port) 
	{
		super();
		udpThread = new UDPThread(port, this);
		udpThread.start();
	}
	
	/**
	 * Method to create Doctor record
	 */
	@Override
	public String createDRecord(String firstName, 
								String lastName,
								String address, 
								String phone, 
								String specialization,
								String location, 
								String clientManagerID) 
	{
		String record = "DR";
		String result = "";
		String key = "";
		int ID;
		
		DRecord objRecord = new DRecord();
		
		key = lastName.substring(0, 1);	
		ID = getDRId();
		
		objRecord.setRecordID(record + ID);
		objRecord.setFirstName(firstName);
		objRecord.setLastName(lastName);
		objRecord.setAddress(address);
		objRecord.setSpecialization(specialization);
		objRecord.setLocation(location);

		//Insert record in Hash Map with key
		insertRecord(key, objRecord);

		result = "Doctor record created Successfully.";
		logInfo(clientManagerID, record + ID, "Doctor record created Successfully.", result, location.toString());
		return result;
	}

	/**
	 *Method to create Nurse record
	 */
	@Override
	public String createNRecord(String firstName, 
								String lastName,
								String designation, 
								String status, 
								String statusDate,
								String clientManagerID) 
	{	
		String record = "NR";
		String result = "";
		String key = "";
		int ID;
		
		NRecord objRecord = new NRecord();
		
		key = lastName.substring(0, 1);	
		ID = getNRId();
		
		objRecord.setRecordID(record + ID);
		objRecord.setFirstName(firstName);
		objRecord.setLastName(lastName);
		objRecord.setDesignation(designation);
		objRecord.setStatus(statusDate);
		objRecord.setStatusDate(statusDate);
		
		//Insert record in Hash Map with key
		insertRecord(key, objRecord);

		result = "Nurse record created Successfully.";
		logInfo(clientManagerID, record + ID, "Nurse record created Successfully.", result, status.toString());
		return result;
	}
	
	/**
	 * Method to get the record counts
	 */
	@Override
	public String getRecordCounts(String recordType, String clientManagerID) 
	{
		String clinicName = clientManagerID.replaceAll("[0-9]", "");
		String result = "";

		//get counts from local server
		result += this.getRecordCount(clinicName);

		//get count from other running servers
		result += " "+ getRecordFromOtherServers(getRemoteServerPorts(clinicName, "getcount", null));
		logInfo(clientManagerID, null, "Get Record Counts", result, null);
		return result;
	}
	
	/**
	 * Method to edit the record based on input parameter
	 */
	@Override
	public String editRecord(String recordID, 
							 String fieldName,
							 String newValue, 
							 String clientManagerID) 
	{
		String result = "";
		
		if(recordID.startsWith("DR")&&
		   fieldName.equalsIgnoreCase("location") && 
		   !(newValue.equalsIgnoreCase("mtl")||
		   newValue.equalsIgnoreCase("lvl")||
		   newValue.equalsIgnoreCase("ddo")))
		{
			result = "Invalid field: "+fieldName;
			logInfo(clientManagerID, recordID, "Cannot update! Invalid field", result, newValue);
		}
		else if(recordID.startsWith("NR")&& 
				fieldName.equalsIgnoreCase("designation") && 
				!(newValue.equalsIgnoreCase("junior")|| 
				newValue.equalsIgnoreCase("senior")))
		{
			result = "Invalid field: "+fieldName;
			logInfo(clientManagerID, recordID, "Cannot update! Invalid field", result, newValue);
		}
		else if(recordID.startsWith("NR")&& 
				fieldName.equalsIgnoreCase("status") && 
				!(newValue.equalsIgnoreCase("terminated")|| 
				newValue.equalsIgnoreCase("active")))
		{
			result = "Invalid field: "+fieldName;
			logInfo(clientManagerID, recordID, "Cannot update! Invalid field", result, newValue);
		}
		else if(fieldName.equalsIgnoreCase("address")||
				fieldName.equalsIgnoreCase("phone")||
				fieldName.equalsIgnoreCase("location")||
				fieldName.equalsIgnoreCase("designation")||
				fieldName.equalsIgnoreCase("status")||
				fieldName.equalsIgnoreCase("statusDate"))
		{
			DNRecord objDNRecord = null;

			boolean recordFound = false;
			
			for (Map.Entry<String,List<DNRecord>> entry : DNRecordHashMap.entrySet())
		    {
			    List<DNRecord> entryList = entry.getValue();
			    
			    synchronized(entryList) 
			    {
			    	for(DNRecord record:entryList)
			    	{
			    		if(recordID.startsWith("DR")&& record instanceof DRecord)
			    		{
			    			objDNRecord=record;
			    			
			    			if(recordID.equals(record.getRecordID()))
			    			{
			    				if(fieldName.equalsIgnoreCase("address"))
			    				{
			    					((DRecord)record).setAddress(newValue); 
			    					 recordFound=true; 
			    					 break;
			    				}
			    				
			    				if(fieldName.equalsIgnoreCase("phone"))
			    				{
			    					((DRecord)record).setPhone(newValue);
			    					 recordFound=true;
			    					 break;
			    				}
			    				
			    				if(fieldName.equalsIgnoreCase("location"))
			    				{
			    					((DRecord)record).setLocation(newValue);
			    					 recordFound=true; 
			    					 break;
			    				}
			    			}
			    		}
			    		else if(recordID.startsWith("NR")&& record instanceof NRecord)
			    		{
			    			objDNRecord=record;
			    			
			    			if(recordID.equals(record.getRecordID()))
			    			{
			    				if(fieldName.equalsIgnoreCase("designation"))
			    				{
			    					((NRecord)record).setDesignation(newValue);
			    					 recordFound=true; 
			    					 break;
			    				}
			    				
			    				if(fieldName.equalsIgnoreCase("status"))
			    				{
			    					((NRecord)record).setStatus(newValue);
			    					 recordFound=true; 
			    					 break;
			    				}
			    				
			    				if(fieldName.equalsIgnoreCase("statusDate"))
			    				{
			    					((NRecord)record).setStatusDate(newValue);
			    					 recordFound=true; 
			    					 break;
			    				}
			    			}
			    		}
			    	}
		        }
			    	if(recordFound) break;
		        }
			if(objDNRecord instanceof DRecord)
			{
				result = "Doctor record updated successfully";
			}
				
			if(objDNRecord instanceof NRecord)
			{
				result = "Nurse record updated successfully";
			}	
		}
		else
		{
			result = "Cannot update record: " + recordID + " Invalid field: " + fieldName;
		}
		logInfo(clientManagerID, recordID, "Edit Record", result, newValue);
		return result;
	}

	/**
	 * Method to transfer record from one server to another
	 */
	@Override
	public String transferRecord(String managerId, String recordId,	String remoteServerName) 
	{
		String response = "";
		Boolean found = false;
		Set<String> keySet = DNRecordHashMap.keySet();
		
		for (String key : keySet) 
		{
			List<DNRecord> list = DNRecordHashMap.get(key);
			synchronized (list) 
			{
				for (DNRecord r : list) 
				{
					if (recordId.equalsIgnoreCase(r.getRecordID())) 
					{
						String record = constructRecord(r, remoteServerName);
						if (record.length() != 0) 
						{
							getRecordFromOtherServers(getRemoteServerPorts(remoteServerName,"transfer",record));
							deleteRecord(recordId, keySet);
						}
						found = true;
						break;
					} 
					else 
					{
						found = false;
					}
				}
			}
		}
		
		//Delete the record when found
		if (!found)
		{
			response += "Record Not Found";
		}		
		else 
		{
			response += "Record deleted";
		}
		logInfo(managerId, recordId, "Transfer Record", response,"");
		return response;
	}

	/**
	 * Method to delete record after transfer
	 */
	private void deleteRecord(String recordId, Set<String> keySet) 
	{
		for (String key : keySet) 
		{
			List<DNRecord> listDNRecord = DNRecordHashMap.get(key);
			Iterator<DNRecord> iterator = listDNRecord.iterator();
			
			while (iterator.hasNext()) 
			{
				DNRecord nextRecord = iterator.next();
				if (recordId.equalsIgnoreCase(nextRecord.getRecordID())) 
				{
					iterator.remove();
					break;
				}
			}
			DNRecordHashMap.put(key, listDNRecord);
		}
	}

	/**
	 * Method to create record as a stream of string to transfer
	 */
	private String constructRecord(DNRecord r, String clientManagerId) 
	{
		String recordID = r.getRecordID();
		StringBuilder record = new StringBuilder();
		record.append(recordID).append("|").append(clientManagerId).append("|").append(r.getFirstName()).append("|").append(r.getLastName()).append("|");
		
		if (recordID.contains("DR")) 
		{
			DRecord objD = (DRecord) r;
			record.append(objD.getAddress()).append("|").append(objD.getPhone()).append("|").append(objD.getSpecialization()).append("|").append(objD.getLocation());
		}
		
		if (recordID.contains("NR")) 
		{
			NRecord objN = (NRecord) r;
			record.append(objN.getDesignation()).append("|").append(objN.getStatus()).append("|").append(objN.getStatusDate());
		}
		
		return record.toString();
	}

	/**
	 * Method to get Doctor record ID
	 */
	private synchronized int getDRId() 
	{
		int id = drRecordID;
		drRecordID++;
		return id;
	}

	/**
	 * Method to get Nurse record ID
	 */
	private synchronized int getNRId() 
	{
		int id = nrRecordID;
		nrRecordID++;
		return id;
	}

	/**
	 * Method to insert record in Hash Map
	 */
	private void insertRecord(String key, DNRecord record) 
	{
		List<DNRecord> list = new ArrayList<DNRecord>();
		
		synchronized (list) 
		{
			if (DNRecordHashMap.containsKey(key)) 
			{
				list = DNRecordHashMap.get(key);
				list.add(record);
			} 
			else 
			{
				list.add(record);
			}
			DNRecordHashMap.put(key, list);
		}
	}

	/**
	 * Method to get server ports for get counts and transfer record
	 */
	private List<String> getRemoteServerPorts(String server, String operation, String record) 
	{
		List<String> list = new ArrayList<String>();
		
		if("transfer".equalsIgnoreCase(operation))
		{
			if ("MTL".equalsIgnoreCase(server)) 
			{
				list.add(Constant.MTL_UDP_PORT + ":"+record);
			} 
			else if ("LVL".equalsIgnoreCase(server)) 
			{
				list.add(Constant.LVL_UDP_PORT + ":"+record);
			} 
			else if ("DDO".equalsIgnoreCase(server)) 
			{
				list.add(Constant.DDO_UDP_PORT + ":"+record);
			}
		}
		else if("getcount".equalsIgnoreCase(operation))
		{
			if ("MTL".equalsIgnoreCase(server)) 
			{
				list.add(Constant.DDO_UDP_PORT + ":DDO");
				list.add(Constant.LVL_UDP_PORT + ":LVL");
			}
			else if ("LVL".equalsIgnoreCase(server)) 
			{
				list.add(Constant.MTL_UDP_PORT + ":MTL");
				list.add(Constant.DDO_UDP_PORT + ":DDO");
			} 
			else if ("DDO".equalsIgnoreCase(server)) 
			{
				list.add(Constant.MTL_UDP_PORT + ":MTL");
				list.add(Constant.LVL_UDP_PORT + ":LVL");
			}
		}
		return list;
	}
	
	/**
	 * Method to create record for transfer record
	 */
	public String transfer(String record) 
	{
		String response = "";
		String[] rec = record.split("\\|");

		if (rec[0].contains("DR"))
		{
			response += createDRecord(rec[2], rec[3], rec[4], rec[5],rec[6],rec[7] ,rec[1]);
		}
		else if(rec[0].contains("NR"))
		{
			response += createNRecord(rec[2], rec[3], rec[4], rec[5],rec[6],rec[1]);
		}

		return response;
	}
	
	/**
	 * Method to get record count
	 */
	public synchronized String getRecordCount(String operartion) 
	{
		int size = 0;
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss a");
		Date date = new Date();
		
		Set<String> keySet = DNRecordHashMap.keySet();
		if (!keySet.isEmpty()) 
		{
			for (String key : keySet)
			{
				size += DNRecordHashMap.get(key).size();
			}
		}
		operartion += ":" + size;
		operartion += " (" + dateFormat.format(date) + ")";
		return operartion;
	}

	/**
	 * Method to get record from servers
	 */
	private String getRecordFromOtherServers(List<String> list) 
	{
		DatagramSocket aSocket = null;
		String result = "";
		
		try 
		{
			aSocket = new DatagramSocket();
			InetAddress aHost = InetAddress.getByName("localhost");

			String msg;
			
			for (String port : list) 
			{
				String[] split = port.split(":");
				String taskMessage = split[1].trim();
				byte[] m = taskMessage.getBytes();
				DatagramPacket request = new DatagramPacket(m,taskMessage.length(), aHost, Integer.parseInt(split[0]));
				aSocket.send(request);

				byte[] buffer = new byte[32];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(reply);
				
				msg = new String(reply.getData());
				result += " " + msg.trim();
			}
		} 
		catch (SocketException e) 
		{
			System.out.println("Socket Exception: " + e.getMessage());
		} 
		catch (IOException e) 
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
		return result;
	}
	
	/**
	 * Method to log information in text file
	 */
	private synchronized void logInfo(String ID, 
									  String recordID,
									  String operation, 
									  String response, 
									  String newStatus) 
	{
		File file;
		BufferedWriter bwriter = null;
		LineNumberReader line = null;
		int lineNumber = 0;
		String server = ID.replaceAll("[0-9]", "").trim();
		
		try 
		{
			file = new File(server + "_log.txt");
			
			if (!file.exists()) 
			{
				//Create new file if file dosen't exist
				file.createNewFile();
				FileWriter fw = new FileWriter(file.getName());
				bwriter = new BufferedWriter(fw);
				String header = "No.\t" + "Manager ID\t" + "Record ID\t"
						+ "Status\t" + "Operation\t\t" + "Time\t\t"
						+ "Operation Status";
				bwriter.write(header);
				bwriter.close();
			}

			FileWriter fw = new FileWriter(file.getName(), true);
			bwriter = new BufferedWriter(fw);
			line = new LineNumberReader(new FileReader(file));
			
			while (null != line.readLine())
			{
				lineNumber = line.getLineNumber();
			}
			line.close();

			bwriter.newLine();
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss a");
			Date date = new Date();
			
			String data = lineNumber + "\t" + ID + "\t"
					+ ((null == recordID) ? "-------" : recordID) + "\t\t"
					+ ((null == newStatus) ? "-------" : newStatus) + "\t"
					+ operation + "\t" + dateFormat.format(date) + "\t"
					+ response;
			bwriter.write(data);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		finally 
		{
			try 
			{
				if (bwriter != null)
					bwriter.close();
				if (line != null)
					line.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
