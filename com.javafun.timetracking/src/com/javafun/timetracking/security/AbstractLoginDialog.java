/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.javafun.timetracking.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.ConfirmationCallback;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractLoginDialog extends TitleAreaDialog implements CallbackHandler {

	boolean _processCallbacks = false;
	boolean _isCancelled = false;
	Callback[] _callbackArray;

	protected final Callback[] getCallbacks() {
		return _callbackArray;
	}

	public abstract void internalHandle();

	public boolean isCancelled() {
		return _isCancelled;
	}

	protected AbstractLoginDialog(final Shell parentShell) {
		super(parentShell);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.security.auth.callback.CallbackHandler#handle(javax.security.auth
	 * .callback.Callback[])
	 */
	public void handle(final Callback[] callbacks) throws IOException {
		_callbackArray = callbacks;
		final Display display = Display.getDefault();
		display.syncExec(new Runnable() {

			public void run() {
				_isCancelled = false;
				setBlockOnOpen(false);
				open();
				final Button okButton = getButton(IDialogConstants.OK_ID);
				okButton.setText("Login");
				okButton.addSelectionListener(new SelectionListener() {

					public void widgetSelected(final SelectionEvent event) {
						_processCallbacks = true;
					}

					public void widgetDefaultSelected(final SelectionEvent event) {
						// nothing to do
					}
				});
				final Button cancel = getButton(IDialogConstants.CANCEL_ID);
				cancel.addSelectionListener(new SelectionListener() {

					public void widgetSelected(final SelectionEvent event) {
						_isCancelled = true;
						_processCallbacks = false;
					}

					public void widgetDefaultSelected(final SelectionEvent event) {
						// nothing to do
					}
				});
			}
		});
		try {
			ModalContext.setAllowReadAndDispatch(true); // Works for now.
			ModalContext.run(new IRunnableWithProgress() {

				public void run(final IProgressMonitor monitor) {
					// Wait here until OK or cancel is pressed, then let it rip.
					// The event listener is responsible for closing the dialog
					// (in the loginSucceeded event).
					while (!_processCallbacks && !_isCancelled) {
						try {
							Thread.sleep(100);
						} catch (final Exception e) {
							// do nothing
						}
					}
					_processCallbacks = false;
					// Call the adapter to handle the callbacks
					if (!isCancelled()) {
						internalHandle();
					} else {
						if (callbacks[3] instanceof ConfirmationCallback) {
							((ConfirmationCallback) callbacks[3]).setSelectedIndex(ConfirmationCallback.CANCEL);
						}
					}
				}
			}, true, new NullProgressMonitor(), Display.getDefault());
		} catch (final Exception e) {
			final IOException ioe = new IOException();
			ioe.initCause(e);
			throw ioe;
		}
	}

	protected void configureShell(final Shell shell) {
		super.configureShell(shell);
		shell.setText("Login");
		
	}
}