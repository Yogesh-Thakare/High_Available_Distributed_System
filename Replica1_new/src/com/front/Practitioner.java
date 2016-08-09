package com.front;

import java.io.Serializable;

/**
 * @author Gaurav
 */
public class Practitioner implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5928591293884793394L;
    private String recordID;
    private String firstName;
    private String lastName;
    private String status;

    /**
     * @return the recordID
     */
    public String getRecordID() {
        return recordID;
    }

    /**
     * @param recordID the recordID to set
     */
    public void setRecordID(String recordID) {
        this.recordID = recordID;
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

}
