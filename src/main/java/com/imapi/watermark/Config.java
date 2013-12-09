package com.imapi.watermark;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Configuration helper. Uses apache commons configuration.
 */
public class Config {
    private static Configuration config;

    static {
        try {
            config = new PropertiesConfiguration("default.properties");
        } catch (ConfigurationException e) {
            config = new BaseConfiguration();
        }
    }

    /**
     * Getter for configuration object.
     *
     * @return Properties file or empty configuration
     */
    public static Configuration getConfig() {
        return config;
    }
}
