package com.javafun.timetracking.ui;

import javax.servlet.http.HttpSession;

import org.eclipse.rwt.RWT;
import org.eclipse.rwt.internal.widgets.JSExecutor;
import org.eclipse.rwt.lifecycle.PhaseEvent;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.rwt.lifecycle.PhaseListener;
import org.eclipse.swt.widgets.Display;

public class UiUtils {
	public static void shutDownApp(final Display display, final HttpSession httpSession) {
		httpSession.removeAttribute("user");
		RWT.getLifeCycle().addPhaseListener(new PhaseListener() {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("restriction")
			public void afterPhase(final PhaseEvent event) {
				if (Display.getCurrent() == null || display == Display.getCurrent()) {
					String jscode = "self.close();";
					JSExecutor.executeJS(jscode);
					RWT.getLifeCycle().removePhaseListener(this);
				}
			}

			public PhaseId getPhaseId() {
				return PhaseId.ANY;
			}

			public void beforePhase(final PhaseEvent event) {
			};
		});
	}
}
