package connection;

import config.Configuration;
import model.MessageTable;

import java.sql.Timestamp;
import java.util.List;

public class DSConnection {

    public String pulse(String targetIp, int targetPort, long timestamp){
        String response;

        // System communication parameters
        String myIp = Configuration.getSystemIPAddress();
        int myPort = Configuration.getSystemPort();

        // Generate message
        String message = "PULSE " + timestamp;
        int messageLength = message.length() + 5;
        String lengthPrefix = String.format("%04d", messageLength);
        message = lengthPrefix + " " + message;

        // Communicate with the network
        Connection connection = new Connection();
        response = connection.send(message, targetIp, targetPort);
        connection.close();

        return response;
    }

    public String pulseResponse(String[] req, String ipAddress, int port) {
        String message = "PULSE_OK " + req[2];

        // Message length with string length suffix
        int messageLength = message.length() + 5;

        // Format length suffix to four character string
        String lengthPrefix = String.format("%04d", messageLength);

        // Add length suffix in front of the message;
        message = lengthPrefix + " " + message;

        // Communicate with the network
        Connection connection = new Connection();
        String response = connection.send(message, ipAddress, port);
        connection.close();

        return response;
    }

    public String join(String myIp, int myPort, String senderIp, int senderPort) {
        String response;

        // Generate message
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String message = "JOIN " + myIp + " " + String.valueOf(myPort) + " " + String.valueOf(timestamp.getTime());
        int messageLength = message.length() + 5;
        String lengthPrefix = String.format("%04d", messageLength);
        message = lengthPrefix + " " + message;

        // Put message in MessageTable for future validation
        MessageTable.put(timestamp.getTime(), message);

        // Communicate with the network
        Connection connection = new Connection();
        response = connection.send(message, senderIp, senderPort);
        connection.close();

        return response;
    }

    public String joinResponse(String ipAddress, int port, long timestamp, int status) {
        String message = "Error";
        if(status == 0) {
            message = "JOINOK 0 ";
        } else if(status == 9999) {
            message = "JOINOK 9999 ";
        } else {
            return message;
        }

        // Add timestamp to identify the request
        message += timestamp;

        // Message length with string length suffix
        int messageLength = message.length() + 5;

        // Format length suffix to four character string
        String lengthPrefix = String.format("%04d", messageLength);

        // Add length suffix in front of the message;
        message = lengthPrefix + " " + message;

        // Communicate with the network
        Connection connection = new Connection();
        String response = connection.send(message, ipAddress, port);
        connection.close();

        return response;
    }

    public String leave(String myIp, int myPort, String senderIp, int senderPort) {
        String response;

        // Generate message
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String message = "LEAVE " + myIp + " " + String.valueOf(myPort) + " " + String.valueOf(timestamp.getTime());
        int messageLength = message.length() + 5;
        String lengthPrefix = String.format("%04d", messageLength);
        message = lengthPrefix + " " + message;

        // Put message in MessageTable for future validation
        MessageTable.put(timestamp.getTime(), message);

        // Communicate with the network
        Connection connection = new Connection();
        response = connection.send(message, senderIp, senderPort);
        connection.close();

        return response;
    }

    public String leaveResponse(String ipAddress, int port, long timestamp, int status) {
        String message = "Error";
        if(status == 0) {
            message = "LEAVEOK 0 ";
        } else if(status == 9999) {
            message = "LEAVEOK 9999 ";
        } else {
            return message;
        }

        // Add timestamp to identify the request
        message += timestamp;

        // Message length with string length suffix
        int messageLength = message.length() + 5;

        // Format length suffix to four character string
        String lengthPrefix = String.format("%04d", messageLength);

        // Add length suffix in front of the message
        message = lengthPrefix + " " + message;

        // Communicate with the network
        Connection connection = new Connection();
        String response = connection.send(message, ipAddress, port);
        connection.close();

        return response;
    }

    public String search(String myIp, int myPort, int hops, String fileName, long timestamp, String senderIp, int senderPort) {
        String response;

        // Generate message
        String message = "SER " + myIp + " " + String.valueOf(myPort) + " " + String.valueOf(hops) + " " + fileName +
                " " + String.valueOf(timestamp);

        // Message length with string length suffix
        int messageLength = message.length() + 5;

        // Format length suffix to four character string
        String lengthPrefix = String.format("%04d", messageLength);

        // Add length suffix in front of the message
        message = lengthPrefix + " " + message;

        // Put message in MessageTable for future validation
        MessageTable.put(timestamp, message);

        // Communicate with the network
        Connection connection = new Connection();
        response = connection.send(message, senderIp, senderPort);
        connection.close();

        return response;
    }

    public String forward(String myIp, int myPort, int hops, String fileName, long timestamp, String senderIp, int senderPort) {
        /*
            If current node does not have requested content
         */

        String response;

        // Generate message
        String message = "SER " + myIp + " " + String.valueOf(myPort) + " " + String.valueOf(hops) + " " + fileName +
                " " + String.valueOf(timestamp);

        // Message length with string length suffix
        int messageLength = message.length() + 5;

        // Format length suffix to four character string
        String lengthPrefix = String.format("%04d", messageLength);

        // Add length suffix in front of the message
        message = lengthPrefix + " " + message;

        // Communicate with the network
        Connection connection = new Connection();
        response = connection.send(message, senderIp, senderPort);
        connection.close();

        return response;
    }

    public String searchResponse(String ipAddress, int port, int hops, List<String> resultSet, long timestamp) {
        /*
            If current node has requested content
         */

        String response;
        String message;

        String myHost = Configuration.getSystemIPAddress();
        String myPort = String.valueOf(Configuration.getSystemPort());

        int status;
        if(resultSet == null) {
            status = 0;
            message = "SEROK " + String.valueOf(status);
        } else {
            status = resultSet.size();
            message = "SEROK " + String.valueOf(status) + " " + myHost + " " + myPort + " " +
                    String.valueOf(hops);
            for(String fileName: resultSet) {
                message += " " + fileName;
            }
        }

        // Add timestamp to identify the response
        message += " " + String.valueOf(timestamp);

        // Message length with string length suffix
        int messageLength = message.length() + 5;

        // Format length suffix to four character string
        String lengthPrefix = String.format("%04d", messageLength);

        // Add length suffix in front of the message
        message = lengthPrefix + " " + message;

        // Put message in MessageTable for future validation
        MessageTable.put(timestamp, message);

        // Communicate with the network
        Connection connection = new Connection();
        response = connection.send(message, ipAddress, port);
        connection.close();

        return response;
    }
}
