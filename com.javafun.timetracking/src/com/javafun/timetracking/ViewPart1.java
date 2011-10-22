package com.javafun.core;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

public class ViewPart1 extends ViewPart {

    public ViewPart1() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void createPartControl(Composite parent) {
        new Label(parent, SWT.NONE).setText("something View part 1");

    }

    @Override
    public void setFocus() {
        // TODO Auto-generated method stub

    }

}
