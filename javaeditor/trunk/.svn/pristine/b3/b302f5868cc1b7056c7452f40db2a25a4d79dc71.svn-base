/*
 * SyntaxDocument.java - Document that can be tokenized
 * Copyright (C) 1999 Slava Pestov
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

package net.sf.gridarta.textedit.textarea;

import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import javax.swing.undo.UndoableEdit;
import net.sf.gridarta.textedit.textarea.tokenmarker.TokenMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A document implementation that can be tokenized by the syntax highlighting
 * system.
 * @author Slava Pestov
 */
public class SyntaxDocument extends PlainDocument {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Returns the token marker that is to be used to split lines of this
     * document up into tokens. May return null if this document is not to be
     * colorized.
     */
    @Nullable
    public TokenMarker getTokenMarker() {
        return tokenMarker;
    }

    /**
     * Sets the token marker that is to be used to split lines of this document
     * up into tokens. May throw an exception if this is not supported for this
     * type of document.
     * @param tokenMarker the new token marker
     */
    public void setTokenMarker(@Nullable final TokenMarker tokenMarker) {
        this.tokenMarker = tokenMarker;
        if (tokenMarker == null) {
            return;
        }

        tokenMarker.insertLines(0, getDefaultRootElement().getElementCount());
        tokenizeLines();
    }

    /**
     * Re-parses the document, by passing all lines to the token marker. This
     * should be called after the document is first loaded.
     */
    private void tokenizeLines() {
        tokenizeLines(0, getDefaultRootElement().getElementCount());
    }

    /**
     * Re-parses the document, by passing the specified lines to the token
     * marker. This should be called after a large quantity of text is first
     * inserted.
     * @param start the first line to parse
     * @param len the number of lines, after the first one to parse
     */
    private void tokenizeLines(final int start, final int len) {
        if (tokenMarker == null) {
            return;
        }

        final Segment lineSegment = new Segment();
        final Element map = getDefaultRootElement();

        final int end = len + start;

        try {
            for (int i = start; i < end; i++) {
                final Element lineElement = map.getElement(i);
                final int lineStart = lineElement.getStartOffset();
                getText(lineStart, lineElement.getEndOffset() - lineStart - 1, lineSegment);
                tokenMarker.markTokens(lineSegment, i);
            }
        } catch (final BadLocationException bl) {
            bl.printStackTrace();
        }
    }

    /**
     * Starts a compound edit that can be undone in one operation. Subclasses
     * that implement undo should override this method; this class has no undo
     * functionality so this method is empty.
     */
    public static void beginCompoundEdit() {
    }

    /**
     * Ends a compound edit that can be undone in one operation. Subclasses that
     * implement undo should override this method; this class has no undo
     * functionality so this method is empty.
     */
    public static void endCompoundEdit() {
    }

    /**
     * Adds an undoable edit to this document's undo list. The edit should be
     * ignored if something is currently being undone.
     * @param edit The undoable edit
     * @since jEdit 2.2pre1
     */
    public static void addUndoableEdit(@NotNull final UndoableEdit edit) {
    }

    // protected members

    @Nullable
    private TokenMarker tokenMarker;

    /**
     * We overwrite this method to update the token marker state immediately so
     * that any event listeners get a consistent token marker.
     */
    @Override
    protected void fireInsertUpdate(@NotNull final DocumentEvent e) {
        if (tokenMarker != null) {
            final DocumentEvent.ElementChange ch = e.getChange(getDefaultRootElement());
            if (ch != null) {
                tokenMarker.insertLines(ch.getIndex() + 1, ch.getChildrenAdded().length - ch.getChildrenRemoved().length);
            }
        }

        super.fireInsertUpdate(e);
    }

    /**
     * We overwrite this method to update the token marker state immediately so
     * that any event listeners get a consistent token marker.
     */
    @Override
    protected void fireRemoveUpdate(@NotNull final DocumentEvent e) {
        if (tokenMarker != null) {
            final DocumentEvent.ElementChange ch = e.getChange(getDefaultRootElement());
            if (ch != null) {
                tokenMarker.deleteLines(ch.getIndex() + 1, ch.getChildrenRemoved().length - ch.getChildrenAdded().length);
            }
        }

        super.fireRemoveUpdate(e);
    }

}
