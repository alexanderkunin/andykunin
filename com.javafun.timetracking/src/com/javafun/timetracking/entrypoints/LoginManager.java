package com.javafun.timetracking.entrypoints;

import java.net.URL;

import javax.security.auth.login.LoginException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.eclipse.jface.dialogs.ErrorDialog;

import com.javafun.timetracking.internal.Activator;

public class LoginManager {
	private static final String JAAS_CONFIG_FILE = "/config/jaas_config.txt";


	public static IStatus login(int atempt) {
		if (atempt > 3) {
			return Status.CANCEL_STATUS;
		}
		int i = ++atempt;
		String configName = "TIMETRACKING";
		Activator activator = Activator.getDefault();
		URL configUrl = activator.getBundle().getEntry(JAAS_CONFIG_FILE);
		ILoginContext secureContext = LoginContextFactory.createContext(
				configName, configUrl);
		try {
			secureContext.login();
			return Status.OK_STATUS;
		} catch (LoginException e) {
			IStatus status = new Status(IStatus.ERROR,
					"com.javafun.timetracking", "Login failed", e);
			ErrorDialog.openError(null, "Error",
					"Login failed. Incorrect Username or password", status);
			return login(i);
		}
	}

}
