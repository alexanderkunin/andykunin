package org.eclipse.rap.security.demo;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class SampleBundle extends Plugin {

  public static final String BUNDLE_ID = "org.eclipse.rap.security.demo";
  private static BundleContext bundleContext;

  public void start( BundleContext context ) throws Exception {
    bundleContext = context;
  }

  public void stop( BundleContext context ) throws Exception {
    bundleContext = context;
  }

  public static BundleContext getBundleContext() {
    return bundleContext;
  }
}
