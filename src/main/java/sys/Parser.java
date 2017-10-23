package sys;

import connection.DSConnection;
import model.MessageTable;
import model.Node;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Parser {
    public static void parseResponse(String response) {
        // Response parser
        System.out.println("Response : " + response);
        if(!response.equals("Error")) {
            String[] message = response.split(" ");
            if(message[1].equals("REGOK")) {
                // Register to bootstrap
                int value = Integer.parseInt(message[2]);
                if(value == 0) {
                    // No users
                    System.out.println("Register BS: No user");
                } else if(value == 1) {
                    // 1 user
                    registerBS1User(message);
                    System.out.println("Register BS: 1 User");
                } else if(value == 2) {
                    // 2 users
                    registerBS2User(message);
                    System.out.println("Register BS: 2 Users");
                } else if(value == 9999) {
                    // Error in command
                    System.out.println("Register BS: Error in command");
                } else if(value == 9998) {
                    // Already registered to you
                    System.out.println("Register BS: Already registered to you");
                } else if(value == 9997) {
                    // Registered to another user
                    System.out.println("Register BS: Registered to another user");
                } else if(value == 9996) {
                    // BS is full
                    System.out.println("Register BS: Full");
                }
            } else if(message[1].equals("UNROK")) {
                // Unregister from bootstrap
                int value = Integer.parseInt(message[2]);
                if(value == 0) {
                    // Success
                    System.out.println("Unregister from BS: Success");
                } else if(value == 9999) {
                    // Error
                    System.out.println("Unregister from BS: Fail");
                }
            } else if(message[1].equals("JOINOK")) {
                // Join distributed system
                int value = Integer.parseInt(message[2]);
                if(value == 0) {
                    // Success
                    System.out.println("Register DS: Success");
                } else if(value == 9999) {
                    // Fail
                    System.out.println("Register DS: Fail");
                }
            } else if(message[1].equals("LEAVEOK")) {
                // Leave from distributed system
                int value = Integer.parseInt(message[2]);
                if(value == 0) {
                    // Success
                    System.out.println("Leave DS: Success");
                } else if(value == 1) {
                    // Fail
                    System.out.println("Leave DS: Fail");
                }
            } else if(message[1].equals("SEROK")) {
                int value = Integer.parseInt(message[2]);
                if(value == 9999) {
                    // Unreachable node
                    System.out.println("Search : Unreachable node");
                } else if(value == 9998) {
                    // other error
                    System.out.println("Search : Other error");
                } else if(value == 0) {
                    // No result
                    System.out.println("Search : No result");
                } else if (value > 0){
                    // has result
                    System.out.println("Search : Has result");
                }
            } else if(message[1].equals("ERROR")) {
                // Error
                System.out.println("Error");
            }
        } else {
            System.err.println("Terminate");
        }
    }

    public static void parseRequest(DatagramPacket receivePacket) {
        // Request parser
        // Received request
        String request = new String(receivePacket.getData(), 0, receivePacket.getLength());

        // Extract sender info
        String senderIPAddress = receivePacket.getAddress().getHostAddress();
        int senderPort = receivePacket.getPort();

        String[] message = request.split(" ");
        if(message[1].equals("JOIN")) {
            // Send join OK reply
            joinNetwork(message, senderIPAddress, senderPort);
        } else if(message[1].equals("LEAVE")) {

        } else if(message[1].equals("SER")) {

        } else if(message[1].equals("API")) {

        }
    }

    private static void registerBS2User(String[] message) {
        Node node1 = new Node(message[3], Integer.parseInt(message[4]));
        Node node2 = new Node(message[5], Integer.parseInt(message[6]));
        Node.addNeighbour(node1);
        Node.addNeighbour(node2);
        DSConnection dsConnection = new DSConnection();

        String response1 = dsConnection.join(node1.getIpAddress(), node1.getPort());
        if(MessageTable.validate(response1)) {
            Parser.parseResponse(response1);
        }

        String response2 = dsConnection.join(node2.getIpAddress(), node2.getPort());
        if(MessageTable.validate(response2)) {
            Parser.parseResponse(response2);
        }
    }

    private static void registerBS1User(String[] message) {
        Node node = new Node(message[3], Integer.parseInt(message[4]));
        Node.addNeighbour(node);
        DSConnection dsConnection = new DSConnection();
        String response = dsConnection.join(node.getIpAddress(), node.getPort());
        if(MessageTable.validate(response)){
            Parser.parseResponse(response);
        }
    }

    private static void joinNetwork(String[] message, String ipAddress, int port) {
        long timestamp = Long.parseLong(message[message.length - 1]);
        DSConnection dsConnection = new DSConnection();
        dsConnection.joinResponse(ipAddress, port, timestamp);
    }

}