/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2011 The Gridarta Developers.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.sf.gridarta.gui.utils.borderpanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link JToggleButton} that displays vertical text.
 * @author Andreas Kirschbaum
 */
public class VerticalToggleButton extends JToggleButton {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The transparent {@link Color} for filling the button's background.
     */
    @NotNull
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    /**
     * Creates a new instance.
     * @param title the button's title
     * @param right whether the text should be rotated right (<code>true</code>)
     * or left (<code>false</code>)
     */
    public VerticalToggleButton(@NotNull final String title, final boolean right) {
        final Font font = getFont();
        final FontMetrics fontMetrics = getFontMetrics(font);
        final int textWidth = fontMetrics.getHeight();
        final int textHeight = fontMetrics.stringWidth(title);
        final BufferedImage bufferedImage = new BufferedImage(textWidth, textHeight, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = bufferedImage.createGraphics();
        try {
            g.setColor(TRANSPARENT);
            g.fillRect(0, 0, textWidth, textHeight);

            g.setColor(getForeground());
            g.setFont(font);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (right) {
                g.rotate(Math.PI / 2.0);
                g.translate(0, 1 - textWidth);
            } else {
                g.rotate(-Math.PI / 2.0);
                g.translate(1 - textHeight, 0);
            }
            g.drawString(title, 0, fontMetrics.getAscent());
        } finally {
            g.dispose();
        }

        setIcon(new ImageIcon(bufferedImage));
    }

}
