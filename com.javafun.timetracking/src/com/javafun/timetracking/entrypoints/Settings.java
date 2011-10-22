/**
 * 
 */
package com.javafun.timetracking.entrypoints;

import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import com.javafun.timetracking.ui.SettingsWorkbenchAdvisor;

/**
 * @author Alexander Kunin
 *
 */
public class Settings implements IEntryPoint {

    public Settings() {
    }

    /* (non-Javadoc)
     * @see org.eclipse.rwt.lifecycle.IEntryPoint#createUI()
     */
    @Override
    public int createUI() {
        Display display = PlatformUI.createDisplay();
        WorkbenchAdvisor advisor = new SettingsWorkbenchAdvisor();
        return PlatformUI.createAndRunWorkbench(display, advisor);
    }

}
