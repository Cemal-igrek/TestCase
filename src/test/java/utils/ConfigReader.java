package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        try {
            // config.properties dosyasını okur
            String path = "src/test/resources/config.properties";
            FileInputStream inputStream = new FileInputStream(path);
            properties = new Properties();
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException("Config file read error");
        }
    }
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
