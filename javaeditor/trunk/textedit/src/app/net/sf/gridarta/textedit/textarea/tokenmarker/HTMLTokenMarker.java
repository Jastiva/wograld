/*
 * HTMLTokenMarker.java - HTML token marker
 * Copyright (C) 1998, 1999 Slava Pestov
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

package net.sf.gridarta.textedit.textarea.tokenmarker;

import javax.swing.text.Segment;
import net.sf.gridarta.textedit.textarea.SyntaxUtilities;
import net.sf.gridarta.textedit.textarea.Token;

/**
 * HTML token marker.
 * @author Slava Pestov
 */
public class HTMLTokenMarker extends TokenMarker {

    private static final byte JAVASCRIPT = Token.INTERNAL_FIRST;

    private final KeywordMap keywords;

    private final boolean js;

    private int lastOffset;

    private int lastKeyword;

    public HTMLTokenMarker() {
        this(true);
    }

    public HTMLTokenMarker(final boolean js) {
        this.js = js;
        keywords = JavaScriptTokenMarker.getJavaScriptKeywords();
    }

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
            case Token.NULL: // HTML text
                backslash = false;
                switch (c) {
                case '<':
                    addToken(i - lastOffset, currentToken);
                    lastOffset = i;
                    lastKeyword = i;
                    if (SyntaxUtilities.regionMatches(false, line, i1, "!--")) {
                        i += 3;
                        currentToken = Token.COMMENT1;
                    } else if (js && SyntaxUtilities.regionMatches(true, line, i1, "script>")) {
                        addToken(8, Token.KEYWORD1);
                        i += 8;
                        lastOffset = i;
                        lastKeyword = i;
                        currentToken = JAVASCRIPT;
                    } else {
                        currentToken = Token.KEYWORD1;
                    }
                    break;

                case '&':
                    addToken(i - lastOffset, currentToken);
                    lastOffset = i;
                    lastKeyword = i;
                    currentToken = Token.KEYWORD2;
                    break;
                }
                break;

            case Token.KEYWORD1: // Inside a tag
                backslash = false;
                if (c == '>') {
                    addToken(i1 - lastOffset, currentToken);
                    lastOffset = i1;
                    lastKeyword = i1;
                    currentToken = Token.NULL;
                }
                break;

            case Token.KEYWORD2: // Inside an entity
                backslash = false;
                if (c == ';') {
                    addToken(i1 - lastOffset, currentToken);
                    lastOffset = i1;
                    lastKeyword = i1;
                    currentToken = Token.NULL;
                    break;
                }
                break;

            case Token.COMMENT1: // Inside a comment
                backslash = false;
                if (SyntaxUtilities.regionMatches(false, line, i, "-->")) {
                    addToken(i + 3 - lastOffset, currentToken);
                    lastOffset = i + 3;
                    lastKeyword = i + 3;
                    currentToken = Token.NULL;
                }
                break;

            case JAVASCRIPT: // Inside a JavaScript
                switch (c) {
                case '<':
                    backslash = false;
                    doKeyword(line, i);
                    if (SyntaxUtilities.regionMatches(true, line, i1, "/script>")) {
                        addToken(i - lastOffset, Token.NULL);
                        addToken(9, Token.KEYWORD1);
                        i += 9;
                        lastOffset = i;
                        lastKeyword = i;
                        currentToken = Token.NULL;
                    }
                    break;

                case '"':
                    if (backslash) {
                        backslash = false;
                    } else {
                        doKeyword(line, i);
                        addToken(i - lastOffset, Token.NULL);
                        lastOffset = i;
                        lastKeyword = i;
                        currentToken = Token.LITERAL1;
                    }
                    break;

                case '\'':
                    if (backslash) {
                        backslash = false;
                    } else {
                        doKeyword(line, i);
                        addToken(i - lastOffset, Token.NULL);
                        lastOffset = i;
                        lastKeyword = i;
                        currentToken = Token.LITERAL2;
                    }
                    break;

                case '/':
                    backslash = false;
                    doKeyword(line, i);
                    if (length - i > 1) {
                        addToken(i - lastOffset, Token.NULL);
                        lastOffset = i;
                        lastKeyword = i;
                        if (array[i1] == '/') {
                            addToken(length - i, Token.COMMENT2);
                            lastOffset = length;
                            lastKeyword = length;
                            break loop;
                        } else if (array[i1] == '*') {
                            currentToken = Token.COMMENT2;
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

            case Token.LITERAL1: // JavaScript "..."
                if (backslash) {
                    backslash = false;
                } else if (c == '"') {
                    addToken(i1 - lastOffset, Token.LITERAL1);
                    lastOffset = i1;
                    lastKeyword = i1;
                    currentToken = JAVASCRIPT;
                }
                break;

            case Token.LITERAL2: // JavaScript '...'
                if (backslash) {
                    backslash = false;
                } else if (c == '\'') {
                    addToken(i1 - lastOffset, Token.LITERAL1);
                    lastOffset = i1;
                    lastKeyword = i1;
                    currentToken = JAVASCRIPT;
                }
                break;

            case Token.COMMENT2: // Inside a JavaScript comment
                backslash = false;
                if (c == '*' && length - i > 1 && array[i1] == '/') {
                    i += 2;
                    addToken(i - lastOffset, Token.COMMENT2);
                    lastOffset = i;
                    lastKeyword = i;
                    currentToken = JAVASCRIPT;
                }
                break;

            default:
                throw new InternalError("Invalid state: " + currentToken);
            }
        }

        switch (currentToken) {
        case Token.LITERAL1:
        case Token.LITERAL2:
            addToken(length - lastOffset, Token.INVALID);
            currentToken = JAVASCRIPT;
            break;

        case Token.KEYWORD2:
            addToken(length - lastOffset, Token.INVALID);
            currentToken = Token.NULL;
            break;

        case JAVASCRIPT:
            doKeyword(line, length);
            addToken(length - lastOffset, Token.NULL);
            break;

        default:
            addToken(length - lastOffset, currentToken);
            break;
        }

        return currentToken;
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
