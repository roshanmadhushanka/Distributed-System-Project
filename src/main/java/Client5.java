import connection.BootstrapConnection;
import model.FileTable;
import sys.Config;
import sys.Listener;
import sys.Parser;

public class Client5 {
    public static void main(String[] args) {
        String name = "Vijini";
        String host = "localhost";
        int port = 55560;

        // Setup configuration
        Config.put("name", name);
        Config.put("host", host);
        Config.put("port", String.valueOf(port));

        // Add files to system
        FileTable.add("Hacking for Dummies");
        FileTable.add("Mission Impossible");
        FileTable.add("Turn Up The Music");

        // Connect to the network
        BootstrapConnection bootstrapConnection = new BootstrapConnection();
        bootstrapConnection.unreg("localhost", port, name);
        String response = bootstrapConnection.reg("localhost", port, name);
        Parser.parseResponse(response);

        if(response.equals("Timeout")) {
            // If client fails to register bootstrap server
            return;
        }

        // Listen to incoming requests
        Listener listener = new Listener(port);
        listener.start();
    }
}
