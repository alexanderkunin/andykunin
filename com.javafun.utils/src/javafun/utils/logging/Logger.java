package javafun.utils.logging;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

// Class imports that are always useful -- do not remove these
import org.apache.log4j.Category;

// Internal imports

/**
 * The Logger class is a static tool-type class which wraps the functionality of
 * a logging package. In this case, it wraps the functionality of log4j.
 * 
 * @author J. Panici
 */
public class Logger {

    // ---------------------------------------------------
    //
    // Constructors
    //
    // NOTE: If ctor is protected or private because
    // this is a static class/singleton or because
    // of the use of the named-constructor idiom
    // please note that here.
    //
    // This is a static utility class used for accessing
    // one Log4j Category called log.
    //
    // ---------------------------------------------------
    /**
     * Logger cannot be instantiated as a class, instead use its static utility
     * methods.
     */
    private Logger() {
    }

    // ---------------------------------------------------
    //
    // Public interfaces
    //
    // ---------------------------------------------------
    /**
     * An instance of log4j's Category class.
     */
    public static Category log = org.apache.log4j.Logger.getLogger(Logger.class.getName());

    public static void logLineTrace() {
        try {
            throw new Exception();
        } catch (Throwable t) {
            StackTraceElement element = t.getStackTrace()[1];
            Logger.debug(element.toString());
        }
    }

    /**
     * Causes an info message to sent to the log.
     */
    public static void info(String s) {
        log.info(s);
    }

    public static void info(Class< ? > c, String s) {
        Category aLog = org.apache.log4j.Logger.getLogger(c.getName());
        aLog.info(s);
    }

    /**
     * Causes a debug message to sent to the log.
     */
    public static void debug(String s) {
        // get the caller's class
        Class< ? > caller = Logger.class;
        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
        if (stackTraceElements != null && 1 <= stackTraceElements.length) {
            try {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                caller = loader.loadClass(stackTraceElements[1].getClassName());
            } catch (Throwable throwable2) {
            }
        }

        try {
            org.apache.log4j.Logger.getLogger(caller.getName()).debug(s);

        } catch (Exception e) {
            System.err.println("LOGGING ERROR:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void debug(String s, Throwable t) {
        // get the caller's class
        Class< ? > caller = Logger.class;
        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
        if (stackTraceElements != null && 1 <= stackTraceElements.length) {
            try {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                caller = loader.loadClass(stackTraceElements[1].getClassName());
            } catch (Throwable throwable2) {
            }
        }

        try {
            org.apache.log4j.Logger.getLogger(caller.getName()).debug(s, t);

        } catch (Exception e) {
            System.out.println("ERROR IN DEBUG");
            e.printStackTrace();
        }
    }

    public static void debug(Throwable t) {
        // get the caller's class
        Class< ? > caller = Logger.class;
        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
        if (stackTraceElements != null && 1 <= stackTraceElements.length) {
            try {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                caller = loader.loadClass(stackTraceElements[1].getClassName());
            } catch (Throwable throwable2) {
            }
        }

        try {
            org.apache.log4j.Logger.getLogger(caller.getName()).debug("Internal Error", t);

        } catch (Exception e) {
            System.out.println("ERROR IN DEBUG");
            e.printStackTrace();
        }
    }

    public static void debug(Class< ? > c, String s) {
        Category aLog = org.apache.log4j.Logger.getLogger(c.getName());
        aLog.debug(s);

    }

    /**
     * Causes a warn message to sent to the log.
     */
    public static void warn(String s) {
        log.warn(s);
    }

    /**
     * Causes a warn message to sent to the log.
     */
    public static void warn(String s, Throwable throwable) {
        log.warn(s, throwable);
    }

    public static void warn(Class< ? > c, String s) {
        Category aLog = org.apache.log4j.Logger.getLogger(c.getName());
        aLog.warn(s);
    }

    public static void warn(Throwable throwable) {
        log.warn(throwable);
    }

    /**
     * Causes an error message to sent to the log.
     */
    public static void error(String s) {
        log.error(s);
    }

    /**
     * Causes an error message to sent to the log.
     */
    public static void error(String s, Throwable t) {
        log.error(s, t);
    }

    public static void error(Class< ? > c, String s) {
        Category aLog = org.apache.log4j.Logger.getLogger(c.getName());
        aLog.error(s);
    }

    final public static void error(Throwable t) {
        error(t.getMessage(), t);
    }

    /**
     * Causes a fatal message to sent to the log.
     */
    public static void fatal(String s) {
        log.fatal(s);
    }

    public static void fatal(Class< ? > c, String s) {
        Category aLog = org.apache.log4j.Logger.getLogger(c.getName());
        aLog.fatal(s);
    }

    public static void fatal(Throwable t) {
        // get the caller's class
        Class< ? > caller = Logger.class;
        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
        if (stackTraceElements != null && 1 <= stackTraceElements.length) {
            try {
                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                caller = loader.loadClass(stackTraceElements[1].getClassName());
            } catch (Throwable throwable2) {
            }
        }

        try {
            org.apache.log4j.Logger.getLogger(caller.getName()).fatal("Internal Error", t);

        } catch (Exception e) {
            System.out.println("ERROR IN DEBUG");
            e.printStackTrace();
        }
    }

}
