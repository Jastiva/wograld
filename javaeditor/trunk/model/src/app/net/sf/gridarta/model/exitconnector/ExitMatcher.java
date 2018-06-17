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

package net.sf.gridarta.model.exitconnector;

import java.awt.Point;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.match.GameObjectMatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Selects valid exit game objects from maps.
 * @author Andreas Kirschbaum
 */
public class ExitMatcher<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements GameObjectMatcher {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The matcher for selecting exit objects.
     */
    @NotNull
    private final GameObjectMatcher exitMatcher;

    /**
     * Creates a new instance.
     * @param exitMatcher the matcher for selecting exit objects
     */
    public ExitMatcher(@NotNull final GameObjectMatcher exitMatcher) {
        this.exitMatcher = exitMatcher;
    }

    /**
     * Returns an exit game object on a given map square having exit
     * information.
     * @param mapModel the map model to check
     * @param point the map square to check
     * @return the head of the exit game object or <code>null</code> if none was
     *         found
     */
    @Nullable
    public G getValidExit(@NotNull final MapModel<G, A, R> mapModel, @Nullable final Point point) {
        if (point == null || !mapModel.getMapArchObject().isPointValid(point)) {
            return null;
        }
        for (final GameObject<G, A, R> part : mapModel.getMapSquare(point)) {
            final G head = part.getHead();
            if (isValidExit(head)) {
                return head;
            }
        }
        return null;
    }

    /**
     * Returns whether the given game object is an exit game object having exit
     * information.
     * @param exit the game object to check
     * @return the game object's head if it is an exit game object or else
     *         <code>null</code>
     */
    @Nullable
    public GameObject<G, A, R> getValidExit(@Nullable final G exit) {
        if (exit == null) {
            return null;
        }
        final GameObject<G, A, R> head = exit.getHead();
        if (!isValidExit(head)) {
            return null;
        }
        return head;
    }

    /**
     * Returns an exit game object on a given map square.
     * @param mapModel the map model to check
     * @param point the map square to check
     * @return the head of the exit game object or <code>null</code> if none was
     *         found
     */
    @Nullable
    @SuppressWarnings("TypeMayBeWeakened")
    public G getExit(@NotNull final MapModel<G, A, R> mapModel, @Nullable final Point point) {
        if (point == null || !mapModel.getMapArchObject().isPointValid(point)) {
            return null;
        }
        for (final GameObject<G, A, R> part : mapModel.getMapSquare(point)) {
            final G head = part.getHead();
            if (isExit(head)) {
                return head;
            }
        }
        return null;
    }

    /**
     * Returns whether the given game object is an exit game object.
     * @param exit the game object to check
     * @return the game object's head if it is an exit game object or else
     *         <code>null</code>
     */
    @Nullable
    public G getExit(@Nullable final G exit) {
        if (exit == null) {
            return null;
        }
        final G head = exit.getHead();
        if (!isExit(head)) {
            return null;
        }
        return head;
    }

    /**
     * Returns whether a {@link GameObject} is a valid exit.
     * @param gameObject the exit game object
     * @return whether the game object has "slaying" set
     */
    private boolean isValidExit(@NotNull final GameObject<G, A, R> gameObject) {
        return isExit(gameObject) && gameObject.hasAttribute(BaseObject.SLAYING);
    }

    /**
     * Returns whether a {@link GameObject} is a valid exit.
     * @param gameObject the exit game object
     * @return whether the game object has "slaying" set
     */
    private boolean isExit(@NotNull final GameObject<?, ?, ?> gameObject) {
        return exitMatcher.isMatching(gameObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMatching(@NotNull final GameObject<?, ?, ?> gameObject) {
        return isExit(gameObject);
    }

}
