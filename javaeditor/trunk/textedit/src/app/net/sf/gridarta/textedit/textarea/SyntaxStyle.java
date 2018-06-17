/*
 * SyntaxStyle.java - A simple text style class
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
import java.awt.FontMetrics;
import java.awt.Graphics;
import org.jetbrains.annotations.Nullable;

/**
 * A simple text style class. It can specify the color, italic flag, and bold
 * flag of a run of text.
 * @author Slava Pestov
 * @author Andreas Kirschbaum
 */
public class SyntaxStyle {

    /**
     * The text color.
     */
    private final Color color;

    /**
     * Whether the text should be italics.
     */
    private final boolean italic;

    /**
     * Whether the text should be bold.
     */
    private final boolean bold;

    /**
     * The base font used to calculate {@link #lastStyledFont}. May be
     * <code>null</code> if unset.
     */
    private Font lastFont;

    /**
     * The font {@link #lastFont} with applied {@link #italic italics} and
     * {@link #bold} settings. May be <code>null</code> if unset.
     */
    private Font lastStyledFont;

    /**
     * The font metrics for {@link #lastStyledFont}. May be <code>null</code> if
     * not yet known.
     */
    @Nullable
    private FontMetrics fontMetrics;

    /**
     * Creates a new instance.
     * @param color the text color
     * @param italic whether the text should be italics
     * @param bold whether the text should be bold
     */
    public SyntaxStyle(final Color color, final boolean italic, final boolean bold) {
        this.color = color;
        this.italic = italic;
        this.bold = bold;
    }

    /**
     * Returns the text color.
     * @return the text color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns <code>true</code> if no font styles are enabled.
     * @return whether no font styles are enabled
     */
    public boolean isPlain() {
        return !bold && !italic;
    }

    /**
     * Returns <code>true</code> if italics is enabled.
     * @return the italics flag
     */
    public boolean isItalic() {
        return italic;
    }

    /**
     * Returns <code>true</code> if boldface is enabled.
     * @return the bold flag
     */
    public boolean isBold() {
        return bold;
    }

    /**
     * Returns the specified font, but with the style's bold and italic flags
     * applied.
     * @param font the specified font
     * @return the <code>font</code> with applied flags
     */
    private Font getStyledFont(final Font font) {
        if (font == null) {
            throw new IllegalArgumentException("font param must not be null");
        }

        updateStyledFont(font);
        return lastStyledFont;
    }

    /**
     * Returns the font metrics for the styled font.
     * @param font the styled font
     * @param g the graphics to use
     * @return the font metrics
     */
    public FontMetrics getFontMetrics(final Font font, final Graphics g) {
        if (font == null) {
            throw new IllegalArgumentException("font param must not be null");
        }

        updateStyledFont(font);
        if (fontMetrics == null) {
            fontMetrics = g.getFontMetrics(lastStyledFont);
        }
        assert fontMetrics != null;
        return fontMetrics;
    }

    /**
     * Sets the text color and font of the specified graphics context to that
     * specified in this style.
     * @param gfx the graphics context that should be modified
     * @param font the font to add the styles to
     */
    public void setGraphicsFlags(final Graphics gfx, final Font font) {
        gfx.setFont(getStyledFont(font));
        gfx.setColor(color);
    }

    /**
     * Update {@link #lastFont} and {@link #lastStyledFont} for the given font.
     * Resets {@link #fontMetrics} to <code>null</code> if the font has
     * changed.
     * @param font the font to use
     */
    private void updateStyledFont(final Font font) {
        if (font.equals(lastFont)) {
            return;
        }

        lastStyledFont = new Font(font.getFamily(), (bold ? Font.BOLD : 0) | (italic ? Font.ITALIC : 0), font.getSize());
        lastFont = font;
        fontMetrics = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getName() + "[color=" + color + (italic ? ",italic" : "") + (bold ? ",bold" : "") + "]";
    }

}
