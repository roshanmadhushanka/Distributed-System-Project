package connection;

import sys.Config;

import java.io.IOException;
import java.net.*;

public class Connection {
    private DatagramSocket socket = null;

    public Connection() {
        /*
            Initiate outgoing socket
         */

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.err.println("Socket Exception: Connection.class constructor");
        }
    }

    public String send(String message, String ipAddress, int port) {
        /*
            Send message to the network
         */

        System.out.println("Sending : " + message + " to " +ipAddress + " " + port);
        String response = "Error";

        byte[] sendData = message.getBytes();
        DatagramPacket sendPacket = null;
        try {
            InetAddress receiverAddress = InetAddress.getByName(ipAddress);
            sendPacket = new DatagramPacket(sendData, sendData.length, receiverAddress, port);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host exception: Connection.class send");
            return response;
        }

        try {
            // Send packet
            socket = new DatagramSocket();
            socket.setSoTimeout(10000);
            socket.send(sendPacket);

            // Receive data
            byte[] receiveData = new byte[65536];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            return response;
        } catch (SocketTimeoutException e) {
            response = "Timeout";
        } catch (SocketException e) {
            System.err.println("Socket exception: Connection.class send");
        } catch (IOException e) {
            System.err.println("IO exception: Connection.class send");
        }

        return response;
    }

    public void close() {
        /*
            Close socket
         */

        if(socket != null) {
            socket.close();
        }
    }


}
