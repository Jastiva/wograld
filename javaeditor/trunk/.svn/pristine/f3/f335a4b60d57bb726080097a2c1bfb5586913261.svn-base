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

package net.sf.gridarta.gui.dialog.plugin;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * the idea for this class stems from limewire's CancelSearchIconProxy class,
 * thanks for going open source guys.
 */
public class ClosingIcon implements Icon {

    //--- Data field(s) ---

    @Nullable
    private final Icon icon;

    private int x;

    private int y;

    private int height = 10;

    private int width = 10;

    //--- Constructor(s) ---

    public ClosingIcon(@Nullable final Icon icon) {
        this.icon = icon;

        if (icon != null) {
            height = icon.getIconHeight();
            width = icon.getIconWidth();
        }
    }

    //--- Method(s) ---

    @Override
    public int getIconHeight() {
        return height;
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    /**
     * Overwrites paintIcon to get hold of the coordinates of the icon, this is
     * a rather rude approach just to find out if the closingIcon was pressed.
     */
    @Override
    public void paintIcon(@NotNull final Component c, @NotNull final Graphics g, final int x, final int y) {
        this.x = x;
        this.y = y;

        if (icon != null) {
            icon.paintIcon(c, g, x, y + 1);
        } else {
            g.drawRect(x, y + 1, width, height);
        }
    }

    /**
     * Returns whether <var>xEvent</var> and <var>yEvent</var> are within the
     * icon's borders.
     * @param xEvent the x coordinate from an event
     * @param yEvent the y coordinate from an event
     * @return whether <var>xEvent</var> and <var>yEvent</var> are within the
     *         icon's borders
     * @retval <code>true</code> if <var>xEvent</var> and <var>yEvent</var> are
     * within the icon's border.
     * @retval <code>false</code> otherwise (<var>xEvent</var> or
     * <var>yEvent</var> or both are outside the icon's border).
     */
    public boolean contains(final int xEvent, final int yEvent) {
        return x <= xEvent && xEvent <= x + width && y <= yEvent && yEvent <= y + height;
    }

}
