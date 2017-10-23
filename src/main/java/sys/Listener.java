package sys;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Listener extends Thread {
    private static Listener listener;
    private boolean listening;
    private int port;
    DatagramSocket socket = null;

    public static Listener getListener() {
        return null;
    }

    public Listener(int port) {
        this.listening = true;
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("Listening to " + port);
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
            System.err.println("Socket exception " + port);
            return;
        }

        while (listening) {
            byte[] receiveData = new byte[65536];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                socket.receive(receivePacket);
                Parser.parseRequest(receivePacket);
            } catch (IOException e) {
                return;
            }
        }

        System.out.println("Listener "+ String.valueOf(port) +" terminate");
    }

    public void close() {
        System.out.println("Closing listener");
        this.listening = false;
        if(socket != null) {
            socket.close();
        }
    }

    public boolean isListening() {
        return listening;
    }

    public int getPort() {
        return port;
    }
}
