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

package net.sf.gridarta.model.autojoin;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.Nullable;

/**
 * The result of an insertion operation involving autojoining. The result can be
 * one of: <ol> <li>a new archetype should be inserted</li> <li>an existing game
 * object has been modified</li> <li>the operation failed; nothing has been
 * modified or should be inserted</li> </ol>
 * @author Andreas Kirschbaum
 */
public class InsertionResult<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link GameObject} that has been modified. Set to <code>null</code>
     * if no game object has been modified.
     */
    @Nullable
    private final G gameObject;

    /**
     * The {@link Archetype} that should be inserted. Set to <code>null</code>
     * if no archetype should be inserted.
     */
    @Nullable
    private final R archetype;

    /**
     * Creates a new instance.
     * @param gameObject the game object that has been modified or
     * <code>null</code> if no game object has been modified
     * @param archetype the archetype that should be inserted or
     * <code>null</code> if no archetype should be inserted
     */
    public InsertionResult(@Nullable final G gameObject, @Nullable final R archetype) {
        if (gameObject != null && archetype != null) {
            throw new IllegalArgumentException();
        }

        this.gameObject = gameObject;
        this.archetype = archetype;
    }

    /**
     * Returns the {@link GameObject} that has been modified.
     * @return the game object that has been modified or <code>null</code> if no
     *         game object has been modified
     */
    @Nullable
    public G getGameObject() {
        return gameObject;
    }

    /**
     * Returns the {@link Archetype} that should be inserted.
     * @return the archetype that should be inserted or <code>null</code> if no
     *         archetype should be inserted
     */
    @Nullable
    public R getArchetype() {
        return archetype;
    }

}
