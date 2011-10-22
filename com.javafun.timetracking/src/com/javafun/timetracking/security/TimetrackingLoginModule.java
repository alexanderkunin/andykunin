package com.javafun.timetracking.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javafun.utils.jdbc.ServerConnectionManager;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.login.LoginException;

import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import com.javafun.timetracking.model.User;

public class TimetrackingLoginModule implements javax.security.auth.spi.LoginModule {

    private CallbackHandler callbackHandler;
    private boolean loggedIn;
    private Subject subject;

    public TimetrackingLoginModule() {
    }

    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ? > sharedState,
            Map<String, ? > options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
    }

    public boolean login() throws LoginException {
        Callback label = new TextOutputCallback(TextOutputCallback.INFORMATION, "Please login!");
        NameCallback nameCallback = new NameCallback("Username/email:");
        PasswordCallback passwordCallback = new PasswordCallback("Password:", false);
        try {
            callbackHandler.handle(new Callback[] { label, nameCallback, passwordCallback });
        } catch (Exception e) {
            e.printStackTrace();
        }

        String username = nameCallback.getName();

        String password = "";
        if (passwordCallback.getPassword() != null) {
            password = String.valueOf(passwordCallback.getPassword());
        }
        User user = null;
        Connection conn = null;
        try {
            conn = ServerConnectionManager.getDefaultInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from user where user_name = ?");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String psw = rs.getString("pswrd");
                if (password.equals(psw)) {
                    user = new User(username, psw);
                    user.setEmail(rs.getString("email"));
                    RWT.getSessionStore().getHttpSession().setAttribute("user", user);
                    // this.window.getWorkbench()
                } else {
                    RWT.getSessionStore().getHttpSession().removeAttribute("user");
                }
            }
            pstmt.close();
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

        if (user != null) {
            loggedIn = true;
            return true;
        }
        return false;
    }

    public boolean commit() throws LoginException {
        //                subject.getPublicCredentials().add(USERS);
        subject.getPrivateCredentials().add(Display.getCurrent());
        subject.getPrivateCredentials().add(SWT.getPlatform());
        return loggedIn;
    }

    public boolean abort() throws LoginException {
        loggedIn = false;
        return true;
    }

    public boolean logout() throws LoginException {
        loggedIn = false;
        return true;
    }
}
