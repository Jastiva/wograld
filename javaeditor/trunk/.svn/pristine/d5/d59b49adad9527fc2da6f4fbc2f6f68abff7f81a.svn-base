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

package net.sf.gridarta.gui.copybuffer;

import java.awt.Point;
import java.util.Collection;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.InsertionModeSet;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.utils.Size2D;
import org.jetbrains.annotations.NotNull;

/**
 * Mode for operations.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public enum CopyMode {

    /**
     * Clear the selection.
     */
    DO_CLEAR {
        @Override
        public <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> void prepare(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Size2D mapSize) {
            // ignore
        }

        @Override
        public <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> void process(@NotNull final MapModel<G, A, R> mapModel, @NotNull final G gameObject, final boolean isEditType, @NotNull final Collection<G> gameObjectsToDelete, @NotNull final Point pos, @NotNull final GameObjectFactory<G, A, R> gameObjectFactory, @NotNull final InsertionModeSet<G, A, R> insertionModeSet) {
            if (isEditType) {
                gameObjectsToDelete.add(gameObject.getHead());
            }
        }

    },

    /**
     * Cut the selection.
     */
    DO_CUT {
        @Override
        public <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> void prepare(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Size2D mapSize) {
            mapModel.getMapArchObject().setMapSize(mapSize);
            mapModel.clearMap();
        }

        @Override
        public <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> void process(@NotNull final MapModel<G, A, R> mapModel, @NotNull final G gameObject, final boolean isEditType, @NotNull final Collection<G> gameObjectsToDelete, @NotNull final Point pos, @NotNull final GameObjectFactory<G, A, R> gameObjectFactory, @NotNull final InsertionModeSet<G, A, R> insertionModeSet) {
            if (isEditType) {
                if (gameObject.isHead() && !gameObject.isInContainer()) {
                    final G clone = gameObjectFactory.cloneGameObject(gameObject);
                    mapModel.addGameObjectToMap(clone, pos, insertionModeSet.getTopmostInsertionMode());
                }
                if (gameObject.getArchetype().getMultiX() >= 0 && gameObject.getArchetype().getMultiY() >= 0) {
                    gameObjectsToDelete.add(gameObject.getHead());
                }
            }
        }

    },

    /**
     * Copy the selection.
     */
    DO_COPY {
        @Override
        public <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> void prepare(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Size2D mapSize) {
            mapModel.getMapArchObject().setMapSize(mapSize);
            mapModel.clearMap();
        }

        @Override
        public <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> void process(@NotNull final MapModel<G, A, R> mapModel, @NotNull final G gameObject, final boolean isEditType, @NotNull final Collection<G> gameObjectsToDelete, @NotNull final Point pos, @NotNull final GameObjectFactory<G, A, R> gameObjectFactory, @NotNull final InsertionModeSet<G, A, R> insertionModeSet) {
            if (isEditType && gameObject.isHead() && !gameObject.isInContainer()) {
                final G clone = gameObjectFactory.cloneGameObject(gameObject);
                mapModel.addGameObjectToMap(clone, pos, insertionModeSet.getTopmostInsertionMode());
            }
        }

    };

    /**
     * Prepares the operation. Will be called once before when the operation
     * starts.
     * @param mapModel the copy buffer's map model
     * @param mapSize the new size of the copy buffer's map model
     */
    public abstract <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> void prepare(@NotNull final MapModel<G, A, R> mapModel, @NotNull final Size2D mapSize);

    /**
     * Processes a {@link GameObject}.
     * @param mapModel the copy buffer's map model
     * @param gameObject the game object
     * @param isEditType whether the game object matches the active edit type
     * @param gameObjectsToDelete a collection for game objects to delete after
     * the operation has finished
     * @param pos the current map position
     * @param gameObjectFactory the game object factory to use
     * @param insertionModeSet the insertion mode set to use
     */
    public abstract <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> void process(@NotNull final MapModel<G, A, R> mapModel, @NotNull final G gameObject, final boolean isEditType, @NotNull final Collection<G> gameObjectsToDelete, @NotNull final Point pos, @NotNull final GameObjectFactory<G, A, R> gameObjectFactory, @NotNull final InsertionModeSet<G, A, R> insertionModeSet);

}
