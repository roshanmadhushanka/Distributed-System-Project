package model;

import java.util.HashMap;

public class ForwardTable {
    private static HashMap<String, Node> forwardTable = new HashMap<String, Node>();

    public static void add(String fileName, Node node) {
        add(fileName, node);
    }

    public static Node get(String fileName) {
        return forwardTable.get(fileName);
    }
}
