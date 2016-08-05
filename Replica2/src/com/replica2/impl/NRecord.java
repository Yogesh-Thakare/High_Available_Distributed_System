package com.replica2.impl;

import java.util.Date;

/**
 * Stores Nurse's record
 */
public class NRecord extends Practitioner
{

	String designation_field;
	String status_field;
	String statusDate_field;

	public NRecord()
	{
		super("","","");
		designation_field = "";		
		status_field = "";
		statusDate_field = "";
		
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
	public NRecord(String recordID,String firstName, String lastName, String designation, String status, String statusDate)	
	{
		super(recordID,firstName,lastName);
		this.designation_field=designation;
		this.status_field=status;
		this.statusDate_field=statusDate;
	}

	/**
	 * Get destination
	 * @return
	 */
	public String getDesignationMethod() 
	{
		return designation_field;
	}


	/**
	 * Set destination
	 * @param designation
	 */
	public void setDesignationMethod(String designation) 
	{
		this.designation_field = designation;
	}


	/**
	 * Get status method
	 * @return
	 */
	public String getStatusMethod() 
	{
		return status_field;
	}


	/**
	 * Set status method
	 * @param status
	 */
	public void setStatusMethod(String status) 
	{
		this.status_field = status;
	}


	/**
	 * Get status date
	 * @return
	 */
	public String getStatusDateMethod() 
	{
		return statusDate_field;
	}

	/**
	 * set status date
	 * @param statusDate
	 */
	public void setStatusDateMethod(String statusDate) 
	{
		this.statusDate_field = statusDate;
	}
}
