package com.client;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.omg.CORBA.ORB;

import FrontEnd.FrontEndInterface;
import FrontEnd.FrontEndInterfaceHelper;


/**
 *
 * @author Abhinoor Singh Pannu
 */
public class ManagerClient extends Thread {

    private String testMode;
    private String managerID;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String specialization;
    private String location;
    private String designation;
    private String status;
    private String statusDate;
    private String recordID;
    private String fieldName;
    private String newValue;
    private String remoteClinicServerName;
    private static int userChoice;

    /**
     * constructor for initializing new object
     *
     * @param testMode
     * @param managerID
     * @param recordID
     * @param firstName
     * @param lastName
     * @param phone
     * @param specialization
     * @param status
     * @param location
     * @param designation
     * @param address
     * @param fieldName
     * @param statusDate
     * @param newValue
     * @param remoteClinicServerName
     */
    public ManagerClient(String testMode, String managerID, String firstName, String lastName, String address, String phone, String specialization, String location, String designation, String status, String statusDate, String recordID, String fieldName, String newValue, String remoteClinicServerName) {
        super();
        this.testMode = testMode;
        this.managerID = managerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.specialization = specialization;
        this.location = location;
        this.designation = designation;
        this.status = status;
        this.statusDate = statusDate;
        this.recordID = recordID;
        this.fieldName = fieldName;
        this.newValue = newValue;
        this.remoteClinicServerName = remoteClinicServerName;
        start();
    }

    public ManagerClient() {
        super();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            Scanner sc = new Scanner(System.in);
            ManagerClient temp = new ManagerClient();

            Boolean validID = false;
            while (!validID) {
                try {
                    System.out.println("Please enter Manager ID");
                    temp.managerID = sc.nextLine();

                    if ((Pattern.matches("MTL[0-9]{4}", temp.managerID) || Pattern.matches("LVL[0-9]{4}", temp.managerID) || Pattern.matches("DDO[0-9]{4}", temp.managerID)) && !(temp.managerID.equals("MTL0000") || temp.managerID.equals("LVL0000") || temp.managerID.equals("DDO0000"))) {
                        validID = true;
                    } else {
                        System.out.println("Invalid Input, please enter an integer: ");
                        validID = false;
                    }

                } catch (Exception e) {
                    System.out.println("Invalid Input, please enter an integer: ");
                    validID = false;
                }

            }

            // display client menu
            showMenu();

            FrontEndInterface serverObj = getRemoteObjectStub();
            String result;

            while (true) {
                Boolean validInput = false;

                while (!validInput) {

                    try {
                        System.out.print("Enter your choice: ");
                        //userChoice = Integer.parseInt(br.readLine());
                        userChoice = Integer.parseInt(sc.nextLine());
                        validInput = true;
                    } catch (Exception e) {
                        System.out.println("Invalid Input, please enter an integer: ");
                        validInput = false;
                    }

                    switch (userChoice) {
                        case 1: //  add doctor record
                            //temp.testMode = userChoice;
                            System.out.print("Insert First Name : ");
                            temp.firstName = sc.nextLine();
                            System.out.print("Insert Last Name : ");
                            temp.lastName = sc.nextLine();
                            System.out.print("Insert Address : ");
                            temp.address = sc.nextLine();
                            System.out.print("Insert Phone : ");
                            temp.phone = sc.nextLine();
                            System.out.print("Insert Specialization : ");
                            temp.specialization = sc.nextLine();

                            temp.location = temp.managerID.substring(0, 3);
                            System.out.println(temp.location);

                            result = serverObj.exectuteOperation("DR", temp.managerID, temp.firstName, temp.lastName, temp.address, temp.phone, temp.specialization, temp.location, "", "", "", "", "", "", "");
                            temp.updateClientLog(temp.managerID, "Add Doctor Record", result);
                            break;
                        case 2: //  add nurse record
                            //temp.testMode = userChoice;
                            System.out.print("Insert First Name : ");
                            temp.firstName = sc.nextLine();
                            System.out.print("Insert Last Name : ");
                            temp.lastName = sc.nextLine();
                            System.out.print("Insert Designation : ");
                            temp.designation = sc.nextLine();
                            System.out.print("Insert Status : ");
                            temp.status = sc.nextLine();
                            System.out.print("Insert Status Date : ");
                            temp.statusDate = sc.nextLine();

                            temp.location = temp.managerID.substring(0, 3);
                            System.out.println(temp.location);

                            result = serverObj.exectuteOperation("NR", temp.managerID, temp.firstName, temp.lastName, "", "", "", "", temp.designation, temp.status, temp.statusDate, "", "", "", "");
                            temp.updateClientLog(temp.managerID, "Add Nurse Record", result);
                            break;
                        case 3: //  get record count
                            //temp.testMode = userChoice;

                            result = serverObj.exectuteOperation("GR", temp.managerID, "", "", "", "", "", "", "", "", "", "", "", "", "");
                            temp.updateClientLog(temp.managerID, "Get Records Count", result);
                            break;
                        case 4: // edit record
                            //temp.testMode = userChoice;
                            System.out.print("Insert Record ID : ");
                            temp.recordID = sc.nextLine();
                            System.out.print("Insert Field Name : ");
                            temp.fieldName = sc.nextLine();
                            System.out.print("Insert New Value : ");
                            temp.newValue = sc.nextLine();

                            result = serverObj.exectuteOperation("ER", temp.managerID, "", "", "", "", "", "", "", "", "", temp.recordID, temp.fieldName, temp.newValue, "");
                            temp.updateClientLog(temp.managerID, "Edit Record", result);
                            break;
                        case 5: // transfer record
                            //temp.testMode = userChoice;
                            System.out.print("Insert Record ID : ");
                            temp.recordID = sc.nextLine();
                            System.out.print("Insert New Server Name : ");
                            temp.remoteClinicServerName = sc.nextLine();

                            result = serverObj.exectuteOperation("TR", temp.managerID, "", "", "", "", "", "", "", "", "", temp.recordID, "", "", temp.remoteClinicServerName);
                            temp.updateClientLog(temp.managerID, "Transfer Record", result);
                            break;
                        case 6: // concurrency test
                            //temp.testMode = userChoice;

                            new ManagerClient("DR", "MTL1111", "Yogesh_MTL1", "Thakare", "333 st marc", "514-555-6766", "surgeon", "MTL", "", "", "", "", "", "", "");
                            new ManagerClient("DR", "MTL1111", "Yogesh_MTL2", "Thakare", "333 st marc", "514-555-6766", "surgeon", "MTL", "", "", "", "", "", "", "");
                            new ManagerClient("DR", "LVL1111", "Yogesh_LVL1", "Thakare", "333 st marc", "514-555-6766", "surgeon", "LVL", "", "", "", "", "", "", "");
                            new ManagerClient("DR", "LVL1111", "Yogesh_LVL2", "Thakare", "333 st marc", "514-555-6766", "surgeon", "LVL", "", "", "", "", "", "", "");
                            new ManagerClient("DR", "DDO1111", "Yogesh_DDO1", "Thakare", "333 st marc", "514-555-6766", "surgeon", "DDO", "", "", "", "", "", "", "");
                            new ManagerClient("DR", "DDO1111", "Yogesh_DDO2", "Thakare", "333 st marc", "514-555-6766", "surgeon", "DDO", "", "", "", "", "", "", "");

                            new ManagerClient("NR", "MTL1111", "Yogesh_MTL1", "Thakare", "", "", "", "", "Senior", "Active", "2015-08-11", "", "", "", "");
                            new ManagerClient("NR", "MTL1111", "Yogesh_MTL2", "Thakare", "", "", "", "", "Junior", "Terminated", "2015-06-21", "", "", "", "");
                            new ManagerClient("NR", "LVL1111", "Yogesh_LVL1", "Thakare", "", "", "", "", "Senior", "Active", "2015-05-17", "", "", "", "");
                            new ManagerClient("NR", "DDO1111", "Yogesh_DDO1", "Thakare", "", "", "", "", "Junior", "Terminated", "2015-07-07", "", "", "", "");

                            new ManagerClient("ER", "MTL1111", "", "", "", "", "", "", "", "", "", "DR00001", "Address", "666 st marc", "");
                            new ManagerClient("ER", "LVL1111", "", "", "", "", "", "", "", "", "", "NR00001", "Status", "Terminated", "");
                            new ManagerClient("ER", "DDO1111", "", "", "", "", "", "", "", "", "", "DR00001", "Phone", "5461237895", "");

                            new ManagerClient("TR", "MTL1111", "", "", "", "", "", "", "", "", "", "DR00001", "", "", "DDO");
                            new ManagerClient("TR", "LVL1111", "", "", "", "", "", "", "", "", "", "NR00001", "", "", "DDO");

                            new ManagerClient("GR", "MTL1111", "", "", "", "", "", "", "", "", "", "", "", "", "");

                            temp.updateClientLog(temp.managerID, "Concurrency Test ", "Concurrency Test Passed");
                            break;
                        default:
                            System.out.println("Invalid Input, please try again.");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {

            String result = "";
            String stationName = managerID.replaceAll("[0-9]", "").trim();
            FrontEndInterface serverObj;

            if ((serverObj = getRemoteObjectStub()) != null) {
                switch (testMode) {
                    case "DR":
                        result = serverObj.exectuteOperation("DR", this.managerID, this.firstName, this.lastName, this.address, this.phone, this.specialization, this.location, "", "", "", "", "", "", "");
                        this.updateClientLog(this.managerID, "Add Doctor Record", result);
                        break;
                    case "NR":
                        result = serverObj.exectuteOperation("NR", this.managerID, this.firstName, this.lastName, "", "", "", "", this.designation, this.status, this.statusDate, "", "", "", "");
                        this.updateClientLog(this.managerID, "Add Nurse Record", result);
                        break;
                    case "GR":
                        result = serverObj.exectuteOperation("GR", this.managerID, "", "", "", "", "", "", "", "", "", "", "", "", "");
                        this.updateClientLog(this.managerID, "Get Records Count", result);
                        break;
                    case "ER":
                        result = serverObj.exectuteOperation("ER", this.managerID, "", "", "", "", "", "", "", "", "", this.recordID, this.fieldName, this.newValue, "");
                        this.updateClientLog(this.managerID, "Edit Record", result);
                        break;
                    case "TR":
                        result = serverObj.exectuteOperation("ER", this.managerID, "", "", "", "", "", "", "", "", "", this.recordID, this.fieldName, this.newValue, "");
                        this.updateClientLog(this.managerID, "Transfer Record", result);
                        break;
                    default:

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showMenu() {
        System.out.println("\n****Welcome to DPIS****\n");
        System.out.println("Test cases(1-6)");
        System.out.println("1. Create Doctor Record");
        System.out.println("2. Create Nurse Record");
        System.out.println("3. Get Records Count");
        System.out.println("4. Edit Record");
        System.out.println("5. Transfer Record");
        System.out.println("6. Test Concurrency");
    }

    /**
     * get the remote object reference
     *
     * @param stationName
     * @return
     * @throws IOException
     */
    private static FrontEndInterface getRemoteObjectStub() throws IOException {
        String[] args = null;

        // initialize ORB
        ORB orb = ORB.init(args, null);

        String property = System.getProperty("user.dir");
        property = property.substring(0, property.lastIndexOf(File.separator));

        // get the IOR string from contact file based on station 
        BufferedReader br = new BufferedReader(new FileReader(property + File.separator + "FrontEnd" + File.separator + "FE_contact.txt"));
        String ior = br.readLine();
        br.close();

        // Get the CORBA object from IOR
        org.omg.CORBA.Object obj = orb.string_to_object(ior);

        // Convert CORBA object to JAVA object
        FrontEndInterface serverObj = FrontEndInterfaceHelper.narrow(obj);

        return serverObj;
    }

    /**
     * method for logging the client operations and status of the performed
     * operations
     *
     * @param badgeID
     * @param operation
     * @param status
     */
    private synchronized void updateClientLog(String managerID, String operation, String status) {
        File file;
        BufferedWriter bw = null;
        String header = "";
        LineNumberReader lnr = null;
        int lineNumber = 0;
        try {
            file = new File(managerID + "_log.txt");

            // check for the files exists, if not create new with header
            if (!file.exists()) {
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getName());
                bw = new BufferedWriter(fw);
                header = "No.\t" + "OPERATION\t\t" + "TIME\t\t" + "STATUS";
                bw.write(header);
                bw.close();
            }

            // if exists, add rows to exising file
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

            // writing the performed operation to respective client file
            String data = lineNumber + "\t" + operation + "\t" + dateFormat.format(date) + "\t" + status;
            bw.write(data);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
