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

package net.sf.gridarta.gui.map.event;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;

/**
 * Interface for Mouse Operations.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public interface MouseOpListener<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Mouse was clicked.
     * @param e the event
     */
    void clicked(MouseOpEvent<G, A, R> e);

    /**
     * Mouse was dragged.
     * @param e the event
     */
    void dragged(MouseOpEvent<G, A, R> e);

    /**
     * Mouse was moved.
     * @param e the event
     */
    void moved(MouseOpEvent<G, A, R> e);

    /**
     * Mouse was pressed.
     * @param e the event
     */
    void pressed(MouseOpEvent<G, A, R> e);

    /**
     * Mouse was released.
     * @param e the event
     */
    void released(MouseOpEvent<G, A, R> e);

}
