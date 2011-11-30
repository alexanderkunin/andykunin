package com.javafun.timetracking.ui;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import com.javafun.timetracking.ui.action.AboutAction;
import com.javafun.timetracking.ui.action.OpenViewAction;
import com.javafun.timetracking.ui.view.View;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class SettingsActionBarAdvisor extends ActionBarAdvisor {

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.
	private IWorkbenchAction _exitAction;
	private IAction _aboutAction;
	private OpenViewAction _openViewAction;

	public SettingsActionBarAdvisor(final IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(final IWorkbenchWindow window) {
		// Creates the actions and registers them.
		// Registering is needed to ensure that key bindings work.
		// The corresponding commands keybindings are defined in the plugin.xml
		// file.
		// Registering also provides automatic disposal of the actions when
		// the window is closed.

		_exitAction = ActionFactory.QUIT.create(window);
		register(_exitAction);

		_aboutAction = new AboutAction(window);
		register(_aboutAction);

		_openViewAction = new OpenViewAction(window, "View", View.ID);
		// openViewAction.setImageDescriptor(newImage);
		register(_openViewAction);

	}

	protected void fillMenuBar(final IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
		MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);

		menuBar.add(fileMenu);
		// Add a group marker indicating where action set menus will appear.
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(helpMenu);

		// File
		fileMenu.add(_openViewAction);
		fileMenu.add(new Separator());
		fileMenu.add(_exitAction);

		// Help
		helpMenu.add(_aboutAction);
	}

	protected void fillCoolBar(final ICoolBarManager coolBar) {
		IToolBarManager toolbar = new ToolBarManager(SWT.FLAT /* | SWT.RIGHT */);
		coolBar.add(new ToolBarContributionItem(toolbar, "index.html"));
		toolbar.add(_openViewAction);
	}
}
