package com.javafun.timetracking.security;

import javafun.utils.StringUtils;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Handles the callbacks to show a RCP/RAP UI for the LoginModule.
 */
public class TimetrackingCallbackHandler extends AbstractLoginDialog {

	private Text _text;
	private Text _passwordText;
	private String _namePrompt;
	private String _passPrompt;

	public TimetrackingCallbackHandler() {
		this(Display.getDefault().getActiveShell());
	}

	protected TimetrackingCallbackHandler(final Shell parentShell) {
		super(parentShell);
	}

	protected Point getInitialSize() {
		return new Point(280, 240);
	}

	protected Control createDialogArea(final Composite parent) {
		Composite dialogarea = (Composite) super.createDialogArea(parent);
		dialogarea.setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite composite = new Composite(dialogarea, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		createCallbackHandlers(composite);
		return composite;
	}

	@Override
	protected Control createButtonBar(final Composite parent) {
		Control control = super.createButtonBar(parent);
		getButton(OK).setEnabled(false);
		return control;
	}

	private void createCallbackHandlers(final Composite composite) {
		Callback[] callbacks = getCallbacks();
		for (int i = 0; i < callbacks.length; i++) {
			Callback callback = callbacks[i];
			if (callback instanceof TextOutputCallback) {
				createTextoutputHandler(composite, (TextOutputCallback) callback);
			} else if (callback instanceof NameCallback) {
				createNameHandler(composite, (NameCallback) callback);
			} else if (callback instanceof PasswordCallback) {
				createPasswordHandler(composite, (PasswordCallback) callback);
			}
		}
	}

	private void createPasswordHandler(final Composite composite, final PasswordCallback callback) {
		Label label = new Label(composite, SWT.NONE);
		_passPrompt = callback.getPrompt();
		label.setText(_passPrompt);
		label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		_passwordText = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.PASSWORD | SWT.BORDER);
		_passwordText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		_passwordText.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent event) {
				validateFields(callback);
			}
		});
	}

	private void createNameHandler(final Composite composite, final NameCallback callback) {
		Label label = new Label(composite, SWT.NONE);
		_namePrompt = callback.getPrompt();
		label.setText(_namePrompt);
		label.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		_text = new Text(composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		_text.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		_text.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent event) {
				validateFields(callback);
			}
		});

	}

	private void createTextoutputHandler(final Composite composite, final TextOutputCallback callback) {
		int messageType = callback.getMessageType();
		int dialogMessageType = IMessageProvider.NONE;
		switch (messageType) {
		case TextOutputCallback.INFORMATION:
			dialogMessageType = IMessageProvider.INFORMATION;
			break;
		case TextOutputCallback.WARNING:
			dialogMessageType = IMessageProvider.WARNING;
			break;
		case TextOutputCallback.ERROR:
			dialogMessageType = IMessageProvider.ERROR;
			break;
		}
		setMessage(callback.getMessage(), dialogMessageType);
	}

	public void internalHandle() {

	}

	private void validateFields(final Callback callback) {
		if (callback instanceof NameCallback) {
			((NameCallback) callback).setName(_text.getText());
		} else if (callback instanceof PasswordCallback) {
			((PasswordCallback) callback).setPassword(_passwordText.getText().toCharArray());
		}
		if (StringUtils.isBlank(_text.getText())) {
			getButton(OK).setEnabled(false);
			setErrorMessage("Enter " + _namePrompt);
		} else if (StringUtils.isBlank(_passwordText.getText())) {
			getButton(OK).setEnabled(false);
			setErrorMessage("Enter " + _passPrompt);
		} else {
			setErrorMessage(null);
			getButton(OK).setEnabled(true);
		}
	}
}
