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

package net.sf.gridarta.gui.dialog.plugin.parameter;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import org.jetbrains.annotations.NotNull;

public class MapParameterCellRenderer extends DefaultListCellRenderer {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The map manager to use.
     */
    @NotNull
    private final MapManager<?, ?, ?> mapManager;

    /**
     * Create a new instance.
     * @param mapManager the map manager to use
     */
    public MapParameterCellRenderer(@NotNull final MapManager<?, ?, ?> mapManager) {
        this.mapManager = mapManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
        final String newVal;
        if (value instanceof MapControl) {
            newVal = ((MapControl<?, ?, ?>) value).getMapModel().getMapArchObject().getMapName();
        } else {
            final MapControl<?, ?, ?> mapControl = mapManager.getCurrentMap();
            final String mapName = mapControl != null ? mapControl.getMapModel().getMapArchObject().getMapName() : "";
            newVal = "Current - " + mapName;
        }
        return super.getListCellRendererComponent(list, newVal, index, isSelected, cellHasFocus);
    }

}
