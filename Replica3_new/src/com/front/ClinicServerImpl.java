package com.front;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.ORB;

import dsms.ClinicService;
import dsms.ClinicServiceHelper;
import dsms.ClinicServicePOA;

/**
 * Implementation class of the server interface
 *
 * @author Gagan
 */
public class ClinicServerImpl extends ClinicServicePOA {

    private HashMap<String, List<Practitioner>> practitionerRecord = new HashMap<String, List<Practitioner>>();
    private static int nrRecordID = 11111;
    private static int drRecordID = 11111;
    private final Object lock = new java.lang.Object();

    private UDPThread udpThread;

    /**
     * Initializes the Server object
     *
     * @param portNum
     */
    protected ClinicServerImpl(int portNum) {
        super();
        udpThread = new UDPThread(portNum, this);
        udpThread.start();
    }

    /**
     * Interface method implementation for creating a new nurse record (NR)
     */
    @Override
    public String createNRecord(String firstName, String lastName,
            String description, String status, String managerID) {

        String record = "NR", response = "";
        String key = lastName.substring(0, 1);

        // set the details received from client
        NurseRecords cd = new NurseRecords();
        int id = getNRId();
        cd.setRecordID(record + id);
        cd.setFirstName(firstName);
        cd.setLastName(lastName);
        cd.setCrimeDescription(description);
        cd.setStatus(status);

        // adding record to list and then to Hashtable
        addRecord(key, cd);

        response = "SUCCESS: Nurse Record Created";
        updateServerLog(managerID, record + id, "Created Nurse Record",
                response, status.toString());
        return response;
    }

    /**
     * Interface method implementation for creating new doctor record (DR)
     */
    @Override
    public String createDRecord(String firstName, String lastName,
            String address, String lastDate, String lastLocation,
            String status, String managerID) {

        String record = "DR", response = "";
        String key = lastName.substring(0, 1);

        // set the details received from client
        DoctorRecords cd = new DoctorRecords();
        int id = getDRId();
        cd.setRecordID(record + id);
        cd.setFirstName(firstName);
        cd.setLastName(lastName);
        cd.setAddress(address);
        cd.setLastDate(lastDate);
        cd.setLastLocation(lastLocation);
        cd.setStatus(status);

        // adding record to list and then to Hashtable
        addRecord(key, cd);

        response = "SUCCESS: Missing Record Created";
        updateServerLog(managerID, record + id, "Created Missing Record",
                response, status.toString());
        return response;
    }

    /**
     * Interface method implementation for getting records
     */
    @Override
    public String getRecordCounts(String managerID) {
        // extracting server name from badgeId
        String clinicName = managerID.replaceAll("[0-9]", "");
        String result = "";

        // getting local server record count
        result += this.getRecordCount(clinicName);

        // getting record count from other remote servers
        result += " "
                + getRecordFromOtherServers(getRemoteServerPorts(clinicName, "getcount", null));
        updateServerLog(managerID, null, "Get Record Count", result, null);
        return result;
    }

    /**
     * Interface method implementation for editing an existing record
     */
    @Override
    public String editCRecord(String lastName, String recordID,
            String newStatus, String managerID) {
        String key = lastName.substring(0, 1);
        String result = "", newstatus = "";

        if (practitionerRecord.containsKey(key)) {

            List<Practitioner> list = practitionerRecord.get(key);

            for (Practitioner cd : list) {
                // synchronizing at record level so multiple threads cannot update
                // the record simultaneously
                synchronized (lock) {

                    // checking if the record already exists 
                    if (recordID.equalsIgnoreCase(cd.getRecordID())
                            && lastName.equalsIgnoreCase(cd.getLastName())) {

                        // check for the same record status
                        if (newStatus.toString().equalsIgnoreCase(
                                cd.getStatus().toString())) {
                            newstatus = cd.getStatus().toString();
                            result = "FAILED: Record already has the same status";
                        } else {
                            // updating the record with new status
                            cd.setStatus(newStatus);
                            int indexOf = list.indexOf(cd);
                            list.set(indexOf, cd);
                            newstatus = newStatus.toString();
                            result = "SUCCESS: Record updated";
                        }
                    } else {
                        result = "FAILED:: Record does not exist";
                    }
                }
            }
            // putting updated list back to Hashtable
            practitionerRecord.put(key, list);
        } else {
            result = "FAILED: Record does not exist";
        }
        updateServerLog(managerID, recordID, "Edit Record Status", result,
                newstatus);
        return result;
    }

    /**
     * Interface method implementation that transfers a record to another
     * location
     */
    @Override
    public String transferRecord(String managerID, String recordId,
            String remoteServerName) {
        String response = "";
        Boolean found = false;
        Set<String> keySet = practitionerRecord.keySet();

        // iterating each key then checking in respective list if record exist 
        // if exists, transfer to specified remote loaction
        for (String key : keySet) {
            List<Practitioner> list = practitionerRecord.get(key);
            for (Practitioner r : list) {
                synchronized (lock) {
                    if (recordId.equalsIgnoreCase(r.getRecordID())) {
                        String record = constructRecord(r, remoteServerName);
                        if (record.length() != 0) {
                            getRecordFromOtherServers(getRemoteServerPorts(remoteServerName, "transfer", record));
                            deleteRecord(recordId, keySet);
                        }
                        found = true;
                        break;
                    } else {
                        found = false;
                    }
                }
            }
        }

        // if record found in local server then after transferring delete record from local server
        if (!found) {
            response += "Record Not Found!";
        } else {
            //deleteRecord(recordId, keySet);
            response += "Record deleted and transferred!";
        }
        updateServerLog(managerID, recordId, "Transfer Record", response,
                "-------");
        return response;
    }

    /**
     * Deletes an existing record after it has been transferred to another
     * location
     *
     * @param recordId
     * @param keySet
     */
    private void deleteRecord(String recordId, Set<String> keySet) {

        // checking for record exist in HashMap of local server
        for (String key : keySet) {
            List<Practitioner> list = practitionerRecord.get(key);
            Iterator<Practitioner> iterator = list.iterator();
            while (iterator.hasNext()) {
                Practitioner next = iterator.next();
                if (recordId.equalsIgnoreCase(next.getRecordID())) {
                    iterator.remove();
                    break;
                }
            }

            // after deleting record from list, putting updated list to Hashtable
            practitionerRecord.put(key, list);
        }
    }

    /**
     *
     * Converts a record into array of strings
     *
     * @param r
     * @param managerID
     * @return array of strings
     */
    private String constructRecord(Practitioner r, String managerID) {
        String recordID = r.getRecordID();
        StringBuilder record = new StringBuilder();
        record.append(recordID).append("|").append(managerID).append("|").append(r.getFirstName()).append("|").append(r.getLastName()).append("|").append(r.getStatus()).append("|");
        if (recordID.contains("NR")) {
            NurseRecords cr = (NurseRecords) r;
            record.append(cr.getCrimeDescription());
        }
        if (recordID.contains("DR")) {
            DoctorRecords mr = (DoctorRecords) r;
            record.append(mr.getAddress()).append("|").append(mr.getLastDate()).append("|").append(mr.getLastLocation());
        }
        return record.toString();

    }

    /**
     * Gets remote object reference of specified location based on clinic name
     *
     * @param clinicName
     * @return object reference of specified server name
     * @throws IOException
     */
    private ClinicService getRemoteServerObject(String clinicName)
            throws IOException {
        String[] args = null;

        // initializing ORB
        ORB orb = ORB.init(args, null);

        // get the current filepath
        String property = System.getProperty("user.dir");
        property += File.separator + "contact" + File.separator + clinicName
                + "_contact.txt";

        // read the contact details from respective IOR file based on server
        BufferedReader br = new BufferedReader(new FileReader(property));
        String ior = br.readLine();
        br.close();

        org.omg.CORBA.Object obj = orb.string_to_object(ior);

        ClinicService serverObj = ClinicServiceHelper.narrow(obj);

        return serverObj;

    }

    /**
     * Synchronized method for getting unique nurse recordID
     *
     * @return
     */
    private synchronized int getNRId() {
        int id = nrRecordID;
        nrRecordID++;
        return id;
    }

    /**
     * Synchronized method for getting unique doctor recordID
     *
     * @return
     */
    private synchronized int getDRId() {
        int id = drRecordID;
        drRecordID++;
        return id;
    }

    /**
     * Method for adding record (doctor or nurse) to Hashtable
     *
     * @param key
     * @param cd
     */
    private void addRecord(String key, Practitioner cd) {
        List<Practitioner> list = new ArrayList<Practitioner>();

        // check for the record, if exist fetch the list and then add otherwise create new list an dadd
        synchronized (list) {
            if (practitionerRecord.containsKey(key)) {
                list = practitionerRecord.get(key);
                list.add(cd);
            } else {
                list.add(cd);
            }
            practitionerRecord.put(key, list);
        }
    }

    /**
     * Gets remote server's UDP port numbers
     *
     * @param clinicName
     * @return list of other servers port numbers
     */
    private List<String> getRemoteServerPorts(String clinicName, String operation, String record) {
        List<String> list = new ArrayList<String>();
        if ("transfer".equalsIgnoreCase(operation)) {
            if ("SPVM".equalsIgnoreCase(clinicName)) {
                list.add(Constants.MTL_UDP_PORT + ":" + record);
            } else if ("SPL".equalsIgnoreCase(clinicName)) {
                list.add(Constants.LVL_UDP_PORT + ":" + record);
            } else if ("SPB".equalsIgnoreCase(clinicName)) {
                list.add(Constants.DDO_UDP_PORT + ":" + record);
            }
        } else if ("getcount".equalsIgnoreCase(operation)) {
            // set the UDP port number for other servers expect own for UDP communication between servers
            if ("SPVM".equalsIgnoreCase(clinicName)) {
                list.add(Constants.DDO_UDP_PORT + ":SPB");
                list.add(Constants.LVL_UDP_PORT + ":SPL");
            } else if ("SPL".equalsIgnoreCase(clinicName)) {
                list.add(Constants.MTL_UDP_PORT + ":SPVM");
                list.add(Constants.DDO_UDP_PORT + ":SPB");
            } else if ("SPB".equalsIgnoreCase(clinicName)) {
                list.add(Constants.MTL_UDP_PORT + ":SPVM");
                list.add(Constants.LVL_UDP_PORT + ":SPL");
            }
        }
        return list;
    }

    /**
     * Generates and updates location wise log files
     *
     * @param managerID
     * @param recordID
     * @param operation
     * @param response
     * @param newStatus
     */
    private synchronized void updateServerLog(String managerID, String recordID,
            String operation, String response, String newStatus) {
        File file;
        BufferedWriter bw = null;
        LineNumberReader lnr = null;
        int lineNumber = 0;
        String stationName = managerID.replaceAll("[0-9]", "").trim();
        try {
            file = new File(stationName + "_log.txt");

            // check for file exists, if not create new file and header
            if (!file.exists()) {
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getName());
                bw = new BufferedWriter(fw);
                String header = "No.\t" + "Badge ID\t" + "Record ID\t"
                        + "Status\t" + "Operation\t\t" + "Time\t\t"
                        + "Transaction Status";
                bw.write(header);
                bw.close();
            }

            // if exist, get line number and add rows
            FileWriter fw = new FileWriter(file.getName(), true);
            bw = new BufferedWriter(fw);
            lnr = new LineNumberReader(new FileReader(file));
            while (null != lnr.readLine()) {
                lineNumber = lnr.getLineNumber();
            }
            lnr.close();

            bw.newLine();
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss a");
            Date date = new Date();

            // write the transaction details of performed operation on server
            String data = lineNumber + "\t" + managerID + "\t"
                    + ((null == recordID) ? "-------" : recordID) + "\t\t"
                    + ((null == newStatus) ? "-------" : newStatus) + "\t"
                    + operation + "\t" + dateFormat.format(date) + "\t"
                    + response;
            bw.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (lnr != null) {
                    lnr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Counts the number of records that exist in Hashtable at particular time
     *
     * @param task
     * @return
     */
    public synchronized String getRecordCount(String task) {
        int size = 0;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss a");
        Date date = new Date();

        // get the count of all record stored in list for all keys
        Set<String> keySet = practitionerRecord.keySet();
        if (!keySet.isEmpty()) {
            for (String key : keySet) {
                size += practitionerRecord.get(key).size();
            }
        }
        task += ":" + size;
        task += " (" + dateFormat.format(date) + ")";
        return task;
    }

    /**
     * Gets record count from remote servers through UDP communication
     *
     * @param list
     * @return
     */
    private String getRecordFromOtherServers(List<String> list) {
        DatagramSocket aSocket = null;
        String result = "";
        try {
            // create socket
            aSocket = new DatagramSocket();
            InetAddress aHost = InetAddress.getByName("localhost");

            String msg;

            // send UDP message to remote servers and get the record count
            for (String port : list) {
                String[] split = port.split(":");
                String taskMessage = split[1].trim();
                byte[] m = taskMessage.getBytes();
                DatagramPacket request = new DatagramPacket(m,
                        taskMessage.length(), aHost, Integer.parseInt(split[0]));
                aSocket.send(request);

                byte[] buffer = new byte[32];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(reply);

                msg = new String(reply.getData());
                result += " " + msg.trim();
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null) {
                aSocket.close();
            }
        }
        return result;
    }

    /**
     *
     * @param rec
     * @return
     */
    public String transfer(String record) {
        String response = "";
        String[] rec = record.split("\\|");
        // creating respective record into remote server
        if (rec[0].contains("NR")) {
            response += createNRecord(rec[2], rec[3], rec[5], rec[4], rec[1]);
        } else if (rec[0].contains("DR")) {
            response += createDRecord(rec[2], rec[3], rec[5], rec[6], rec[7],
                    rec[4], rec[1]);
        }

        return response;
    }
}
