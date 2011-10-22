package javafun.utils.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Properties;

import javafun.utils.Domain;
import javafun.utils.DomainContext;
import javafun.utils.StringUtils;
import javafun.utils.logging.Logger;

import org.apache.log4j.PropertyConfigurator;
import org.logicalcobwebs.proxool.ProxoolDriver;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.mysql.ConnectionManager;

/**
 * A database connection manager for server applications. This manager uses the
 * Proxool connection pool library to manage a pool of connecions.
 * 
 * @author
 */
public class ServerConnectionManager {
    //    private static final Log log = LogFactory.getLog(ServerConnectionManager.class);
    protected static final String SQL_MODE = "set session sql_mode='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';";
    private static final String proxoolAlias = "timetracking";
    private boolean _shuttingDown;
    private static ServerConnectionManager _instance = new ServerConnectionManager();

    // Not null if the Isolation level has been specified in the configuration
    // file.
    // Otherwise, it is left to the Driver's default value.
    private Integer _isolation;
    private final Domain _domain; 
    private final String _proxoolUrl;

    /**
     * Initialize the connection provider from given properties.
     * 
     * @param props
     *            <tt>SessionFactory</tt> properties
     * @throws ProxoolException
     */
    private ServerConnectionManager() {
        _domain = DomainContext.getCurrentDomain();

        String domainUrl = _domain.getUrl();
        // /**
        // * strip off properties (everyting after the ?
        // * For some reason MYSQL doesn;t like the combination of properties
        // and Proxool
        // */
        // if (0 <= domainUrl.indexOf('?'))
        // domainUrl= domainUrl.substring(0, domainUrl.indexOf('?'));
        String params = "";
        // Add ConnectorJ configuration properties
        if (domainUrl.indexOf("useOldAliasMetadataBehavior=true") < 0) {
            if (domainUrl.indexOf("?") < 0) {
                params += "?";
            } else {
                params += "&";
            }
            params += "useOldAliasMetadataBehavior=true";
        }
        if (domainUrl.indexOf("zeroDateTimeBehavior=convertToNull") < 0) {
            if (domainUrl.indexOf("?") < 0 && params.indexOf("?") < 0) {
                params += "?";
            } else {
                params += "&";
            }
            params += "zeroDateTimeBehavior=convertToNull";
        }
        domainUrl += params;
        _proxoolUrl = "proxool." + proxoolAlias + ":" + _domain.getDriver() + ":" + domainUrl;

        Logger.info("Configuring Proxool Provider using " + _domain.getName() + " domain info");

        // cause Proxool driver to be loaded.
        Logger.debug("proxool driver loaded :" + ProxoolDriver.class.getName());

        Properties properties = new Properties();
        properties.setProperty("jdbc-0.proxool.alias", proxoolAlias);
        properties.setProperty("jdbc-0.proxool.driver-url", domainUrl);
        properties.setProperty("jdbc-0.proxool.user", _domain.getUser());
        properties.setProperty("jdbc-0.proxool.password", _domain.getPwd());
        properties.setProperty("jdbc-0.proxool.driver-class", _domain.getDriver());
        properties.setProperty("jdbc-0.proxool.maximum-connection-count", "100");
        properties.setProperty("jdbc-0.proxool.minimum-connection-count", "10");

        /*
         *  Should be set to the maximum number of threads that CORE is configured to use.
         *  See SystemUtils.asyncExec
         */
        properties.setProperty("jdbc-0.proxool.simultaneous-build-throttle", "20");

        properties.setProperty("jdbc-0.proxool.house-keeping-test-sql", "select CURRENT_DATE");
        properties.setProperty("jdbc-0.proxool.test-before-use", "true");
        properties.setProperty("jdbc-0.proxool.maximum-active-time", Long.toString(1000L * 60 * 60 * 8));

        PropertyConfigurator.configure(properties);

        // Append the stem to the proxool pool alias
        Logger.info("Configuring Proxool Provider to use pool alias: " + proxoolAlias);
        //			
        // // Remember Isolation level
        // isolation = PropertiesHelper.getInteger(Environment.ISOLATION,
        // props);
        // if (isolation!=null) {
        // log.info("JDBC isolation level: " +
        // Environment.isolationLevelToString( isolation.intValue() ) );
        // }

        // set up service termination hook (gets called
        // when the JVM terminates from a signal):
        //        Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));
    }

    /**
     * Grab a connection
     * 
     * @return a JDBC connection
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        // get a connection from the pool (through DriverManager, cfr. Proxool doc)

        //        try {
        //            Driver driver = DriverManager.getDriver(_proxoolUrl);
        //            Connection c = driver.connect(_proxoolUrl, null);
        //
        //            // set the Transaction Isolation if defined
        //            if (_isolation != null)
        //                c.setTransactionIsolation(_isolation.intValue());
        //
        //            // toggle autoCommit to false if set
        //            if (c.getAutoCommit())
        //                c.setAutoCommit(false);
        //            setSQLMode(c);
        //            // return the connection
        //            return c;
        //        } finally {
        //        }

        try {
            Connection c = ConnectionManager.getDefaultInstance().getProxoolConnection(_proxoolUrl, _domain.getUser(), _domain.getPwd());
            //            Connection c = ConnectionManager.getDatabaseInstance(_proxoolUrl, _proxoolUrl, "", "");
            // set the Transaction Isolation if defined
            if (_isolation != null) {
                c.setTransactionIsolation(_isolation.intValue());
            }
            // toggle autoCommit to false if set
            if (c.getAutoCommit()) {
                c.setAutoCommit(false);
            }
            setSQLMode(c);
            // return the connection
            return c;
        } finally {
            //            Thread.currentThread().setContextClassLoader(originalLoader);
        }
    }

    public void test() {
        //        ConnectionManager.getDefaultInstance().test();
        //        ConnectionManager.getDefaultInstance().testProxool();
    }

    /**
     * Dispose of a used connection.
     * 
     * @param conn
     *            a JDBC connection
     * @throws SQLException
     */
    public void closeConnection(Connection conn) throws SQLException {
        try {
            if (conn != null) {
                if (!conn.getAutoCommit()) {
                    conn.commit();
                }
                conn.close();
            }
        } catch (NoSuchElementException e) {
            // ignore
        }
    }

    /**
     * Release all resources held by this provider.
     * @throws HibernateException
     */
    public void close() {
        if (!_shuttingDown) {
            _shuttingDown = true;
            ProxoolFacade.shutdown(0);
            javafun.utils.logging.Logger.info("Released database connections");
        }
    }

    //    class ShutdownHook extends Thread {
    //        public ShutdownHook(ServerConnectionManager managedClass) {
    //            _managedClass = managedClass;
    //        }
    //        private ServerConnectionManager _managedClass;
    //        public void run() {
    //            Logger.info("ServerConnectionManager shutdown hook thread started");
    //            try {
    //                _managedClass.close();
    //            } catch (Exception e) {
    //                e.printStackTrace();
    //            }
    //            Logger.info("ServerConnectionManager shutdown hook thread finished");
    //        }
    //    }

    protected void setSQLMode(Connection _connection) {
        // See http://bugs.mysql.com/bug.php?id=18551
        PreparedStatement sqlMode = null;
        try {
            sqlMode = _connection.prepareStatement(SQL_MODE);
            sqlMode.execute();
            sqlMode.close();
            String rpcUser = System.getProperty("rpc_username");
            if (StringUtils.isBlank(rpcUser)) {
                rpcUser = _connection.getMetaData().getUserName();
            }
            sqlMode = _connection.prepareStatement("set @rpcuser='" + rpcUser + "'");
            sqlMode.execute();
        } catch (SQLException e) {
            // ignore
        } finally {
            if (sqlMode != null) {
                try {
                    sqlMode.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public static ServerConnectionManager getDefaultInstance() {
        return _instance;
    }
}
