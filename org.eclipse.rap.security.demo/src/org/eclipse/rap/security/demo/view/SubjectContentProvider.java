package org.eclipse.rap.security.demo.view;

import java.util.Set;

import javax.security.auth.Subject;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

class SubjectContentProvider implements ITreeContentProvider {

  public Object[] getElements( Object inputElement ) {
    if( inputElement instanceof Subject )
      return getChildren( inputElement );
    return new Object[]{};
  }

  public void dispose() {
    // nothing to do
  }

  public void inputChanged( Viewer viewer, Object oldInput, Object newInput ) {
    // nothing to do
  }

  public Object[] getChildren( Object parentElement ) {
    if( parentElement instanceof Subject )
      return new Object[]{
        ( ( Subject )parentElement ).getPrincipals(),
        ( ( Subject )parentElement ).getPublicCredentials(),
        ( ( Subject )parentElement ).getPrivateCredentials()
      };
    else if( parentElement instanceof Set )
      return ( ( Set )parentElement ).toArray();
    else
      return null;
  }

  public Object getParent( Object element ) {
    return null;
  }

  public boolean hasChildren( Object element ) {
    if( element instanceof Subject )
      return true;
    else if( element instanceof Set )
      return !( ( Set )element ).isEmpty();
    return false;
  }
}