import connection.BootstrapConnection;
import connection.DSConnection;
import model.FileTable;
import model.Node;
import sys.Config;
import sys.Listener;
import sys.Parser;

public class Client3 {
    public static void main(String[] args) {
        String name = "Alwis";
        int port = 55558;

        // Setup configuration
        Config.put("name", name);
        Config.put("host", "localhost");
        Config.put("port", String.valueOf(port));

        // Add files to system
        FileTable.add("Microsoft Office 2010");
        FileTable.add("Happy Feet");
        FileTable.add("Modern Family");
        FileTable.add("American Idol");
        FileTable.add("Hacking for Dummies");

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

        Node node = Node.getNeighbours().get(1);
        node.describe();
        DSConnection dsConnection = new DSConnection();
        response = dsConnection.search("localhost", port, 5, "The vampire", node.getIpAddress(), node.getPort());
        System.out.println(response);
    }
}
