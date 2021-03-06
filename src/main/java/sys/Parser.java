package sys;

import com.sun.org.apache.regexp.internal.RE;
import config.Configuration;
import connection.BootstrapConnection;
import connection.DSConnection;
import model.*;
import stat.JoinQueryStat;
import stat.LeaveQueryStat;
import stat.SearchQueryStat;
import stat.Statistics;

import java.net.DatagramPacket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Parser {
    public static void parseResponse(String response) {
        // Response parser
        System.out.println("Response : " + response);
        String[] message = response.split(" ");

        if(!message[0].equals("Error")) {
            if(message[1].equals("REGOK")) {
                // Register to bootstrap
                int value = Integer.parseInt(message[2]);
                if(value == 0) {
                    // No users
                    System.out.println("Register BS : No user");
                } else if(value == 1) {
                    // 1 user
                    System.out.println("Register BS : 1 User");
                    registerBS1User(message);
                } else if(value == 2) {
                    // 2 users
                    System.out.println("Register BS : 2 Users");
                    registerBS2User(message);
                } else if(value == 9999) {
                    // Error in command
                    System.out.println("Register BS : Error in command");
                } else if(value == 9998) {
                    // Already registered to you
                    System.out.println("Register BS : Already registered to you");
                } else if(value == 9997) {
                    // Registered to another user
                    System.out.println("Register BS : Registered to another user");
                } else if(value == 9996) {
                    // BS is full
                    System.out.println("Register BS : Full");
                }
            } else if(message[1].equals("UNROK")) {
                // Unregister from bootstrap
                int value = Integer.parseInt(message[2]);
                if(value == 0) {
                    // Success
                    System.out.println("Unregister from BS : Success");
                    unregisterUser(message);
                } else if(value == 9999) {
                    // Error
                    System.out.println("Unregister from BS : Fail");
                }
            } else if(message[1].equals("JOINOK")) {
                // Join distributed system


                // Stat - Begin
                long timestamp = Long.parseLong(message[message.length-1]);
                JoinQueryStat joinQueryStat = new JoinQueryStat();
                joinQueryStat.setReceivedTime(new Timestamp(System.currentTimeMillis()).getTime());
                joinQueryStat.setSentTime(timestamp);
                JoinQueryStat.append(joinQueryStat);
                // Stat - End

                int value = Integer.parseInt(message[2]);
                if(value == 0) {
                    // Success
                    System.out.println("Register DS : Success");
                } else if(value == 9999) {
                    // Fail
                    System.out.println("Register DS : Fail");
                }
            } else if(message[1].equals("LEAVEOK")) {
                // Leave from distributed system

                // Stat - Begin
                long timestamp = Long.parseLong(message[message.length-1]);
                LeaveQueryStat leaveQueryStat = new LeaveQueryStat();
                leaveQueryStat.setReceivedTime(new Timestamp(System.currentTimeMillis()).getTime());
                leaveQueryStat.setSentTime(timestamp);
                LeaveQueryStat.append(leaveQueryStat);
                // Stat - End

                int value = Integer.parseInt(message[2]);
                if(value == 0) {
                    // Success
                    System.out.println("Leave DS : Success");

                    // Extract message info
                    String ipAddress = message[message.length - 3];
                    int port = Integer.parseInt(message[message.length - 2]);

                    // Remove neighbour
                    Node.removeNeighbour(ipAddress, port);

                } else if(value == 9999) {
                    // Fail
                    System.out.println("Leave DS : Fail");
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
                    // Stat - Begin

                    /*
                    long timestamp = Long.parseLong(message[message.length-1]);
                    int hops = Integer.parseInt(message[message.length-2]);
                    String query = message[message.length-3];
                    SearchQueryStat searchQueryStat = new SearchQueryStat();
                    searchQueryStat.setQuery(query);
                    searchQueryStat.setFileName("<None>");
                    searchQueryStat.setReceivedTime(new Timestamp(System.currentTimeMillis()).getTime());
                    searchQueryStat.setSentTime(timestamp);
                    searchQueryStat.setHopsCount(hops);
                    SearchQueryStat.append(searchQueryStat);
                    */

                    // Stat - End
                    // No result
                    System.out.println("Search : No result");
                } else if (value > 0){
                    // has result
                    // Stat - Begin
                    long timestamp = Long.parseLong(message[message.length-1]);
                    int hops = Integer.parseInt(message[message.length-2]);
                    String query = message[message.length-3];
                    SearchQueryStat searchQueryStat = new SearchQueryStat();
                    searchQueryStat.setQuery(query);
                    searchQueryStat.setReceivedTime(new Timestamp(System.currentTimeMillis()).getTime());
                    searchQueryStat.setSentTime(timestamp);
                    searchQueryStat.setHopsCount(hops);

                    List<String> files = new ArrayList<String>();
                    Node node = new Node(message[3], Integer.parseInt(message[4]));
                    for(int i=5; i<message.length-3; i++) {
                        FileToLocationTable.add(message[i], node);
                        files.add(message[i]);
                    }

                    searchQueryStat.setFileName(Arrays.toString(files.toArray()));
                    SearchQueryStat.append(searchQueryStat);
                    // Stat - End

                    System.out.println("Search : Has result");
                }
            } else if(message[1].equals("ERROR")) {
                // Error
            } else if(message[1].equals("PULSE")) {
                // Generate pulse ok
            }
        } else {
            System.err.println("Terminate");
        }
    }

    public static void parseRequest(DatagramPacket receivePacket) {
        // Request parser
        // Received request
        String request = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("Request : " + request);
        // Extract sender info
        String senderIPAddress = receivePacket.getAddress().getHostAddress();
        int senderPort = receivePacket.getPort();

        String[] message = request.split(" ");
        if(message[1].equals("JOIN")) {
            // Send join OK reply
            generateJoinResponse(message, senderIPAddress, senderPort);
        } else if(message[1].equals("LEAVE")) {
            // Send leave OK reply
            generateLeaveResponse(message);
        } else if(message[1].equals("SER")) {
            generateSearchResponse(message);
        }
    }

    private static void registerBS2User(String[] message) {
        /*
            Connect to two nodes
         */

        // Create nodes
        Node node1 = new Node(message[3], Integer.parseInt(message[4]));
        Node node2 = new Node(message[5], Integer.parseInt(message[6]));
        String myIp = Configuration.getSystemIPAddress();
        int myPort = Configuration.getSystemPort();

        // Communicate with network
        DSConnection dsConnection = new DSConnection();

        // Join to first node
        String response1 = dsConnection.join(myIp, myPort, node1.getIpAddress(), node1.getPort());
        addAsANeighbour(node1, response1);

        // Join to second node
        String response2 = dsConnection.join(myIp, myPort, node2.getIpAddress(), node2.getPort());
        addAsANeighbour(node2, response2);
    }

    private static void registerBS1User(String[] message) {
        /*
            Connect to one node
         */

        // Create node
        Node node = new Node(message[3], Integer.parseInt(message[4]));
        String myIp = Configuration.getSystemIPAddress();
        int myPort = Configuration.getSystemPort();

        DSConnection dsConnection = new DSConnection();

        // Join to node
        String response = dsConnection.join(myIp, myPort, node.getIpAddress(), node.getPort());
        addAsANeighbour(node, response);
    }

    private static void addAsANeighbour(Node node, String response) {
        /*
            Based on the response, decide whether to add node as a neighbour
            then add accordingly
         */

        if(!response.equals("Timeout") && MessageTable.validate(response)){
            String[] responseChunk = response.split(" ");

            if(responseChunk[2].equals("0")) {
                // Add node as a neighbour for successful response
                Node.addNeighbour(node);
            }

            // Parse response
            Parser.parseResponse(response);

        } else {
            System.out.println(node.getIpAddress() + " " + node.getPort() + " is not reachable");
        }
    }

    private static void generateJoinResponse(String[] message, String ipAddress, int port) {
        /*
            Generate response for join request
         */

        // Extract timestamp from original message
        long timestamp = Long.parseLong(message[message.length - 1]);

        // Add neighbour
        Node.addNeighbour(new Node(message[2], Integer.parseInt(message[3])));

        // Send response to the node
        DSConnection dsConnection = new DSConnection();
        dsConnection.joinResponse(ipAddress, port, timestamp, 0);
    }

    private static void unregisterUser(String[] message) {
        /*
            Unregister node from network
         */

        DSConnection dsConnection = new DSConnection();
        String myIp = Configuration.getSystemIPAddress();
        int myPort = Configuration.getSystemPort();

        for(Node node: Node.getNeighbours()) {
            // Generate leave response
            String response = dsConnection.leave(myIp, myPort, node.getIpAddress(), node.getPort());

            if(!response.equals("Timeout") && MessageTable.validate(response)) {
                String[] responseChunk = response.split(" ");

                if(responseChunk[2].equals("0")) {
                    // Remove node from neighbours
                    Node.removeNeighbour(node.getIpAddress(), node.getPort());
                }
            } else {
                System.out.println(node.getIpAddress() + " " + node.getPort() + " is not reachable");
            }
        }
    }

    private static void generateLeaveResponse(String[] message) {
        /*
            Generate response for leave request
         */

        // Retrieve timestamp
        long timestamp = Long.parseLong(message[message.length - 1]);
        String ipAddress = message[2];
        int port = Integer.parseInt(message[3]);

        // Remove neighbour
        Node.removeNeighbour(ipAddress, port);

        DSConnection dsConnection = new DSConnection();
        dsConnection.leaveResponse(ipAddress, port, timestamp, 0);
    }

    private static void searchInNeighbours(String fileName, long timestamp, int hopsCount, String originalReceiverIp,
                                           int originalReceiverPort) {

        /*
            Search for a file within neighbours
         */
        List<Node> neighbours = FileToLocationTable.getLocations(fileName);
        for(Node node: Node.getNeighbours()) {
            neighbours.add(node);
        }

        DSConnection dsConnection = new DSConnection();
        String response = "";
        boolean haveNeighbours = false;

        hopsCount--;
        for(Node node: neighbours) {
            // Iterate through neighbours and forward request
            int maxAttempts = Configuration.getMaxAttempts();

            if(!ForwardTable.isForwarded(timestamp, fileName, node.getIpAddress(), node.getPort())) {
                // If request has not already forwarded from your node

                if(hopsCount <= 0) {
//                    // Maximum hop limit exceeded
//                    dsConnection.searchResponse(originalReceiverIp, originalReceiverPort, hopsCount, fileName,
//                            null, timestamp);
                    return;
                }

                dsConnection.forward(originalReceiverIp, originalReceiverPort, hopsCount, fileName, timestamp,
                        node.getIpAddress(), node.getPort());
                ForwardTable.add(timestamp, fileName, node);
            }
        }
    }

    private static void generateSearchResponse(String[] message) {
        /*
            Generate search
         */

        // Extract meta data from message
        String fileName = message[5];
        int hopsCount = Integer.parseInt(message[4]);
        String originalReceiverIp = message[2];
        int originalReceiverPort = Integer.parseInt(message[3]);
        long timestamp = Long.parseLong(message[message.length-1]);

        // Search for given file within the node
        List<String> resultSet = FileTable.search(fileName);

        if(resultSet.size() > 0) {
            // This node has the requested file. Generate response with results
            if(!ResponseTable.isResponded(timestamp, fileName, originalReceiverIp, originalReceiverPort)) {
                // Avoid initiating multiple responses for same request that comes through different neighbours
                DSConnection dsConnection = new DSConnection();
                dsConnection.searchResponse(originalReceiverIp, originalReceiverPort, hopsCount, fileName, resultSet,
                        timestamp);
                ResponseTable.add(timestamp, fileName, new Node(originalReceiverIp, originalReceiverPort));
                searchInNeighbours(fileName, timestamp, --hopsCount, originalReceiverIp, originalReceiverPort);
            }
        } else if(Node.getNeighbours().size() > 0){
            // This node does not have requested file and has neighbours
            // Decrement hop count and forward to neighbours
            searchInNeighbours(fileName, timestamp, --hopsCount, originalReceiverIp, originalReceiverPort);
        } else {
            // Node does not have neighbours
            DSConnection dsConnection = new DSConnection();
            dsConnection.searchResponse(originalReceiverIp, originalReceiverPort, hopsCount, fileName,null,
                    timestamp);
        }
    }
}
