package com.front;

public class DoctorRecords extends Practitioner{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7499842809869530877L;
	private String address;
	private String lastDate;
	private String lastLocation;
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the lastDate
	 */
	public String getLastDate() {
		return lastDate;
	}
	/**
	 * @param lastDate the lastDate to set
	 */
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
	/**
	 * @return the lastLocation
	 */
	public String getLastLocation() {
		return lastLocation;
	}
	/**
	 * @param lastLocation the lastLocation to set
	 */
	public void setLastLocation(String lastLocation) {
		this.lastLocation = lastLocation;
	}
	
}
