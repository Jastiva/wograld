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

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.GameObjectContainer;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.match.GameObjectMatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A single Map Square. This class is implemented in a way that changes via some
 * modifier methods in a map square will automatically fire events in the
 * associated MapModel. A MapSquare always knows its model. It's not possible to
 * create a MapSquare that is not associated to a model. <p/> <p>The objects are
 * stored bottom to top: {@link #getFirst()} returns the object at the bottom,
 * {@link #getLast()} returns the object at the top of the square.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class MapSquare<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends GameObjectContainer<G, A, R> {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The MaoModel this square is associated with.
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * The X Coordinate of this map square within the model's grid.
     */
    private final int mapX;

    /**
     * The Y Coordinate of this map square within the model's grid.
     */
    private final int mapY;

    /**
     * The maximum light radius of all objects within this map square. Set to
     * <code>0</code> if no light emitting objects are present.
     */
    private int lightRadius;

    /**
     * The {@link MapSquare MapSquares} on the {@link #mapModel map} that
     * contain light emitting game objects that affect this map square. Set to
     * {@link Collections#emptyList()} when empty.
     */
    @NotNull
    private List<MapSquare<G, A, R>> lightSources = Collections.emptyList();

    /**
     * Creates a new instance.
     * @param mapModel the map model this map square is part of
     * @param mapX the x coordinate of this map square within the model's grid
     * @param mapY the y coordinate of this map square within the model's grid
     */
    public MapSquare(@NotNull final MapModel<G, A, R> mapModel, final int mapX, final int mapY) {
        this.mapModel = mapModel;
        this.mapX = mapX;
        this.mapY = mapY;
    }

    /**
     * Returns the {@link MapModel} this map square is part of.
     * @return the map model
     */
    @NotNull
    public MapModel<G, A, R> getMapModel() {
        return mapModel;
    }

    /**
     * Returns the x coordinate on the map.
     * @return the x coordinate on map
     */
    public int getMapX() {
        return mapX;
    }

    /**
     * Returns the y coordinate on the map.
     * @return the y coordinate on map
     */
    public int getMapY() {
        return mapY;
    }

    /**
     * Returns the coordinate on the map.
     * @return the coordinate on the map
     */
    @NotNull
    public Point getMapLocation() {
        return new Point(mapX, mapY);
    }

    /**
     * Returns the coordinate on the map.
     * @param pos returns the coordinate on the map
     */
    public void getMapLocation(@NotNull final Point pos) {
        pos.x = mapX;
        pos.y = mapY;
    }

    /**
     * Returns the coordinate with an offset on the map.
     * @param pos returns the coordinate on the map
     * @param dx the x offset to add to the coordinate
     * @param dy the y offset to add to the coordinate
     */
    public void getMapLocation(@NotNull final Point pos, final int dx, final int dy) {
        pos.x = mapX + dx;
        pos.y = mapY + dy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public MapSquare<G, A, R> getMapSquare() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void notifyBeginChange() {
        mapModel.beginSquareChange(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void notifyEndChange() {
        mapModel.endSquareChange(this);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    protected MapSquare<G, A, R> clone() {
        final MapSquare<G, A, R> clone = (MapSquare<G, A, R>) super.clone();
        return clone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setThisContainer(@NotNull final G gameObject) {
        gameObject.setContainer(this, mapX, mapY);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public G asGameObject() {
        return null;
    }

    /**
     * Returns the last occurrence of a matching game object.
     * @param gameObjectMatcher the matcher to use
     * @return the last match, or <code>null</code> if no such game object
     *         exists
     */
    @Nullable
    public G getLast(@NotNull final GameObjectMatcher gameObjectMatcher) {
        for (final G gameObject : reverse()) {
            if (gameObjectMatcher.isMatching(gameObject)) {
                return gameObject;
            }
        }
        return null;
    }

    /**
     * Returns the game object after the last occurrence of a matching game
     * object.
     * @param gameObjectMatcher the matcher to use
     * @return the game object after the last match, or <code>null</code> if no
     *         such game object exists
     * @noinspection TypeMayBeWeakened
     */
    @Nullable
    public G getAfterLast(@NotNull final GameObjectMatcher gameObjectMatcher) {
        G prev = null;
        for (final G gameObject : reverse()) {
            if (gameObjectMatcher.isMatching(gameObject)) {
                return prev;
            }
            prev = gameObject;
        }
        return null;
    }

    /**
     * Returns the first occurrence of a matching game object.
     * @param gameObjectMatcher the matcher to use
     * @return the first match, or <code>null</code> if no such game object
     *         exists
     * @noinspection TypeMayBeWeakened
     */
    @Nullable
    public G getFirst(@NotNull final GameObjectMatcher gameObjectMatcher) {
        for (final G gameObject : this) {
            if (gameObjectMatcher.isMatching(gameObject)) {
                return gameObject;
            }
        }
        return null;
    }

    /**
     * Returns the game object before the first occurrence of a matching game
     * object.
     * @param gameObjectMatcher the matcher to use
     * @return the game object before the first match, or <code>null</code> if
     *         no such game object exists
     * @noinspection TypeMayBeWeakened
     */
    @Nullable
    public G getBeforeFirst(@NotNull final GameObjectMatcher gameObjectMatcher) {
        G prev = null;
        for (final G gameObject : this) {
            if (gameObjectMatcher.isMatching(gameObject)) {
                return prev;
            }
            prev = gameObject;
        }
        return null;
    }

    /**
     * Returns the last game object of the initial segment of matching game
     * objects.
     * @param gameObjectMatcher the matcher to use
     * @return the result or <code>null</code> if the first game object does not
     *         match <code>gameObjectMatcher</code>
     * @noinspection TypeMayBeWeakened
     */
    @Nullable
    public G getLastOfLeadingSpan(@NotNull final GameObjectMatcher gameObjectMatcher) {
        @Nullable G result = null;
        for (final G tmp : this) {
            if (!gameObjectMatcher.isMatching(tmp)) {
                break;
            }
            result = tmp;
        }
        return result;
    }

    /**
     * Method to notify the model that a map square is about to change.
     */
    public void beginSquareChange() {
        mapModel.beginSquareChange(this);
    }

    /**
     * Method to notify the model that a map square was changed.
     */
    public void endSquareChange() {
        mapModel.endSquareChange(this);
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        final MapSquare<?, ?, ?> mapSquare = (MapSquare<?, ?, ?>) obj;
        return mapModel == mapSquare.mapModel && mapX == mapSquare.mapX && mapY == mapSquare.mapY;
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    public int hashCode() {
        return System.identityHashCode(mapModel) + 13 * mapX + 65537 * mapY;
    }

    /**
     * {@inheritDoc}}
     */
    @NotNull
    @Override
    public String toString() {
        return mapModel.getMapArchObject().getMapName() + ":" + mapX + "/" + mapY;
    }

    /**
     * Returns the maximum light radius of all light emitting objects within
     * this map square.
     * @return the light radius or <code>0</code></codE> if no light emitting
     *         objects are present
     */
    public int getLightRadius() {
        return lightRadius;
    }

    /**
     * Sets the maximum light radius of all light emitting objects within this
     * map square.
     * @param lightRadius the light radius or <code>0</code></codE> if no light
     * emitting objects are present
     */
    public void setLightRadius(final int lightRadius) {
        if (this.lightRadius == lightRadius) {
            return;
        }

        notifyBeginChange();
        try {
            this.lightRadius = lightRadius;
        } finally {
            notifyEndChange();
        }
    }

    /**
     * Adds a light emitting game object that affects this map square.
     * @param mapSquare the map square that contains the game object
     */
    public void addLightSource(@NotNull final MapSquare<G, A, R> mapSquare) {
        if (lightSources.isEmpty()) {
            lightSources = new ArrayList<MapSquare<G, A, R>>();
        }

        notifyBeginChange();
        try {
            lightSources.add(mapSquare);
        } finally {
            notifyEndChange();
        }
    }

    /**
     * Removes a light emitting game object that affects this map square.
     * @param mapSquare the map square that contains the game object
     */
    public void removeLightSource(@NotNull final MapSquare<G, A, R> mapSquare) {
        notifyBeginChange();
        try {
            if (!lightSources.remove(mapSquare)) {
                assert false;
            }
            if (lightSources.isEmpty()) {
                lightSources = Collections.emptyList();
            }
        } finally {
            notifyEndChange();
        }
    }

    /**
     * Returns whether this map square is affected by any light emitting game
     * objects.
     * @return whether this map square is affected by any light emitting game
     *         objects
     */
    public boolean isLight() {
        return !lightSources.isEmpty();
    }

}
