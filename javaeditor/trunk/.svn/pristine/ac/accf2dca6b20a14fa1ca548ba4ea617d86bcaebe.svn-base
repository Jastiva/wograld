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

package net.sf.gridarta.model.mapmodel;

import java.io.File;
import java.util.EventListener;
import java.util.Set;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.validation.ErrorCollector;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for listeners listening on {@link MapModel} events.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public interface MapModelListener<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends EventListener {

    /**
     * The size of a map has changed.
     * @param newSize the new map size
     */
    void mapSizeChanged(@NotNull Size2D newSize);

    /**
     * Squares of a map have changed. The following things are square changes:
     * <ul> <li>A GameObject was added to the MapSquare</li> <li>A GameObject
     * was removed from a MapSquare</li> <li>A GameObject was moved up or down
     * within a MapSquare</li> </ul>
     * @param mapSquares the map squares that have been changed
     */
    void mapSquaresChanged(@NotNull Set<MapSquare<G, A, R>> mapSquares);

    /**
     * One or more GameObjects on a map have changed. The following things are
     * gameObject changes: <ul> <li>One or more attributes of a GameObject was
     * changed</li> <li>The inventory of a GameObject has changed (items added,
     * removed or moved)</li> </ul> The following things are transient
     * gameObject changes: <ul> <li>the edit type has changed</li> </ul>
     * @param gameObjects the change objects that have been changed; does not
     * include game objects that have changed transiently only
     * @param transientGameObjects the game objects that have been changed
     * transiently; does not include game objects that have changed
     * non-transiently
     */
    void mapObjectsChanged(@NotNull Set<G> gameObjects, @NotNull Set<G> transientGameObjects);

    /**
     * The errors of a map model have changed.
     * @param errors the new errors
     */
    void errorsChanged(@NotNull final ErrorCollector<G, A, R> errors);

    /**
     * The map file has changed.
     * @param oldMapFile the map file before the change
     */
    void mapFileChanged(@Nullable File oldMapFile);

    /**
     * The modified flag has changed.
     */
    void modifiedChanged();

}
