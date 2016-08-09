package com.front;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;

import org.omg.CORBA.ORB;

import dsms.ClinicService;
import dsms.ClinicServiceHelper;

/**
 * @author Gagan
 */
public class UDPReceiver extends Thread {

    private int port;

    public UDPReceiver() {
    }

    public UDPReceiver(int port) {
        this.port = port;
        start();
    }

    /**
     * Called by each thread and wait for requests to come in
     */
    @Override
    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(port);
            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket request = new DatagramPacket(receiveData,
                        receiveData.length);
                socket.receive(request);
                ObjectInputStream inStream = new ObjectInputStream(
                        new ByteArrayInputStream(request.getData()));
                Message msg = (Message) inStream.readObject();
                msg.setProcessID(new ClinicServers().getProcessID());
                inStream.close();

                //Multi casts message to all other active replicas
                MulticastMessageSender sender = new MulticastMessageSender();
                List<Message> results = sender.multicastMessage(msg);

                //get the result from local replica
                results.add(getResultFromLocalServer(msg));

                byte[] outputBytes = compareResults(results).getBytes();

                DatagramPacket response = new DatagramPacket(outputBytes,
                        outputBytes.length, request.getAddress(), request.getPort());
                socket.send(response);
            }
        } catch (SocketException e) {
            System.out.println("SOCKET:" + e.getMessage());
        } catch (Exception e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    /**
     *
     * @param msg
     * @return
     */
    public Message getResultFromLocalServer(Message msg) {
        ClinicService object = null;
        Message msgg = new Message();
        String response = "";
        if (null != msg) {
            String method = msg.getMethod();
            String badgeId = msg.getBadgeId();
            String stationName = badgeId.replaceAll("[0-9]", "");

            if (null != (object = getRemoteObjectStub(stationName))) {
                if ("CR".equalsIgnoreCase(method)) {
                    response = object.createNRecord(msg.getFirstName(),
                            msg.getLastName(), msg.getDescription(),
                            msg.getStatus(), msg.getBadgeId());
                } else if ("MR".equalsIgnoreCase(method)) {
                    response = object.createDRecord(msg.getFirstName(),
                            msg.getLastName(), msg.getAddress(),
                            msg.getLastDate(), msg.getLastLocation(),
                            msg.getStatus(), msg.getBadgeId());
                } else if ("GR".equalsIgnoreCase(method)) {
                    response = object.getRecordCounts(msg.getBadgeId());
                } else if ("ER".equalsIgnoreCase(method)) {
                    response = object.editCRecord(msg.getLastName(),
                            msg.getRecordId(), msg.getStatus(),
                            msg.getBadgeId());
                } else if ("TR".equalsIgnoreCase(method)) {
                    response = object.transferRecord(msg.getBadgeId(),
                            msg.getRecordId(), msg.getRemoteServerName());
                }
            }
        }

        msgg.setProcessID(new ClinicServers().getProcessID());
        msgg.setMessage(response);
        msgg.setMessageID(msg.getMessageID());
        msgg.setMethod(msg.getMethod());
        return msgg;
    }

    /**
     * Gets the remote object reference
     *
     * @param serverName
     * @return
     * @throws IOException
     */
    private static ClinicService getRemoteObjectStub(String stationName) {
        String[] args = null;
        ClinicService serverObj = null;

        ORB orb = ORB.init(args, null);

        String property = System.getProperty("user.dir");
        try {
            // get the IOR string from contact file based on station
            BufferedReader br = new BufferedReader(new FileReader(property
                    + File.separator + "contact" + File.separator + stationName
                    + "_contact.txt"));
            String ior = br.readLine();
            br.close();

            org.omg.CORBA.Object obj = orb.string_to_object(ior);

            serverObj = ClinicServiceHelper.narrow(obj);
        } catch (Exception e) {
            System.out.println("IO:" + e.getMessage());
        }

        return serverObj;
    }

    /**
     * Compares results from different replicas and return single result based
     * on majority
     *
     * @param results
     * @return
     */
    public String compareResults(List<Message> results) {
        String response = "", positiveRes = "", negResponse = "";
        int successCounter = 0;
        int majority = 0;
        if (results.size() != 0) {
            majority = results.size() / 2 + 1;
            for (Message msg : results) {
                if (!msg.getMethod().equalsIgnoreCase("GR")) {
                    if (msg.getMessage().split(":")[0]
                            .equalsIgnoreCase("SUCCESS")) {
                        positiveRes = msg.getMessage();
                        successCounter++;
                    } else {
                        negResponse = msg.getMessage();
                    }
                } else {
                    response = msg.getMessage();
                    break;
                }
            }
        }

        if (successCounter >= majority) {
            response = positiveRes;
        } else {
            response = negResponse;
        }

        return response;
    }
}
