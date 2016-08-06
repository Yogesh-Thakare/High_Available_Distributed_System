package com.fe;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import FrontEnd.FrontEndInterfacePOA;

/**
 *
 * @author Abhinoor Singh Pannu
 */
class FrontEndInterfaceImpl extends FrontEndInterfacePOA {

    private static Long sequence = 1L;
    private Object lock = new Object();

    @Override
    public String exectuteOperation(String method, String managerID, String firstName, String lastName, String address, String phone, String specialization, String location, String designation, String status, String statusDate, String recordID, String fieldName, String newValue, String remoteClinicServerName) {
        String leaderInfo = "", result = "";
        DatagramSocket socket = null;
        try {
            synchronized (lock) {
                do {
                    leaderInfo = getLeaderInfo();
                } while (null == leaderInfo && "".equalsIgnoreCase(leaderInfo));

                Message msg = constructRequest(method, managerID, firstName,
                        lastName, address, phone, specialization, location,
                        designation, status, statusDate, recordID, fieldName, newValue, remoteClinicServerName);
                String[] split = leaderInfo.split(":");

                //serialize the message object
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput oo = new ObjectOutputStream(bos);
                oo.writeObject(msg);
                oo.close();
                socket = new DatagramSocket();
                InetAddress host = InetAddress.getByName(split[0]);
                byte[] serializedMsg = bos.toByteArray();
                DatagramPacket request = new DatagramPacket(serializedMsg,
                        serializedMsg.length, host, Integer.parseInt(split[1]));
                socket.send(request);

                //send reply back to client
                byte[] buffer = new byte[100];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                socket.receive(reply);
                result = new String(reply.getData());
            }

        } catch (SocketException s) {
            System.out.println("Socket: " + s.getMessage());
        } catch (Exception e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
        return result;
    }

    /**
     * read the group leader info from file
     *
     * @return
     */
    private String getLeaderInfo() {
        String info = "";
        String property = System.getProperty("user.dir");
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(property + File.separator
                    + "leader.txt"));
            info = br.readLine();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return info;
    }

    /**
     * construct Message object from method parameters
     *
     * @param method
     * @param badgeId
     * @param firstName
     * @param lastName
     * @param description
     * @param status
     * @param address
     * @param lastDate
     * @param lastLocation
     * @param recordId
     * @param remoteServerName
     * @return
     */
    private synchronized Message constructRequest(String method, String managerID, String firstName,
            String lastName, String address, String phone, String specialization, String location,
            String designation, String status, String statusDate, String recordID, String fieldName, String newValue, String remoteClinicServerName) {
        Message msg = new Message();

        /*msg.setMessageID(sequence);
        msg.setMethod(method);
        msg.setFirstName(firstName);
        msg.setLastName(lastName);
        msg.setAddress(address);
        msg.setPhone(phone);
        msg.setSpecialization(specialization);
        msg.setLocation(location);
        msg.setDesignation(designation);
        msg.setStatus(status);
        msg.setStatusDate(statusDate);
        msg.setRecordId(recordID);
        msg.setFieldName(fieldName);
        msg.setNewValue(newValue);
        msg.setRemoteServerName(remoteClinicServerName);*/
        if (null != firstName || !"".equalsIgnoreCase(firstName)) {
            msg.setFirstName(firstName);
        }
        if (null != lastName || !"".equalsIgnoreCase(lastName)) {
            msg.setLastName(lastName);
        }
        if (null != address || !"".equalsIgnoreCase(address)) {
            msg.setAddress(address);
        }
        if (null != phone || !"".equalsIgnoreCase(phone)) {
            msg.setPhone(phone);
        }
        if (null != specialization || !"".equalsIgnoreCase(specialization)) {
            msg.setSpecialization(specialization);
        }
        if (null != location || !"".equalsIgnoreCase(location)) {
            msg.setLocation(location);
        }
        if (null != designation || !"".equalsIgnoreCase(designation)) {
            msg.setDesignation(designation);
        }
        if (null != status || !"".equalsIgnoreCase(status)) {
            msg.setStatus(status);
        }
        if (null != statusDate || !"".equalsIgnoreCase(statusDate)) {
            msg.setStatusDate(statusDate);
        }
        if (null != recordID || !"".equalsIgnoreCase(recordID)) {
            msg.setRecordId(recordID);
        }
        if (null != fieldName || !"".equalsIgnoreCase(fieldName)) {
            msg.setFieldName(fieldName);
        }
        if (null != newValue || !"".equalsIgnoreCase(newValue)) {
            msg.setNewValue(newValue);
        }
        if (null != remoteClinicServerName || !"".equalsIgnoreCase(remoteClinicServerName)) {
            msg.setRemoteServerName(remoteClinicServerName);
        }
        msg.setMessageType("operation");
        try {
            msg.setMessage(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        sequence++;

        return msg;
    }

}
