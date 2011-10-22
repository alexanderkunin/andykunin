package com.javafun.timetracking.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.javafun.timetracking.model.User;

public class LoginDialog extends Dialog {

    private Text _name;
    private Text _pswrd;
    private User _user;

    public LoginDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false));
        new Label(composite, SWT.NONE).setText("User Name / E-mail");
        _name = new Text(composite, SWT.BORDER);
        new Label(composite, SWT.NONE).setText("Password");
        _pswrd = new Text(composite, SWT.BORDER);
        _pswrd.setEchoChar('*');
        return super.createContents(parent);
    }

    public User getUser() {
        return _user;
    }

    @Override
    protected void okPressed() {
        _user = new User(_name.getText(), _pswrd.getText());
        super.okPressed();
    }
}
