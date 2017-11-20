package model;

import java.util.HashMap;

public class ResponseTable {
    private static HashMap<String, Node> responseTable = new HashMap<String, Node>();

    public static void add(long timestamp, String fileName, Node node) {
        String vec = String.valueOf(timestamp) + fileName + node.getIpAddress() + String.valueOf(node.getPort());
        responseTable.put(vec, node);
    }

    public static boolean isResponded(long timestamp, String fileName, String ipAddress, int port) {
        String vec = String.valueOf(timestamp) + fileName + ipAddress + String.valueOf(port);
        if(responseTable.containsKey(vec)) {
            return true;
        }
        return false;
    }

    public static void clear() {
        responseTable = new HashMap<String, Node>();
    }
}
