package javafun.utils.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafun.utils.StringUtils;
import javafun.utils.logging.Logger;

/**
 * A singleton for managing JDBC connections. This class supplies database
 * connections to the DatabaseManager class.
 * 
 * This singlton has different implementations for servers and client
 * applications.
 * 
 * @author ted stockwell
 */
abstract public class JDBCConnectionManager {

    //  Preserve compatibility with old queries, yet add some 5.x strictness...
    // (this actually improves performance slightly too)
    protected static final String SQL_MODE = "set session sql_mode='NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';";

    private static JDBCConnectionManager __instance = new DesktopConnectionManager();
    static {
        Logger.info("Using the default JDBC connection manager");
    }

    public static final JDBCConnectionManager getDefaultInstance() throws SQLException {
        return __instance;
    }

    public static final void setConnectionManager(JDBCConnectionManager connectionManager) {
        __instance = connectionManager;
        Logger.info("JDBC connection manager set to " + connectionManager);
    }

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

    abstract public Connection getConnection() throws SQLException;

    abstract public void closeConnection(Connection connection) throws SQLException;

}
