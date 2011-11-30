package com.javafun.timetracking.security;

import java.net.URL;
import java.security.PrivilegedAction;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpSession;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.ILoginContextListener;
import org.eclipse.equinox.security.auth.LoginContextFactory;

import com.javafun.timetracking.internal.Activator;

public class LoginManager {
	private static final String JAAS_CONFIG_FILE = "/config/jaas_config.txt";

	public static IStatus login(final HttpSession httpSession, int atempt) {
		if (atempt > 2) {
			return Status.CANCEL_STATUS;
		}
		int i = ++atempt;
		String configName = "TIMETRACKING";
		Activator activator = Activator.getDefault();
		URL configUrl = activator.getBundle().getEntry(JAAS_CONFIG_FILE);

		final ILoginContext secureContext = LoginContextFactory.createContext(configName, configUrl);
		ILoginContextListener listener = new ILoginContextListener() {
			@Override
			public void onLogoutStart(final Subject subject) {
			}

			@Override
			public void onLogoutFinish(final Subject subject, final LoginException logoutException) {
			}

			@Override
			public void onLoginStart(final Subject subject) {
			}

			@Override
			public void onLoginFinish(final Subject subject, final LoginException loginException) {
				httpSession.removeAttribute("status");
				if (loginException != null && "Login canceled".equals(loginException.getMessage())) {
					httpSession.setAttribute("status", Status.CANCEL_STATUS);
				}
			}
		};
		try {
			secureContext.registerListener(listener);
			secureContext.login();
			// final IStatus status = (IStatus)
			// httpSession.getAttribute("status");
			secureContext.unregisterListener(listener);
			listener = null;
			return Status.OK_STATUS;
		} catch (LoginException e) {
			final IStatus status = (IStatus) httpSession.getAttribute("status");
			secureContext.unregisterListener(listener);
			listener = null;
			if (status != null) {
				return status;
			}
			// IStatus status = new Status(IStatus.ERROR,
			// "com.javafun.timetracking", "Login failed", e);
			// ErrorDialog.openError(null, "Error",
			// "Login failed. Incorrect Username or password", status);
			return login(httpSession, i);
		}
	}

	private static PrivilegedAction getRunAction() {
		return new PrivilegedAction() {
			public Object run() {
				// int result = PlatformUI.createAndRunWorkbench( display,
				// new ApplicationWorkbenchAdvisor() );
				return new Integer(0);
			}
		};
	}
}
