/*
 * TextAreaDefaults.java - Encapsulates default values for various settings
 * Copyright (C) 1999 Slava Pestov
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

package net.sf.gridarta.textedit.textarea;

import java.awt.Color;
import javax.swing.JPopupMenu;
import net.sf.gridarta.textedit.scripteditor.ScriptEditControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Encapsulates default settings for a text area. This can be passed to the
 * constructor once the necessary fields have been filled out. The advantage of
 * doing this over calling lots of set() methods after creating the text area is
 * that this method is faster.
 */
public class TextAreaDefaults {

    @NotNull
    private final InputHandler inputHandler;

    @Nullable
    private final SyntaxDocument document = null;

    private final boolean editable = true;

    private final boolean caretVisible = true;

    private final boolean caretBlinks = true;

    private final boolean blockCaret = false;

    private final int electricScroll = 3;

    private final int cols = 80;

    private final int rows = 25;

    /**
     * The syntax styles used to paint colorized text.
     */
    @NotNull
    private final SyntaxStyles styles = SyntaxUtilities.getDefaultSyntaxStyles();

    @NotNull
    private final Color caretColor = Color.red;

    @NotNull
    private final Color selectionColor = new Color(0xccccff);

    @NotNull
    private final Color lineHighlightColor = new Color(0xffff80);

    private final boolean lineHighlight = true;

    @NotNull
    private final Color bracketHighlightColor = Color.gray;

    private final boolean bracketHighlight = true;

    @NotNull
    private final Color eolMarkerColor = new Color(0x009999);

    private final boolean eolMarkers = false;

    private final boolean paintInvalid = true;

    @Nullable
    private final JPopupMenu popup = null;

    /**
     * Creates a new instance.
     * @param scriptEditControl the script edit control to affect
     */
    public TextAreaDefaults(@NotNull final ScriptEditControl scriptEditControl) {
        inputHandler = new DefaultInputHandler(scriptEditControl);
        inputHandler.addDefaultKeyBindings();
    }

    @NotNull
    public InputHandler getInputHandler() {
        return inputHandler;
    }

    @Nullable
    public SyntaxDocument getDocument() {
        return document;
    }

    public boolean getEditable() {
        return editable;
    }

    public boolean getCaretVisible() {
        return caretVisible;
    }

    public boolean getCaretBlinks() {
        return caretBlinks;
    }

    public boolean getBlockCaret() {
        return blockCaret;
    }

    public int getElectricScroll() {
        return electricScroll;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    /**
     * Returns the syntax styles used to paint colorized text.
     * @return the styles
     */
    @NotNull
    public SyntaxStyles getStyles() {
        return styles;
    }

    @NotNull
    public Color getCaretColor() {
        return caretColor;
    }

    @NotNull
    public Color getSelectionColor() {
        return selectionColor;
    }

    @NotNull
    public Color getLineHighlightColor() {
        return lineHighlightColor;
    }

    public boolean getLineHighlight() {
        return lineHighlight;
    }

    @NotNull
    public Color getBracketHighlightColor() {
        return bracketHighlightColor;
    }

    public boolean getBracketHighlight() {
        return bracketHighlight;
    }

    @NotNull
    public Color getEolMarkerColor() {
        return eolMarkerColor;
    }

    public boolean getEolMarkers() {
        return eolMarkers;
    }

    public boolean getPaintInvalid() {
        return paintInvalid;
    }

    @Nullable
    public JPopupMenu getPopup() {
        return popup;
    }

}
