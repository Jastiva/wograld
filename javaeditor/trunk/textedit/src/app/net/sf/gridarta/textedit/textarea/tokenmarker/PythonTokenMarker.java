/*
 * PythonTokenMarker.java - Python token marker
 * Copyright (C) 1999 Jonathan Revusky
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
 * Python token marker.
 * @author Jonathan Revusky
 */
public class PythonTokenMarker extends TokenMarker {

    private static final byte TRIPLE_QUOTE1 = Token.INTERNAL_FIRST;

    private static final byte TRIPLE_QUOTE2 = Token.INTERNAL_LAST;

    private static KeywordMap pythonKeywords;

    private final KeywordMap keywords;

    private int lastOffset;

    private int lastKeyword;

    public PythonTokenMarker() {
        keywords = getPythonKeywords();
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
            case Token.NULL:
                switch (c) {
                case '#':
                    if (backslash) {
                        backslash = false;
                    } else {
                        doKeyword(line, i);
                        addToken(i - lastOffset, currentToken);
                        addToken(length - i, Token.COMMENT1);
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
                        if (SyntaxUtilities.regionMatches(false, line, i1, "\"\"")) {
                            currentToken = TRIPLE_QUOTE1;
                        } else {
                            currentToken = Token.LITERAL1;
                        }
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
                        if (SyntaxUtilities.regionMatches(false, line, i1, "''")) {
                            currentToken = TRIPLE_QUOTE2;
                        } else {
                            currentToken = Token.LITERAL2;
                        }
                        lastOffset = i;
                        lastKeyword = i;
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

            case TRIPLE_QUOTE1:
                if (backslash) {
                    backslash = false;
                } else if (SyntaxUtilities.regionMatches(false, line, i, "\"\"\"")) {
                    i += 4;
                    addToken(i - lastOffset, Token.LITERAL1);
                    currentToken = Token.NULL;
                    lastOffset = i;
                    lastKeyword = i;
                }
                break;

            case TRIPLE_QUOTE2:
                if (backslash) {
                    backslash = false;
                } else if (SyntaxUtilities.regionMatches(false, line, i, "'''")) {
                    i += 4;
                    addToken(i - lastOffset, Token.LITERAL1);
                    currentToken = Token.NULL;
                    lastOffset = i;
                    lastKeyword = i;
                }
                break;

            default:
                throw new InternalError("Invalid state: " + currentToken);
            }
        }

        switch (currentToken) {
        case TRIPLE_QUOTE1:
        case TRIPLE_QUOTE2:
            addToken(length - lastOffset, Token.LITERAL1);
            break;

        case Token.NULL:
            doKeyword(line, length);

        default:
            addToken(length - lastOffset, currentToken);
            break;
        }

        return currentToken;
    }

    private static KeywordMap getPythonKeywords() {
        if (pythonKeywords == null) {
            pythonKeywords = new KeywordMap(false);
            pythonKeywords.add("and", Token.KEYWORD3);
            pythonKeywords.add("not", Token.KEYWORD3);
            pythonKeywords.add("or", Token.KEYWORD3);
            pythonKeywords.add("if", Token.KEYWORD1);
            pythonKeywords.add("for", Token.KEYWORD1);
            pythonKeywords.add("assert", Token.KEYWORD1);
            pythonKeywords.add("break", Token.KEYWORD1);
            pythonKeywords.add("continue", Token.KEYWORD1);
            pythonKeywords.add("elif", Token.KEYWORD1);
            pythonKeywords.add("else", Token.KEYWORD1);
            pythonKeywords.add("except", Token.KEYWORD1);
            pythonKeywords.add("exec", Token.KEYWORD1);
            pythonKeywords.add("finally", Token.KEYWORD1);
            pythonKeywords.add("raise", Token.KEYWORD1);
            pythonKeywords.add("return", Token.KEYWORD1);
            pythonKeywords.add("try", Token.KEYWORD1);
            pythonKeywords.add("while", Token.KEYWORD1);
            pythonKeywords.add("yield", Token.KEYWORD1);
            pythonKeywords.add("as", Token.KEYWORD2);
            pythonKeywords.add("def", Token.KEYWORD2);
            pythonKeywords.add("class", Token.KEYWORD2);
            pythonKeywords.add("del", Token.KEYWORD2);
            pythonKeywords.add("from", Token.KEYWORD2);
            pythonKeywords.add("global", Token.KEYWORD2);
            pythonKeywords.add("import", Token.KEYWORD2);
            pythonKeywords.add("in", Token.KEYWORD2);
            pythonKeywords.add("is", Token.KEYWORD2);
            pythonKeywords.add("lambda", Token.KEYWORD2);
            pythonKeywords.add("pass", Token.KEYWORD2);
            pythonKeywords.add("print", Token.KEYWORD2);
            pythonKeywords.add("with", Token.KEYWORD2);
        }
        return pythonKeywords;
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
