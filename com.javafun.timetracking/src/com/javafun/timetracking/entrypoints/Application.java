package com.javafun.core.entrypoints;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import com.javafun.core.ui.ApplicationWorkbenchAdvisor;

/**
 * This class controls all aspects of the application's execution
 * and is contributed through the plugin.xml.
 */
public class Application implements IEntryPoint {

  public int createUI() {
    Display display = PlatformUI.createDisplay();
    WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();
    return PlatformUI.createAndRunWorkbench( display, advisor );
  }
}
