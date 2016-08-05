package com.Replica3.impl;

/**
 * DNRecord class
 * @author Gagandeep
 */
public class DNRecord 
{
	String recordID;
	String firstName;
	String lastName;
	
	/**
	 * Constructor
	 */
	public DNRecord(String recordID,String firstName,String lastName)
	{
		this.recordID = recordID;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	/**
	 * Method to get record Id
	 */
	public String getRecordID() 
	{
		return recordID;
	}
	
	/**
	 * Method to set record Id
	 */
	public void setRecordID(String ID) 
	{
		this.recordID = ID;
	}
	
	/**
	 * Method to get first name
	 */
	public String getFirstName() 
	{
		return firstName;
	}
	
	/**
	 * Method to set first name
	 */
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}
	
	/**
	 * Method to get last name
	 */
	public String getLastName() 
	{
		return lastName;
	}

	/**
	 * Method to set last name
	 */
	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}
}
