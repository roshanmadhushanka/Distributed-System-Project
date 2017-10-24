package connection;

import config.Configuration;
import java.io.IOException;
import java.util.Properties;

public class BootstrapConnection {
    public String reg(String ipAddress, int port, String userName) {
        /*
            Register to the bootstrap server
         */
        String response = "Error";

        // Generate message
        String message = "REG " + ipAddress + " " + String.valueOf(port) + " " + userName;
        int messageLength = message.length() + 5;
        String length = String.format("%04d", messageLength);
        message = length + " " + message;

        // Get bootstrap server info
        Properties properties = null;
        try {
            properties = Configuration.getProperties();
        } catch (IOException e) {
            System.err.println("IO Exception: BootstrapConnection.class reg");
            return response;
        }

        // Communicate with bootstrap server
        String bsIPAddress = properties.getProperty("bs.ipAddress");
        int bsPort = Integer.parseInt(properties.getProperty("bs.port"));
        int noOfMaxAttempts = Integer.parseInt(properties.getProperty("connection.noOfMaxAttempts"));
        Connection connection = new Connection();
        for(int i=0; i<noOfMaxAttempts; i++) {
            // Retry to send
            response = connection.send(message, bsIPAddress, bsPort);
            if(!response.equals("Timeout"))
                break;
        }
        connection.close();

        return response;
    }

    public String unreg(String ipAddress, int port, String userName) {
        /*
            Unregister from bootstrap server
         */
        String response = "Error";

        // Generate message
        String message = "UNREG " + ipAddress + " " + String.valueOf(port) + " " + userName;
        int messageLength = message.length() + 5;
        String length = String.format("%04d", messageLength);
        message = length + " " + message;

        // Get bootstrap server info
        Properties properties = null;
        try {
            properties = Configuration.getProperties();
        } catch (IOException e) {
            System.err.println("IO Exception: BootstrapConnection.class unreg");
            return response;
        }

        // Communicate with bootstrap server
        String bsIPAddress = properties.getProperty("bs.ipAddress");
        int bsPort = Integer.parseInt(properties.getProperty("bs.port"));
        Connection connection = new Connection();
        response = connection.send(message, bsIPAddress, bsPort);
        connection.close();

        return response;
    }
}
