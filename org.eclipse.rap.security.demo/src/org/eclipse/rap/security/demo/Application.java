package org.eclipse.rap.security.demo;

import java.net.URL;
import java.security.PrivilegedAction;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.LoginContextFactory;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.osgi.framework.BundleContext;

/**
 * This class controls all aspects of the application's execution and is
 * contributed through the plugin.xml.
 */
public class Application implements IApplication {

  private static final String JAAS_CONFIG_FILE = "data/jaas_config.txt";

  public Object start( IApplicationContext context ) throws Exception {
    // String configName = "KeyStore";
    // String configName = "UNIX";
    String configName = "DUMMY";
    BundleContext bundleContext = SampleBundle.getBundleContext();
    URL configUrl = bundleContext.getBundle().getEntry( JAAS_CONFIG_FILE );
    ILoginContext secureContext = LoginContextFactory.createContext( configName,
                                                                     configUrl );
    try {
      secureContext.login();
    } catch( LoginException e ) {
      IStatus status = new Status( IStatus.ERROR,
                                   "org.eclipse.rap.security.demo",
                                   "Login failed",
                                   e );
      ErrorDialog.openError( null, "Error", "Login failed", status );
    }
    Integer result = null;
    Display display = PlatformUI.createDisplay();
    try {
      result = ( Integer )Subject.doAs( secureContext.getSubject(),
                                        getRunAction( display ) );
    } catch( Exception e ) {
      e.printStackTrace();
    } finally {
      display.dispose();
      secureContext.logout();
    }
    return result;
  }

  private PrivilegedAction getRunAction( final Display display ) {
    return new PrivilegedAction() {

      public Object run() {
        int result = PlatformUI.createAndRunWorkbench( display,
                                                       new ApplicationWorkbenchAdvisor() );
        return new Integer( result );
      }
    };
  }

  public void stop() {
    final IWorkbench workbench = PlatformUI.getWorkbench();
    if( workbench == null )
      return;
    final Display display = workbench.getDisplay();
    display.syncExec( new Runnable() {

      public void run() {
        if( !display.isDisposed() )
          workbench.close();
      }
    } );
  }
  class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    private static final String PERSPECTIVE_ID = "org.eclipse.rap.security.demo.perspective";

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor( IWorkbenchWindowConfigurer configurer )
    {
      return new ApplicationWorkbenchWindowAdvisor( configurer );
    }

    public String getInitialWindowPerspectiveId() {
      return PERSPECTIVE_ID;
    }
  }
  class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor( IWorkbenchWindowConfigurer configurer )
    {
      super( configurer );
    }

    public ActionBarAdvisor createActionBarAdvisor( IActionBarConfigurer configurer )
    {
      return new ActionBarAdvisor( configurer );
    }

    public void preWindowOpen() {
      IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
      configurer.setInitialSize( new Point( 500, 300 ) );
      configurer.setShowCoolBar( false );
      configurer.setShowMenuBar( false );
      configurer.setShowStatusLine( false );
      configurer.setTitle( "Equinox Security on RAP" );
    }
  }
}
