package com.javafun.timetracking.entrypoints;

import org.eclipse.core.runtime.Status;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.internal.widgets.JSExecutor;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import com.javafun.timetracking.model.User;
import com.javafun.timetracking.security.LoginManager;
import com.javafun.timetracking.ui.SettingsWorkbenchAdvisor;

/**
 * @author Alexander Kunin
 */
public class Settings implements IEntryPoint {

	public int createUI() {
		// String configName = "KeyStore";
		// String configName = "UNIX";
		User user = (User) RWT.getSessionStore().getHttpSession().getAttribute("user");
		if (user == null) {
			if (Status.CANCEL_STATUS == LoginManager.login(RWT.getSessionStore().getHttpSession(), 0)) {
				String jscode = "window.location.href = 'index.html';";
				JSExecutor.executeJS(jscode);
				return -1;
			}
		}
		Display display = PlatformUI.createDisplay();
		WorkbenchAdvisor advisor = new SettingsWorkbenchAdvisor();
		RWT.getSessionStore().getHttpSession().setMaxInactiveInterval(900);
		display.disposeExec(new Runnable() {
			public void run() {
				// Perform cleanup
				RWT.getSessionStore().getHttpSession().removeAttribute("user");
			}
		});
		return PlatformUI.createAndRunWorkbench(display, advisor);
	}
}
