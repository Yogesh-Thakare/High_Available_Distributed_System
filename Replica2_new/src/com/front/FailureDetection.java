package com.front;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Abhinoor
 */
public class FailureDetection extends Thread {

    /**
     * Starts the failure detection thread
     */
    @Override
    public void run() {
        new MulticastMessageReceiver(Constants.MC_FDS_PORT1);
        new MulticastMessageReceiver(Constants.MC_FDS_PORT3);
        while (true) {
            List<String> activeProcesses = new MulticastMessageSender().multicastRequest("FDS", Constants.MC_FDS_PORT2);
            int currentGroupLeader = new ClinicServers().getGroupLeaderProcessID();

            for (String str : activeProcesses) {
                System.out.println(str);
            }

            if (activeProcesses.size() == 0) {
                try {
                    notifyFrontEnd(InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                List<Integer> processes = new ArrayList<Integer>();;
                for (String process : activeProcesses) {
                    processes.add(Integer.parseInt(process.split(":")[0]));
                }

                if (!processes.contains(currentGroupLeader)) {
                    System.out.println("Leader Election System started...");
                    new LeaderElection(activeProcesses).start();
                }
            }
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Notifies the FrontEnd about new Group Leader
     *
     * @param address
     */
    private void notifyFrontEnd(String address) {
        DatagramSocket socket = null;
        DatagramPacket packet = null;
        try {
            socket = new DatagramSocket();
            byte[] bytes = address.getBytes();
            packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(new ClinicServers().getFrontEndAddress()), Constants.GL_NOTIFY_PORT);
            socket.send(packet);
        } catch (Exception e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            if (null != socket) {
                socket.close();
            }
        }

    }

}
