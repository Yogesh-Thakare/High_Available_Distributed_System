package com.Replica3.impl;

/**
 * Doctor Record class
 * @author Gagandeep
 */
public class DRecord extends DNRecord
{
	String address;
	String phone;
	String specialization;
	String location;
	
	/**
	 * Constructor
	 */
	public DRecord()
	{
		super("","","");
		address = "";		
		phone = "";
		specialization = "";
		location = "";
		
	}

	/**
	 * Constructor
	 */
	public DRecord(String recordID,
					String firstName, 
					String lastName, 
					String address, 
					String phone, 
					String specialization,
					String location) 
	{	
		super(recordID,firstName,lastName);
		this.firstName=firstName;
		this.lastName=lastName;
		this.address=address;
		this.phone=phone;
		this.specialization=specialization;
		this.location=location;
	}

	/**
	 * Method to get address
	 */
	public String getAddress() 
	{
		return address;
	}

	/**
	 * Method to set address
	 */
	public void setAddress(String add) 
	{
		this.address = add;
	}

	/**
	 * Method to get Phone
	 */
	public String getPhone() 
	{
		return phone;
	}

	/**
	 * Method to set Phone
	 */
	public void setPhone(String phone) 
	{
		this.phone = phone;
	}

	/**
	 * Method to get Specialization
	 */
	public String getSpecialization() 
	{
		return specialization;
	}

	/**
	 * Method to set Specialization
	 */
	public void setSpecialization(String specialization) 
	{
		this.specialization = specialization;
	}

	/**
	 * Method to get Location
	 */
	public String getLocation() 
	{
		return location;
	}

	/**
	 * Method to set Location
	 */
	public void setLocation(String loc) 
	{
		this.location = loc;
	}
}
