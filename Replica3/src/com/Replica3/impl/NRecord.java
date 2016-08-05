package com.Replica3.impl;

/**
 * Nurse record class 
 * @author Gagandeep
 */
public class NRecord extends DNRecord
{
	String designation;
	String status;
	String statusDate;

	/**
	 * Constructor
	 */
	public NRecord()
	{
		super("","","");
		designation = "";		
		status = "";
		statusDate = "";
		
	}
	
	/**
	 * Constructor
	 */
	public NRecord(String recordID,String firstName, String lastName, String designation, String status, String statusDate)	
	{
		super(recordID,firstName,lastName);
		this.designation=designation;
		this.status=status;
		this.statusDate=statusDate;
	}

	/**
	 * Method to get Nurse destination
	 */
	public String getDesignation() 
	{
		return designation;
	}

	/**
	 * Method to set Nurse destination
	 */
	public void setDesignation(String des) 
	{
		this.designation = des;
	}

	/**
	 * Method to get status
	 */
	public String getStatus() 
	{
		return status;
	}

	/**
	 * Method to set status
	 */
	public void setStatus(String sta) 
	{
		this.status = sta;
	}

	/**
	 * Method to get status date
	 */
	public String getStatusDate() 
	{
		return statusDate;
	}

	/**
	 * Method to set status date
	 */
	public void setStatusDate(String stDt) 
	{
		this.statusDate = stDt;
	}
}
