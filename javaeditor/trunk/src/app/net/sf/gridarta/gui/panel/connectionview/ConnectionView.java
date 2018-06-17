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

import java.util.Comparator;
import net.sf.gridarta.gui.delayedmapmodel.DelayedMapModelListenerManager;
import net.sf.gridarta.gui.map.mapview.MapViewManager;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.connectionview.Connections;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * The view of the connection view control. It holds information about the
 * connections of the selected connection value on the selected map.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class ConnectionView<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends View<Integer, G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Create a ConnectionView.
     * @param mapViewManager the map view manager
     * @param delayedMapModelListenerManager the delayed map model listener
     * manager to use
     */
    public ConnectionView(@NotNull final MapViewManager<G, A, R> mapViewManager, @NotNull final DelayedMapModelListenerManager<G, A, R> delayedMapModelListenerManager) {
        super(new Comparator<Integer>() {

                @Override
                public int compare(@NotNull final Integer o1, @NotNull final Integer o2) {
                    if (o1 < o2) {
                        return -1;
                    }
                    if (o1 > o2) {
                        return +1;
                    }
                    return 0;
                }
            }, new ConnectionCellRenderer(), mapViewManager, delayedMapModelListenerManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void scanGameObjectForConnections(@NotNull final G gameObject) {
        scanGameObject(gameObject);
        for (final GameObject<G, A, R> invObject : gameObject.recursive()) {
            scanGameObject(invObject);
        }
    }

    /**
     * Add the given game object as a connection if it has a "connected" field.
     * @param gameObject the game object to process
     */
    private void scanGameObject(@NotNull final GameObject<G, A, R> gameObject) {
        final int[] values = Connections.parseConnections(gameObject);
        if (values == null) {
            return;
        }

        for (final int value : values) {
            addConnection(value, gameObject);
        }
    }

    /**
     * A {@link CellRenderer} for the connection view.
     * @author Andreas Kirschbaum
     */
    private static class ConnectionCellRenderer extends CellRenderer<Integer> {

        /**
         * The serial version UID.
         */
        private static final long serialVersionUID = 1L;

        @NotNull
        @Override
        protected String formatKey(@NotNull final Integer key) {
            return key.toString();
        }

        @NotNull
        @Override
        protected String formatValue(@NotNull final BaseObject<?, ?, ?, ?> gameObject) {
            return gameObject.getBestName();
        }

    }

}
