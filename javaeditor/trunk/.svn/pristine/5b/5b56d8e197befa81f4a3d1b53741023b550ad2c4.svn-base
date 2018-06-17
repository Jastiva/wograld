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

package net.sf.gridarta.model.floodfill;

import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.InsertionMode;
import net.sf.gridarta.model.mapmodel.InsertionModeSet;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.utils.RandomUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class implementing fill operations on map instances.
 * @author Andreas Kirschbaum
 */
public class FillUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private FillUtils() {
    }

    /**
     * Fills a set of squares in a map.
     * @param mapModel the map to fill
     * @param selection the squares to fill
     * @param insertionMode the insertion mode to use
     * @param gameObjects the game objects to fill with
     * @param density the fill density in percent; -1 to disable
     * @param noAdjacent whether squares are skipped if an adjacent squares is
     * not free
     */
    public static <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> void fill(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Collection<MapSquare<G, A, R>> selection, @NotNull final InsertionMode<G, A, R> insertionMode, @NotNull final List<? extends BaseObject<G, A, R, ?>> gameObjects, final int density, final boolean noAdjacent) {
        if (selection.isEmpty()) {
            return;
        }

        if (gameObjects.isEmpty()) {
            return;
        }

        @Nullable final Set<String> archetypeNames;
        if (noAdjacent) {
            archetypeNames = new HashSet<String>();
            for (final BaseObject<?, ?, ?, ?> baseObject : gameObjects) {
                archetypeNames.add(baseObject.getArchetype().getArchetypeName());
            }
        } else {
            archetypeNames = null;
        }

        mapModel.beginTransaction("Fill"); // TODO; I18N/L10N
        try {
            for (final MapSquare<G, A, R> mapSquare : selection) {
                if (density != -1 && density != 100 && density < RandomUtils.rnd.nextInt(100) + 1) {
                    continue;
                }
                final BaseObject<G, A, R, ?> gameObject = gameObjects.get(RandomUtils.rnd.nextInt(gameObjects.size()));
                if (archetypeNames != null) {
                    final Archetype<G, A, R> archetype = gameObject.getArchetype();
                    final int w = archetype.getSizeX();
                    final int h = archetype.getSizeY();
                    if (containsArchetype(mapModel, archetypeNames, mapSquare, w, h)) {
                        continue;
                    }
                }
                mapModel.insertBaseObject(gameObject, mapSquare.getMapLocation(), false, false, insertionMode);
            }
        } finally {
            mapModel.endTransaction();
        }
    }

    /**
     * Checks whether an area and its adjacent squares of a map does contain an
     * archetype.
     * @param mapModel the map model to check
     * @param archetypeNames the archetype names to check for
     * @param topLeftMapSquare the top left corner of the area to check
     * @param w the width of the area to check
     * @param h the height of the area to check
     * @return whether a similar game object was found
     */
    private static <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> boolean containsArchetype(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Collection<String> archetypeNames, @NotNull final MapSquare<G, A, R> topLeftMapSquare, final int w, final int h) {
        final Point pos = new Point();
        for (int dy = -1; dy < h + 1; dy++) {
            for (int dx = -1; dx < w + 1; dx++) {
                topLeftMapSquare.getMapLocation(pos, dx, dy);
                final MapSquare<G, A, R> mapSquare;
                try {
                    mapSquare = mapModel.getMapSquare(pos);
                } catch (final IndexOutOfBoundsException ignored) {
                    continue;
                }

                for (final GameObject<G, A, R> gameObject : mapSquare) {
                    if (archetypeNames.contains(gameObject.getHead().getArchetype().getArchetypeName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Flood-fill the the map, starting at the cursor position.
     * @param mapModel the map to fill
     * @param start the starting location
     * @param gameObjects the game objects to fill with
     * @param insertionModeSet the insertion mode set to use
     */
    public static <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> void floodFill(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Point start, @NotNull final List<? extends BaseObject<G, A, R, ?>> gameObjects, @NotNull final InsertionModeSet<G, A, R> insertionModeSet) {
        if (gameObjects.isEmpty()) {
            return;
        }

        if (!mapModel.getMapArchObject().isPointValid(start) || !mapModel.getMapSquare(start).isEmpty()) {
            return;
        }

        mapModel.beginTransaction("Flood-fill"); // TODO: I18N/L10N
        try {
            new FloodFill<G, A, R>().floodFill(mapModel, start, gameObjects, insertionModeSet);
        } finally {
            mapModel.endTransaction();
        }
    }

}
