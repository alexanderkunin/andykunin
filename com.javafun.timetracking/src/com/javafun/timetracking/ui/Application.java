package com.javafun.timetracking.ui;

import java.net.URL;

import javax.security.auth.login.LoginException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import com.javafun.timetracking.internal.Activator;
import com.javafun.timetracking.model.User;

/**
 * This class controls all aspects of the application's execution
 * and is contributed through the plugin.xml.
 */
public class Application implements IEntryPoint {
    private static final String JAAS_CONFIG_FILE = "/config/jaas_config.txt";

    public int createUI() {
        // String configName = "KeyStore";
        // String configName = "UNIX";
        User user = (User) RWT.getSessionStore().getHttpSession().getAttribute("user");
        if (RWT.getSessionStore().getHttpSession().getAttribute("user") == null) {
            if (Status.CANCEL_STATUS == login(0)) {
                IStatus status = new Status(IStatus.ERROR, "com.javafun.timetracking", "Login failed", null);
                ErrorDialog.openError(null, "Error", "Login failed. Contact with administrator", status);
                return -1;
            }
        }
        Display display = PlatformUI.createDisplay();
        WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();
        RWT.getSessionStore().getHttpSession().setMaxInactiveInterval(900);
        display.disposeExec(new Runnable() {
            public void run() {
                // Perform cleanup
                //                RWT.getSessionStore().removeAttribute("user");
            }
        });
        return PlatformUI.createAndRunWorkbench(display, advisor);

    }

    private IStatus login(int atempt) {
        if (atempt > 3) {
            return Status.CANCEL_STATUS;
        }
        int i = ++atempt;
        String configName = "TIMETRACKING";
        Activator activator = Activator.getDefault();
        URL configUrl = activator.getBundle().getEntry(JAAS_CONFIG_FILE);
        ILoginContext secureContext = LoginContextFactory.createContext(configName, configUrl);
        try {
            secureContext.login();
            return Status.OK_STATUS;
        } catch (LoginException e) {
            IStatus status = new Status(IStatus.ERROR, "com.javafun.timetracking", "Login failed", e);
            ErrorDialog.openError(null, "Error", "Login failed. Incorrect Username or password", status);
            return login(i);
        }
    }
}
