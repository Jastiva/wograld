/*
 * TextUtilities.java - Utility functions used by the text area classes
 * Copyright (C) 1999 Slava Pestov
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

package net.sf.gridarta.textedit.textarea;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Class with several utility functions used by the text area component.
 * @author Slava Pestov
 */
public class TextUtilities {

    /**
     * Prevent instantiation.
     */
    private TextUtilities() {
    }

    /**
     * Returns the offset of the bracket matching the one at the specified
     * offset of the document, or -1 if the bracket is unmatched (or if the
     * character is not a bracket).
     * @param doc the document
     * @param offset the offset
     * @return the offset or <code>-1</code>
     * @throws BadLocationException if an out-of-bounds access was attempted on
     * the document text
     */
    public static int findMatchingBracket(final Document doc, final int offset) throws BadLocationException {
        if (doc.getLength() == 0) {
            return -1;
        }
        final char c = doc.getText(offset, 1).charAt(0);
        final char cPrime; // c` - corresponding character
        final boolean direction; // true = back, false = forward

        switch (c) {
        case '(':
            cPrime = ')';
            direction = false;
            break;
        case ')':
            cPrime = '(';
            direction = true;
            break;
        case '[':
            cPrime = ']';
            direction = false;
            break;
        case ']':
            cPrime = '[';
            direction = true;
            break;
        case '{':
            cPrime = '}';
            direction = false;
            break;
        case '}':
            cPrime = '{';
            direction = true;
            break;
        default:
            return -1;
        }

        // How to merge these two cases is left as an exercise
        // for the reader.

        // Go back or forward
        if (direction) {
            // Count is 1 initially because we have already
            // `found' one closing bracket
            int count = 1;

            // Get text[0,offset-1];
            final CharSequence text = doc.getText(0, offset);

            // Scan backwards
            for (int i = offset - 1; i >= 0; i--) {
                // If text[i] == c, we have found another
                // closing bracket, therefore we will need
                // two opening brackets to complete the
                // match.
                final char x = text.charAt(i);
                if (x == c) {
                    count++;
                } else if (x == cPrime) {
                    // If text[i] == cPrime, we have found a
                    // opening bracket, so we return i if
                    // --count == 0
                    if (--count == 0) {
                        return i;
                    }
                }
            }
        } else {
            // Number of characters to check
            final int len = doc.getLength() - (offset + 1);

            // Count is 1 initially because we have already
            // `found' one opening bracket
            int count = 1;

            // Get text[offset+1,len];
            final CharSequence text = doc.getText(offset + 1, len);

            // Scan forwards
            for (int i = 0; i < len; i++) {
                // If text[i] == c, we have found another
                // opening bracket, therefore we will need
                // two closing brackets to complete the
                // match.
                final char x = text.charAt(i);

                if (x == c) {
                    count++;
                } else if (x == cPrime) {
                    // If text[i] == cPrime, we have found an
                    // closing bracket, so we return i if
                    // --count == 0
                    if (--count == 0) {
                        return i + offset + 1;
                    }
                }
            }
        }

        // Nothing found
        return -1;
    }

    /**
     * Locates the start of the word at the specified position.
     * @param line the text
     * @param pos the position
     * @param noWordSep characters that are not part of a word; may be
     * <code>null</code>
     * @return the start position of the word
     */
    public static int findWordStart(final CharSequence line, final int pos, final String noWordSep) {
        char ch = line.charAt(pos - 1);

        final boolean selectNoLetter = selectNoLetter(ch, noWordSep);

        int wordStart = 0;
        for (int i = pos - 1; i >= 0; i--) {
            ch = line.charAt(i);
            if (selectNoLetter ^ selectNoLetter(ch, noWordSep)) {
                wordStart = i + 1;
                break;
            }
        }

        return wordStart;
    }

    /**
     * Locates the end of the word at the specified position.
     * @param line the text
     * @param pos the position
     * @param noWordSep characters that are not part of a word; may be
     * <code>null</code>
     * @return the end position of the word
     */
    public static int findWordEnd(final CharSequence line, final int pos, final String noWordSep) {
        char ch = line.charAt(pos);

        final boolean selectNoLetter = selectNoLetter(ch, noWordSep);

        int wordEnd = line.length();
        for (int i = pos; i < line.length(); i++) {
            ch = line.charAt(i);
            if (selectNoLetter ^ selectNoLetter(ch, noWordSep)) {
                wordEnd = i;
                break;
            }
        }
        return wordEnd;
    }

    /**
     * Returns whether a character is not part of a word.
     * @param ch the character
     * @param noWordSep characters that are not part of a word; may be
     * <code>null</code>
     * @return whether the character is not part of a word
     */
    private static boolean selectNoLetter(final char ch, final String noWordSep) {
        return !Character.isLetterOrDigit(ch) && (noWordSep == null || noWordSep.indexOf(ch) == -1);
    }

}
