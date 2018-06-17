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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTabbedPane;
import org.jetbrains.annotations.NotNull;

public class ClosingListener extends MouseAdapter {

    /**
     * The {@link JTabbedPane} to close.
     */
    @NotNull
    private final JTabbedPane tabbedPane;

    /**
     * Creates a new instance.
     * @param tabbedPane the tabbed pane to close
     */
    public ClosingListener(@NotNull final JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    /**
     * {@inheritDoc}
     * @noinspection RefusedBequest
     */
    @Override
    public void mouseReleased(@NotNull final MouseEvent e) {
        final int i = tabbedPane.getSelectedIndex();

        // nothing selected
        if (i == -1) {
            return;
        }

        final ClosingIcon icon = (ClosingIcon) tabbedPane.getIconAt(i);

        // close tab, if icon was clicked
        if (icon != null && icon.contains(e.getX(), e.getY())) {
            tabbedPane.removeTabAt(i);
        }
    }

}
