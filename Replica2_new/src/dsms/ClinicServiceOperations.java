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
    public String createNRecord(String firstName, String lastName, String description, String status, String badgeID);

    /**
     * Operation createDRecord
     */
    public String createDRecord(String firstName, String lastName, String address, String lastDate, String lastLocation, String status, String badgeID);

    /**
     * Operation getRecordCounts
     */
    public String getRecordCounts(String badgeID);

    /**
     * Operation editCRecord
     */
    public String editCRecord(String lastName, String recordID, String newStatus, String badgeID);

    /**
     * Operation transferRecord
     */
    public String transferRecord(String badgeId, String recordId, String remoteServerName);

}
