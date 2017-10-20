package com.meteogroup.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesLoader {

  private static final Properties properties;

  static {
    properties = fromResource("application");
  }

  private PropertiesLoader() { }

  private static Properties fromResource(final String resource) {
    Properties properties = new Properties();
    try (InputStream in = PropertiesLoader.class.getClassLoader().getResourceAsStream(resource + ".properties")) {
      properties.load(in);
    } catch (IOException ex) {
      throw new IllegalStateException(String.format("Unable to fromResource %s.properties file.", resource));
    }

    return properties;
  }

  public static String getProperty(String propertyKey) {
    return properties.getProperty(propertyKey);
  }
}