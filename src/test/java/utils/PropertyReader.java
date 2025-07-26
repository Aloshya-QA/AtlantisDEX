package utils;

import java.io.*;
import java.util.Properties;

public final class PropertyReader {

    private static String propertiesPath = "src/test/resources/config.properties";
    private static volatile Properties properties;

    private PropertyReader() {
    }

    public static Properties readProperties() {
        properties = new Properties();
        try (InputStream inputStream = new FileInputStream(propertiesPath)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (properties.getProperty("config_file") != null) {
            Properties additional = getProperties(properties.getProperty("config_file"));
            properties.putAll(additional);
        }

        return properties;
    }

    public static Properties getProperties(String path) {
        propertiesPath = path;
        return readProperties();
    }

    public static String getProperty(String key) {
        if (properties == null) readProperties();
        return properties.getProperty(key);
    }

    public static void setProperty(String key, String value) {
        if (properties == null) readProperties();
        properties.setProperty(key, value);
    }

    public static void saveProperties() {
        if (properties == null) return;
        try (OutputStream output = new FileOutputStream(propertiesPath)) {
            properties.store(output, "Updated by PropertyReader");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeProperty(String key) {
        if (properties == null) readProperties();
        properties.remove(key);
    }

    public static void clearProperties() {
        properties.clear();
        saveProperties();
    }
}
