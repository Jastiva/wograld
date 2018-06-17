/*
 * JEditTextArea.java - jEdit's text component
 * Copyright (C) 1999 Slava Pestov
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

package net.sf.gridarta.textedit.textarea;

import java.awt.AWTEvent;
import java.awt.Adjustable;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import net.sf.gridarta.textedit.textarea.tokenmarker.TokenMarker;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * jEdit's text area component. It is more suited for editing program source
 * code than JEditorPane, because it drops the unnecessary features (images,
 * variable-width lines, and so on) and adds a whole bunch of useful goodies
 * such as: <ul> <li>More flexible key binding scheme <li>Supports macro
 * recorders <li>Rectangular selection <li>Bracket highlighting <li>Syntax
 * highlighting <li>Command repetition <li>Block caret can be enabled </ul> It
 * is also faster and doesn't have as many problems. It can be used in other
 * applications; the only other part of jEdit it depends on is the syntax
 * package.<p> <p/> To use it in your app, treat it like any other component,
 * for example:
 * <pre>JEditTextArea ta = new JEditTextArea();
 * ta.setTokenMarker(new JavaTokenMarker());
 * ta.setText("public class Test {\n"
 *     + "    public static void main(String[] args) {\n"
 *     + "        System.err.println(\"Hello World\");\n"
 *     + "    }\n"
 *     + "}");</pre>
 * @author Slava Pestov
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author Andreas Kirschbaum
 */
public class JEditTextArea extends JComponent {

    /**
     * The Logger for printing log messages.
     */
    private static final Category log = Logger.getLogger(JEditTextArea.class);

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The text contents in the last "unmodified" state.
     */
    @NotNull
    private String unmodifiedText = "";

    @Nullable
    private static JEditTextArea focusedComponent;

    @NotNull
    private static final Timer caretTimer = new Timer(500, new CaretBlinker());

    static {
        caretTimer.setInitialDelay(500);
        caretTimer.start();
    }

    @NotNull
    private final TextAreaPainter painter;

    @Nullable
    private final JPopupMenu popup;

    private final boolean caretBlinks;

    private boolean caretVisible;

    private boolean blink;

    private final boolean editable;

    private int firstLine;

    private int visibleLines;

    private final int electricScroll;

    private int horizontalOffset;

    @NotNull
    private final JScrollBar vertical = new JScrollBar(Adjustable.VERTICAL);

    @NotNull
    private final JScrollBar horizontal = new JScrollBar(Adjustable.HORIZONTAL);

    private boolean scrollBarsInitialized;

    @NotNull
    private final InputHandler inputHandler;

    @Nullable
    private SyntaxDocument document;

    @NotNull
    private final DocumentListener documentHandler;

    @NotNull
    private final Segment lineSegment;

    private int selectionStart;

    private int selectionStartLine;

    private int selectionEnd;

    private int selectionEndLine;

    private boolean biasLeft;

    private int bracketPosition;

    private int bracketLine;

    private int magicCaret;

    private boolean overwrite;

    private boolean rectangleSelect;

    /**
     * Creates a new JEditTextArea with the specified settings.
     * @param defaults the default settings
     */
    public JEditTextArea(@NotNull final TextAreaDefaults defaults) {
        // Enable the necessary events
        enableEvents(AWTEvent.KEY_EVENT_MASK);

        // Initialize some misc. stuff
        painter = new TextAreaPainter(this, defaults);
        documentHandler = new DocumentHandler();
        lineSegment = new Segment();
        bracketLine = -1;
        bracketPosition = -1;
        blink = true;

        // Initialize the GUI
        setLayout(new ScrollLayout(this));
        add(ScrollLayout.CENTER, painter);
        add(ScrollLayout.RIGHT, vertical);
        add(ScrollLayout.BOTTOM, horizontal);

        // Add some event listeners
        vertical.addAdjustmentListener(new AdjustHandler());
        horizontal.addAdjustmentListener(new AdjustHandler());
        painter.addComponentListener(new ComponentHandler());
        final MouseHandler mouseHandler = new MouseHandler();
        painter.addMouseListener(mouseHandler);
        painter.addMouseWheelListener(mouseHandler);
        painter.addMouseMotionListener(new DragHandler());
        addFocusListener(new FocusHandler());

        // Load the defaults
        inputHandler = defaults.getInputHandler();
        setDocument(defaults.getDocument());
        editable = defaults.getEditable();
        caretVisible = defaults.getCaretVisible();
        caretBlinks = defaults.getCaretBlinks();
        electricScroll = defaults.getElectricScroll();

        popup = defaults.getPopup();

        // free tab key from the focus traversal manager
        freeTabKeyFromFocusTraversal();

        // We don't seem to get the initial focus event?
        focusedComponent = this;
    }

    /**
     * In JDKs above 1.4, the tab key is used for focus traversal. That means
     * the tab key normally does not "work" inside the text area. But that would
     * be a pity, because we need the tab key for indentation. <p/> So what this
     * method does is setting the focus traversal to "tab + &lt;control&gt;", in
     * order to "free" the tab key from focus traversal events. In JDKs above
     * 1.4, this task can be accomplished simply by three lines of code: <code>
     * Set forwardTraversalKeys = new HashSet(); forwardTraversalKeys.add(KeyStroke.getKeyStroke("control
     * TAB")); setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
     * forwardTraversalKeys); </code> <p/> Now, what's the big deal there? The
     * problem is that the class "KeyboardFocusManager" as well as the method
     * "setFocusTraversalKeys" are undefined in JDKs 1.3.* and below. Hence, if
     * above code was inserted as-is, this application would no longer compile
     * with java 1.3. <p/> The solution to this problem is implemented here: The
     * critical classes and methods are accessed through the reflection
     * interface, which allows to execute them *if defined* but still compile
     * *if undefined*.
     */
    private void freeTabKeyFromFocusTraversal() {
        try {
            // preparing the key set first, this should be harmless
            final Set<KeyStroke> forwardTraversalKeys = new HashSet<KeyStroke>();
            forwardTraversalKeys.add(KeyStroke.getKeyStroke("control TAB"));

            // here we try to access java.awt.KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS
            final Field field = Class.forName("java.awt.KeyboardFocusManager").getField("FORWARD_TRAVERSAL_KEYS");
            final Integer value = field.getInt(field); // store the value of this field

            for (final Method method : getClass().getMethods()) {
                // here we try to find the method "setFocusTraversalKeys", and execute it if found
                if (method.getName().equalsIgnoreCase("setFocusTraversalKeys")) {
                    method.invoke(this, value, forwardTraversalKeys);
                    // System.err.println("freeTabKeyFromFocusTraversal() succeeded!");
                }
            }
        } catch (final ClassNotFoundException ignored) {
            // ignore
        } catch (final IllegalAccessException ignored) {
            // ignore
        } catch (final InvocationTargetException ignored) {
            // ignore
        } catch (final NoSuchFieldException ignored) {
            // ignore
        }
    }

    /**
     * Set the TextArea font
     * @param font font
     */
    @Override
    public void setFont(@NotNull final Font font) {
        painter.setFont(font);
    }

    /**
     * Returns the object responsible for painting this text area.
     */
    @NotNull
    public TextAreaPainter getPainter() {
        return painter;
    }

    /**
     * Returns the input handler.
     */
    @NotNull
    public InputHandler getInputHandler() {
        return inputHandler;
    }

    /**
     * Returns true if the caret is visible, false otherwise.
     */
    public boolean isCaretVisible() {
        return (!caretBlinks || blink) && caretVisible;
    }

    /**
     * Sets if the caret should be visible.
     * @param caretVisible true if the caret should be visible, false otherwise
     */
    public void setCaretVisible(final boolean caretVisible) {
        this.caretVisible = caretVisible;
        blink = true;

        painter.invalidateSelectedLines();
    }

    /**
     * Blinks the caret.
     */
    public void blinkCaret() {
        if (caretBlinks) {
            blink = !blink;
            painter.invalidateSelectedLines();
        } else {
            blink = true;
        }
    }

    /**
     * Returns the number of lines from the top and button of the text area that
     * are always visible.
     */
    public int getElectricScroll() {
        return electricScroll;
    }

    /**
     * Updates the state of the scroll bars. This should be called if the number
     * of lines in the document changes, or when the size of the text are
     * changes.
     */
    public void updateScrollBars() {
        if (visibleLines != 0) {
            vertical.setValues(firstLine, visibleLines, 0, getLineCount());
            //vertical.setUnitIncrement(2);
            vertical.setUnitIncrement(1); // scroll one line per click
            vertical.setBlockIncrement(visibleLines);
        }

        final int width = painter.getWidth();
        if (width != 0) {
            horizontal.setValues(-horizontalOffset, width, 0, width * 5);
            //horizontal.setUnitIncrement(painter.getFontMetrics().charWidth('w'));
            horizontal.setUnitIncrement(painter.getDefaultCharWidth());
            horizontal.setBlockIncrement(width / 2);
        }
    }

    /**
     * Returns the line displayed at the text area's origin.
     */
    public int getFirstLine() {
        return firstLine;
    }

    /**
     * Sets the line displayed at the text area's origin without updating the
     * scroll bars.
     */
    public void setFirstLine(final int firstLine) {
        if (firstLine == this.firstLine) {
            return;
        }

        this.firstLine = firstLine;
        if (firstLine != vertical.getValue()) {
            updateScrollBars();
        }
        painter.repaint();
    }

    /**
     * Returns the number of lines visible in this text area.
     */
    public int getVisibleLines() {
        return visibleLines;
    }

    /**
     * Recalculates the number of visible lines. This should not be called
     * directly.
     */
    public void recalculateVisibleLines() {
        if (painter == null) {
            return;
        }

        final int height = painter.getHeight();

        // get line height
        final int lineHeight;
        if (painter.getFontMetrics() == null) {
            lineHeight = painter.getDefaultLineHeight(); // default height might be wrong, take it only when needed
        } else {
            lineHeight = painter.getFontMetrics().getHeight();
        }

        visibleLines = height / lineHeight;
        updateScrollBars();
    }

    /**
     * Returns the horizontal offset of drawn lines.
     */
    public int getHorizontalOffset() {
        return horizontalOffset;
    }

    /**
     * Sets the horizontal offset of drawn lines. This can be used to implement
     * horizontal scrolling.
     * @param horizontalOffset offset The new horizontal offset
     */
    public void setHorizontalOffset(final int horizontalOffset) {
        if (horizontalOffset == this.horizontalOffset) {
            return;
        }

        this.horizontalOffset = horizontalOffset;
        if (horizontalOffset != horizontal.getValue()) {
            updateScrollBars();
        }

        painter.repaint();
    }

    /**
     * A fast way of changing both the first line and horizontal offset.
     * @param firstLine the new first line
     * @param horizontalOffset the new horizontal offset
     */
    public void setOrigin(final int firstLine, final int horizontalOffset) {
        boolean changed = false;

        if (horizontalOffset != this.horizontalOffset) {
            this.horizontalOffset = horizontalOffset;
            changed = true;
        }

        if (firstLine != this.firstLine) {
            this.firstLine = firstLine;
            changed = true;
        }

        if (changed) {
            updateScrollBars();
            painter.repaint();
        }
    }

    /**
     * Ensures that the caret is visible by scrolling the text area if
     * necessary.
     */
    public void scrollToCaret() {
        final int line = getCaretLine();
        final int lineStart = getLineStartOffset(line);
        final int offset = Math.max(0, Math.min(getLineLength(line) - 1, getCaretPosition() - lineStart));

        scrollTo(line, offset);
    }

    /**
     * Sets the focus to this TextArea, so this component is instantly
     * registered for key press events. The graphics context must be fully
     * initialized before calling this method.
     */
    public void setEditingFocus() {
        try {
            requestFocus();
            setCaretVisible(true);
            focusedComponent = this;
            setCaretPosition(0); // set caret to 0, 0 coordinates
        } catch (final NullPointerException e) {
            log.error("Null Pointer Exception in JEditTextArea.setEditingFocus()");
        }
    }

    /**
     * Ensures that the specified line and offset is visible by scrolling the
     * text area if necessary.
     * @param line the line to scroll to
     * @param offset the offset in the line to scroll to
     */
    public void scrollTo(final int line, final int offset) {
        // visibleLines == 0 before the component is realized
        // we can't do any proper scrolling then, so we have
        // this hack...
        if (visibleLines == 0) {
            setFirstLine(Math.max(0, line - electricScroll));
            return;
        }

        int newFirstLine = firstLine;
        int newHorizontalOffset = horizontalOffset;

        if (line < firstLine + electricScroll) {
            newFirstLine = Math.max(0, line - electricScroll);
        } else if (line + electricScroll >= firstLine + visibleLines) {
            newFirstLine = (line - visibleLines) + electricScroll + 1;
            if (newFirstLine + visibleLines >= getLineCount()) {
                newFirstLine = getLineCount() - visibleLines;
            }
            if (newFirstLine < 0) {
                newFirstLine = 0;
            }
        }

        final int x = offsetToX2(line, offset);
        final int width = painter.getFontMetrics().charWidth('w');

        if (x < 0) {
            newHorizontalOffset = Math.min(0, horizontalOffset - x + width + 5);
        } else if (x + width >= painter.getWidth()) {
            newHorizontalOffset = horizontalOffset + (painter.getWidth() - x) - width - 5;
        }

        setOrigin(newFirstLine, newHorizontalOffset);
    }

    /**
     * Converts a line index to a y co-ordinate.
     * @param line the line
     */
    public int lineToY(final int line) {
        final FontMetrics fm = painter.getFontMetrics();
        return (line - firstLine) * fm.getHeight() - (fm.getLeading() + fm.getMaxDescent());
    }

    /**
     * Converts a y co-ordinate to a line index.
     * @param y the y co-ordinate
     */
    public int yToLine(final int y) {
        final FontMetrics fm = painter.getFontMetrics();
        final int height = fm.getHeight();
        return Math.max(0, Math.min(getLineCount() - 1, y / height + firstLine));
    }

    /**
     * Converts an offset in a line into an x co-ordinate. This is a slow
     * version that can be used any time.
     * @param line the line
     * @param offset the offset, from the start of the line
     */
    public int offsetToX(final int line, final int offset) {
        // don't use cached tokens
        painter.setCurrentLineTokens(null);
        return offsetToX2(line, offset);
    }

    /**
     * Converts an offset in a line into an x co-ordinate. This is a fast
     * version that should only be used if no changes were made to the text
     * since the last repaint.
     * @param line the line
     * @param offset the offset, from the start of the line
     */
    public int offsetToX2(final int line, final int offset) {
        final TokenMarker tokenMarker = getTokenMarker();

        /* Use painter's cached info for speed */
        FontMetrics fm = painter.getFontMetrics();

        getLineText(line, lineSegment);

        final int segmentOffset = lineSegment.offset;
        int x = horizontalOffset;

        /* If syntax coloring is disabled, do simple translation */
        if (tokenMarker == null) {
            lineSegment.count = offset;
            return x + Utilities.getTabbedTextWidth(lineSegment, fm, x, painter, 0);
        } else {
            /* If syntax coloring is enabled, we have to do this because
      * tokens can vary in width */
            final List<Token> tokens;
            if (painter.getCurrentLineIndex() == line && painter.getCurrentLineTokens() != null) {
                tokens = painter.getCurrentLineTokens();
            } else {
                painter.setCurrentLineIndex(line);
                tokens = tokenMarker.markTokens(lineSegment, line);
                painter.setCurrentLineTokens(tokens);
            }

            final Font defaultFont = painter.getFont();
            final SyntaxStyles styles = painter.getStyles();

            for (final Token token : tokens) {
                final byte id = token.getId();
                if (id == Token.NULL) {
                    fm = painter.getFontMetrics();
                } else {
                    fm = styles.getStyle(id).getFontMetrics(defaultFont, painter.getGraphics());
                }

                final int length = token.getLength();

                if (offset + segmentOffset < lineSegment.offset + length) {
                    lineSegment.count = offset - (lineSegment.offset - segmentOffset);
                    return x + Utilities.getTabbedTextWidth(lineSegment, fm, x, painter, 0);
                }
                lineSegment.count = length;
                x += Utilities.getTabbedTextWidth(lineSegment, fm, x, painter, 0);
                lineSegment.offset += length;
            }
            return x;
        }
    }

    /**
     * Converts an x co-ordinate to an offset within a line.
     * @param line the line
     * @param x the x co-ordinate
     */
    public int xToOffset(final int line, final int x) {
        final TokenMarker tokenMarker = getTokenMarker();

        /* Use painter's cached info for speed */
        FontMetrics fm = painter.getFontMetrics();

        getLineText(line, lineSegment);

        final char[] segmentArray = lineSegment.array;
        final int segmentOffset = lineSegment.offset;
        final int segmentCount = lineSegment.count;

        int width = horizontalOffset;

        if (tokenMarker == null) {
            for (int i = 0; i < segmentCount; i++) {
                final char c = segmentArray[i + segmentOffset];
                final int charWidth;
                if (c == '\t') {
                    charWidth = (int) painter.nextTabStop((float) width, i) - width;
                } else {
                    charWidth = fm.charWidth(c);
                }

                if (painter.isBlockCaretEnabled()) {
                    if (x - charWidth <= width) {
                        return i;
                    }
                } else {
                    if (x - charWidth / 2 <= width) {
                        return i;
                    }
                }

                width += charWidth;
            }

            return segmentCount;
        } else {
            final List<Token> tokens;
            if (painter.getCurrentLineIndex() == line && painter.getCurrentLineTokens() != null) {
                tokens = painter.getCurrentLineTokens();
            } else {
                painter.setCurrentLineIndex(line);
                tokens = tokenMarker.markTokens(lineSegment, line);
                painter.setCurrentLineTokens(tokens);
            }

            int offset = 0;
            final Font defaultFont = painter.getFont();
            final SyntaxStyles styles = painter.getStyles();

            for (final Token token : tokens) {
                final byte id = token.getId();
                if (id == Token.NULL) {
                    fm = painter.getFontMetrics();
                } else {
                    fm = styles.getStyle(id).getFontMetrics(defaultFont, painter.getGraphics());
                }

                final int length = token.getLength();

                for (int i = 0; i < length; i++) {
                    final char c = segmentArray[segmentOffset + offset + i];
                    final int charWidth;
                    if (c == '\t') {
                        charWidth = (int) painter.nextTabStop((float) width, offset + i) - width;
                    } else {
                        charWidth = fm.charWidth(c);
                    }

                    if (painter.isBlockCaretEnabled()) {
                        if (x - charWidth <= width) {
                            return offset + i;
                        }
                    } else {
                        if (x - charWidth / 2 <= width) {
                            return offset + i;
                        }
                    }

                    width += charWidth;
                }

                offset += length;
            }
            return offset;
        }
    }

    /**
     * Converts a point to an offset, from the start of the text.
     * @param x the x co-ordinate of the point
     * @param y the y co-ordinate of the point
     */
    public int xyToOffset(final int x, final int y) {
        final int line = yToLine(y);
        final int start = getLineStartOffset(line);
        return start + xToOffset(line, x);
    }

    /**
     * Returns the document this text area is editing.
     */
    @NotNull
    @SuppressWarnings("NullableProblems")
    public SyntaxDocument getDocument() {
        if (document == null) {
            throw new IllegalStateException();
        }
        return document;
    }

    /**
     * Sets the document this text area is editing.
     * @param document the document
     */
    public final void setDocument(@Nullable final SyntaxDocument document) {
        if (this.document == document) {
            return;
        }

        if (this.document != null) {
            this.document.removeDocumentListener(documentHandler);
        }

        this.document = document;

        document.addDocumentListener(documentHandler);

        select(0, 0);
        updateScrollBars();
        painter.repaint();
    }

    /**
     * Returns the document's token marker. Equivalent to calling
     * <code>getDocument().getTokenMarker()</code>.
     */
    @Nullable
    public TokenMarker getTokenMarker() {
        return document.getTokenMarker();
    }

    /**
     * Returns the length of the document. Equivalent to calling
     * <code>getDocument().getLength()</code>.
     */
    public int getDocumentLength() {
        return document.getLength();
    }

    /**
     * Returns the number of lines in the document.
     */
    public int getLineCount() {
        return document.getDefaultRootElement().getElementCount();
    }

    /**
     * Returns the line containing the specified offset.
     * @param offset the offset
     */
    public int getLineOfOffset(final int offset) {
        return document.getDefaultRootElement().getElementIndex(offset);
    }

    /**
     * Returns the start offset of the specified line.
     * @param line the line
     * @return the start offset of the specified line, or -1 if the line is
     *         invalid
     */
    public int getLineStartOffset(final int line) {
        final Element lineElement = document.getDefaultRootElement().getElement(line);
        if (lineElement == null) {
            return -1;
        } else {
            return lineElement.getStartOffset();
        }
    }

    /**
     * Returns the end offset of the specified line.
     * @param line the line
     * @return the end offset of the specified line, or -1 if the line is
     *         invalid
     */
    public int getLineEndOffset(final int line) {
        final Element lineElement = document.getDefaultRootElement().getElement(line);
        if (lineElement == null) {
            return -1;
        } else {
            return lineElement.getEndOffset();
        }
    }

    /**
     * Returns the length of the specified line.
     * @param line the line
     */
    public int getLineLength(final int line) {
        final Element lineElement = document.getDefaultRootElement().getElement(line);
        if (lineElement == null) {
            return -1;
        } else {
            return lineElement.getEndOffset() - lineElement.getStartOffset() - 1;
        }
    }

    /**
     * Returns the entire text of this text area.
     */
    @NotNull
    public String getText() {
        try {
            return document.getText(0, document.getLength());
        } catch (final BadLocationException bl) {
            bl.printStackTrace();
            return "";
        }
    }

    /**
     * Sets the entire text of this text area.
     */
    public void setText(@NotNull final String text) {
        try {
            SyntaxDocument.beginCompoundEdit();
            document.remove(0, document.getLength());
            document.insertString(0, text, null);
        } catch (final BadLocationException bl) {
            bl.printStackTrace();
        } finally {
            SyntaxDocument.endCompoundEdit();
        }
    }

    /**
     * Returns the specified substring of the document.
     * @param start the start offset
     * @param len the length of the substring
     * @return the substring, or null if the offsets are invalid
     */
    @Nullable
    public String getText(final int start, final int len) {
        try {
            return document.getText(start, len);
        } catch (final BadLocationException bl) {
            bl.printStackTrace();
            return null;
        }
    }

    /**
     * Copies the specified substring of the document into a segment. If the
     * offsets are invalid, the segment will contain a null string.
     * @param start the start offset
     * @param len the length of the substring
     * @param segment the segment
     */
    public void getText(final int start, final int len, @NotNull final Segment segment) {
        try {
            document.getText(start, len, segment);
        } catch (final BadLocationException bl) {
            bl.printStackTrace();
            segment.offset = 0;
            segment.count = 0;
        }
    }

    /**
     * Returns the text on the specified line.
     * @param lineIndex the line
     * @return the text, or null if the line is invalid
     */
    @NotNull
    public CharSequence getLineText(final int lineIndex) {
        final int start = getLineStartOffset(lineIndex);
        return getText(start, getLineEndOffset(lineIndex) - start - 1);
    }

    /**
     * Copies the text on the specified line into a segment. If the line is
     * invalid, the segment will contain a null string.
     * @param lineIndex the line
     */
    public void getLineText(final int lineIndex, @NotNull final Segment segment) {
        final int start = getLineStartOffset(lineIndex);
        getText(start, getLineEndOffset(lineIndex) - start - 1, segment);
    }

    /**
     * Returns the selection start offset.
     */
    public int getSelectionStart() {
        return selectionStart;
    }

    /**
     * Returns the selection start line.
     */
    public int getSelectionStartLine() {
        return selectionStartLine;
    }

    /**
     * Returns the selection end offset.
     */
    public int getSelectionEnd() {
        return selectionEnd;
    }

    /**
     * Returns the selection end line.
     */
    public int getSelectionEndLine() {
        return selectionEndLine;
    }

    /**
     * Returns the caret position. This will either be the selection start or
     * the selection end, depending on which direction the selection was made
     * in.
     */
    public int getCaretPosition() {
        return biasLeft ? selectionStart : selectionEnd;
    }

    /**
     * Returns the caret line.
     */
    public int getCaretLine() {
        return biasLeft ? selectionStartLine : selectionEndLine;
    }

    /**
     * Returns the mark position. This will be the opposite selection bound to
     * the caret position.
     * @see #getCaretPosition()
     */
    public int getMarkPosition() {
        return biasLeft ? selectionEnd : selectionStart;
    }

    /**
     * Sets the caret position. The new selection will consist of the caret
     * position only (hence no text will be selected).
     * @param caret the caret position
     * @see #select(int, int)
     */
    public void setCaretPosition(final int caret) {
        select(caret, caret);
    }

    /**
     * Selects all text in the document.
     */
    public void selectAll() {
        select(0, getDocumentLength());
    }

    /**
     * Selects from the start offset to the end offset. This is the general
     * selection method used by all other selecting methods. The caret position
     * will be start if start &lt; end, and end if end &gt; start.
     * @param start the start offset
     * @param end the end offset
     */
    public void select(final int start, final int end) {
        final int newStart;
        final int newEnd;
        final boolean newBias;
        if (start <= end) {
            newStart = start;
            newEnd = end;
            newBias = false;
        } else {
            newStart = end;
            newEnd = start;
            newBias = true;
        }

        if (newStart < 0 || newEnd > getDocumentLength()) {
            throw new IllegalArgumentException("Bounds out of range: " + newStart + ", " + newEnd);
        }

        // If the new position is the same as the old, we don't
        // do all this crap, however we still do the stuff at
        // the end (clearing magic position, scrolling)
        if (newStart != selectionStart || newEnd != selectionEnd || newBias != biasLeft) {
            final int newStartLine = getLineOfOffset(newStart);
            final int newEndLine = getLineOfOffset(newEnd);

            if (painter.isBracketHighlightEnabled()) {
                if (bracketLine != -1) {
                    painter.invalidateLine(bracketLine);
                }

                updateBracketHighlight(end);
                if (bracketLine != -1) {
                    painter.invalidateLine(bracketLine);
                }
            }

            painter.invalidateLineRange(selectionStartLine, selectionEndLine);
            painter.invalidateLineRange(newStartLine, newEndLine);

            SyntaxDocument.addUndoableEdit(new CaretUndo(selectionStart, selectionEnd));

            selectionStart = newStart;
            selectionEnd = newEnd;
            selectionStartLine = newStartLine;
            selectionEndLine = newEndLine;
            biasLeft = newBias;
        }

        // When the user is typing, etc, we don't want the caret
        // to blink
        blink = true;
        caretTimer.restart();

        // Disable rectangle select if selection start = selection end
        if (selectionStart == selectionEnd) {
            rectangleSelect = false;
        }

        // Clear the `magic' caret position used by up/down
        magicCaret = -1;

        scrollToCaret();
    }

    /**
     * Returns the selected text, or null if no selection is active.
     */
    @Nullable
    public String getSelectedText() {
        if (selectionStart == selectionEnd) {
            return null;
        }

        if (rectangleSelect) {
            // Return each row of the selection on a new line

            final Element map = document.getDefaultRootElement();

            int start = selectionStart - map.getElement(selectionStartLine).getStartOffset();
            int end = selectionEnd - map.getElement(selectionEndLine).getStartOffset();

            // Certain rectangles satisfy this condition...
            if (end < start) {
                final int tmp = end;
                end = start;
                start = tmp;
            }

            final StringBuilder buf = new StringBuilder();
            final Segment seg = new Segment();

            for (int i = selectionStartLine; i <= selectionEndLine; i++) {
                final Element lineElement = map.getElement(i);
                int lineStart = lineElement.getStartOffset();
                final int lineEnd = lineElement.getEndOffset() - 1;

                lineStart = Math.min(lineStart + start, lineEnd);
                final int lineLen = Math.min(end - start, lineEnd - lineStart);

                getText(lineStart, lineLen, seg);
                buf.append(seg.array, seg.offset, seg.count);

                if (i != selectionEndLine) {
                    buf.append('\n');
                }
            }

            return buf.toString();
        } else {
            return getText(selectionStart, selectionEnd - selectionStart);
        }
    }

    /**
     * Replaces the selection with the specified text.
     * @param selectedText the replacement text for the selection
     */
    public void setSelectedText(@NotNull final String selectedText) {
        if (!editable) {
            throw new InternalError("Text component read only");
        }

        SyntaxDocument.beginCompoundEdit();

        try {
            if (rectangleSelect) {
                final Element map = document.getDefaultRootElement();

                int start = selectionStart - map.getElement(selectionStartLine).getStartOffset();
                int end = selectionEnd - map.getElement(selectionEndLine).getStartOffset();

                // Certain rectangles satisfy this condition...
                if (end < start) {
                    final int tmp = end;
                    end = start;
                    start = tmp;
                }

                int lastNewline = 0;
                int currNewline = 0;

                for (int i = selectionStartLine; i <= selectionEndLine; i++) {
                    final Element lineElement = map.getElement(i);
                    final int lineStart = lineElement.getStartOffset();
                    final int lineEnd = lineElement.getEndOffset() - 1;
                    final int rectangleStart = Math.min(lineEnd, lineStart + start);

                    document.remove(rectangleStart, Math.min(lineEnd - rectangleStart, end - start));

                    if (selectedText == null) {
                        continue;
                    }

                    currNewline = selectedText.indexOf('\n', lastNewline);
                    if (currNewline == -1) {
                        currNewline = selectedText.length();
                    }

                    document.insertString(rectangleStart, selectedText.substring(lastNewline, currNewline), null);

                    lastNewline = Math.min(selectedText.length(), currNewline + 1);
                }

                if (selectedText != null && currNewline != selectedText.length()) {
                    final int offset = map.getElement(selectionEndLine).getEndOffset() - 1;
                    document.insertString(offset, "\n", null);
                    document.insertString(offset + 1, selectedText.substring(currNewline + 1), null);
                }
            } else {
                document.remove(selectionStart, selectionEnd - selectionStart);
                if (selectedText != null) {
                    document.insertString(selectionStart, selectedText, null);
                }
            }
        } catch (final BadLocationException bl) {
            bl.printStackTrace();
            throw new InternalError("Cannot replace selection");
        } finally {
            // No matter what happens... stops us from leaving document
            // in a bad state
            SyntaxDocument.endCompoundEdit();
        }

        setCaretPosition(selectionEnd);
    }

    /**
     * Returns true if this text area is editable, false otherwise.
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Returns the `magic' caret position. This can be used to preserve the
     * column position when moving up and down lines.
     */
    public int getMagicCaretPosition() {
        return magicCaret;
    }

    /**
     * Sets the `magic' caret position. This can be used to preserve the column
     * position when moving up and down lines.
     * @param magicCaret the magic caret position
     */
    public void setMagicCaretPosition(final int magicCaret) {
        this.magicCaret = magicCaret;
    }

    /**
     * Similar to <code>setSelectedText()</code>, but overstrikes the
     * appropriate number of characters if overwrite mode is enabled.
     * @param str the string
     * @see #setSelectedText(String)
     * @see #isOverwriteEnabled()
     */
    public void overwriteSetSelectedText(@NotNull final String str) {
        // Don't overstrike if there is a selection
        if (!overwrite || selectionStart != selectionEnd) {
            setSelectedText(str);
            return;
        }

        // Don't overstrike if we're on the end of
        // the line
        final int caret = getCaretPosition();
        final int caretLineEnd = getLineEndOffset(getCaretLine());
        if (caretLineEnd - caret <= str.length()) {
            setSelectedText(str);
            return;
        }

        SyntaxDocument.beginCompoundEdit();

        try {
            document.remove(caret, str.length());
            document.insertString(caret, str, null);
        } catch (final BadLocationException bl) {
            bl.printStackTrace();
        } finally {
            SyntaxDocument.endCompoundEdit();
        }
    }

    /**
     * Returns true if overwrite mode is enabled, false otherwise.
     */
    public boolean isOverwriteEnabled() {
        return overwrite;
    }

    /**
     * Sets if overwrite mode should be enabled.
     * @param overwrite true if overwrite mode should be enabled, false
     * otherwise
     */
    public void setOverwriteEnabled(final boolean overwrite) {
        this.overwrite = overwrite;
        painter.invalidateSelectedLines();
    }

    /**
     * Returns true if the selection is rectangular, false otherwise.
     */
    public boolean isSelectionRectangular() {
        return rectangleSelect;
    }

    /**
     * Sets if the selection should be rectangular.
     * @param rectangleSelect true if the selection should be rectangular, false
     * otherwise
     */
    public void setSelectionRectangular(final boolean rectangleSelect) {
        this.rectangleSelect = rectangleSelect;
        painter.invalidateSelectedLines();
    }

    /**
     * Returns the position of the highlighted bracket (the bracket matching the
     * one before the caret).
     */
    public int getBracketPosition() {
        return bracketPosition;
    }

    /**
     * Returns the line of the highlighted bracket (the bracket matching the one
     * before the caret).
     */
    public int getBracketLine() {
        return bracketLine;
    }

    /**
     * Deletes the selected text from the text area and places it into the
     * clipboard.
     */
    public void cut() {
        if (editable) {
            copy();
            setSelectedText("");
        }
    }

    /**
     * Places the selected text into the clipboard.
     */
    public void copy() {
        if (selectionStart != selectionEnd) {
            final Clipboard clipboard = getToolkit().getSystemClipboard();

            final String selection = getSelectedText();

            final int repeatCount = inputHandler.getRepeatCount();
            final StringBuilder buf = new StringBuilder();
            for (int i = 0; i < repeatCount; i++) {
                buf.append(selection);
            }

            clipboard.setContents(new StringSelection(buf.toString()), null);
        }
    }

    /**
     * Inserts the clipboard contents into the text.
     */
    public void paste() {
        if (editable) {
            final Clipboard clipboard = getToolkit().getSystemClipboard();
            try {
                // The MacOS MRJ doesn't convert \r to \n,
                // so do it here
                final String selection = ((String) clipboard.getContents(this).getTransferData(DataFlavor.stringFlavor)).replace('\r', '\n');

                final int repeatCount = inputHandler.getRepeatCount();
                final StringBuilder buf = new StringBuilder();
                for (int i = 0; i < repeatCount; i++) {
                    buf.append(selection);
                }
                setSelectedText(buf.toString());
            } catch (final IOException e) {
                getToolkit().beep();
                log.error("Clipboard does not contain a string");
            } catch (final UnsupportedFlavorException e) {
                getToolkit().beep();
                log.error("Clipboard does not contain a string");
            }
        }
    }

    /**
     * Called by the AWT when this component is removed from it's parent. This
     * stops clears the currently focused component.
     */
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (focusedComponent == this) {
            focusedComponent = null;
        }
    }

    /**
     * Forwards key events directly to the input handler. This is slightly
     * faster than using a KeyListener because some Swing overhead is avoided.
     */
    @Override
    public void processKeyEvent(@NotNull final KeyEvent e) {
        if (inputHandler == null) {
            return;
        }

        switch (e.getID()) {
        case KeyEvent.KEY_TYPED:
            inputHandler.keyTyped(e);
            break;

        case KeyEvent.KEY_PRESSED:
            inputHandler.keyPressed(e);
            break;

        case KeyEvent.KEY_RELEASED:
            inputHandler.keyReleased(e);
            break;
        }

        super.processKeyEvent(e);
    }

    void updateBracketHighlight(final int newCaretPosition) {
        if (newCaretPosition == 0) {
            bracketPosition = -1;
            bracketLine = -1;
            return;
        }

        try {
            final int offset = TextUtilities.findMatchingBracket(document, newCaretPosition - 1);
            if (offset != -1) {
                bracketLine = getLineOfOffset(offset);
                bracketPosition = offset - getLineStartOffset(bracketLine);
                return;
            }
        } catch (final BadLocationException bl) {
            bl.printStackTrace();
        }

        bracketLine = -1;
        bracketPosition = -1;
    }

    void documentChanged(@NotNull final DocumentEvent evt) {
        final DocumentEvent.ElementChange ch = evt.getChange(document.getDefaultRootElement());

        final int count;
        if (ch == null) {
            count = 0;
        } else {
            count = ch.getChildrenAdded().length - ch.getChildrenRemoved().length;
        }

        final int line = getLineOfOffset(evt.getOffset());
        if (count == 0) {
            painter.invalidateLine(line);
        } else if (line < firstLine) {
            // do magic stuff
            setFirstLine(firstLine + count);
            // end of magic stuff
        } else {
            painter.invalidateLineRange(line, firstLine + visibleLines);
            updateScrollBars();
        }
    }

    /**
     * Return whether the text content has been modified from the "unmodified"
     * state.
     * @return <code>true</code> if the text content has been modified, or
     *         <code>false</code> if it is unmodified
     */
    public boolean isModified() {
        return !unmodifiedText.equals(getText());
    }

    /**
     * Reset the "modified" state.
     */
    public void resetModified() {
        unmodifiedText = getText();
    }

    private static class CaretBlinker implements ActionListener {

        @Override
        public void actionPerformed(@NotNull final ActionEvent e) {
            if (focusedComponent != null && focusedComponent.hasFocus()) {
                focusedComponent.blinkCaret();
            }
        }

    }

    private class MutableCaretEvent extends CaretEvent {

        /**
         * Serial Version UID.
         */
        private static final long serialVersionUID = 1L;

        MutableCaretEvent() {
            super(JEditTextArea.this);
        }

        @Override
        public int getDot() {
            return getCaretPosition();
        }

        @Override
        public int getMark() {
            return getMarkPosition();
        }

    }

    private class AdjustHandler implements AdjustmentListener {

        @Override
        public void adjustmentValueChanged(@NotNull final AdjustmentEvent e) {
            if (!scrollBarsInitialized) {
                return;
            }

            // If this is not done, mousePressed events accumulate
            // and the result is that scrolling doesn't stop after
            // the mouse is released
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    if (e.getAdjustable() == vertical) {
                        setFirstLine(vertical.getValue());
                    } else {
                        setHorizontalOffset(-horizontal.getValue());
                    }
                }

            });
        }

    }

    private class ComponentHandler extends ComponentAdapter {

        @Override
        public void componentResized(@NotNull final ComponentEvent e) {
            recalculateVisibleLines();
            scrollBarsInitialized = true;
        }

    }

    private class DocumentHandler implements DocumentListener {

        @Override
        public void insertUpdate(@NotNull final DocumentEvent e) {
            documentChanged(e);

            final int offset = e.getOffset();
            final int length = e.getLength();

            final int newStart;
            if (selectionStart > offset || (selectionStart == selectionEnd && selectionStart == offset)) {
                newStart = selectionStart + length;
            } else {
                newStart = selectionStart;
            }

            final int newEnd;
            if (selectionEnd >= offset) {
                newEnd = selectionEnd + length;
            } else {
                newEnd = selectionEnd;
            }

            select(newStart, newEnd);
        }

        @Override
        public void removeUpdate(@NotNull final DocumentEvent e) {
            documentChanged(e);

            final int offset = e.getOffset();
            final int length = e.getLength();

            final int newStart;
            if (selectionStart > offset) {
                if (selectionStart > offset + length) {
                    newStart = selectionStart - length;
                } else {
                    newStart = offset;
                }
            } else {
                newStart = selectionStart;
            }

            final int newEnd;
            if (selectionEnd > offset) {
                if (selectionEnd > offset + length) {
                    newEnd = selectionEnd - length;
                } else {
                    newEnd = offset;
                }
            } else {
                newEnd = selectionEnd;
            }

            select(newStart, newEnd);
        }

        @Override
        public void changedUpdate(@NotNull final DocumentEvent e) {
        }

    }

    private class DragHandler implements MouseMotionListener {

        @Override
        public void mouseDragged(@NotNull final MouseEvent e) {
            if (popup != null && popup.isVisible()) {
                return;
            }

            setSelectionRectangular((e.getModifiers() & InputEvent.CTRL_MASK) != 0);
            select(getMarkPosition(), xyToOffset(e.getX(), e.getY()));
        }

        @Override
        public void mouseMoved(@NotNull final MouseEvent e) {
        }

    }

    private class FocusHandler implements FocusListener {

        @Override
        public void focusGained(@NotNull final FocusEvent e) {
            setCaretVisible(true);
            focusedComponent = JEditTextArea.this;
        }

        @Override
        public void focusLost(@NotNull final FocusEvent e) {
            setCaretVisible(false);
            focusedComponent = null;
        }

    }

    /**
     * @noinspection RefusedBequest
     */
    private class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(@NotNull final MouseEvent e) {
            requestFocus();

            // Focus events not fired sometimes?
            setCaretVisible(true);
            focusedComponent = JEditTextArea.this;

            if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0 && popup != null) {
                popup.show(painter, e.getX(), e.getY());
                return;
            }

            final int line = yToLine(e.getY());
            final int offset = xToOffset(line, e.getX());
            final int dot = getLineStartOffset(line) + offset;

            switch (e.getClickCount()) {
            case 1:
                doSingleClick(e, dot);
                break;
            case 2:
                doDoubleClick(line, offset, dot);
                break;
            case 3:
                doTripleClick(line);
                break;
            }
        }

        @Override
        public void mouseWheelMoved(@NotNull final MouseWheelEvent e) {
            final int diff;
            if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
                diff = e.getUnitsToScroll() * vertical.getUnitIncrement(1);
            } else {
                diff = e.getWheelRotation() * vertical.getBlockIncrement();
            }
            vertical.setValue(vertical.getValue() + diff);
        }

        private void doSingleClick(@NotNull final InputEvent evt, final int dot) {
            if ((evt.getModifiers() & InputEvent.SHIFT_MASK) == 0) {
                setCaretPosition(dot);
            } else {
                rectangleSelect = (evt.getModifiers() & InputEvent.CTRL_MASK) != 0;
                select(getMarkPosition(), dot);
            }
        }

        private void doDoubleClick(final int line, final int offset, final int dot) {
            // Ignore empty lines
            if (getLineLength(line) == 0) {
                return;
            }

            try {
                int bracket = TextUtilities.findMatchingBracket(document, Math.max(0, dot - 1));
                if (bracket != -1) {
                    int mark = getMarkPosition();
                    // Hack
                    if (bracket > mark) {
                        bracket++;
                        mark--;
                    }
                    select(mark, bracket);
                    return;
                }
            } catch (final BadLocationException bl) {
                bl.printStackTrace();
            }

            // Ok, it's not a bracket... select the word
            final CharSequence lineText = getLineText(line);
            char ch = lineText.charAt(Math.max(0, offset - 1));

            String noWordSep = (String) document.getProperty("noWordSep");
            if (noWordSep == null) {
                noWordSep = "";
            }

            // If the user clicked on a non-letter char,
            // we select the surrounding non-letters
            final boolean selectNoLetter = !Character.isLetterOrDigit(ch) && noWordSep.indexOf(ch) == -1;

            int wordStart = 0;

            for (int i = offset - 1; i >= 0; i--) {
                ch = lineText.charAt(i);
                if (selectNoLetter ^ (!Character.isLetterOrDigit(ch) && noWordSep.indexOf(ch) == -1)) {
                    wordStart = i + 1;
                    break;
                }
            }

            int wordEnd = lineText.length();
            for (int i = offset; i < lineText.length(); i++) {
                ch = lineText.charAt(i);
                if (selectNoLetter ^ (!Character.isLetterOrDigit(ch) && noWordSep.indexOf(ch) == -1)) {
                    wordEnd = i;
                    break;
                }
            }

            final int lineStart = getLineStartOffset(line);
            select(lineStart + wordStart, lineStart + wordEnd);

            /*
            String lineText = getLineText(line);
            String noWordSep = (String)document.getProperty("noWordSep");
            int wordStart = TextUtilities.findWordStart(lineText, offset, noWordSep);
            int wordEnd = TextUtilities.findWordEnd(lineText, offset, noWordSep);

            int lineStart = getLineStartOffset(line);
            select(lineStart + wordStart, lineStart + wordEnd);
            */
        }

        private void doTripleClick(final int line) {
            select(getLineStartOffset(line), getLineEndOffset(line) - 1);
        }

    }

    private class CaretUndo extends AbstractUndoableEdit {

        /**
         * Serial Version UID.
         */
        private static final long serialVersionUID = 1L;

        private int start;

        private int end;

        CaretUndo(final int start, final int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean isSignificant() {
            return false;
        }

        @NotNull
        @Override
        public String getPresentationName() {
            return "caret move";
        }

        @Override
        public void undo() throws CannotUndoException {
            super.undo();

            select(start, end);
        }

        @Override
        public void redo() throws CannotRedoException {
            super.redo();

            select(start, end);
        }

        @Override
        public boolean addEdit(@NotNull final UndoableEdit anEdit) {
            if (anEdit instanceof CaretUndo) {
                final CaretUndo caretUndo = (CaretUndo) anEdit;
                start = caretUndo.start;
                end = caretUndo.end;
                caretUndo.die();

                return true;
            } else {
                return false;
            }
        }

    }

}
