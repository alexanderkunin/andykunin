package com.javafun.timetracking.ui.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.javafun.timetracking.ui.view.SettinsNavigationView;
import com.javafun.timetracking.ui.view.View;

/**
 * Configures the perspective layout. This class is contributed through the
 * plugin.xml.
 */
public class SettingsPerspective implements IPerspectiveFactory {

	public void createInitialLayout(final IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);

		layout.addStandaloneView(SettinsNavigationView.ID, true, IPageLayout.LEFT, 0.25f, editorArea);
		IFolderLayout folder = layout.createFolder("messages", IPageLayout.TOP, 0.5f, editorArea);
		folder.addPlaceholder(View.ID + ":*");
		folder.addView(View.ID);

		layout.getViewLayout(SettinsNavigationView.ID).setCloseable(false);
	}

}
