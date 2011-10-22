package com.javafun.core.ui.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.javafun.core.ui.view.NavigationView;
import com.javafun.core.ui.view.View;

/**
 * Configures the perspective layout. This class is contributed through the
 * plugin.xml.
 */
public class PerspectiveSettings implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addStandaloneView(NavigationView.ID, false, IPageLayout.LEFT, 0.10f, editorArea);
        IFolderLayout folder = layout.createFolder("messages", IPageLayout.TOP, 0.5f, editorArea);
        folder.addPlaceholder(View.ID + ":*");
        folder.addView(View.ID);

        layout.getViewLayout(NavigationView.ID).setCloseable(false);
    }
}
