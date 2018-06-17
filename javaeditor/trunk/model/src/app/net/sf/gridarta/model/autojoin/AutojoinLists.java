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

import java.awt.Point;
import java.io.Serializable;
import java.util.IdentityHashMap;
import java.util.Map;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manages a mapping between archetypes to {@link AutojoinList AutojoinLists}.
 * @author Andreas Kirschbaum
 */
public class AutojoinLists<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> implements Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Mapping of archetypes to autojoin lists.
     */
    private final Map<Archetype<G, A, R>, AutojoinList<G, A, R>> autojoinLists = new IdentityHashMap<Archetype<G, A, R>, AutojoinList<G, A, R>>();

    /**
     * The {@link MapViewSettings} to use.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * Creates a new instance.
     * @param mapViewSettings the map view settings to use
     */
    public AutojoinLists(@NotNull final MapViewSettings mapViewSettings) {
        this.mapViewSettings = mapViewSettings;
    }

    /**
     * Adds a new autojoin list.
     * @param autojoinList the autojoin list to add
     * @throws IllegalAutojoinListException if the autojoin list cannot be
     * added
     */
    public void addAutojoinList(@NotNull final AutojoinList<G, A, R> autojoinList) throws IllegalAutojoinListException {
        for (int i = 0; i < AutojoinList.SIZE; i++) {
            final Archetype<G, A, R> archetype = autojoinList.getArchetype(i);
            if (autojoinLists.containsKey(archetype)) {
                throw new IllegalAutojoinListException("archetype '" + archetype.getArchetypeName() + "' contained in more than one autojoin list");
            }
        }

        for (int i = 0; i < AutojoinList.SIZE; i++) {
            for (final R archetype : autojoinList.getArchetypes(i)) {
                autojoinLists.put(archetype, autojoinList);
            }
        }
    }

    /**
     * Do autojoining on insertion of an game object on the map. All arches
     * around the insert point get adjusted, and the archetype name of the
     * correct archetype to be inserted is returned. This method must be called
     * from the appropriate element of the AutojoinList, best use the link from
     * the default archetype.
     * @param point Location of the insert point on the map
     * @param archetype the archetype to connect with
     * @param mapModel Data model of the map
     * @return the insertion result
     */
    @NotNull
    public InsertionResult<G, A, R> joinInsert(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Point point, @NotNull final R archetype) {
        if (!mapViewSettings.isAutojoin()) {
            return new InsertionResult<G, A, R>(null, archetype);
        }

        if (archetype.isMulti()) {
            return new InsertionResult<G, A, R>(null, archetype);
        }

        final AutojoinList<G, A, R> autojoinList = autojoinLists.get(archetype);
        if (autojoinList == null) {
            return new InsertionResult<G, A, R>(null, archetype);
        }

        // if there already is an archetype of this list at point -> abort
        final boolean isMainIndex = autojoinList.isMainIndex(archetype);
        final G gameObject = findGameObjectOfAutojoinList(mapModel, point, autojoinList);
        if (gameObject != null) {
            final R gameObjectArchetype = gameObject.getArchetype();
            final boolean isExistingMainIndex = autojoinList.isMainIndex(gameObjectArchetype);
            if (isMainIndex) {
                // alt/main -> main
                if (isExistingMainIndex) {
                    // ignore main -> main
                    return new InsertionResult<G, A, R>(null, null); // we don't want same archetypes over each other
                } else {
                    // alt -> main -> update
                }
            } else {
                // alt/main -> alt
                if (isExistingMainIndex) {
                    // main -> alt -> update
                } else {
                    // alt -> alt -> update if different archetype
                    if (gameObjectArchetype == archetype) {
                        return new InsertionResult<G, A, R>(null, null); // we don't want same archetypes over each other
                    }
                }
            }
        }

        // now do the joining in all four directions:
        final int altIndex = isMainIndex ? -1 : autojoinList.getAlternativeIndex(archetype);
        int newIndex = 0;      // return value, see above
        newIndex |= joinInsert(mapModel, point, autojoinList, 0, -1, AutojoinList.NORTH, AutojoinList.SOUTH, altIndex);
        newIndex |= joinInsert(mapModel, point, autojoinList, +1, 0, AutojoinList.EAST, AutojoinList.WEST, altIndex);
        newIndex |= joinInsert(mapModel, point, autojoinList, 0, +1, AutojoinList.SOUTH, AutojoinList.NORTH, altIndex);
        newIndex |= joinInsert(mapModel, point, autojoinList, -1, 0, AutojoinList.WEST, AutojoinList.EAST, altIndex);
        final R newArchetype = isMainIndex ? autojoinList.getArchetype(newIndex) : archetype;
        if (gameObject != null) {
            gameObject.setArchetype(newArchetype);
            return new InsertionResult<G, A, R>(gameObject, null);
        }
        return new InsertionResult<G, A, R>(null, newArchetype);
    }

    private int joinInsert(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Point point, @NotNull final AutojoinList<G, A, R> autojoinList, final int dx, final int dy, final int dir, final int reverseDir, final int altIndex) {
        final Point tmp = new Point(point.x + dx, point.y + dy);
        if (!mapModel.getMapArchObject().isPointValid(tmp)) {
            return 0;
        }

        final GameObject<G, A, R> gameObject = findGameObjectOfAutojoinList(mapModel, tmp, autojoinList);
        if (gameObject == null) {
            return 0;
        }

        final R archetype = gameObject.getArchetype();
        final int index = autojoinList.getAlternativeIndex(archetype);
        if (index != -1) {
            return (index & reverseDir) == 0 ? 0 : dir;
        }

        final int archetypeIndex = autojoinList.getIndex(archetype);
        final int newIndex;
        if ((altIndex & dir) == 0) {
            newIndex = archetypeIndex & ~reverseDir;
        } else {
            newIndex = archetypeIndex | reverseDir;
        }
        gameObject.setArchetype(autojoinList.getArchetype(newIndex));
        return dir;
    }

    /**
     * Do autojoining on deletion of an GameObject on the map. All arches around
     * the insert point get adjusted. This method must be called from the
     * appropriate element of the AutojoinList, best use the link from the
     * default archetype.
     * @param point Location of the insert point on the map
     * @param archetype the archetype to connect with
     * @param mapModel Data model of the map
     */
    public void joinDelete(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Point point, @NotNull final R archetype) {
        if (!mapViewSettings.isAutojoin()) {
            return;
        }

        if (archetype.isMulti()) {
            return;
        }

        final AutojoinList<G, A, R> autojoinList = autojoinLists.get(archetype);
        if (autojoinList == null) {
            return;
        }

        final boolean isMainIndex = autojoinList.isMainIndex(archetype);
        final int altIndex = isMainIndex ? -1 : autojoinList.getAlternativeIndex(archetype);

        joinDelete(mapModel, point, autojoinList, 0, -1, AutojoinList.NORTH, AutojoinList.SOUTH, altIndex);
        joinDelete(mapModel, point, autojoinList, +1, 0, AutojoinList.EAST, AutojoinList.WEST, altIndex);
        joinDelete(mapModel, point, autojoinList, 0, +1, AutojoinList.SOUTH, AutojoinList.NORTH, altIndex);
        joinDelete(mapModel, point, autojoinList, -1, 0, AutojoinList.WEST, AutojoinList.EAST, altIndex);
    }

    private void joinDelete(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Point point, @NotNull final AutojoinList<G, A, R> autojoinList, final int dx, final int dy, final int reverseDir, final int dir, final int altIndex) {
        if ((altIndex & reverseDir) == 0) {
            return;
        }

        final Point tmp = new Point(point.x + dx, point.y + dy);
        if (!mapModel.getMapArchObject().isPointValid(tmp)) {
            return;
        }

        final GameObject<G, A, R> gameObject = findMainGameObjectOfAutojoinList(mapModel, tmp, autojoinList);
        if (gameObject == null) {
            return;
        }

        gameObject.setArchetype(autojoinList.getArchetype(autojoinList.getIndex(gameObject.getArchetype()) & ~dir));
    }

    /**
     * Looks for an archetype at map-position point which is part of an autojoin
     * list.
     * @param mapModel the map model to search
     * @param point location to search
     * @param autojoinList the autojoin list
     * @return the archetype which is part of this join-list or
     *         <code>null</code> if no such archetype exists
     */
    @Nullable
    private G findGameObjectOfAutojoinList(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Point point, @NotNull final AutojoinList<G, A, R> autojoinList) {
        for (final G gameObject : mapModel.getMapSquare(point).reverse()) {
            if (autojoinLists.get(gameObject.getArchetype()) == autojoinList) {
                return gameObject;
            }
        }

        return null;
    }

    /**
     * Looks for an archetype at map-position point which is the main archetypes
     * part of an autojoin list.
     * @param mapModel the map model to search
     * @param point location to search
     * @param autojoinList the autojoin list
     * @return the archetype which is part of this join-list or
     *         <code>null</code> if no such archetype exists
     */
    @Nullable
    private GameObject<G, A, R> findMainGameObjectOfAutojoinList(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Point point, @NotNull final AutojoinList<G, A, R> autojoinList) {
        for (final GameObject<G, A, R> gameObject : mapModel.getMapSquare(point).reverse()) {
            final R archetype = gameObject.getArchetype();
            if (autojoinLists.get(archetype) == autojoinList && autojoinList.isMainIndex(archetype)) {
                return gameObject;
            }
        }

        return null;
    }

}
