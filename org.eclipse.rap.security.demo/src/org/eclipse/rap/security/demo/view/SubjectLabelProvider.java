package org.eclipse.rap.security.demo.view;

import java.security.Principal;
import java.util.Set;

import javax.security.auth.Subject;

import org.eclipse.jface.viewers.LabelProvider;

class SubjectLabelProvider extends LabelProvider {

  private final Subject subject;

  public SubjectLabelProvider( Subject subject ) {
    this.subject = subject;
  }

  public String getText( Object object ) {
    if( object == this.subject )
      return "User Subject (" + object.getClass().getName() + ")";
    if( object == this.subject.getPrincipals() )
      return "Principals (" + Set.class.getName() + ")";
    if( object == this.subject.getPublicCredentials() )
      return "Public Credentials (" + Set.class.getName() + ")";
    if( object == this.subject.getPrivateCredentials() )
      return "Private Credentials (" + Set.class.getName() + ")";
    if( object instanceof Principal )
      return "Name: "
             + ( ( Principal )object ).getName()
             + " ("
             + object.getClass().getName()
             + ")";
    return object.getClass().getName() + " [" + object.toString() + "]";
  }
}