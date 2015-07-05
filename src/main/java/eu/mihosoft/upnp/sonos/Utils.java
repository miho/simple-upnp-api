package eu.mihosoft.upnp.sonos;

import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * Based on code from 
 * <a href="https://github.com/bertjan/sonos-java-api">
 * https://github.com/bertjan/sonos-java-api</a>
 */
public class Utils {

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {

        }
    }

    private static void setLogLevel(String name, ch.qos.logback.classic.Level level) {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(name)).setLevel(level);
    }

    public static void configureLogging() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        setLogLevel("ROOT", Level.INFO);
        setLogLevel("org.fourthline.cling", Level.ERROR);
        setLogLevel("org.tensin.sonos.control", Level.ERROR);
    }

}
