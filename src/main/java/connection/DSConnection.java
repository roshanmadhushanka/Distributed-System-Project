package connection;

import model.MessageTable;

import java.sql.Timestamp;

public class DSConnection {

    public String join(String ipAddress, int port) {
        String response;

        // Generate message
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String message = "JOIN " + ipAddress + " " + String.valueOf(port) + " " + String.valueOf(timestamp.getTime());
        int messageLength = message.length() + 5;
        String lengthPrefix = String.format("%04d", messageLength);
        message = lengthPrefix + " " + message;

        // Put message in MessageTable for future validation
        MessageTable.put(timestamp.getTime(), message);

        // Communicate with the network
        Connection connection = new Connection();
        response = connection.send(message, ipAddress, port);
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

    public String leave(String ipAddress, int port) {
        String response;

        // Generate message
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String message = "LEAVE " + ipAddress + " " + String.valueOf(port) + " " + String.valueOf(timestamp.getTime());
        int messageLength = message.length() + 5;
        String lengthPrefix = String.format("%04d", messageLength);
        message = lengthPrefix + " " + message;

        // Put message in MessageTable for future validation
        MessageTable.put(timestamp.getTime(), message);

        // Communicate with the network
        Connection connection = new Connection();
        response = connection.send(message, ipAddress, port);
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

        // Add length suffix in front of the message;
        message = lengthPrefix + " " + message;

        // Communicate with the network
        Connection connection = new Connection();
        String response = connection.send(message, ipAddress, port);
        connection.close();

        return response;
    }


}
