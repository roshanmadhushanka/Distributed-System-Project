package model;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private static List<Node> neighbours = new ArrayList<Node>(2);
    private String ipAddress;
    private int port;

    public Node(String host, int port) {
        this.ipAddress = host;
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static void addNeighbour(Node node) {
        if(!neighbours.contains(node)) {
            neighbours.add(node);
        }
    }

    public static void removeNeighbour(String host, int port) {
        int i=0;
        while(i<neighbours.size()) {
            if(neighbours.get(i).ipAddress.equals(host) && neighbours.get(i).port == port) {
                break;
            }
            i++;
        }
        neighbours.remove(i);
    }

    public static boolean contains(String host, int port) {
        int i=0;
        while(i<neighbours.size()) {
            if(neighbours.get(i).ipAddress.equals(host) && neighbours.get(i).port == port) {
                return true;
            }
            i++;
        }
        return false;
    }

    public static List<Node> getNeighbours() {
        return new ArrayList<Node>(neighbours);
    }

    public void describe() {
        // Debugging purpose only
        System.out.println("----------Node----------");
        System.out.println("IP address : " + ipAddress);
        System.out.println("Port       : " + port);
        System.out.println("------------------------");
    }

}
