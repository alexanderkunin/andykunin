package javafun.utils.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javafun.utils.logging.Logger;

import org.mysql.ConnectionManager;

/**
 * @author ALexander K
 * Singleton that returns a java.sql.Connection object.
 */
public class JDBCConnection {

    private JDBCConnection() {
    }

    public static final Connection getDatabaseInstance(String JDBCDriver, String URL, String user, String password)
        throws SQLException {
        try {
            Class.forName(JDBCDriver);
        } catch (ClassNotFoundException e) {
            Logger.error("No Class Found " + e.getMessage());
            throw new SQLException(e.getMessage());
        }
        String params = "";
        // Add ConnectorJ configuration properties
        if (URL.indexOf("useOldAliasMetadataBehavior=true") < 0) {
            if (URL.indexOf("?") < 0) {
                params += "?";
            } else {
                params += "&";
            }
            params += "useOldAliasMetadataBehavior=true";
        }
        if (URL.indexOf("zeroDateTimeBehavior=convertToNull") < 0) {
            if (URL.indexOf("?") < 0 && params.indexOf("?") < 0) {
                params += "?";
            } else {
                params += "&";
            }
            params += "zeroDateTimeBehavior=convertToNull";
        }
        URL += params;
        Connection c = null;
        try {
            c = ConnectionManager.getConnection(URL, user, password);
        } catch (SQLException e) {
            Logger.error("General error getting connection from JDBCConnection " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return c;
    }
}
