package connection;

import config.Configuration;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Socket {
    private static DatagramSocket socket = null;

    public static DatagramSocket getSocket() throws SocketException {
        if(socket == null) {
            socket = new DatagramSocket(Configuration.getSystemPort());
        }
        return socket;
    }
}
