package connection;

import sys.Config;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Socket {
    private static DatagramSocket socket = null;

    public static DatagramSocket getSocket() throws SocketException {
        if(socket == null) {
            socket = new DatagramSocket(Integer.parseInt(Config.get("port")));
        }
        return socket;
    }


}
