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

package net.sf.gridarta.gui.map.maptilepane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.TitledBorder;
import org.jetbrains.annotations.NotNull;

/**
 * A MapTilePanel extends a {@link TilePanel} with a border and makes focus
 * traversal work within the map tile pane.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class MapTilePanel {

    @NotNull
    private final TilePanel tilePanel;

    /**
     * Creates a new instance.
     * @param direction index, used to determine the direction and corresponding
     * locale keys
     * @param nextFocus the indices of next focus
     * @param tilePanel the tile panel to show
     */
    public MapTilePanel(final int direction, @NotNull final int[] nextFocus, @NotNull final TilePanel tilePanel, @NotNull final String name) {
        this.tilePanel = tilePanel;
        final TitledBorder border = new TitledBorder(name);
        border.setTitleJustification(TitledBorder.CENTER);
        tilePanel.setBorder(border);

        final ActionListener actionListener = new ActionListener() {

            /**
             * Bound to the textField to update the relative/absolute state.
             */
            @Override
            public void actionPerformed(@NotNull final ActionEvent e) {
                tilePanel.updateRAState();
                ((AbstractMapTilePane<?, ?, ?>) tilePanel.getParent().getParent()).getTilePath(nextFocus[direction]).tilePanel.activateTextField();
            }

        };
        tilePanel.addTextFieldActionListener(actionListener);
    }

    @NotNull
    public TilePanel getTilePanel() {
        return tilePanel;
    }

}
