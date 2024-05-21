package cn.stars.starx.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class StarXLogger {
    // Enhanced Logger
    private static final Logger logger = LogManager.getLogger("StarX");
    private static final String prefix = "[StarX] ";
    private static final Exception exception = new Exception("StarX Internal Exception");
    private static final Marker marker = MarkerManager.getMarker("StarX Internal Marker");
    public static final String mcl = "(Minecraft) ";

    public static Logger getStarXLogger() {
        return logger;
    }

    public static Exception getStarXException() {
        return exception;
    }

    public static Marker getStarXMarker() {
        return marker;
    }

    public static void info(String s) {
        logger.info(prefix + s);
    }

    public static void warn(String s) {
        logger.warn(prefix + s);
    }

    public static void error(String s) {
        logger.error(prefix + s);
    }

    public static void error(String s, Exception e) {
        logger.error(prefix + s, e);
    }

    public static void error(String s, Throwable t) {
        logger.error(prefix + s, t);
    }

    public static void debug(String s) {
        logger.debug(prefix + s);
    }

    public static void debug(String s, Exception e) {
        logger.debug(prefix + s, e);
    }

    public static void debug(String s, Throwable t) {
        logger.debug(prefix + s, t);
    }

    public static void fatal(String s) {
        logger.fatal(prefix + s);
    }

    public static void fatal(String s, Exception e) {
        logger.fatal(prefix + s, e);
    }

    public static void fatal(String s, Throwable t) {
        logger.fatal(prefix + s, t);
    }
}
