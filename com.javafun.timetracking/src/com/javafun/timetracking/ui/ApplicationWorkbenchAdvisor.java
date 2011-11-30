package com.javafun.timetracking.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * This workbench advisor creates the window advisor, and specifies the
 * perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "com.javafun.core.perspective";

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(final IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	@Override
	public boolean preShutdown() {
		final Display display = Display.getCurrent();
		return MessageDialog.openQuestion(display.getActiveShell(), "Exit", "Are you sure you want to exit the application?");
	}

	@Override
	public void postShutdown() {
		super.postShutdown();
		UiUtils.shutDownApp(Display.getCurrent(), RWT.getSessionStore().getHttpSession());
	}
}
