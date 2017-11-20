package connection;

import config.Configuration;

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

        // Bootstrap server parameters
        String bsIPAddress = Configuration.getBsIpAddress();
        int bsPort = Configuration.getBsPort();
        int noOfMaxAttempts = Configuration.getMaxAttempts();

        // Initiate connection
        Connection connection = new Connection();

        // Retry for failure attempts
        for(int i=0; i<noOfMaxAttempts; i++) {
            // Retry to send
            response = connection.send(message, bsIPAddress, bsPort);
            if(!response.equals("Timeout"))
                break;
        }

        // End connection
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

        // Communicate with bootstrap server
        String bsIPAddress = Configuration.getBsIpAddress();
        int bsPort = Configuration.getBsPort();
        Connection connection = new Connection();
        response = connection.send(message, bsIPAddress, bsPort);
        connection.close();

        return response;
    }
}
