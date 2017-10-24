import config.Configuration;
import connection.BootstrapConnection;
import model.FileTable;
import sys.Listener;
import sys.Parser;

import java.util.HashMap;

public class App {
    public static void main(String[] args) {
//        FileTable.add("Adventures of Tintin");
//        FileTable.add("Jack and Jill");
//        FileTable.add("Glee");
//        FileTable.add("The Vampire Diarie");
//        FileTable.add("King Arthur");
//        FileTable.add("Windows XP");
//        FileTable.add("Harry Potter");
//        FileTable.add("Kung Fu Panda");
//        FileTable.add("Lady Gaga");
//        FileTable.add("Twilight");
//        FileTable.add("Windows 8");
//        FileTable.add("Mission Impossible");
//        FileTable.add("Turn Up The Music");
//        FileTable.add("Super Mario");
//        FileTable.add("American Pickers");
//        FileTable.add("Microsoft Office 2010");
//        FileTable.add("Happy Feet");
//        FileTable.add("Modern Family");
//        FileTable.add("American Idol");
//        FileTable.add("Hacking for Dummies");
        HashMap<String[], String> map = new HashMap<String[], String>();
        String[] vec = new String[2];
        vec[0] = "a";
        vec[1] = "b";
        map.put(vec, "Roshan");
        System.out.println(map.get(vec));

    }
}
