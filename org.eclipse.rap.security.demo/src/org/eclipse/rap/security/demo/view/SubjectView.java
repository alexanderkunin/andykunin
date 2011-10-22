package org.eclipse.rap.security.demo.view;

import java.security.AccessController;
import javax.security.auth.Subject;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class SubjectView extends ViewPart {

  private TreeViewer subjectViewer;
  Subject subject;

  public void createPartControl( Composite parent ) {
    subjectViewer = new TreeViewer( parent, SWT.MULTI
                                            | SWT.H_SCROLL
                                            | SWT.V_SCROLL );
    subject = Subject.getSubject( AccessController.getContext() );
    subjectViewer.setContentProvider( new SubjectContentProvider() );
    subjectViewer.setLabelProvider( new SubjectLabelProvider( subject ) );
    subjectViewer.setInput( subject );
  }

  /**
   * Passing the focus request to the viewer's control.
   */
  public void setFocus() {
    subjectViewer.getControl().setFocus();
  }
}