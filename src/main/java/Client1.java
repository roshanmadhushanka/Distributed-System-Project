import connection.BootstrapConnection;
import model.FileTable;
import sys.Config;
import sys.Listener;
import sys.Parser;

public class Client1 {
    public static void main(String[] args) {
        String name = "Roshan";
        int port = 55556;

        // Setup configuration
        Config.put("name", name);
        Config.put("host", "localhost");
        Config.put("port", String.valueOf(port));

        // Add files to system
        FileTable.add("Adventures of Tintin");
        FileTable.add("Jack and Jill");
        FileTable.add("Glee");
        FileTable.add("The Vampire Diarie");
        FileTable.add("King Arthur");
        FileTable.add("Windows XP");

        BootstrapConnection bootstrapConnection = new BootstrapConnection();
        bootstrapConnection.unreg("localhost", port, name);
        String response = bootstrapConnection.reg("localhost", port, name);
        Parser.parseResponse(response);

        if(response.equals("Timeout")) {
            // If client fails to register bootstrap server
            return;
        }

        Listener listener = new Listener(port);
        listener.start();
    }
}
