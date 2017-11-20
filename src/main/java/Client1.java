import config.Configuration;
import connection.BootstrapConnection;
import connection.DSConnection;
import model.*;
import stat.JoinQueryStat;
import stat.LeaveQueryStat;
import stat.SearchQueryStat;
import stat.Statistics;
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
            // Show neighbours
            Node.displayAsTable();

        } else if (fileName.equals("s")) {
            // Display stat
            Statistics.displayStatistics();

        } else if (fileName.equals("l")) {
            // Gracefully leave the network
            BootstrapConnection bootstrapConnection = new BootstrapConnection();
            String response = bootstrapConnection.unreg(Configuration.getSystemIPAddress(), Configuration.getSystemPort(),
                    Configuration.getSystemName());
            Parser.parseResponse(response);
        } else if (fileName.equals("j")) {
            // Join the network
            BootstrapConnection bootstrapConnection = new BootstrapConnection();
            String response = bootstrapConnection.reg(Configuration.getSystemIPAddress(), Configuration.getSystemPort(),
                    Configuration.getSystemName());
            Parser.parseResponse(response);
        } else if(fileName.equals("c")) {
            // Clear stat
            System.out.println("Clear Tables");
            System.out.println("============");
            JoinQueryStat.clear();
            System.out.println("Join Table");
            LeaveQueryStat.clear();
            System.out.println("Leave Table");
            SearchQueryStat.clear();
            System.out.println("Search Table");
            Statistics.clear();
            FileToLocationTable.clear();
            System.out.println("File to Location Table");
            ForwardTable.clear();
            System.out.println("Forwards Table");
            MessageTable.clear();
            System.out.println("Message Table");
            ResponseTable.clear();
            System.out.println("Response Table");
        } else if (fileName.equals("f")) {
            // Display Files
            FileTable.displayAsTable();
        } else {
            // Search for file
            String host = Configuration.getSystemIPAddress();
            int port = Configuration.getSystemPort();

            List<Node> neighbors = FileToLocationTable.getLocations(fileName);
            for(Node node: Node.getNeighbours()) {
                neighbors.add(node);
            }

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            hopCount--;
            for(Node node: neighbors) {
                DSConnection dsConnection = new DSConnection();
                dsConnection.search(host, port, hopCount, fileName, timestamp.getTime(), node.getIpAddress(), node.getPort());
            }
        }
    }


    public static void main(String[] args) throws IOException {
        // Load configurations
        Configuration.loadConfigurations();
        Configuration.display();

        // Files
        FileTable.displayAsTable();

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
            search(input, Configuration.getMaxHopCount());
        }
    }
}
