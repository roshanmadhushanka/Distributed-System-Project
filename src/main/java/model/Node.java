package model;

import dnl.utils.text.table.TextTable;
import stat.SearchQueryStat;

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

    public static synchronized void removeNeighbour(String host, int port) {
        int i=0;
        while(i<neighbours.size()) {
            if(neighbours.get(i).ipAddress.equals(host) && neighbours.get(i).port == port) {
                break;
            }
            i++;
        }

        if(i<neighbours.size()) {
            neighbours.remove(i);
        }
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

    public static void displayAsTable() {
        System.out.println("Neighbours");
        System.out.println("==========");
        Object[][] data = new Object[neighbours.size()][2];
        for(int i=0; i<neighbours.size(); i++) {
            Node node = neighbours.get(i);
            data[i][0] = node.ipAddress;
            data[i][1] = node.port;
        }

        TextTable searchStat = new TextTable(new String[]{ "IP Address", "Port" }, data);
        searchStat.setAddRowNumbering(true);
        searchStat.setSort(0);
        searchStat.printTable();
        System.out.println();
    }

}
