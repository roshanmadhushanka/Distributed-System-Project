import connection.BootstrapConnection;
import model.FileTable;
import sys.Config;
import sys.Listener;
import sys.Parser;

public class Client1 {
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
