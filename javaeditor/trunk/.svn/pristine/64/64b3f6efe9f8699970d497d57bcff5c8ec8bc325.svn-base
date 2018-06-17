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

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import net.sf.gridarta.model.mapmanager.MapManager;
import org.jetbrains.annotations.NotNull;

public class MapParameterComboBoxModel<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends DefaultComboBoxModel {

    private final Object currentMap = new Object();

    private Object selected;

    private static final long serialVersionUID = 1L;

    /**
     * The map manager to use.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * Creates a new instance.
     * @param mapManager the map manager to use
     */
    public MapParameterComboBoxModel(@NotNull final MapManager<G, A, R> mapManager) {
        this.mapManager = mapManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getElementAt(final int index) {
        if (index == 0) {
            return currentMap;
        }

        return mapManager.getOpenedMaps().get(index - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIndexOf(final Object anObject) {
        final List<MapControl<G, A, R>> maps = mapManager.getOpenedMaps();
        if (anObject == currentMap) {
            return 0;
        }

        for (int i = 0; i < maps.size(); i++) {
            if (maps.get(i) == anObject) {
                return i + 1;
            }
        }

        return -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getSelectedItem() {
        return selected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return mapManager.getOpenedMaps().size() + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectedItem(final Object anObject) {
        selected = anObject;
    }

}
