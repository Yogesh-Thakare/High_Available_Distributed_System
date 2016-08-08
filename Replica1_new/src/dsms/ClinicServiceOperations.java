package dsms;

/**
 * Interface definition: ClinicService.
 * 
 * @author OpenORB Compiler
 */
public interface ClinicServiceOperations
{
    /**
     * Operation createNRecord
     */
    public String createNRecord(String firstName, String lastName, String description, String status, String managerID);

    /**
     * Operation createDRecord
     */
    public String createDRecord(String firstName, String lastName, String address, String lastDate, String lastLocation, String status, String managerID);

    /**
     * Operation getRecordCounts
     */
    public String getRecordCounts(String managerID);

    /**
     * Operation editCRecord
     */
    public String editCRecord(String lastName, String recordID, String newStatus, String managerID);

    /**
     * Operation transferRecord
     */
    public String transferRecord(String managerId, String recordId, String remoteServerName);

}
