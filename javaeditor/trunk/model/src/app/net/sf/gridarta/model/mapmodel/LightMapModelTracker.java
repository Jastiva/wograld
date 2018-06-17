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

package net.sf.gridarta.model.mapmodel;

import java.awt.Point;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;

/**
 * Tracks a {@link MapModel} for light emitting game objects. Whenever such a
 * game object is added to, removed from, or modified while on the map, all
 * affected map squares are updated.
 * @author Andreas Kirschbaum
 */
public class LightMapModelTracker<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The maximal supported light radius. The return values of {@link
     * GameObject#getLightRadius()} is clipped to this value.
     */
    private static final int MAX_LIGHT_RADIUS = 4;

    /**
     * The tracked {@link MapModel}.
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * A temporary point. Stored in a field to avoid reallocations.
     */
    @NotNull
    private final Point point = new Point();

    /**
     * A temporary point. Stored in a field to avoid reallocations.
     */
    @NotNull
    private final Point point2 = new Point();

    /**
     * Creates a new instance.
     * @param mapModel the tracked map model
     */
    public LightMapModelTracker(@NotNull final MapModel<G, A, R> mapModel) {
        this.mapModel = mapModel;
    }

    /**
     * Called whenever the tracked map is about to change size.
     * @param newSize the new map size
     * @param oldSize the current map size
     */
    public void mapSizeChanging(@NotNull final Size2D newSize, @NotNull final Size2D oldSize) {
        final int newWidth = newSize.getWidth();
        final int oldWidth = oldSize.getWidth();
        final int newHeight = newSize.getHeight();
        final int oldHeight = oldSize.getHeight();
        for (point.x = newWidth; point.x < oldWidth; point.x++) {
            for (point.y = 0; point.y < oldHeight; point.y++) {
                final MapSquare<G, A, R> mapSquare = mapModel.getMapSquare(point);
                setLightRadius(mapSquare, 0);
            }
        }
        for (point.x = 0; point.x < newWidth; point.x++) {
            for (point.y = newHeight; point.y < newHeight; point.y++) {
                final MapSquare<G, A, R> mapSquare = mapModel.getMapSquare(point);
                setLightRadius(mapSquare, 0);
            }
        }
    }

    /**
     * Called whenever some game objects have changed.
     * @param mapSquares the map square that contain changed game objects
     */
    public void mapSquaresChanged(@NotNull final Iterable<MapSquare<G, A, R>> mapSquares) {
        for (final MapSquare<G, A, R> mapSquare : mapSquares) {
            updateLightRadius(mapSquare);
        }
    }

    /**
     * Recalculates information about light emitting game objects in a map
     * square.
     * @param mapSquare the map square
     */
    private void updateLightRadius(@NotNull final MapSquare<G, A, R> mapSquare) {
        int maxLightRadius = 0;
        for (final G gameObject : mapSquare) {
            final int lightRadius = gameObject.getLightRadius();
            if (maxLightRadius < lightRadius) {
                maxLightRadius = lightRadius;
            }
        }
        setLightRadius(mapSquare, Math.min(maxLightRadius, MAX_LIGHT_RADIUS));
    }

    /**
     * Updates the light radius of a map square. The light radius is the maximal
     * light radius of all light emitting game objects in the map square.
     * @param mapSquare the map square to update
     * @param lightRadius the new light radius to set
     */
    private void setLightRadius(@NotNull final MapSquare<G, A, R> mapSquare, final int lightRadius) {
        final int prevLightRadius = mapSquare.getLightRadius();
        if (lightRadius == prevLightRadius) {
            return;
        }
        mapSquare.setLightRadius(lightRadius);
        if (lightRadius < prevLightRadius) {
            // light radius shrinked => remove light sources
            for (int dx = -prevLightRadius; dx <= prevLightRadius; dx++) {
                for (int dy = -prevLightRadius; dy <= prevLightRadius; dy++) {
                    if (dx < -lightRadius || dx > lightRadius || dy < -lightRadius || dy > lightRadius || lightRadius == 0) {
                        point2.x = mapSquare.getMapX() + dx;
                        point2.y = mapSquare.getMapY() + dy;
                        try {
                            final MapSquare<G, A, R> mapSquare2 = mapModel.getMapSquare(point2);
                            mapSquare2.removeLightSource(mapSquare);
                        } catch (final IndexOutOfBoundsException ignored) {
                            // skip points outside map bounds
                        }
                    }
                }
            }
        } else {
            // light increased shrinked => add light sources
            for (int dx = -lightRadius; dx <= lightRadius; dx++) {
                for (int dy = -lightRadius; dy <= lightRadius; dy++) {
                    if (dx < -prevLightRadius || dx > prevLightRadius || dy < -prevLightRadius || dy > prevLightRadius || prevLightRadius == 0) {
                        point2.x = mapSquare.getMapX() + dx;
                        point2.y = mapSquare.getMapY() + dy;
                        try {
                            mapModel.getMapSquare(point2).addLightSource(mapSquare);
                        } catch (final IndexOutOfBoundsException ignored) {
                            // skip points outside map bounds
                        }
                    }
                }
            }
        }
    }

}
