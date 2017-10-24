package model;

import java.util.HashMap;

public class ResponseTable {
    private static HashMap<String, Node> responseTable = new HashMap<String, Node>();

    public static void add(long timestamp, String fileName, Node node) {
        String vec = String.valueOf(timestamp) + fileName;
        responseTable.put(vec, node);
    }

    public static boolean isResponded(long timestamp, String fileName) {
        String vec = String.valueOf(timestamp) + fileName;
        if(responseTable.containsKey(vec)) {
            return true;
        }
        return false;
    }
}
