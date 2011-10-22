package com.javafun.core.ui.internal;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javafun.utils.logging.Logger;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;

/**
 * Class CorePluginResources provides static methods to return 
 * ImageRegistry, FontRegistry and Image. An image registry maintains
 * a mapping between symbolic image names and SWT image objects or special
 * image descriptor objects which defer the creation of SWT image
 * objects until they are needed. A font registry maintains a mapping
 * between symbolic font names and SWT fonts.
 */
public class PluginResources {
    public static enum Checkbox {
        CHECKED, UNCHECKED
    }

    private final static String PLUGIN_PROPERTIES = "plugin.properties";
    private static URL _propsFileURL;

    /**
     * The image registry - is <code>null</code> until
     * lazily initialized.
     */
    private static ImageRegistry _imageRegistry;

    /**
     * The font registry - is <code>null</code> until
     * lazily initialized.
     */
    private static FontRegistry _fontRegistry;

    /**
     * Returns the image in CORE's image registry with the given key, 
     * or <code>null</code> if none.
     * Convenience method equivalent to
     * <pre>
     * CorePluginResources.getImageRegistry().get(key)
     * </pre>
     *
     * @param key the key
     * @return the image, or <code>null</code> if none
     */
    public static Image getImage(String key) {
        Image image = getImageRegistry().get(Activator.PLUGIN_ID + "." + key);
        if (image == null) {
            image = getImageByFileName(Activator.PLUGIN_ID, key);
            getImageRegistry().put(Activator.PLUGIN_ID + "." + key, image);
        }
        return image;
    }

    private static Image getImageByFileName(String pluginId, String fileName) {
        ImageDescriptor descriptor = null;
        try {
            URL installURL = FileLocator.resolve(Platform.getBundle(pluginId).getEntry("/"));
            URL url = new URL(installURL, "icons/" + fileName);
            descriptor = ImageDescriptor.createFromURL(url);
        } catch (MalformedURLException eURL) {
            Logger.error(eURL);
        } catch (IOException x) {
            Logger.error(x);
        }
        if (descriptor == null) {
            descriptor = ImageDescriptor.getMissingImageDescriptor();
        }
        return descriptor.createImage();
    }

    public static Image getImage(Checkbox key) {
        return getImage(key.toString());
    }

    /**
     * Returns the image in CORE's image registry with the given key, 
     * or <code>null</code> if none.
     * Convenience method equivalent to
     * <pre>
     * CorePluginResources.getImageRegistry().get(key)
     * </pre>
     * @param pluginId
     * @param key the key
     * @return the image, or <code>null</code> if none
     */
    public static Image getImage(String pluginId, String key) {
        Image image = getImageRegistry().get(pluginId + "." + key);
        if (image == null) {
            image = getImageByFileName(pluginId, key);
            getImageRegistry().put(pluginId + "." + key, image);
        }
        return image;
    }

    /**
     * Returns the image in CORE's image registry with the given key, 
     * or <code>null</code> if none.
     * Convenience method equivalent to
     * <pre>
     * CorePluginResources.getImageRegistry().get(key)
     * </pre>
     * @param pluginId
     * @param key the key
     * @return the image, or <code>null</code> if none
     */
    public static ImageDescriptor getImageDescriptor(String pluginId, String key) {
        ImageDescriptor descriptor = null;
        try {
            descriptor = ImageDescriptor.createFromURL(new URL(
                FileLocator.resolve(Platform.getBundle(pluginId).getEntry("/")), "icons/" + key));
        } catch (MalformedURLException eURL) {
            Logger.error(eURL);
        } catch (IOException x) {
            Logger.error(x);
        }
        if (descriptor == null) {
            descriptor = ImageDescriptor.getMissingImageDescriptor();
        }
        return descriptor;
    }

    /**
     * Returns the image registry for JFace itself.
     * <p>
     * Note that the static convenience method <code>getImage</code>
     * is also provided on this class.
     * </p>
     */
    public static ImageRegistry getImageRegistry() {
        if (_imageRegistry == null) {
            _imageRegistry = new ImageRegistry();
            _imageRegistry.put(Activator.PLUGIN_ID + "." + Checkbox.CHECKED.toString(), getImageByFileName(Activator.PLUGIN_ID, "checked.gif"));
            _imageRegistry.put(Activator.PLUGIN_ID + "." + Checkbox.UNCHECKED.toString(), getImageByFileName(Activator.PLUGIN_ID, "unchecked.gif"));
            //            _imageRegistry.put(CorePlugin.ID_PLUGIN + "." + Checkbox.CHECKED.toString(), makeShot(true));
            //            _imageRegistry.put(CorePlugin.ID_PLUGIN + "." + Checkbox.UNCHECKED.toString(), makeShot(false));
        }
        return _imageRegistry;
    }

    //    private static Image makeShot(boolean type) {
    //        Shell s = new Shell(Display.getCurrent(), SWT.NO_TRIM);
    //        s.setLocation(0, 0);
    //        Button b = new Button(s, SWT.CHECK);
    //        b.setSelection(type);
    //        Point bsize = b.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    //        b.setSize(bsize);
    //        b.setLocation(0, 0);
    //        s.setSize(bsize);
    //        s.open();
    //        s.setFocus();
    //        GC gc = new GC(s);
    //        Image image = new Image(s.getDisplay(), bsize.x, bsize.y);
    //        gc.copyArea(image, 0, 0);
    //        gc.dispose();
    //        s.close();
    //        return image;
    //    }

    /**
     * Returns the font registry for JFace itself.
     * </p>
     */
    public static FontRegistry getFontRegistry() {
        if (_fontRegistry == null) {
            _fontRegistry = new FontRegistry();
        }
        return _fontRegistry;
    }

    protected static File getPropsFile(Bundle bundle) throws IOException {
        File file = null;
        if (_propsFileURL != null) {
            return new File(_propsFileURL.getFile());
        }
        _propsFileURL = FileLocator.resolve(bundle.getEntry(PLUGIN_PROPERTIES));
        file = new File(_propsFileURL.getFile());
        return file;
    }

    protected static Bundle getBundle() {
        return Activator.getContext().getBundle();
    }
}
