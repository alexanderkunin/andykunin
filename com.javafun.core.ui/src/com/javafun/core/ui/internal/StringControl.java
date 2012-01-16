package com.javafun.core.ui.internal;

import javafun.utils.StringUtils;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;

/**
 * A customized text control. The most important additional feature that this
 * control provides over the standard SWT text control is that you can set the
 * column width of the control. This control also selects all its text when it
 * gains the focus.
 */
public class StringControl extends Composite {
    static final public int COLUMN_WIDTH_AVERAGE_CHAR = 0;
    static final public int COLUMN_WIDTH_AVERAGE_DIGIT = 1;
    static final public int COLUMN_WIDTH_AVERAGE_LETTER = 2;
    static final public int COLUMN_WIDTH_WIDEST_CHAR = 3;
    static final public int COLUMN_WIDTH_WIDEST_DIGIT = 4;
    static final public int COLUMN_WIDTH_WIDEST_LETTER = 5;

    static final public int DEFAULT_STYLE = SWT.BORDER | SWT.SINGLE;
    static final public int UPPER_CASE = 1 << 23; // not used by text
    static final public int NUMERICAL = 1 << 30; // not used by text
    static final public int DOUBLE = 1 << 28; // not used by text

    int _columns;
    int _columnType = COLUMN_WIDTH_AVERAGE_CHAR;
    protected int _style;
    protected boolean _required;
    protected boolean _dirty;
    protected boolean _isReadOnly;
    protected boolean _blockDirtyFlag;
    private final ControlDecoration _controlDecoration;

    protected Text _text;
    private Point _preferredSize;

    public StringControl(Composite parent) {
        this(parent, DEFAULT_STYLE);
    }

    public StringControl(final Composite parent, int style) {
        super(parent, SWT.NO_FOCUS);
        setLayout(new FillLayout());

        final int finalStyle = style;
        _text = new Text(this, style);
        if ((style & NUMERICAL) != 0) {
            new SimpleNumericalDecorator(_text);
        }
        if ((style & DOUBLE) != 0) {
            new SimpleDoubleDecorator(_text);
        }
        _text.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if ((finalStyle & SWT.NO_FOCUS) == 0 && !"".equals(_text.getText())) {
                    _text.selectAll();
                }
            }
        });
        _text.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                _dirty |= !_blockDirtyFlag;
                updateRequired();
            }
        });
        _text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
                if (_isReadOnly) {
                    e.doit = false;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (_isReadOnly) {
                    e.doit = false;
                }
            }
        });
        if ((style & SWT.READ_ONLY) != 0) {
            style |= SWT.NO_FOCUS;
            setReadOnly(true);
        }
        if ((style & UPPER_CASE) != 0) {
            _text.addListener(SWT.Verify, new Listener() {
                public void handleEvent(Event e) {
                    for (int i = 0; i < e.text.length(); i++) {
                        if (Character.isLowerCase(e.text.charAt(i))) {
                            e.text = e.text.toUpperCase();
                            break;
                        }
                    }
                }
            });
        }
        _controlDecoration = new ControlDecoration(_text, SWT.TOP | SWT.LEFT, parent);
        _controlDecoration.setImage(PluginResources.getImage("required-overlay.gif"));
        _controlDecoration.setDescriptionText("This is required information");
        _controlDecoration.hide();
    }

    public Text getTextControl() {
        return _text;
    }

    @Override
    public int getStyle() {
        return _style;
    }

    /**
     * @see com.rpc.core.ui.forms.FieldViewer#setFocus()
     */
    @Override
    public boolean setFocus() {
        if (_text == null) {
            return false;
        }
        return _text.setFocus();
    }

    @Override
    public Point computeSize(int wHint, int hHint, boolean changed) {
        if (_columns <= 0) {
            return super.computeSize(wHint, hHint, changed);
        }
        _preferredSize = _text.computeSize(wHint, hHint, changed);
        //        /*
        //         * Adding twice the avg character advance is really just a fudge factor
        //         * to account for the left & right margins inside the text box. SWT does
        //         * not give us a way to query for these margins but we could figure them
        //         * out exactly with some work. However, I've no time for that now - so
        //         * I've fudged it.
        //         */
        //        _preferredSize.x = (UIUtils.getAverageTextCharacterAdvance() + _text.getBorderWidth()) * 2;
        //        switch (_columnType) {
        //        case COLUMN_WIDTH_AVERAGE_DIGIT: {
        //            _preferredSize.x += UIUtils.getAverageTextDigitAdvance() * _columns;
        //        }
        //            break;
        //        case COLUMN_WIDTH_AVERAGE_LETTER: {
        //            _preferredSize.x += UIUtils.getAverageTextLetterAdvance() * _columns;
        //        }
        //            break;
        //        case COLUMN_WIDTH_WIDEST_CHAR: {
        //            _preferredSize.x += UIUtils.getWidestTextCharacterAdvance() * _columns;
        //        }
        //            break;
        //        case COLUMN_WIDTH_WIDEST_DIGIT: {
        //            _preferredSize.x += UIUtils.getWidestTextDigitAdvance() * _columns;
        //        }
        //            break;
        //        case COLUMN_WIDTH_WIDEST_LETTER: {
        //            _preferredSize.x += UIUtils.getWidestTextLetterAdvance() * _columns;
        //        }
        //            break;
        //        default: { // COLUMN_WIDTH_AVERAGE_CHAR
        //            _preferredSize.x += UIUtils.getAverageTextCharacterAdvance() * _columns;
        //        }
        //        }
        return _preferredSize;
    }

    public void setColumns(int columns, int columnType) {
        _columns = columns;
        _columnType = columnType;
    }

    public void setColumns(int columns) {
        setColumns(columns, COLUMN_WIDTH_AVERAGE_CHAR);
    }

    public int getColumns() {
        return _columns;
    }

    public int getColumnType() {
        return _columnType;
    }

    // /////////////////////////////////////////////
    // 
    // methods from the Text class
    //

    public void addModifyListener(ModifyListener listener) {
        _text.addModifyListener(listener);
    }

    public void addSelectionListener(SelectionListener listener) {
        _text.addSelectionListener(listener);
    }

    public void addVerifyListener(VerifyListener listener) {
        _text.addVerifyListener(listener);
    }

    public void append(String string) {
        _text.append(string);
    }

    public void clearSelection() {
        _text.clearSelection();
    }

    //    public void copy() {
    //        _text.copy();
    //    }
    //
    //    public void cut() {
    //        _text.cut();
    //    }
    //
    //    public int getCaretLineNumber() {
    //        return _text.getCaretLineNumber();
    //    }
    //
    //    public Point getCaretLocation() {
    //        return _text.getCaretLocation();
    //    }

    public int getCaretPosition() {
        return _text.getCaretPosition();
    }

    public int getCharCount() {
        return _text.getCharCount();
    }

    //    public boolean getDoubleClickEnabled() {
    //        return _text.getDoubleClickEnabled();
    //    }

    public char getEchoChar() {
        return _text.getEchoChar();
    }

    public boolean getEditable() {
        return _text.getEditable();
    }

    //    public int getLineCount() {
    //        return _text.getLineCount();
    //    }

    public String getLineDelimiter() {
        return _text.getLineDelimiter();
    }

    public int getLineHeight() {
        return _text.getLineHeight();
    }

    public Point getSelection() {
        return _text.getSelection();
    }

    public int getSelectionCount() {
        return _text.getSelectionCount();
    }

    public String getSelectionText() {
        return _text.getSelectionText();
    }

    //    public int getTabs() {
    //        return _text.getTabs();
    //    }

    /**
     * Returns the widget text.
     * The text for a text widget is the characters in the widget, or an empty string if this has never been set.
     * @return the widget text
      */
    public String getText() {
        return _text.getText();
    }

    public String getText(int start, int end) {
        return _text.getText(start, end);
    }

    public int getTextLimit() {
        return _text.getTextLimit();
    }

    //    public int getTopIndex() {
    //        return _text.getTopIndex();
    //    }
    //
    //    public int getTopPixel() {
    //        return _text.getTopPixel();
    //    }

    public void insert(String string) {
        _text.insert(string);
    }

    //    public void paste() {
    //        _text.paste();
    //    }

    public void removeModifyListener(ModifyListener listener) {
        _text.removeModifyListener(listener);
    }

    public void removeSelectionListener(SelectionListener listener) {
        _text.removeSelectionListener(listener);
    }

    public void removeVerifyListener(VerifyListener listener) {
        _text.removeVerifyListener(listener);
    }

    public void selectAll() {
        _text.selectAll();
    }

    //    public void setDoubleClickEnabled(boolean doubleClick) {
    //        _text.setDoubleClickEnabled(doubleClick);
    //    }

    public void setEchoChar(char echo) {
        _text.setEchoChar(echo);
    }

    public void setEditable(boolean editable) {
        _text.setEditable(editable);
    }

    @Override
    public void setFont(Font font) {
        _text.setFont(font);
    }

    public void setSelection(int start) {
        _text.setSelection(start);
    }

    public void setSelection(int start, int end) {
        _text.setSelection(start, end);
    }

    @Override
    public void setRedraw(boolean redraw) {
        _text.setRedraw(redraw);
    }

    public void setSelection(Point selection) {
        _text.setSelection(selection);
    }

    //    public void setTabs(int tabs) {
    //        _text.setTabs(tabs);
    //    }

    //    public void setText(Money money) {
    //        setText(money != null ? FormatUtils.DECIMAL_FORMAT.format(money) : null);
    //    }

    public void setText(String string) {
        if (_text.isDisposed()) {
            return;
        }
        if (string == null) {
            string = "";
        }
        // replace newlines with the local separator
        int style = getStyle();
        if (((style & SWT.MULTI) != 0) && ((style & SWT.READ_ONLY) != 0)) {
            String lineSep = System.getProperty("line.separator");
            string = StringUtils.replaceAll(string, lineSep, "\n");
            string = StringUtils.replaceAll(string, "\n", lineSep);
        }

        String oldText = _text.getText();
        if (string.equals(oldText) == false) {
            _blockDirtyFlag = true;
            _text.setText(string);
            _blockDirtyFlag = false;
        }
        updateRequired();
    }

    public void setTextLimit(int limit) {
        _text.setTextLimit(limit);
    }

    //    public void setTopIndex(int index) {
    //        _text.setTopIndex(index);
    //    }
    //
    //    public void showSelection() {
    //        _text.showSelection();
    //    }

    // ///////////////////////////////////////////////////////
    //
    // methods from Control
    //

    @Override
    public void addFocusListener(FocusListener listener) {
        _text.addFocusListener(listener);
    }

    @Override
    public void addHelpListener(HelpListener listener) {
        _text.addHelpListener(listener);
    }

    @Override
    public void addKeyListener(KeyListener listener) {
        _text.addKeyListener(listener);
    }

    @Override
    public void addMouseListener(MouseListener listener) {
        _text.addMouseListener(listener);
    }

    //    @Override
    //    public void addMouseTrackListener(MouseTrackListener listener) {
    //        _text.addMouseTrackListener(listener);
    //    }
    //
    //    @Override
    //    public void addMouseMoveListener(MouseMoveListener listener) {
    //        _text.addMouseMoveListener(listener);
    //    }

    //    @Override
    //    public void addPaintListener(PaintListener listener) {
    //        _text.addPaintListener(listener);
    //    }

    @Override
    public void addTraverseListener(TraverseListener listener) {
        _text.addTraverseListener(listener);
    }

    @Override
    public void addListener(int eventType, Listener listener) {
        _text.addListener(eventType, listener);
    }

    @Override
    public void removeListener(int eventType, Listener listener) {
        _text.removeListener(eventType, listener);
    }

    @Override
    public boolean forceFocus() {
        return _text.forceFocus();
    }

    //    @Override
    //    public Accessible getAccessible() {
    //        return _text.getAccessible();
    //    }

    @Override
    public Color getBackground() {
        if (_text != null && !_text.isDisposed()) {
            return _text.getBackground();
        }
        return null;
    }

    @Override
    public Font getFont() {
        if (_text != null && !_text.isDisposed()) {
            return _text.getFont();
        }
        return null;
    }

    @Override
    public Color getForeground() {
        if (_text != null && !_text.isDisposed()) {
            return _text.getForeground();
        }
        return null;
    }

    @Override
    public Menu getMenu() {
        return _text.getMenu();
    }

    @Override
    public boolean isFocusControl() {
        return _text.isFocusControl();
    }

    @Override
    public void removeFocusListener(FocusListener listener) {
        _text.removeFocusListener(listener);
    }

    @Override
    public void removeHelpListener(HelpListener listener) {
        _text.removeHelpListener(listener);
    }

    @Override
    public void removeKeyListener(KeyListener listener) {
        _text.removeKeyListener(listener);
    }

    //    @Override
    //    public void removeMouseTrackListener(MouseTrackListener listener) {
    //        _text.removeMouseTrackListener(listener);
    //    }

    @Override
    public void removeMouseListener(MouseListener listener) {
        _text.removeMouseListener(listener);
    }

    //    @Override
    //    public void removeMouseMoveListener(MouseMoveListener listener) {
    //        _text.removeMouseMoveListener(listener);
    //    }

    //    @Override
    //    public void removePaintListener(PaintListener listener) {
    //        _text.removePaintListener(listener);
    //    }

    @Override
    public void removeTraverseListener(TraverseListener listener) {
        _text.removeTraverseListener(listener);
    }

    @Override
    public void setBackground(Color color) {
        if (_text != null && !_text.isDisposed() && !_isReadOnly) {
            _text.setBackground(color);
        }
    }

    //    @Override
    //    public void setCapture(boolean capture) {
    //        _text.setCapture(capture);
    //    }

    @Override
    public void setCursor(Cursor cursor) {
        _text.setCursor(cursor);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        _text.setEnabled(enabled);
    }

    @Override
    public void setForeground(Color color) {
        if (_text != null && !_text.isDisposed()) {
            _text.setForeground(color);
        }
    }

    @Override
    public void setMenu(Menu menu) {
        _text.setMenu(menu);
    }

    @Override
    public void setToolTipText(String string) {
        super.setToolTipText(string);
        _text.setToolTipText(string);
    }

    //    @Override
    //    public boolean traverse(int traversal) {
    //        return _text.traverse(traversal);
    //    }

    public boolean isRequired() {
        return _required;
    }

    public boolean getRequired() {
        return _required;
    }

    private void updateRequired() {
        if (_required) {
            if (StringUtils.isNotBlank(_text.getText())) {
                _controlDecoration.hide();
            } else {
                _controlDecoration.show();
            }
        } else {
            _controlDecoration.hide();
        }
    }

    public void setRequired(boolean isRequired) {
        _required = isRequired;
        if (_text != null) {
            updateRequired();
        }
    }

    public boolean isDirty() {
        return _dirty;
    }

    public void setDirty(boolean dirty) {
        _dirty = dirty;
    }

    public boolean getIsReadOnly() {
        return _isReadOnly;
    }

    public void setReadOnly(boolean isReadOnly) {
        _isReadOnly = isReadOnly;
        Display display = getDisplay();
        _text.setBackground(display.getSystemColor(_isReadOnly ? SWT.COLOR_WIDGET_BACKGROUND
                : SWT.COLOR_LIST_BACKGROUND));
    }

    public Point getPreferredSize() {
        return _preferredSize;
    }
}
