package javafun.utils.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafun.utils.Domain;
import javafun.utils.DomainContext;
import javafun.utils.logging.Logger;

/**
 * A connection manager for client-side, desktop applications. This manager
 * creates a single physical connection to the underlying database. This manager
 * returns proxies to the application that wrap the physical DB connection.
 * 
 * If any call to the physical DB connection results in a sql exception then a
 * new physical connection will be created on the call to the createConnection
 * method. A physical DB connection is closed when all its proxies are closed.
 * 
 * Physical connections are dumped whenever they fail for any reason and a new
 * connection is created and the operation is retried.
 * 
 * This strategy for managing database connection is highly efficient for
 * desktop applications as it usually only creates a single database connection
 * and it keeps that connection open.
 * 
 * @see ServerConnectionManager
 * 
 * @author ted stockwell
 */
public class DesktopConnectionManager extends JDBCConnectionManager {
    /**
     * The physical connection to the DB
     */
    private final Map<Connection, List<InvocationHandler>> _proxiesByConnection = new HashMap<Connection, List<InvocationHandler>>();

    /**
     * The physical connection to the DB.
     */
    private Connection _connection = null;

    synchronized void closeProxy(ConnectionHandler proxy, Connection physicalConnection) throws SQLException {
        List<InvocationHandler> proxies = _proxiesByConnection.get(physicalConnection);
        if (proxies == null) {
            closePhysicalConnection(physicalConnection);
        } else {
            proxies.remove(proxy);
            if (proxies.isEmpty()) {
                _proxiesByConnection.remove(physicalConnection);
                closePhysicalConnection(physicalConnection);
            } else {
                Logger.debug("Database connection proxy closed.  " + proxies.size() + " proxies still open");
            }
        }
    }

    private void closePhysicalConnection(Connection physicalConnection) throws SQLException {
        if (physicalConnection == _connection) {
            _connection = null;
        }
        if (physicalConnection != null) {
            physicalConnection.close();
        }
    }

    synchronized void getNewProxyConnection(ConnectionHandler proxy) throws SQLException {
        try {
            closeProxy(proxy, proxy._handlerConnection);
        } catch (Throwable t) {
        }
        if (_connection == null) {
            Domain domain = DomainContext.getCurrentDomain();
            //            _connection = JDBCConnection.getDatabaseInstance(domain.getDriver(), domain.getUrl(), domain.getUser(), domain.getPwd());
            _connection = JDBCConnection.getDatabaseInstance(domain.getDriver(), domain.getUrl(), domain.getUser(), domain.getPwd());
        }
        proxy._handlerConnection = _connection;
        List<InvocationHandler> proxies = _proxiesByConnection.get(_connection);
        if (proxies == null) {
            proxies = new ArrayList<InvocationHandler>();
            _proxiesByConnection.put(_connection, proxies);
        }
        proxies.add(proxy);
    }

    /** 
     * @see com.rpc.core.utils.JDBCConnectionManager#getConnection()
     */
    synchronized public Connection getConnection() throws SQLException {
        if (_connection == null) {
            Domain domain = DomainContext.getCurrentDomain();
            _connection = JDBCConnection.getDatabaseInstance(domain.getDriver(), domain.getUrl(), domain.getUser(), domain.getPwd());
            setSQLMode(_connection);
        } else {
            try {
                pingConnection();
            }
            // for some reason connection is no good, reconnect
            catch (Throwable t) {
                if (_connection != null) {
                    _proxiesByConnection.remove(_connection);
                }
                Domain domain = DomainContext.getCurrentDomain();
                _connection = JDBCConnection.getDatabaseInstance(domain.getDriver(), domain.getUrl(), domain.getUser(), domain.getPwd());
            }
        }
        Connection proxy = (Connection) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { Connection.class }, new ConnectionHandler(
            _connection));

        List<InvocationHandler> proxies = _proxiesByConnection.get(_connection);
        if (proxies == null) {
            proxies = new ArrayList<InvocationHandler>();
            _proxiesByConnection.put(_connection, proxies);
        }
        proxies.add(Proxy.getInvocationHandler(proxy));
        return proxy;
    }

    private void pingConnection() throws SQLException {
        /*
         * ping the connection, if the ping fails then get a new connection and
         * retry the method call.
         */
        Statement pingStatement = _connection.createStatement();
        try {
            pingStatement.execute("select CURRENT_DATE");
        } finally {
            try {
                if (pingStatement != null) {
                    pingStatement.close();
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * @see com.rpc.core.utils.JDBCConnectionManager#closeConnection(java.sql.Connection)
     */
    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    class ConnectionHandler implements InvocationHandler {
        Connection _handlerConnection;

        /**
         * Set to true if this handler actually changes the state
         * of the underlying connection's autoCommit attribute to true.
         * If true then autoCommit is set back to false when the handler is closed.
         */
        boolean _resetAutoCommit = false;

        public ConnectionHandler(Connection connection) {
            _handlerConnection = connection;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("close")) {
                try {
                    closeProxy(this, _handlerConnection);
                } catch (SQLException x) {
                    throw new InvocationTargetException(x);
                } finally {
                    /**
                     * The underlying connection's autoCommit state was changed to true by this handler.
                     * Set the auto commit back to false.
                     */
                    if (_resetAutoCommit) {
                        try {
                            _handlerConnection.setAutoCommit(true);
                        } catch (Throwable t) {
                        }
                    }
                }
                return null;
            }
            if (method.getName().equals("setAutoCommit")) {
                boolean value = ((Boolean) args[0]).booleanValue();
                if (_handlerConnection.getAutoCommit() != value) {
                    if (value == false) {
                        _resetAutoCommit = true;
                    }
                    if (_resetAutoCommit && value == true) {
                        _resetAutoCommit = false;
                    }
                    _handlerConnection.setAutoCommit(value);
                }
                return null;
            }

            try {
                return method.invoke(_handlerConnection, args);
            } catch (InvocationTargetException e) {

                /*
                 * Some error happened during a method call. Make sure that we
                 * create a new physical connection for the next call to
                 * getConnection.
                 */
                synchronized (DesktopConnectionManager.this) {
                    if (_handlerConnection == _connection) {
                        _connection = null;
                    }
                }

                // if we're in the middle of a transaction then we're hosed, give up...
                if (_handlerConnection.getAutoCommit() == true) {
                    throw e.getCause();
                }

                // Don't trust the current connection, get a new connection
                // and try again
                try {
                    getNewProxyConnection(this);
                } catch (SQLException x2) {
                    throw e; // give up
                }
                try {
                    return method.invoke(_handlerConnection, args);
                } catch (InvocationTargetException e2) {
                    throw e; // give up
                }
            }
        }
    }
}
