package sys;

import java.util.HashMap;

public class Config {
    private static HashMap<String, String> configuration = new HashMap<String, String>();

    public static void put(String config, String value) {
        configuration.put(config, value);
    }

    public static String get(String config) {
        return configuration.get(config);
    }

}
