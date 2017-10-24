package model;

import java.util.HashMap;

public class ForwardTable {
    private static HashMap<String, Node> forwardTable = new HashMap<String, Node>();

    public static void add(long timestamp, String fileName, Node node) {
        String vec = String.valueOf(timestamp) + fileName;
        forwardTable.put(vec, node);
    }

    public static boolean isForwarded(long timestamp, String fileName) {
        String vec = String.valueOf(timestamp) + fileName;
        if(forwardTable.containsKey(vec)) {
            return true;
        }
        return false;
    }

}
