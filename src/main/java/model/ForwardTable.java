package model;

import java.util.HashMap;

public class ForwardTable {
    private static HashMap<String, Node> forwardTable = new HashMap<String, Node>();

    public static void add(long timestamp, String fileName, Node node) {
        String vec = String.valueOf(timestamp) + fileName + node.getIpAddress() + String.valueOf(node.getPort());
        forwardTable.put(vec, node);
    }

    public static boolean isForwarded(long timestamp, String fileName, String ipAddress, int port) {
        String vec = String.valueOf(timestamp) + fileName + ipAddress + String.valueOf(port);
        if(forwardTable.containsKey(vec)) {
            return true;
        }
        return false;
    }

    public static void clear() {
        forwardTable = new HashMap<String, Node>();
    }

}
