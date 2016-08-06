package com.front;

import java.util.Date;

/**
 * Stores Nurse's record
 */
public class NurseRecord extends Practitioner
{

	String designation;
	String status;
	String statusDate;

	public NurseRecord()
	{
		super("","","");
		designation = "";		
		status = "";
		statusDate = "";
		
	}
	/**
	 * Constructor
	 * @param recordID
	 * @param firstName
	 * @param lastName
	 * @param designation
	 * @param status
	 * @param statusDate
	 */
	public NurseRecord(String recordID,String firstName, String lastName, String designation, String status, String statusDate)	
	{
		super(recordID,firstName,lastName);
		this.designation=designation;
		this.status=status;
		this.statusDate=statusDate;
	}

	/**
	 * Get destination
	 * @return
	 */
	public String getDesignation() 
	{
		return designation;
	}


	/**
	 * Set destination
	 * @param designation
	 */
	public void setDesignation(String designation) 
	{
		this.designation = designation;
	}


	/**
	 * Get status method
	 * @return
	 */
	public String getStatus() 
	{
		return status;
	}


	/**
	 * Set status method
	 * @param status
	 */
	public void setStatus(String status) 
	{
		this.status = status;
	}


	/**
	 * Get status date
	 * @return
	 */
	public String getStatusDate() 
	{
		return statusDate;
	}

	/**
	 * set status date
	 * @param statusDate
	 */
	public void setStatusDate(String statusDate) 
	{
		this.statusDate = statusDate;
	}
}
