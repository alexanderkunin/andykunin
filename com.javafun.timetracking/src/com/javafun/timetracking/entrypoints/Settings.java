package com.javafun.timetracking.entrypoints;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import com.javafun.timetracking.model.User;
import com.javafun.timetracking.ui.SettingsWorkbenchAdvisor;

/**
 * @author Alexander Kunin
 * 
 */
public class Settings implements IEntryPoint {

	public int createUI() {
		// String configName = "KeyStore";
		// String configName = "UNIX";
		User user = (User) RWT.getSessionStore().getHttpSession()
				.getAttribute("user");
		if (user == null) {
			if (Status.CANCEL_STATUS == LoginManager.login(0)) {
				IStatus status = new Status(IStatus.ERROR,
						"com.javafun.timetracking", "Login failed", null);
				ErrorDialog.openError(null, "Error",
						"Login failed. Contact with administrator", status);
				return -1;
			}
		}
		Display display = PlatformUI.createDisplay();
		WorkbenchAdvisor advisor = new SettingsWorkbenchAdvisor();
		RWT.getSessionStore().getHttpSession().setMaxInactiveInterval(900);
		display.disposeExec(new Runnable() {
			public void run() {
				// Perform cleanup
				// RWT.getSessionStore().removeAttribute("user");
			}
		});
		return PlatformUI.createAndRunWorkbench(display, advisor);

	}
}
