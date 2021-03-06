/*
 * TokenMarker.java - Generic token marker
 * Copyright (C) 1998, 1999 Slava Pestov
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

package net.sf.gridarta.textedit.textarea.tokenmarker;

import java.util.LinkedList;
import java.util.List;
import javax.swing.text.Segment;
import net.sf.gridarta.textedit.textarea.Token;
import org.jetbrains.annotations.Nullable;

/**
 * A token marker that splits lines of text into tokens. Each token carries a
 * length field and an identification tag that can be mapped to a color for
 * painting that token. <p/> <p>For performance reasons, the linked list of
 * tokens is reused after each line is tokenized. Therefore, the return value of
 * <code>markTokens</code> should only be used for immediate painting. Notably,
 * it cannot be cached.
 * @author Slava Pestov
 * @author Andreas Kirschbaum
 * @see Token
 */
public abstract class TokenMarker {

    /**
     * The collected tokens.
     */
    private final List<Token> tokens = new LinkedList<Token>();

    /**
     * An array for storing information about lines. It is enlarged and shrunk
     * automatically by the <code>insertLines()</code> and
     * <code>deleteLines()</code> methods.
     */
    private LineInfo[] lineInfo;

    /**
     * The number of lines in the model being tokenized. This can be less than
     * the length of the <code>lineInfo</code> array.
     */
    private int length;

    /**
     * The last tokenized line.
     */
    private int lastLine;

    /**
     * True if the next line should be painted.
     */
    private boolean nextLineRequested;

    /**
     * A wrapper for the lower-level <code>markTokensImpl</code> method that is
     * called to split a line up into tokens.
     * @param line the line
     * @param lineIndex the line number
     * @return the tokens
     */
    public List<Token> markTokens(final Segment line, final int lineIndex) {
        if (lineIndex >= length) {
            throw new IllegalArgumentException("Tokenizing invalid line: " + lineIndex);
        }

        tokens.clear();

        final LineInfo info = lineInfo[lineIndex];
        @Nullable final LineInfo prev;
        if (lineIndex == 0) {
            prev = null;
        } else {
            prev = lineInfo[lineIndex - 1];
        }

        final byte oldToken = info.getToken();
        final byte token = markTokensImpl(prev == null ? Token.NULL : prev.getToken(), line, lineIndex);

        info.setToken(token);

        /*
         * This is a foul hack. It stops nextLineRequested
         * from being cleared if the same line is marked twice.
         *
         * Why is this necessary? It's all JEditTextArea's fault.
         * When something is inserted into the text, firing a
         * document event, the insertUpdate() method shifts the
         * caret (if necessary) by the amount inserted.
         *
         * All caret movement is handled by the select() method,
         * which eventually pipes the new position to scrollTo()
         * and calls repaint().
         *
         * Note that at this point in time, the new line hasn't
         * yet been painted; the caret is moved first.
         *
         * scrollTo() calls offsetToX(), which tokenizes the line
         * unless it is being called on the last line painted
         * (in which case it uses the text area's painter cached
         * token list). What scrollTo() does next is irrelevant.
         *
         * After scrollTo() has done it's job, repaint() is
         * called, and eventually we end up in paintLine(), whose
         * job is to paint the changed line. It, too, calls
         * markTokens().
         *
         * The problem was that if the line started a multi.line
         * token, the first markTokens() (done in offsetToX())
         * would set nextLineRequested (because the line end
         * token had changed) but the second would clear it
         * (because the line was the same that time) and therefore
         * paintLine() would never know that it needed to repaint
         * subsequent lines.
         *
         * This bug took me ages to track down, that's why I wrote
         * all the relevant info down so that others wouldn't
         * duplicate it.
         */
        if (!(lastLine == lineIndex && nextLineRequested)) {
            nextLineRequested = oldToken != token;
        }

        lastLine = lineIndex;

        return tokens;
    }

    /**
     * An abstract method that splits a line up into tokens. It should parse the
     * line, and call <code>addToken()</code> to add syntax tokens to the token
     * list. Then, it should return the initial token type for the next line.<p>
     * <p/> For example if the current line contains the start of a multi-line
     * comment that doesn't end on that line, this method should return the
     * comment token type so that it continues on the next line.
     * @param token the initial token type for this line
     * @param line the line to be tokenized
     * @param lineIndex the index of the line in the document, starting at 0
     * @return the initial token type for the next line
     */
    protected abstract byte markTokensImpl(byte token, Segment line, int lineIndex);

    /**
     * Informs the token marker that lines have been inserted into the document.
     * This inserts a gap in the <code>lineInfo</code> array.
     * @param index the first line number
     * @param lines the number of lines
     */
    public void insertLines(final int index, final int lines) {
        if (lines <= 0) {
            return;
        }
        length += lines;
        ensureCapacity(length);
        final int len = index + lines;
        System.arraycopy(lineInfo, index, lineInfo, len, lineInfo.length - len);

        for (int i = index + lines - 1; i >= index; i--) {
            lineInfo[i] = new LineInfo();
        }
    }

    /**
     * Informs the token marker that line have been deleted from the document.
     * This removes the lines in question from the <code>lineInfo</code> array.
     * @param index the first line number
     * @param lines the number of lines
     */
    public void deleteLines(final int index, final int lines) {
        if (lines <= 0) {
            return;
        }
        final int len = index + lines;
        length -= lines;
        System.arraycopy(lineInfo, len, lineInfo, index, lineInfo.length - len);
    }

    /**
     * Returns the number of lines in this token marker.
     */
    public int getLineCount() {
        return length;
    }

    /**
     * Returns true if the next line should be repainted. This will return true
     * after a line has been tokenized that starts a multi-line token that
     * continues onto the next line.
     */
    public boolean isNextLineRequested() {
        return nextLineRequested;
    }

    /**
     * Creates a new <code>TokenMarker</code>. This DOES NOT create a lineInfo
     * array; an initial call to <code>insertLines()</code> does that.
     */
    protected TokenMarker() {
        lastLine = -1;
    }

    /**
     * Ensures that the <code>lineInfo</code> array can contain the specified
     * index. This enlarges it if necessary. No action is taken if the array is
     * large enough already.<p> <p/> It should be unnecessary to call this under
     * normal circumstances; <code>insertLine()</code> should take care of
     * enlarging the line info array automatically.
     * @param index the array index
     */
    private void ensureCapacity(final int index) {
        if (lineInfo == null) {
            lineInfo = new LineInfo[index + 1];
        } else if (lineInfo.length <= index) {
            final LineInfo[] lineInfoN = new LineInfo[(index + 1) * 2];
            System.arraycopy(lineInfo, 0, lineInfoN, 0, lineInfo.length);
            lineInfo = lineInfoN;
        }
    }

    /**
     * Adds a token to the token list.
     * @param length the length of the token
     * @param id the id of the token
     */
    protected void addToken(final int length, final byte id) {
        if (id >= Token.INTERNAL_FIRST && id <= Token.INTERNAL_LAST) {
            throw new InternalError("Invalid id: " + id);
        }

        if (length == 0) {
            return;
        }

        tokens.add(new Token(length, id));
    }

}
