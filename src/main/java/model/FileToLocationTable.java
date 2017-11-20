package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileToLocationTable {
    private static HashMap<String, List<Node>> fileToLocation = new HashMap<String, List<Node>>();

    private static boolean hasNeighbour(String fileName, Node neighbour){
        List<Node> neighbours = fileToLocation.get(fileName);
        for(Node n: neighbours) {
            if(n.getIpAddress().equals(neighbour.getIpAddress()) && n.getPort() == neighbour.getPort()) {
                return true;
            }
        }
        return false;
    }

    public static void add(String fileName, Node neighbour) {
        /*
            Add location to a given file
         */

        if(fileToLocation.containsKey(fileName)) {
            if(!hasNeighbour(fileName, neighbour)) {
                fileToLocation.get(fileName).add(neighbour);
            }
        } else {
            List<Node> neighbours = new ArrayList<Node>();
            neighbours.add(neighbour);
            fileToLocation.put(fileName, neighbours);
        }
    }

    public static boolean hasLocation(String fileName) {
        /*
            Check whether the given file has previous node locations
         */

        return fileToLocation.containsKey(fileName);
    }

    public static List<Node> getLocations(String fileName) {
        /*
            Return available locations for a given file
         */
        List<Node> results = fileToLocation.get(fileName);
        if(results == null) {
            results = new ArrayList<Node>();
        }
        return new ArrayList<Node>(results);
    }

    public static void removeLocation(String fileName, Node neighbour) {
        /*
            Remove locations for a given file
         */

        if(fileToLocation.containsKey(fileName)) {
            if(fileToLocation.get(fileName).contains(neighbour)) {
                fileToLocation.get(fileName).remove(neighbour);
            }
        }
    }

    public static HashMap<String, List<Node>> getFileToLocation() {
        /*
            Get file to location table
         */

        return fileToLocation;
    }

    public static void clear() {
        fileToLocation = new HashMap<String, List<Node>>();
    }

}
