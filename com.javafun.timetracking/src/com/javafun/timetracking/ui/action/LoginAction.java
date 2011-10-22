package com.javafun.core.ui.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafun.utils.jdbc.ServerConnectionManager;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.rwt.RWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.javafun.core.model.User;
import com.javafun.core.ui.ICommandIds;
import com.javafun.core.ui.dialog.LoginDialog;

/**
 * When run, this action will show a message dialog.
 */
public class LoginAction extends Action {

    private final IWorkbenchWindow window;

    public LoginAction(String text, IWorkbenchWindow window) {
        super(text);
        this.window = window;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_LOGIN);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_LOGIN);
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("com.javafun.core", "/icons/sample3.gif"));
    }

    public void run() {
        try {
            LoginDialog dilog = new LoginDialog(window.getShell());
            if (dilog.open() == Dialog.OK) {
                User user = dilog.getUser();

                Connection conn = null;
                try {
                    conn = ServerConnectionManager.getDefaultInstance().getConnection();
                    PreparedStatement pstmt = conn.prepareStatement("select * from user where user_name = ?");
                    pstmt.setString(1, user.getUserName());
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        String psw = rs.getString("pswrd");
                        if (user.getPswrd().equals(psw)) {
                            user.setEmail(rs.getString("email"));
                            RWT.getSessionStore().getHttpSession().setAttribute("user", user);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //        MessageDialog.openInformation(window.getShell(), "Open", "Open Message Dialog!");
    }
}