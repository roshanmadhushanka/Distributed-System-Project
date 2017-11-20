package model;

import dnl.utils.text.table.TextTable;
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
            if(evaluator.cosineSimilarity(query, file) > 0.3) {
                resultSet.add(file);
            }
        }
        return resultSet;
    }

    public static void add(List<String> files) {
        fileList = files;
    }

    public static void display() {
        System.out.println("> File Table");
        System.out.println("------------");
        for(String file: fileList) {
            System.out.println(file.replaceAll(" ", "_"));
        }
        System.out.println();
    }

    public static void displayAsTable() {
        System.out.println("File Table");
        System.out.println("==========");
        Object[][] data = new Object[fileList.size()][1];
        for(int i=0; i<fileList.size(); i++) {
            data[i][0] = fileList.get(i);
        }

        TextTable receivedStat = new TextTable(new String[] { "File Name" }, data);
        receivedStat.setAddRowNumbering(true);
        receivedStat.setSort(0);
        receivedStat.printTable();
        System.out.println();
    }
}
