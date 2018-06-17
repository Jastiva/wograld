/*
 * Gridarta MMORPG map editor for Crossfire, Daimonin and similar games.
 * Copyright (C) 2000-2010 The Gridarta Developers.
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

package net.sf.gridarta.model.mapmanager;

import java.util.EventListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for listeners listening to {@link MapManager} changes. The
 * following events are created (in roughly the given order): <ul> <li>{@link
 * #mapCreated(MapControl, boolean)} - after a map has been created or opened
 * <li>{@link #currentMapChanged(MapControl)} - the newly created map now is the
 * current map <li>{@link #mapClosed(MapControl)} - the map has been closed
 * </ul>
 * @author Andreas Kirschbaum
 */
public interface MapManagerListener<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends EventListener {

    /**
     * This event handler is called when the current map has changed.
     * @param mapControl the new map control, or <code>null</code> if no opened
     * map exists
     */
    void currentMapChanged(@Nullable MapControl<G, A, R> mapControl);

    /**
     * This event handler is called when a map was created.
     * @param mapControl the created map control
     * @param interactive whether the map has been loaded interactively
     */
    void mapCreated(@NotNull MapControl<G, A, R> mapControl, boolean interactive);

    /**
     * This event handler is called when a map is to be closed. Afterwards
     * {@link #mapClosed(MapControl)} will be called.
     * @param mapControl the map control that is to be closed
     */
    void mapClosing(@NotNull MapControl<G, A, R> mapControl);

    /**
     * This event handler is called when a map has been closed.
     * @param mapControl the map control that has been closed
     */
    void mapClosed(@NotNull MapControl<G, A, R> mapControl);

}
