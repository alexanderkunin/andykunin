package com.javafun.timetracking.ui.action;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.javafun.timetracking.ui.ICommandIds;

/**
 * When run, this action will show a message dialog.
 */
public class LoginPopupAction extends Action {

    private final IWorkbenchWindow window;

    public LoginPopupAction(String text, IWorkbenchWindow window) {
        super(text);
        this.window = window;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_OPEN_MESSAGE);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_OPEN_MESSAGE);
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("jcom.javafun.core", "/icons/sample3.gif"));
    }

    public void run() {
        //        try {
        //            LoginDialog dilog = new LoginDialog(window.getShell());
        //            if (dilog.open() == Dialog.OK) {
        //                User user = dilog.getUser();
        //
        //                Connection conn = null;
        //                try {
        //                    conn = ServerConnectionManager.getDefaultInstance().getConnection();
        //                    PreparedStatement pstmt = conn.prepareStatement("select * from user where user_name = ?");
        //                    pstmt.setString(1, user.getUserName());
        //                    ResultSet rs = pstmt.executeQuery();
        //                    if (rs.next()) {
        //                        String psw = rs.getString("pswrd");
        //                        if (user.getPswrd().equals(psw)) {
        //                            user.setEmail(rs.getString("email"));
        //                            RWT.getSessionStore().setAttribute("user", user);
        //                            // this.window.getWorkbench()
        //                        } else {
        //                            RWT.getSessionStore().removeAttribute("user");
        //                        }
        //                    }
        //                    pstmt.close();
        //                } catch (SQLException e) {
        //                    e.printStackTrace();
        //                } finally {
        //                    if (conn != null) {
        //                        try {
        //                            conn.close();
        //                        } catch (SQLException e) {
        //                            e.printStackTrace();
        //                        }
        //                    }
        //                }
        //            }
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }

        //        MessageDialog.openInformation(window.getShell(), "Open", "Open Message Dialog!");
    }
}