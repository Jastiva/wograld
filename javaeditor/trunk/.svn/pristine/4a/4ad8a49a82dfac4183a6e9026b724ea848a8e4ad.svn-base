/*
 * Token.java - Generic token
 * Copyright (C) 1998, 1999 Slava Pestov
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

package net.sf.gridarta.textedit.textarea;

/**
 * A linked list of tokens. Each token has three fields - a token identifier,
 * which is a byte value that can be looked up in the array returned by
 * <code>SyntaxDocument.getColors()</code> to get a color value and a length
 * value which is the length of the token in the text.
 * @author Slava Pestov
 * @author Andreas Kirschbaum
 */
public class Token {

    /**
     * Normal text token id. This should be used to mark normal text.
     */
    public static final byte NULL = (byte) 0;

    /**
     * Comment 1 token id. This can be used to mark a comment.
     */
    public static final byte COMMENT1 = (byte) 1;

    /**
     * Comment 2 token id. This can be used to mark a comment.
     */
    public static final byte COMMENT2 = (byte) 2;

    /**
     * Literal 1 token id. This can be used to mark a string literal (eg, C mode
     * uses this to mark "..." literals).
     */
    public static final byte LITERAL1 = (byte) 3;

    /**
     * Literal 2 token id. This can be used to mark an object literal (eg, Java
     * mode uses this to mark true, false, etc).
     */
    public static final byte LITERAL2 = (byte) 4;

    /**
     * Label token id. This can be used to mark labels (eg, C mode uses this to
     * mark ...: sequences).
     */
    public static final byte LABEL = (byte) 5;

    /**
     * Keyword 1 token id. This can be used to mark a keyword. This should be
     * used for general language constructs.
     */
    public static final byte KEYWORD1 = (byte) 6;

    /**
     * Keyword 2 token id. This can be used to mark a keyword. This should be
     * used for preprocessor commands, or variables.
     */
    public static final byte KEYWORD2 = (byte) 7;

    /**
     * Keyword 3 token id. This can be used to mark a keyword. This should be
     * used for data types.
     */
    public static final byte KEYWORD3 = (byte) 8;

    /**
     * Operator token id. This can be used to mark an operator. (eg, SQL mode
     * marks +, -, etc with this token type).
     */
    public static final byte OPERATOR = (byte) 9;

    /**
     * Invalid token id. This can be used to mark invalid or incomplete tokens,
     * so the user can easily spot syntax errors.
     */
    public static final byte INVALID = (byte) 10;

    /**
     * The total number of defined token ids.
     */
    public static final byte ID_COUNT = (byte) 11;

    /**
     * The first id that can be used for internal state in a token marker.
     */
    public static final byte INTERNAL_FIRST = (byte) 100;

    /**
     * The last id that can be used for internal state in a token marker.
     */
    public static final byte INTERNAL_LAST = (byte) 126;

    /**
     * The length of this token.
     */
    private int length;

    /**
     * The id of this token.
     */
    private byte id;

    /**
     * Creates a new token.
     * @param length the length of the token
     * @param id the id of the token
     */
    public Token(final int length, final byte id) {
        this.length = length;
        this.id = id;
    }

    /**
     * Returns a string representation of this token.
     */
    @Override
    public String toString() {
        return "[id=" + id + ",length=" + length + "]";
    }

    /**
     * Returns the id of this token.
     * @return the id of this token
     */
    public byte getId() {
        return id;
    }

    /**
     * Sets the id of this token.
     * @param id the new id of this token
     */
    public void setId(final byte id) {
        this.id = id;
    }

    /**
     * Returns the length of this token.
     * @return the length of this token
     */
    public int getLength() {
        return length;
    }

    /**
     * Sets the length of this token.
     * @param length the new length of this token
     */
    public void setLength(final int length) {
        this.length = length;
    }

}
