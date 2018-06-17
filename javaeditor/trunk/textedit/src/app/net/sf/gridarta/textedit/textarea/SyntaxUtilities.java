/*
 * SyntaxUtilities.java - Utility functions used by syntax colorizing
 * Copyright (C) 1999 Slava Pestov
 * Copyright (C) 2000-2010 The Gridarta Developers.
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

package net.sf.gridarta.textedit.textarea;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.text.Segment;
import javax.swing.text.TabExpander;
import javax.swing.text.Utilities;

/**
 * Class with several utility functions used by jEdit's syntax colorizing
 * subsystem.
 * @author Slava Pestov
 * @author Andreas Kirschbaum
 */
public class SyntaxUtilities {

    /**
     * Private constructor to prevent instantiation.
     */
    private SyntaxUtilities() {
    }

    /**
     * Checks if a sub-region of a <code>Segment</code> is equal to a string.
     * @param ignoreCase true if case should be ignored, false otherwise
     * @param text the segment
     * @param offset the offset into the segment
     * @param match the string to match
     * @return whether the sub-region matches
     */
    public static boolean regionMatches(final boolean ignoreCase, final Segment text, final int offset, final CharSequence match) {
        final int length = offset + match.length();
        final char[] textArray = text.array;
        if (length > text.offset + text.count) {
            return false;
        }

        for (int i = offset, j = 0; i < length; i++, j++) {
            char c1 = textArray[i];
            char c2 = match.charAt(j);
            if (ignoreCase) {
                c1 = Character.toUpperCase(c1);
                c2 = Character.toUpperCase(c2);
            }
            if (c1 != c2) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the default styles. This can be passed to {@link
     * TextAreaPainter#setStyles(SyntaxStyles)} to use the default syntax
     * styles.
     * @return the default styles
     */
    public static SyntaxStyles getDefaultSyntaxStyles() {
        final SyntaxStyle[] styles = new SyntaxStyle[Token.ID_COUNT];

        styles[(int) Token.COMMENT1] = new SyntaxStyle(new Color(0x008000), true, false); // green comments
        styles[(int) Token.COMMENT2] = new SyntaxStyle(new Color(0x990033), true, false);
        styles[(int) Token.KEYWORD1] = new SyntaxStyle(Color.blue, false, true);
        styles[(int) Token.KEYWORD2] = new SyntaxStyle(Color.magenta, false, false);
        styles[(int) Token.KEYWORD3] = new SyntaxStyle(new Color(0x009600), false, false);
        styles[(int) Token.LITERAL1] = new SyntaxStyle(new Color(0x650099), false, false);
        styles[(int) Token.LITERAL2] = new SyntaxStyle(new Color(0x650099), false, true);
        styles[(int) Token.LABEL] = new SyntaxStyle(new Color(0x990033), false, true);
        styles[(int) Token.OPERATOR] = new SyntaxStyle(Color.black, false, true);
        styles[(int) Token.INVALID] = new SyntaxStyle(Color.red, false, true);

        return new SyntaxStyles(styles);
    }

    /**
     * Paints the specified line onto the graphics context. Note that this
     * method modifies the offset and count values of the segment.
     * @param line the line segment
     * @param tokens the token list for the line
     * @param styles the syntax styles
     * @param expander the tab expander used to determine tab stops. May be
     * null
     * @param gfx the graphics context
     * @param x the x co-ordinate
     * @param y the y co-ordinate
     * @return the x co-ordinate, plus the width of the painted string
     */
    public static int paintSyntaxLine(final Segment line, final Iterable<Token> tokens, final SyntaxStyles styles, final TabExpander expander, final Graphics gfx, final int x, final int y) {
        final Font defaultFont = gfx.getFont();
        final Color defaultColor = gfx.getColor();

        int xPos = x;
        for (final Token token : tokens) {
            final byte id = token.getId();

            final int length = token.getLength();
            if (id == Token.NULL) {
                if (!defaultColor.equals(gfx.getColor())) {
                    gfx.setColor(defaultColor);
                }
                if (!defaultFont.equals(gfx.getFont())) {
                    gfx.setFont(defaultFont);
                }
            } else {
                styles.getStyle(id).setGraphicsFlags(gfx, defaultFont);
            }

            line.count = length;
            xPos = Utilities.drawTabbedText(line, xPos, y, gfx, expander, 0);
            line.offset += length;
        }

        return xPos;
    }

}
