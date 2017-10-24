import connection.BootstrapConnection;
import model.FileTable;
import sys.Config;
import sys.Listener;
import sys.Parser;

public class Client4 {
    public static void main(String[] args) {
        String name = "Lakmal";
        String host = "localhost";
        int port = 55559;

        // Setup configuration
        Config.put("name", name);
        Config.put("host", host);
        Config.put("port", String.valueOf(port));

        // Add files to system
        FileTable.add("King Arthur");
        FileTable.add("Windows XP");
        FileTable.add("Super Mario");
        FileTable.add("American Pickers");

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
