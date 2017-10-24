package model;

import sys.Evaluator;

import java.util.ArrayList;
import java.util.List;

public class FileTable {

    private static List<String> fileList = new ArrayList<String>(5);

    public static void add(String fileName) {
        fileList.add(fileName);
    }

    public static List<String> search(String query) {
        List<String> resultSet = new ArrayList<String>();
        Evaluator evaluator = new Evaluator();
        for(String file: fileList) {
            System.out.println(evaluator.cosineSimilarity(query, file));
        }
        return resultSet;
    }


}
