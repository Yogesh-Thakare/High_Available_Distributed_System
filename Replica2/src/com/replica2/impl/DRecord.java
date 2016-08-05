package com.replica2.impl;

/**
 * Stores Doctor's record
 */
public class DRecord extends Practitioner
{
	String address_field;
	String phone_field;
	String specialization_field;
	String location_field;
	
	public DRecord()
	{
		super("","","");
		address_field = "";		
		phone_field = "";
		specialization_field = "";
		location_field = "";
		
	}

	/**
	 * Constructor
	 * @param recordID
	 * @param firstName
	 * @param lastName
	 * @param address
	 * @param phone
	 * @param specialization
	 * @param location
	 */
	public DRecord(String recordID,String firstName, String lastName, String address, String phone, String specialization,
			String location) 
	{	
		super(recordID,firstName,lastName);
		this.firstName_field=firstName;
		this.lastName_field=lastName;
		this.address_field=address;
		this.phone_field=phone;
		this.specialization_field=specialization;
		this.location_field=location;
	}

	/** 
	 * @return address
	 */
	public String getAddressMethod() 
	{
		return address_field;
	}


	/**
	 * Set address method
	 * @param address
	 */
	public void setAddressMethod(String address) 
	{
		this.address_field = address;
	}

	/**
	 * @return phone no.
	 */
	public String getPhoneMethod() 
	{
		return phone_field;
	}

	/**
	 * Set phone no.
	 * @param phone
	 */
	public void setPhoneMethod(String phone) 
	{
		this.phone_field = phone;
	}


	/**
	 * Get specialization
	 * @return
	 */
	public String getSpecializationMethod() 
	{
		return specialization_field;
	}


	/**
	 * Set specialization method
	 * @param specialization
	 */
	public void setSpecializationMethod(String specialization) 
	{
		this.specialization_field = specialization;
	}


	/**
	 * Get location
	 * @return
	 */
	public String getLocationMethod() 
	{
		return location_field;
	}


	/**
	 * set location method
	 * @param location
	 */
	public void setLocationMethod(String location) 
	{
		this.location_field = location;
	}
}
