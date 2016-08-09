package com.front;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

/**
 * @author Abhinoor
 */
public class LeaderElection extends Thread {

    private List<String> activeProcesses = null;

    public LeaderElection(List<String> activeProcesses) {
        this.activeProcesses = activeProcesses;
    }

    @Override
    public void run() {
        // new MulticastMessageReceiver(Constants.MC_LES_PORT);
        //activeProcesses = new MulticastMessageSender().multicastRequest("LES", Constants.MC_LES_PORT);

        electLeader();

    }

    /**
     * Chooses the process with the lowest processID among the active replicas
     */
    private void electLeader() {
        int minProcessID = Constants.MAX_REPLICA;
        if (activeProcesses.size() != 0) {
            for (String process : activeProcesses) {
                int processId = Integer.parseInt(process.split(":")[0]);
                if (processId < minProcessID) {
                    minProcessID = processId;
                }
            }
        }

        getAddress(minProcessID);
    }

    /**
     * Gets the address of FrontEnd
     *
     * @param process
     */
    private void getAddress(int process) {
        String address = "LCHANGED";
        if (activeProcesses.size() != 0) {
            for (String proc : activeProcesses) {
                String[] leaderInfo = proc.split(":");
                if (process == Integer.parseInt(leaderInfo[0])) {
                    address += ":" + leaderInfo[1];
                    break;
                }
            }
        }
        notifyFrontEnd(address);
        notifyActiveReplicas(process);
    }

    /**
     * After successful election, notify other active replicas about the
     * selected Leader
     *
     * @param process
     */
    private void notifyActiveReplicas(int process) {
        new MulticastMessageSender().multicastRequest("LES:" + process, Constants.MC_LES_PORT);
        System.out.println("New Group Leader Replica: " + process);
    }

    /**
     * After successful election, notify the FrontEnd about the selected Leader
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

    /**
     * @return the activeProcesses
     */
    public List<String> getActiveProcesses() {
        return activeProcesses;
    }

    /**
     * Sets the activeProcesses
     *
     * @param activeProcesses
     */
    public void setActiveProcesses(List<String> activeProcesses) {
        this.activeProcesses = activeProcesses;
    }

}
