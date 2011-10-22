package org.eclipse.rap.security.demo;

import org.eclipse.swt.SWT;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * Configures the perspective layout. This class is contributed through the
 * plugin.xml.
 */
public class Perspective implements IPerspectiveFactory {

  public void createInitialLayout( IPageLayout layout ) {
    layout.setEditorAreaVisible( false );
    layout.addView( "org.eclipse.rap.security.demo.view", SWT.LEFT, 0.9f, null );
  }
}
