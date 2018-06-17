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

package net.sf.gridarta.gui.mapmenu;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.Autoscroll;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.tree.TreeModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link JTree} that supports auto-scrolling while drag and drop is active.
 * @author Andreas Kirschbaum
 */
public class AutoscrollJTree extends JTree implements Autoscroll {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The active border size.
     */
    private static final int BORDER_SIZE = 12;

    /**
     * Creates a new instance.
     * @param treeModel the tree's model
     */
    public AutoscrollJTree(@NotNull final TreeModel treeModel) {
        super(treeModel);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Insets getAutoscrollInsets() {
        final Insets insets = new Insets(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE);
        final JViewport viewport = getViewport();
        if (viewport != null) {
            final Dimension size = getSize();
            final Rectangle viewRectangle = viewport.getViewRect();
            insets.top += viewRectangle.y;
            insets.left += viewRectangle.x;
            insets.bottom += size.height - (viewRectangle.y + viewRectangle.height);
            insets.right += size.width - (viewRectangle.x + viewRectangle.width);
        }
        return insets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void autoscroll(@NotNull final Point cursorLocn) {
        final JViewport viewport = getViewport();
        if (viewport == null) {
            return;
        }

        final Insets insets = getAutoscrollInsets();
        final Rectangle bounds = getBounds();
        final Rectangle viewRectangle = viewport.getViewRect();
        if (cursorLocn.x < insets.left) {
            viewRectangle.x -= getScrollableUnitIncrement(viewRectangle, SwingConstants.HORIZONTAL, -1);
        } else if (cursorLocn.x > bounds.width - insets.right) {
            viewRectangle.x += getScrollableUnitIncrement(viewRectangle, SwingConstants.HORIZONTAL, 1);
        }
        if (cursorLocn.y < insets.top) {
            viewRectangle.y -= getScrollableUnitIncrement(viewRectangle, SwingConstants.VERTICAL, -1);
        } else if (cursorLocn.y > bounds.height - insets.bottom) {
            viewRectangle.y += getScrollableUnitIncrement(viewRectangle, SwingConstants.VERTICAL, 1);
        }
        scrollRectToVisible(viewRectangle);
    }

    /**
     * Returns the parent {@link JViewport} component.
     * @return the parent or <code>null</code>
     */
    @Nullable
    private JViewport getViewport() {
        final Container parent = getParent();
        return parent instanceof JViewport ? (JViewport) parent : null;
    }

}
