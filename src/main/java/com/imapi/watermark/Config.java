package com.imapi.watermark;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: ivan_bondarenko
 * Date: 12/8/13
 * Time: 11:05 PM
 * To change this template use File | Settings | File Templates.
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

    public static Configuration getConfig() {
        return config;
    }
}
