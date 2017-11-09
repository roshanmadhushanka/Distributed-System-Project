import config.Configuration;
import connection.BootstrapConnection;
import connection.DSConnection;
import model.FileTable;
import model.Node;
import sys.Listener;
import sys.Parser;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client1 {
    private static void search(String fileName, int hopCount) {
        /*
            Simulate search
         */

        if(fileName.equals("n")) {
            System.out.println("Neighbours");
            System.out.println("----------");
            for(Node node: Node.getNeighbours()) {
                System.out.println(node.getIpAddress() + ":" + node.getPort());
            }
        } else {
            String host = Configuration.getSystemIPAddress();
            int port = Configuration.getSystemPort();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            for(Node node: Node.getNeighbours()) {
                DSConnection dsConnection = new DSConnection();
                dsConnection.search(host, port, hopCount, fileName, timestamp.getTime(), node.getIpAddress(), node.getPort());
                hopCount--;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // Load configurations
        Configuration.loadConfigurations();
        Configuration.display();

        // Files
        FileTable.display();

        // System parameters
        String systemName = Configuration.getSystemName();
        String systemIPAddress = Configuration.getSystemIPAddress();
        int systemPort = Configuration.getSystemPort();

        // Connect to the network
        BootstrapConnection bootstrapConnection = new BootstrapConnection();
        bootstrapConnection.unreg(systemIPAddress, systemPort, systemName);

        String response = bootstrapConnection.reg(systemIPAddress, systemPort, systemName);
        Parser.parseResponse(response);

        if(response.equals("Timeout")) {
            // If client fails to register bootstrap server
            return;
        }

        // Listen to incoming requests
        Listener listener = new Listener(systemPort);
        listener.start();

        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (true) {
            input = scanner.nextLine();
            search(input, 10);
        }
    }
}
