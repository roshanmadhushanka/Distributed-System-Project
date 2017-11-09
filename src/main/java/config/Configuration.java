package config;

import model.FileTable;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class Configuration {
    /*
        Configuration
        =============
        Contains system configurations
     */

    // System info
    private static String systemIPAddress;
    private static int systemPort;
    private static String systemName;

    // Bootstrap server info
    private static String bsIpAddress;
    private static int bsPort;

    // Connection parameters
    private static int maxAttempts;


    public static String getSystemIPAddress() {
        return systemIPAddress;
    }

    public static void setSystemIPAddress(String systemIPAddress) {
        Configuration.systemIPAddress = systemIPAddress;
    }

    public static int getSystemPort() {
        return systemPort;
    }

    public static void setSystemPort(int systemPort) {
        Configuration.systemPort = systemPort;
    }

    public static String getSystemName() {
        return systemName;
    }

    public static void setSystemName(String systemName) {
        Configuration.systemName = systemName;
    }

    public static String getBsIpAddress() {
        return bsIpAddress;
    }

    public static void setBsIpAddress(String bsIpAddress) {
        Configuration.bsIpAddress = bsIpAddress;
    }

    public static int getBsPort() {
        return bsPort;
    }

    public static void setBsPort(int bsPort) {
        Configuration.bsPort = bsPort;
    }

    public static int getMaxAttempts() {
        return maxAttempts;
    }

    public static void setMaxAttempts(int maxAttempts) {
        Configuration.maxAttempts = maxAttempts;
    }

    public static void loadConfigurations() throws IOException {
        // Setup encryptor to load encrypted content (password)
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("jasypt");

        Properties props = new EncryptableProperties(encryptor);

//        // Load properties
//        props.load(new FileInputStream(System.getProperty("user.dir") + "/src/main/java/config/" +
//                "application.properties"));

        // Build configurations
        props.load(new FileInputStream(System.getProperty("user.dir") + "/" +
                "application.properties"));

        // Set properties
        setSystemName(props.getProperty("server.name"));
        setSystemIPAddress(props.getProperty("server.host"));

//        String host = InetAddress.getLocalHost().getHostAddress();
//        setSystemIPAddress(host);

        setSystemPort(Integer.parseInt(props.getProperty("server.port")));
        setBsIpAddress(props.getProperty("bootstrap.host"));
        setBsPort(Integer.parseInt(props.getProperty("bootstrap.port")));
        setMaxAttempts(Integer.parseInt(props.getProperty("connection.noOfMaxAttempts")));

        // Set files in file table
        String[] files = props.getProperty("files").split(";");
        List<String> fileList = Arrays.asList(files);
        FileTable.add(fileList);
    }

    public static void display() {
        System.out.println("> Configurations");
        System.out.println("----------------");
        System.out.println("Server Name    : " + getSystemName());
        System.out.println("Server IP      : " + getSystemIPAddress());
        System.out.println("Server Port    : " + getSystemPort());
        System.out.println("Bootstrap IP   : " + getBsIpAddress());
        System.out.println("Bootstrap Port : " + getBsPort());
        System.out.println();
    }
}
