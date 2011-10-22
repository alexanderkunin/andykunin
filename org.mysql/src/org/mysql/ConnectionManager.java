package org.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {
    private static final ConnectionManager _connectionManager = new ConnectionManager();

    public static final Connection getDatabaseInstance(String JDBCDriver, String URL, String user, String password)
        throws SQLException {
        Connection c = null;
        try {
            c = DriverManager.getConnection(URL, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return c;
    }

    public static final Connection getDatabaseInstance(String JDBCDriver, String URL) throws SQLException {
        Connection c = null;
        try {
            c = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return c;
    }

    public static ConnectionManager getDefaultInstance() {
        return _connectionManager;
    }

    public static Connection getConnection(String url, String user, String password) throws SQLException {
        Connection c = null;
        ClassLoader originalLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(org.gjt.mm.mysql.Driver.class.getClassLoader());
            c = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            Thread.currentThread().setContextClassLoader(originalLoader);
        }
        return c;
    }

    public void test() {
        String url = "jdbc:mysql://localhost:3308/ims?useOldAliasMetadataBehavior=true&zeroDateTimeBehavior=convertToNull";
        String user = "root";
        String password = "root";
        //        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from ims_item");
            while (rs.next()) {
                System.out.println(rs.getString("item_id"));
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void testProxool() {
        String url = "proxool.core:org.gjt.mm.mysql.Driver:jdbc:mysql://localhost:3308/ims?useOldAliasMetadataBehavior=true&zeroDateTimeBehavior=convertToNull";
        //            proxool.timetracking:org.gjt.mm.mysql.Driver: jdbc:mysql://localhost:3308/timetracking_dev?useOldAliasMetadataBehavior=true&zeroDateTimeBehavior=convertToNull
        String user = "root";
        String password = "root";
        //       Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        try {
            Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
        } catch (Throwable e1) {
            e1.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from ims_item");
            while (rs.next()) {
                System.out.println(rs.getString("item_id"));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Connection getProxoolConnection(String proxoolUrl, String user, String password) {
        //        String url = "proxool.core:org.gjt.mm.mysql.Driver:jdbc:mysql://localhost:3308/ims?useOldAliasMetadataBehavior=true&zeroDateTimeBehavior=convertToNull";
        //        String user = "root";
        //        String password = "root";
        //       Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        try {
            Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
        } catch (Throwable e1) {
            e1.printStackTrace();
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(proxoolUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static Connection openConnection(String url, String user, String password) {
        //        String url = "jdbc:mysql://localhost:3308/ims?useOldAliasMetadataBehavior=true&zeroDateTimeBehavior=convertToNull";
        //        String user = "root";
        //        String password = "root";
        try {
            Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
        } catch (Throwable e1) {
            e1.printStackTrace();
        }
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
