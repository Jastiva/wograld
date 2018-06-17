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

package net.sf.gridarta.gui.panel.connectionview;

import net.sf.gridarta.gui.delayedmapmodel.DelayedMapModelListenerManager;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * The controller of the connection view control.
 * @author Andreas Kirschbaum
 */
public class ConnectionControl<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends Control<Integer, G, A, R> {

    /**
     * Create a new instance.
     * @param mapViewManager the map view manager
     * @param delayedMapModelListenerManager the delayed map model listener
     * manager to use
     */
    public ConnectionControl(@NotNull final MapViewManager<G, A, R> mapViewManager, @NotNull final DelayedMapModelListenerManager<G, A, R> delayedMapModelListenerManager) {
        super(new ConnectionView<G, A, R>(mapViewManager, delayedMapModelListenerManager));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doubleClick(@NotNull final Connection<Integer> connection) {
        // ignore
    }

}
