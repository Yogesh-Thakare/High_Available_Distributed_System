package com.front;

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
 * @author Yogesh
 */
public class FrontEndInterfaceImpl extends FrontEndInterfacePOA {

    private static Long sequence = 1L;
    private Object lock = new Object();

    /**
     * a synchronized method that receives method name and arguments from client
     * and forwards to Leader process
     */
    @Override
    public String executeOperation(String method, String managerId,
            String firstName, String lastName, String designation,
            String status, String address, String phone,
            String location, String recordId, String remoteServerName) {
        String leaderInfo = "", result = "";
        DatagramSocket socket = null;
        try {
            synchronized (lock) {
                do {
                    leaderInfo = getLeaderInfo();
                } while (null == leaderInfo && "".equalsIgnoreCase(leaderInfo));

                Message msg = constructRequest(method, managerId, firstName,
                        lastName, designation, status, address, location,
                        location, recordId, remoteServerName);
                String[] split = leaderInfo.split(":");

                // serialize the message object to send it over udp
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

                //receive reply from Leader
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
     * Find the Leader by reading from a file
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
     * make a Message object from method parameters that can be serialized
     *
     * @param method
     * @param managerId
     * @param firstName
     * @param lastName
     * @param designation
     * @param status
     * @param address
     * @param phone
     * @param location
     * @param recordId
     * @param remoteServerName
     * @return
     */
    private synchronized Message constructRequest(String method,
            String managerId, String firstName, String lastName,
            String designation, String status, String address, String phone,
            String location, String recordId, String remoteServerName) {
        Message msg = new Message();
        msg.setMessageID(sequence);
        msg.setMethod(method);
        msg.setFirstName(firstName);
        msg.setLastName(lastName);
        msg.setManagerId(managerId);
        msg.setStatus(status);

        if (null != address || !"".equalsIgnoreCase(address)) {
            msg.setAddress(address);
        }
        if (null != designation || !"".equalsIgnoreCase(designation)) {
            msg.setDesignation(designation);
        }
        if (null != phone || !"".equalsIgnoreCase(phone)) {
            msg.setPhone(phone);
        }
        if (null != location || !"".equalsIgnoreCase(location)) {
            msg.setLocation(location);
        }
        if (null != recordId || !"".equalsIgnoreCase(recordId)) {
            msg.setRecordId(recordId);
        }
        if (null != remoteServerName || !"".equalsIgnoreCase(remoteServerName)) {
            msg.setRemoteServerName(remoteServerName);
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
