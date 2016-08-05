package com.replica2.impl;


/**
 * Parent class for storing object in HashMap
 */
public class Practitioner {

	String recordID_field;
	String firstName_field;
	String lastName_field;

	/**
	 * Constructor
	 * @param recordID
	 * @param firstName
	 * @param lastName
	 */
	public Practitioner(String recordID,String firstName,String lastName)
	{
		this.recordID_field=recordID;
		this.firstName_field=firstName;
		this.lastName_field=lastName;
	}

	/**
	 * Get RecordID method
	 * @return
	 */
	public String getRecordID() 
	{
		return recordID_field;
	}


	/**
	 * 
	 * Set RecordID
	 * @param recordID
	 */
	public void setRecordID(String recordID) 
	{
		this.recordID_field = recordID;
	}


	/**
	 * Get first name method
	 * @return
	 */
	public String getFirstName() 
	{
		return firstName_field;
	}

	/**
	 * Set First Name method
	 * @param firstName
	 */
	public void setFirstName(String firstName) 
	{
		this.firstName_field = firstName;
	}


	/**
	 * Get Last name method
	 * @return
	 */
	public String getLastName() 
	{
		return lastName_field;
	}


	/**
	 * Set last name method
	 * @param lastName
	 */
	public void setLastName(String lastName) 
	{
		this.lastName_field = lastName;
	}
}
