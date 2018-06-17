/*
 * LuaTokenMarker.java - Lua token marker
 *
 * Based on:
 *
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
 * Lua token marker.
 */
public class LuaTokenMarker extends TokenMarker {

    private static final byte SINGLE_QUOTE = Token.INTERNAL_FIRST;

    private static final byte DOUBLE_QUOTE = (byte) ((int) Token.INTERNAL_FIRST + 1);

    private static final byte MULTI_COMMENT = (byte) ((int) Token.INTERNAL_FIRST + 2);

    private static final byte MULTI_LITERAL = Token.INTERNAL_LAST;

    private static KeywordMap luaKeywords;

    private final KeywordMap keywords;

    private int lastOffset;

    private int lastKeyword;

    public LuaTokenMarker() {
        keywords = getLuaKeywords();
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
                if (backslash) {
                    backslash = false;
                }
                switch (c) {
                case '-':
                    if (i < length && array[i1] == '-') {
                        doKeyword(line, i);
                        addToken(i - lastOffset, currentToken);
                        if (i < length - 2 && SyntaxUtilities.regionMatches(false, line, i + 2, "[[")) {
                            currentToken = MULTI_COMMENT;
                            lastOffset = i;
                            lastKeyword = i;
                            i += 3;
                        } else {
                            addToken(length - i, Token.COMMENT1);
                            lastOffset = length;
                            lastKeyword = length;
                            break loop;
                        }
                    }
                    break;
                case '[':
                    if (i < length && array[i1] == '[') {
                        doKeyword(line, i);
                        addToken(i - lastOffset, currentToken);
                        currentToken = MULTI_LITERAL;
                        lastOffset = i;
                        lastKeyword = i;
                        i++;
                    }
                    break;
                case '"':
                    doKeyword(line, i);
                    addToken(i - lastOffset, currentToken);
                    currentToken = DOUBLE_QUOTE;
                    lastOffset = i;
                    lastKeyword = i;
                    break;
                case '\'':
                    doKeyword(line, i);
                    addToken(i - lastOffset, currentToken);
                    currentToken = SINGLE_QUOTE;
                    lastOffset = i;
                    lastKeyword = i;
                    break;
                default:
                    if (!Character.isLetterOrDigit(c) && c != '_') {
                        doKeyword(line, i);
                    }
                    break;
                }
                break;
            case SINGLE_QUOTE:
                if (backslash) {
                    backslash = false;
                } else if (c == '\'') {
                    addToken(i1 - lastOffset, Token.LITERAL1);
                    currentToken = Token.NULL;
                    lastOffset = i1;
                    lastKeyword = i1;
                }
                break;
            case DOUBLE_QUOTE:
                if (backslash) {
                    backslash = false;
                } else if (c == '"') {
                    addToken(i1 - lastOffset, Token.LITERAL1);
                    currentToken = Token.NULL;
                    lastOffset = i1;
                    lastKeyword = i1;
                }
                break;
            case MULTI_COMMENT:
                if (backslash) {
                    backslash = false;
                }
                if (SyntaxUtilities.regionMatches(false, line, i, "]]")) {
                    i += 2;
                    addToken(i - lastOffset, Token.COMMENT1);
                    currentToken = Token.NULL;
                    lastOffset = i;
                    lastKeyword = i;
                }
                break;
            case MULTI_LITERAL:
                if (backslash) {
                    backslash = false;
                }
                if (SyntaxUtilities.regionMatches(false, line, i, "]]")) {
                    i += 2;
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
        case SINGLE_QUOTE:
        case DOUBLE_QUOTE:
        case MULTI_LITERAL:
            addToken(length - lastOffset, Token.LITERAL1);
            break;
        case MULTI_COMMENT:
            addToken(length - lastOffset, Token.COMMENT1);
            break;
        case Token.NULL:
            doKeyword(line, length);
        default:
            addToken(length - lastOffset, currentToken);
            break;
        }

        return currentToken;
    }

    private static KeywordMap getLuaKeywords() {
        if (luaKeywords == null) {
            luaKeywords = new KeywordMap(false);
            luaKeywords.add("break", Token.KEYWORD1);
            luaKeywords.add("do", Token.KEYWORD1);
            luaKeywords.add("else", Token.KEYWORD1);
            luaKeywords.add("elseif", Token.KEYWORD1);
            luaKeywords.add("end", Token.KEYWORD1);
            luaKeywords.add("for", Token.KEYWORD1);
            luaKeywords.add("function", Token.KEYWORD1);
            luaKeywords.add("if", Token.KEYWORD1);
            luaKeywords.add("in", Token.KEYWORD1);
            luaKeywords.add("local", Token.KEYWORD1);
            luaKeywords.add("repeat", Token.KEYWORD1);
            luaKeywords.add("require", Token.KEYWORD1);
            luaKeywords.add("return", Token.KEYWORD1);
            luaKeywords.add("then", Token.KEYWORD1);
            luaKeywords.add("until", Token.KEYWORD1);
            luaKeywords.add("while", Token.KEYWORD1);
            luaKeywords.add("false", Token.LITERAL2);
            luaKeywords.add("nil", Token.LITERAL2);
            luaKeywords.add("true", Token.LITERAL2);
            luaKeywords.add("and", Token.OPERATOR);
            luaKeywords.add("not", Token.OPERATOR);
            luaKeywords.add("or", Token.OPERATOR);
        }
        return luaKeywords;
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
