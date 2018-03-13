package fr.ensibs.socialnetwork.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A class to give access to configuration properties stored in a user defined
 * file or in the application resources. The properties are loaded in this
 * order:
 * <ul>
 * <li>from the application resources</li>
 * <li>from a default file in the user home directory</li>
 * <li>from a file given as a vm property</li>
 * <li>from an input stream given through the addProperties method</li>
 * </ul>
 * The names of the vm property and the default file are defined by CONFIG_NAME
 *
 * @author Pascale Launay
 */
public class ConfigurationManager {

    public static final String CONFIG_NAME = "socialnetwork.conf";
    public static final String SERVER_HOST = "SERVER_HOST";
    public static final String RMI_PORT = "RMI_PORT";
    public static final String RMI_OBJ = "RMI_OBJ";
    public static final String JMS_PORT = "JMS_PORT";
    public static final String JMS_HOME = "JMS_HOME";
    public static final String AXIS2_PORT = "AXIS2_PORT";
    public static final String AXIS2_HOME = "AXIS2_HOME";
    public static final String AXIS2_SERVICE = "AXIS2_SERVICE";
    public static final String RIVER_PORT = "RIVER_PORT";

    private static ConfigurationManager instance;

    private final Properties properties;

    /**
     * Give a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    /**
     * Constructor. Load the property from a file
     */
    private ConfigurationManager() {
        properties = new Properties();
        addProperties(getInternalInputStream());
        addProperties(getDefaultInputStream());
        addProperties(getInpuStreamFromProperties());
    }

    /**
     * Display the properties to the standard output
     */
    public void displayProperties() {
        for (String key : properties.stringPropertyNames()) {
            System.out.println(format(key) + "\t" + properties.getProperty(key));
        }
    }

    /**
     * Add properties from lines KEY=VALUE loaded from the given input stream
     *
     * @param in the input stream
     */
    public final void addProperties(InputStream in) {
        if (in != null) {
            Properties props = new Properties();
            try {
                props.load(in);
                for (String key : props.stringPropertyNames()) {
                    properties.put(key, props.getProperty(key));
                }
            } catch (IOException ex) {
                System.err.println("Unable to load properties");
            }
        }
    }

    /**
     * Give a string property having the given name
     *
     * @param name the property's name
     * @param defaultValue the property's default value
     * @return the value of the property, or defaultValue
     */
    public String getProperty(String name, String defaultValue) {
        String value = properties.getProperty(name);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    /**
     * Give an integer property having the given name
     *
     * @param name the property's name
     * @param defaultValue the property's default value
     * @return the value of the property, or defaultValue
     */
    public int getIntegerProperty(String name, int defaultValue) {
        String prop = properties.getProperty(name);
        if (prop != null) {
            try {
                return Integer.parseInt(prop);
            } catch (NumberFormatException e) {
                // DO NOTHING
            }
        }
        return defaultValue;
    }

    /**
     * Give an input stream from the CONFIG_NAME file in the application
     * resources
     *
     * @return the input stream or null if the file does not exist (should not
     * occur)
     */
    private InputStream getInternalInputStream() {
        InputStream in = ConfigurationManager.class.getResourceAsStream("/" + CONFIG_NAME);
        if (in == null) {
            System.err.println("[error] Application /" + CONFIG_NAME + " file not found");
        }
        return in;
    }

    /**
     * Give an input stream from the CONFIG_NAME file in the user home directory
     *
     * @return the input stream or null if the file does not exist
     */
    private InputStream getDefaultInputStream() {
        File homedir = new File(System.getProperty("user.home"));
        File file = new File(homedir, CONFIG_NAME);
        return getInputStream(file);
    }

    /**
     * Give an input stream from the file given as a CONFIG_NAME vm property
     *
     * @return the input stream or null if the property is not set or if the
     * file does not exist
     */
    private InputStream getInpuStreamFromProperties() {
        String filename = System.getProperty(CONFIG_NAME);
        if (filename != null) {
            File file = new File(filename);
            InputStream in = getInputStream(file);
            if (in == null) {
                System.err.println("[error] File " + file.getName() + " not found");
            }
            return in;
        }
        return null;
    }

    /**
     * Give an input stream from a given file
     *
     * @param file the file
     * @return the input stream or null if the file does not exist
     */
    private InputStream getInputStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            // DO NOTHING
        }
        return null;
    }

    /**
     * Format the given string to a fixed size string
     *
     * @param s the input string
     * @return the formatted string
     */
    private String format(String s) {
        if (s.length() < 15) {
            s += "          ";
        }
        return s.substring(0, 15);
    }
}
