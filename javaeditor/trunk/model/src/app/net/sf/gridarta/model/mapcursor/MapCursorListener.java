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

package net.sf.gridarta.model.mapcursor;

import java.awt.Point;
import java.util.EventListener;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapSquare;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for listeners listening to {@link MapCursor} related events.
 * @author <a href="mailto:dlviegas@gmail.com">Daniel Viegas</a>
 * @author Andreas Kirschbaum
 */
public interface MapCursorListener<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends EventListener {

    /**
     * This event handler is called when {@link MapCursor} has moved, appeared
     * or disappeared.
     * @param location the new location or <code>null</code> if the cursor
     * disappeared
     */
    void mapCursorChangedPos(@Nullable Point location);

    /**
     * This event handler is called when {@link MapCursor} changes mode (drag,
     * select).
     */
    void mapCursorChangedMode();

    /**
     * Called whenever the selected game object has changed. This function is
     * <em>not</em> called if {@link #mapCursorChangedPos(Point)} occurs
     * concurrently.
     * @param mapSquare the newly selected map square or <code>null</code> if no
     * map square is selected
     * @param gameObject the newly selected game object or <code>null</code> if
     * no game object is selected
     */
    void mapCursorChangedGameObject(@Nullable MapSquare<G, A, R> mapSquare, @Nullable G gameObject);

}
