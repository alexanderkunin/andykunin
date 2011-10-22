package com.javafun.core.ui.internal;

import javafun.utils.StringUtils;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

/**
 * Custom text field for displaying formatted values. Beeps and ignores
 * characters that cause the content of the control to become unparsable.
 * 
 * @author Ihor Strutynskyj
 */
public class SimpleNumericalDecorator {
    Text _text;

    public SimpleNumericalDecorator(Text text) {
        _text = text;
        _init();
    }

    protected void _init() {
        _text.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                String eText = e.text;
                if (StringUtils.isBlank(eText)) {
                    return;
                }
                if (!eText.matches("\\d+")) {
                    e.display.beep();
                    e.doit = false;
                    return;
                }
                String newText = _text.getText();
                newText = newText.substring(0, e.start) + eText + newText.substring(e.end);
                if (!newText.matches("\\d+")) {
                    e.display.beep();
                    e.doit = false;
                    return;
                }
            }
        });
    }
}
