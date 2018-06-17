/*
 * CTokenMarker.java - C token marker
 * Copyright (C) 1998, 1999 Slava Pestov
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

package net.sf.gridarta.textedit.textarea.tokenmarker;

import javax.swing.text.Segment;
import net.sf.gridarta.textedit.textarea.Token;

/**
 * C token marker.
 * @author Slava Pestov
 * @author Andreas Kirschbaum
 */
public class CTokenMarker extends TokenMarker {

    /**
     * Default keywords for C.
     */
    private static KeywordMap cKeywords;

    /**
     * Whether preprocessor tokens should be marked.
     */
    private final boolean cpp;

    /**
     * The keywords to mark.
     */
    private final KeywordMap keywords;

    private int lastOffset;

    private int lastKeyword;

    /**
     * Create a new instance using default keywords.
     */
    public CTokenMarker() {
        this(true, getCKeywords());
    }

    /**
     * Creates a new instance.
     * @param cpp whether preprocessor tokens should be marked
     * @param keywords the keywords to mark
     */
    protected CTokenMarker(final boolean cpp, final KeywordMap keywords) {
        this.cpp = cpp;
        this.keywords = keywords;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte markTokensImpl(final byte token, final Segment line, final int lineIndex) {
        final char[] array = line.array;
        final int offset = line.offset;
        lastOffset = offset;
        lastKeyword = offset;
        final int length = line.count + offset;
        boolean backslash = false;

        byte currentToken = token;
loop:
        for (int i = offset; i < length; i++) {
            final int i1 = i + 1;

            final char c = array[i];
            if (c == '\\') {
                backslash = !backslash;
                continue;
            }

            switch (currentToken) {
            case Token.NULL:
                switch (c) {
                case '#':
                    if (backslash) {
                        backslash = false;
                    } else if (cpp) {
                        doKeyword(line, i);
                        addToken(i - lastOffset, currentToken);
                        addToken(length - i, Token.KEYWORD2);
                        lastOffset = length;
                        lastKeyword = length;
                        break loop;
                    }
                    break;

                case '"':
                    doKeyword(line, i);
                    if (backslash) {
                        backslash = false;
                    } else {
                        addToken(i - lastOffset, currentToken);
                        currentToken = Token.LITERAL1;
                        lastOffset = i;
                        lastKeyword = i;
                    }
                    break;

                case '\'':
                    doKeyword(line, i);
                    if (backslash) {
                        backslash = false;
                    } else {
                        addToken(i - lastOffset, currentToken);
                        currentToken = Token.LITERAL2;
                        lastOffset = i;
                        lastKeyword = i;
                    }
                    break;

                case ':':
                    if (lastKeyword == offset) {
                        doKeyword(line, i);
                        backslash = false;
                        addToken(i1 - lastOffset, Token.LABEL);
                        lastOffset = i1;
                        lastKeyword = i1;
                    } else {
                        doKeyword(line, i);
                    }
                    break;

                case '/':
                    backslash = false;
                    doKeyword(line, i);
                    if (length - i > 1) {
                        switch (array[i1]) {
                        case '*':
                            addToken(i - lastOffset, currentToken);
                            lastOffset = i;
                            lastKeyword = i;
                            if (length - i > 2 && array[i + 2] == '*') {
                                currentToken = Token.COMMENT2;
                            } else {
                                currentToken = Token.COMMENT1;
                            }
                            break;

                        case '/':
                            addToken(i - lastOffset, currentToken);
                            addToken(length - i, Token.COMMENT1);
                            lastOffset = length;
                            lastKeyword = length;
                            break loop;
                        }
                    }
                    break;

                default:
                    backslash = false;
                    if (!Character.isLetterOrDigit(c) && c != '_') {
                        doKeyword(line, i);
                    }
                    break;
                }
                break;

            case Token.COMMENT1:
            case Token.COMMENT2:
                backslash = false;
                if (c == '*' && length - i > 1) {
                    if (array[i1] == '/') {
                        i++;
                        addToken(i + 1 - lastOffset, currentToken);
                        currentToken = Token.NULL;
                        lastOffset = i + 1;
                        lastKeyword = i + 1;
                    }
                }
                break;

            case Token.LITERAL1:
                if (backslash) {
                    backslash = false;
                } else if (c == '"') {
                    addToken(i1 - lastOffset, currentToken);
                    currentToken = Token.NULL;
                    lastOffset = i1;
                    lastKeyword = i1;
                }
                break;

            case Token.LITERAL2:
                if (backslash) {
                    backslash = false;
                } else if (c == '\'') {
                    addToken(i1 - lastOffset, Token.LITERAL1);
                    currentToken = Token.NULL;
                    lastOffset = i1;
                    lastKeyword = i1;
                }
                break;

            default:
                throw new InternalError("Invalid state: " + currentToken);
            }
        }

        if (currentToken == Token.NULL) {
            doKeyword(line, length);
        }

        switch (currentToken) {
        case Token.LITERAL1:
        case Token.LITERAL2:
            addToken(length - lastOffset, Token.INVALID);
            currentToken = Token.NULL;
            break;

        case Token.KEYWORD2:
            addToken(length - lastOffset, currentToken);
            if (!backslash) {
                currentToken = Token.NULL;
            }

            // @devs what is with this fallthrough? Intention or Accident?
        default:
            addToken(length - lastOffset, currentToken);
            break;
        }

        return currentToken;
    }

    /**
     * Returns the default keywords for C.
     * @return the keywords
     */
    private static KeywordMap getCKeywords() {
        if (cKeywords == null) {
            cKeywords = new KeywordMap(false);
            cKeywords.add("char", Token.KEYWORD3);
            cKeywords.add("double", Token.KEYWORD3);
            cKeywords.add("enum", Token.KEYWORD3);
            cKeywords.add("float", Token.KEYWORD3);
            cKeywords.add("int", Token.KEYWORD3);
            cKeywords.add("long", Token.KEYWORD3);
            cKeywords.add("short", Token.KEYWORD3);
            cKeywords.add("signed", Token.KEYWORD3);
            cKeywords.add("struct", Token.KEYWORD3);
            cKeywords.add("typedef", Token.KEYWORD3);
            cKeywords.add("union", Token.KEYWORD3);
            cKeywords.add("unsigned", Token.KEYWORD3);
            cKeywords.add("void", Token.KEYWORD3);
            cKeywords.add("_Bool", Token.KEYWORD3);
            cKeywords.add("_Complex", Token.KEYWORD3);
            cKeywords.add("_Imaginary", Token.KEYWORD3);
            cKeywords.add("auto", Token.KEYWORD1);
            cKeywords.add("const", Token.KEYWORD1);
            cKeywords.add("extern", Token.KEYWORD1);
            cKeywords.add("register", Token.KEYWORD1);
            cKeywords.add("restrict", Token.KEYWORD1);
            cKeywords.add("static", Token.KEYWORD1);
            cKeywords.add("volatile", Token.KEYWORD1);
            cKeywords.add("break", Token.KEYWORD1);
            cKeywords.add("case", Token.KEYWORD1);
            cKeywords.add("continue", Token.KEYWORD1);
            cKeywords.add("default", Token.KEYWORD1);
            cKeywords.add("do", Token.KEYWORD1);
            cKeywords.add("else", Token.KEYWORD1);
            cKeywords.add("for", Token.KEYWORD1);
            cKeywords.add("goto", Token.KEYWORD1);
            cKeywords.add("if", Token.KEYWORD1);
            cKeywords.add("return", Token.KEYWORD1);
            cKeywords.add("sizeof", Token.KEYWORD1);
            cKeywords.add("switch", Token.KEYWORD1);
            cKeywords.add("while", Token.KEYWORD1);
            cKeywords.add("asm", Token.KEYWORD2);
            cKeywords.add("asmlinkage", Token.KEYWORD2);
            cKeywords.add("far", Token.KEYWORD2);
            cKeywords.add("huge", Token.KEYWORD2);
            cKeywords.add("inline", Token.KEYWORD2);
            cKeywords.add("near", Token.KEYWORD2);
            cKeywords.add("pascal", Token.KEYWORD2);
            cKeywords.add("true", Token.LITERAL2);
            cKeywords.add("false", Token.LITERAL2);
            cKeywords.add("NULL", Token.LITERAL2);
        }
        return cKeywords;
    }

    private void doKeyword(final Segment line, final int i) {
        final int i1 = i + 1;

        final int len = i - lastKeyword;
        final byte id = keywords.lookup(line, lastKeyword, len);
        if (id != Token.NULL) {
            if (lastKeyword != lastOffset) {
                addToken(lastKeyword - lastOffset, Token.NULL);
            }
            addToken(len, id);
            lastOffset = i;
        }
        lastKeyword = i1;
    }

}
