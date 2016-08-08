package com.front;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MulticastMessageSender {

	/**
	 * method for multicasting operation to other replicas
	 * @param msg
	 * @return
	 */
	public List<Message> multicastMessage(Message msg) {
		DatagramSocket aSocket = null;
		DatagramPacket request = null;
		byte[] buffer;
		try {
			aSocket = new DatagramSocket();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			ObjectOutput oo = new ObjectOutputStream(outStream);
			oo.writeObject(msg);
			oo.close();

			buffer = outStream.toByteArray();
			// Send to multicast IP address and port
			InetAddress address = InetAddress.getByName("224.2.2.3");
			request = new DatagramPacket(buffer, buffer.length, address,
					Constants.MC_OPERATION_PORT);
			aSocket.send(request);

		} catch (SocketException e) {
			System.out.println("SOCKET:" + e.getMessage());
		} catch (Exception e) {
			System.out.println("IO:" + e.getMessage());
		}
		return getResults(aSocket);
	}

	/**
	 * receive the operation output from other replicas
	 * @param soc
	 * @return
	 */
	private List<Message> getResults(DatagramSocket soc) {

		List<Message> list = new ArrayList<Message>();
		byte[] buffer = null;
		try {
			soc.setSoTimeout(2000);
			while (true) {
				try {
					buffer = new byte[1024];
					DatagramPacket res = new DatagramPacket(buffer,
							buffer.length);
					soc.receive(res);
					list.add(deserializeMessge(res));
				} catch (SocketTimeoutException e) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("IO:" + e.getMessage());
		} finally {
			if (null != soc)
				soc.close();
		}
		for (Message msg : list)
			System.out.println(msg.getMessage());

		return list;
	}

	/**
	 * general method for multicast request to other replicas
	 * @param request
	 * @param port
	 * @return
	 */
	public List<String> multicastRequest(String request, int port) {
		DatagramSocket socket = null;
		DatagramPacket packet = null;
		try {
			InetAddress address = InetAddress.getByName("224.2.2.3");
			socket = new DatagramSocket();
			byte[] reqBytes = request.getBytes();
			packet = new DatagramPacket(reqBytes, reqBytes.length, address,
					port);
			socket.send(packet);
		} catch (Exception e) {
			System.out.println("IO:" + e.getMessage());
		}
		return getResponses(socket);
	}

	/**
	 * method from getting responses of request from multiple replicas 
	 * @param soc
	 * @return
	 */
	private List<String> getResponses(DatagramSocket soc) {

		List<String> list = new ArrayList<String>();
		byte[] buffer = null;
		try {
			soc.setSoTimeout(2000);
			while (true) {
				try {
					buffer = new byte[10];
					DatagramPacket res = new DatagramPacket(buffer,
							buffer.length);
					soc.receive(res);
					list.add(new String(res.getData()).trim()+":"+res.getAddress().getHostAddress());
				} catch (SocketTimeoutException e) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("IO:" + e.getMessage());
		} finally {
			if (null != soc)
				soc.close();
		}
		try {
			list.add(Constants.PROCESS_ID+":"+InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * Deserialize message object from input stream
	 * @param inPacket
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Message deserializeMessge(DatagramPacket inPacket)
			throws IOException, ClassNotFoundException {
		ObjectInputStream inStream = new ObjectInputStream(
				new ByteArrayInputStream(inPacket.getData()));
		Message msg = (Message) inStream.readObject();
		inStream.close();
		return msg;
	}

}
