/**
 * 
 */
package com.javafun.timetracking.entrypoints;

import org.eclipse.rap.ui.branding.IExitConfirmation;
import org.eclipse.rwt.RWT;

/**
 * @author Administrator
 * 
 */
public class ExitConfirmation implements IExitConfirmation {

	public ExitConfirmation() {
		System.out.println("ExitConfirmation.ExitConfirmation()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.rap.ui.branding.IExitConfirmation#showExitConfirmation()
	 */
	@Override
	public boolean showExitConfirmation() {
		return RWT.getSessionStore().getHttpSession().getAttribute("user") != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.rap.ui.branding.IExitConfirmation#getExitConfirmationText()
	 */
	@Override
	public String getExitConfirmationText() {
		return "The application will be closed.";
	}
}
