import config.Configuration;
import connection.BootstrapConnection;
import connection.DSConnection;
import model.FileTable;
import model.Node;
import sys.Listener;
import sys.Parser;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Scanner;

public class Client2 {
    private static void search(String fileName, int hopCount) {
        /*
            Simulate search
         */

        String host = Configuration.getSystemIPAddress();
        int port = Configuration.getBsPort();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for(Node node: Node.getNeighbours()) {
            DSConnection dsConnection = new DSConnection();
            dsConnection.search(host, port, hopCount, fileName, timestamp.getTime(), node.getIpAddress(), node.getPort());
            hopCount--;
        }
    }

    private static void leave() {
        /*
            Leave from network
         */

        String name = Configuration.getSystemName();
        String host = Configuration.getSystemIPAddress();
        int port = Configuration.getBsPort();

        BootstrapConnection bootstrapConnection = new BootstrapConnection();
        bootstrapConnection.unreg(host, port, name);

        DSConnection dsConnection = new DSConnection();
        for(Node node: Node.getNeighbours()) {
            dsConnection.leave(host, port, node.getIpAddress(), node.getPort());
        }
    }

    public static void main(String[] args) throws IOException {
        // Add files to system
        FileTable.add("Harry Potter");
        FileTable.add("Kung Fu Panda");
        FileTable.add("Lady Gaga");
        FileTable.add("Twilight");
        FileTable.add("Windows 8");

        // Load configurations
        Configuration.loadConfigurations();
        Configuration.setSystemPort(12346);
        Configuration.setSystemName("Client 2");
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
            search(input, 3);
        }


    }
}
