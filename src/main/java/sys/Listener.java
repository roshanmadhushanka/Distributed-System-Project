package sys;

import connection.Socket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Listener extends Thread {
    private boolean listening;
    private int port;
    DatagramSocket socket = null;

    public Listener(int port) {
        this.listening = true;
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("Listening to " + port);
        try {
            socket = Socket.getSocket();
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
