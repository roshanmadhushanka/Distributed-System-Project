import connection.BootstrapConnection;
import connection.DSConnection;
import model.FileTable;
import model.Node;
import sys.Config;
import sys.Listener;
import sys.Parser;
import java.sql.Timestamp;

public class Client1 {
    private static void search(String fileName, int hopCount) {
        /*
            Simulate search
         */
        String host = Config.get("host");
        int port = Integer.parseInt(Config.get("port"));
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for(Node node: Node.getNeighbours()) {
            DSConnection dsConnection = new DSConnection();
            dsConnection.search(host, port, hopCount, fileName, timestamp.getTime(), node.getIpAddress(), node.getPort());
            hopCount--;
        }
    }

    public static void main(String[] args) {
        String name = "Roshan";
        String host = "localhost";
        int port = 55556;

        // Setup configuration
        Config.put("name", name);
        Config.put("host", host);
        Config.put("port", String.valueOf(port));

        // Add files to system
        FileTable.add("Adventures of Tintin");
        FileTable.add("Jack and Jill");
        FileTable.add("Glee");
        FileTable.add("The_Vampire_Diarie");


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
