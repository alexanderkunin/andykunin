package org.eclipse.rap.security.dummy;

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
public class DummyCallbackHandler extends AbstractLoginDialog {

  public DummyCallbackHandler() {
    this( Display.getDefault().getActiveShell() );
  }

  protected DummyCallbackHandler( Shell parentShell ) {
    super( parentShell );
  }

  protected Point getInitialSize() {
    return new Point( 380, 280 );
  }

  protected Control createDialogArea( Composite parent ) {
    Composite dialogarea = ( Composite )super.createDialogArea( parent );
    dialogarea.setLayoutData( new GridData( GridData.FILL_BOTH ) );
    Composite composite = new Composite( dialogarea, SWT.NONE );
    composite.setLayout( new GridLayout() );
    createCallbackHandlers( composite );
    return composite;
  }

  private void createCallbackHandlers( Composite composite ) {
    Callback[] callbacks = getCallbacks();
    for( int i = 0; i < callbacks.length; i++ ) {
      Callback callback = callbacks[ i ];
      if( callback instanceof TextOutputCallback ) {
        createTextoutputHandler( composite, ( TextOutputCallback )callback );
      } else if( callback instanceof NameCallback ) {
        createNameHandler( composite, ( NameCallback )callback );
      } else if( callback instanceof PasswordCallback ) {
        createPasswordHandler( composite, ( PasswordCallback )callback );
      }
    }
  }

  private void createPasswordHandler( Composite composite,
                                      final PasswordCallback callback )
  {
    Label label = new Label( composite, SWT.NONE );
    label.setText( callback.getPrompt() );
    final Text passwordText = new Text( composite, SWT.SINGLE
                                                   | SWT.LEAD
                                                   | SWT.PASSWORD
                                                   | SWT.BORDER );
    passwordText.addModifyListener( new ModifyListener() {

      public void modifyText( ModifyEvent event ) {
        callback.setPassword( passwordText.getText().toCharArray() );
      }
    } );
  }

  private void createNameHandler( Composite composite,
                                  final NameCallback callback )
  {
    Label label = new Label( composite, SWT.NONE );
    label.setText( callback.getPrompt() );
    final Text text = new Text( composite, SWT.SINGLE | SWT.LEAD | SWT.BORDER );
    text.addModifyListener( new ModifyListener() {

      public void modifyText( ModifyEvent event ) {
        callback.setName( text.getText() );
      }
    } );
  }

  private void createTextoutputHandler( Composite composite,
                                        TextOutputCallback callback )
  {
    int messageType = callback.getMessageType();
    int dialogMessageType = IMessageProvider.NONE;
    switch( messageType ) {
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
    setMessage( callback.getMessage(), dialogMessageType );
  }

  public void internalHandle() {
  }
}
