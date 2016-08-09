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
    public String createNRecord(String firstName, String lastName, String designation, String status, String managerID);

    /**
     * Operation createDRecord
     */
    public String createDRecord(String firstName, String fieldName, String address, String phone, String location, String status, String managerID);

    /**
     * Operation getRecordCounts
     */
    public String getRecordCounts(String managerID);

    /**
     * Operation editRecord
     */
    public String editRecord(String lastName, String recordID, String newValue, String managerID);

    /**
     * Operation transferRecord
     */
    public String transferRecord(String managerId, String recordId, String remoteServerName);

}
