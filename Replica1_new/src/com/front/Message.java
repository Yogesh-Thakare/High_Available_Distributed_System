package com.front;

import java.io.Serializable;

public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8222295000987513816L;
	private Long messageID;
	private int processID;
	private String message;
	private String method;
	private String badgeId;
	private String firstName;
	private String lastName;
	private String description;
	private String status;
	private String address;
	private String lastDate;
	private String lastLocation;
	private String recordId; 
	private String remoteServerName;
	private String messageType;
	/**
	 * @return the messageType
	 */
	public String getMessageType() {
		return messageType;
	}
	/**
	 * @param messageType the messageType to set
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	/**
	 * @return the messageID
	 */
	public Long getMessageID() {
		return messageID;
	}
	/**
	 * @param messageID the messageID to set
	 */
	public void setMessageID(Long messageID) {
		this.messageID = messageID;
	}
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * @return the badgeId
	 */
	public String getBadgeId() {
		return badgeId;
	}
	/**
	 * @param badgeId the badgeId to set
	 */
	public void setBadgeId(String badgeId) {
		this.badgeId = badgeId;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @return the processID
	 */
	public int getProcessID() {
		return processID;
	}
	/**
	 * @param processID the processID to set
	 */
	public void setProcessID(int processID) {
		this.processID = processID;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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
	/**
	 * @return the recordId
	 */
	public String getRecordId() {
		return recordId;
	}
	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	/**
	 * @return the remoteServerName
	 */
	public String getRemoteServerName() {
		return remoteServerName;
	}
	/**
	 * @param remoteServerName the remoteServerName to set
	 */
	public void setRemoteServerName(String remoteServerName) {
		this.remoteServerName = remoteServerName;
	}
	
	
}
