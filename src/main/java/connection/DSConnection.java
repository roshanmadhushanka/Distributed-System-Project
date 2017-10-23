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

    public String joinResponse(String ipAddress, int port, long timestamp) {
        String message = "JOINOK 0 " + timestamp;
        int messageLength = message.length() + 5;
        String lengthPrefix = String.format("%04d", messageLength);
        message = lengthPrefix + " " + message;

        // Communicate with the network
        Connection connection = new Connection();
        String response = connection.send(message, ipAddress, port);
        connection.close();

        return response;
    }


}
