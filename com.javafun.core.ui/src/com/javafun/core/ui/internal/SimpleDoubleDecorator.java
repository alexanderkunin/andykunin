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
public class SimpleDoubleDecorator {
    private volatile boolean _initialized = true;
    private Text _text;
    private VerifyListener _listener;

    public SimpleDoubleDecorator(Text text) {
        _text = text;
        _listener = new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                if (_initialized) {
                    String eText = e.text;
                    if ("".equals(eText)) {
                        return;
                    }
                    if (StringUtils.isBlank(eText) || (!".".equals(eText) && !isValid(eText))) {
                        e.display.beep();
                        e.doit = false;
                        return;
                    }
                    String text = _text.getText().substring(0, e.start) + eText + _text.getText().substring(e.end);
                    if (!isValid(text)) {
                        e.display.beep();
                        e.doit = false;
                        return;
                    }
                }
            }
        };
        init();
    }

    public void init() {
        _text.addVerifyListener(_listener);
    }

    protected boolean isValid(String text) {
        boolean valid = true;
        try {
            Double.valueOf(text);
        } catch (Exception e) {
            valid = false;
        }
        return valid;
    }

    public void setInitialized(boolean initialized) {
        _initialized = initialized;
    }
}
