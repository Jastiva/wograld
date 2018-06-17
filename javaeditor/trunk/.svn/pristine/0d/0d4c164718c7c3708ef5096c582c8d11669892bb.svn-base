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

import java.io.File;
import java.io.IOException;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapcontrol.MapControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A MapManager manages all opened maps.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public interface MapManager<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Loads a map file. The returned instance must be freed with {@link
     * #release(MapControl)} when not needed anymore.
     * @param file the map file to load
     * @param interactive if set, may ask to delete undefined archetypes; if
     * unset, keep undefined archetypes
     * @return the map control
     * @throws IOException if an I/O error occurs
     */
    @NotNull
    MapControl<G, A, R> openMapFile(@NotNull File file, final boolean interactive) throws IOException;

    /**
     * Closes the given map, ignoring modified status.
     * @param mapControl the map to close
     */
    void closeMap(@NotNull MapControl<G, A, R> mapControl);

    /**
     * Creates a new map control without view. The returned instance must be
     * freed with {@link #release(MapControl)} when not needed anymore.
     * @param objects the game objects to insert; may be <code>null</code>
     * @param mapArchObject the map arch object for the new map
     * @param file the map file, or <code>null</code> if the map was not yet
     * saved
     * @param interactive if set, the new map becomes the current map
     * @return the newly created map control
     */
    @NotNull
    MapControl<G, A, R> newMap(@Nullable List<G> objects, @NotNull A mapArchObject, @Nullable File file, boolean interactive);

    /**
     * Sets the given map as the current one.
     * @param mapControl the <code>MapControl</code> of the new current map
     */
    void setCurrentMap(@Nullable MapControl<G, A, R> mapControl);

    /**
     * Returns all opened maps.
     * @return the opened maps
     */
    @NotNull
    List<MapControl<G, A, R>> getOpenedMaps();

    /**
     * Returns the current map.
     * @return the current map
     */
    @Nullable
    MapControl<G, A, R> getCurrentMap();

    /**
     * Returns one open map.
     * @return the map or <code>null</code> if no open maps exist
     */
    @Nullable
    MapControl<G, A, R> getOpenMap();

    /**
     * Adds a {@link MapManagerListener} to be notified.
     * @param listener the listener to add
     */
    void addMapManagerListener(@NotNull MapManagerListener<G, A, R> listener);

    /**
     * Removes a {@link MapManagerListener} to be notified.
     * @param listener the listener to remove
     */
    void removeMapManagerListener(@NotNull MapManagerListener<G, A, R> listener);

    /**
     * Reverts one map.
     * @param mapControl the map to revert
     * @throws IOException if an I/O error occurs
     */
    void revert(@NotNull MapControl<G, A, R> mapControl) throws IOException;

    /**
     * Returns a guess for a script directory to use.
     * @return the directory
     */
    File getLocalMapDir();

    /**
     * Releases a {@link MapControl} instance.
     * @param mapControl the map control instance
     */
    void release(@NotNull MapControl<G, A, R> mapControl);

}
