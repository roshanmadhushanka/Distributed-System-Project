package config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class Configuration {
    public static Properties getProperties() throws IOException {
        /*
            Load system configurations
         */

        // Setup encryptor to load encrypted content (password)
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("jasypt");

        // Load properties
        Properties props = new EncryptableProperties(encryptor);
        props.load(new FileInputStream(System.getProperty("user.dir") + "/src/main/java/config/" +
                "application.properties"));

        return props;
    }
}
