package com.javafun.timetracking.ui.resources;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceMessages {
	private static final String BUNDLE_NAME = "com.javafun.timetracking.ui.resources.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private ResourceMessages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
