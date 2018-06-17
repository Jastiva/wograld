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

package net.sf.gridarta.gui.dialog.gomap;

import java.awt.Component;
import java.io.File;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import net.sf.gridarta.model.index.MapsIndex;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link DefaultListCellRenderer} that renders values of a {@link
 * MapsIndex}.
 * @author Andreas Kirschbaum
 */
public class MapListCellRenderer extends DefaultListCellRenderer {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The displayed {@link MapsIndex}.
     */
    @NotNull
    private final MapsIndex mapsIndex;

    /**
     * Creates a new instance.
     * @param mapsIndex the maps index to display
     */
    public MapListCellRenderer(@NotNull final MapsIndex mapsIndex) {
        this.mapsIndex = mapsIndex;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public Component getListCellRendererComponent(@NotNull final JList list, @NotNull final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        final File file = (File) value;
        final String name = mapsIndex.getName(file);
        setText((name == null ? "?" : name) + " (" + file.getName() + ")");
        setToolTipText(value.toString());
        return this;
    }

}
