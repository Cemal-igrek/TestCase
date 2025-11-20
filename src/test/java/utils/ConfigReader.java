package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);

    static {
        try {
            String path = "src/test/resources/config.properties";
            FileInputStream inputStream = new FileInputStream(path);
            properties = new Properties();
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            logger.error("❌ Config dosyası okunamadı: " + e.getMessage());
            throw new RuntimeException("Config file read error");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}