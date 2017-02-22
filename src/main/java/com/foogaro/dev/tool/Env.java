package com.foogaro.dev.tool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by foogaro
 * Class to handle properties file.
 * System properties win over configuration file ones.
 */
public class Env {

    private static Properties properties = null;
    private final static String CONFIG_FILE = "configFile";
    private final static String DEFAULT_CONFIG_FILE_NAME = "env.properties";

    private static void loadProperties(String filename) {
        properties = new Properties();

        try {
            InputStream inputStream = Env.class.getClassLoader().getResourceAsStream(filename);
            if (inputStream != null) {
                try {
                    properties.load(inputStream);
                } catch (IOException e) {
                    throw new IllegalStateException("Error while loading the file: " + filename);
                }
            } else {
                throw new IllegalStateException("Property file \"" + filename + "\" not found in the classpath.");
            }
        } catch (Throwable t) {
            if (Boolean.parseBoolean(System.getProperty("skipConfigFile", "false"))) {
                // it's the sound of silence
            } else {
                throw new IllegalStateException(t);
            }
        }
    }

    public static String getProperty(String name) {
        return getProperty(name, null);
    }

    public static String getProperty(String name, String defaulValue) {
        if (properties == null) {
            synchronized (properties) {
                loadProperties(System.getProperty(CONFIG_FILE, DEFAULT_CONFIG_FILE_NAME));
            }
        }

        return System.getProperty(name, properties.getProperty(name, defaulValue));
    }

}
