package com.javafun.core.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * Configures the initial size and appearance of a workbench window.
 */
public class SettingsWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public SettingsWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new SettingsActionBarAdvisor(configurer);
    }

    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        // configurer.setInitialSize(new Point(600, 400));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        //        configurer.setShellStyle(SWT.NONE);
        configurer.setShellStyle(SWT.NO_TRIM);
        configurer.setTitle("Application");
    }

    public void postWindowCreate() {
        Shell shell = getWindowConfigurer().getWindow().getShell();
        shell.setMaximized(true);
    }

}
