import connection.BootstrapConnection;
import model.FileTable;
import sys.Config;
import sys.Listener;
import sys.Parser;

public class Client2 {
    public static void main(String[] args) {
        String name = "Madhushanka";
        int port = 55557;

        // Setup configuration
        Config.put("name", name);
        Config.put("host", "localhost");
        Config.put("port", String.valueOf(port));

        // Add files to system
        FileTable.add("Harry Potter");
        FileTable.add("Kung Fu Panda");
        FileTable.add("Lady Gaga");
        FileTable.add("Twilight");
        FileTable.add("Windows 8");
        FileTable.add("Mission Impossible");
        FileTable.add("Turn Up The Music");
        FileTable.add("Super Mario");
        FileTable.add("American Pickers");

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
