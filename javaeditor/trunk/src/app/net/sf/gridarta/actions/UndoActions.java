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

package net.sf.gridarta.actions;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.undo.UndoModel;
import net.sf.gridarta.model.undo.UndoState;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class implementing undo and redo actions.
 * @author Andreas Kirschbaum
 */
public class UndoActions {

    /**
     * Private constructor to prevent instantiation.
     */
    private UndoActions() {
    }

    /**
     * Perform an "undo" action on a {@link MapModel}.
     * @param undoModel the state to undo
     * @param mapModel the map model to affect
     */
    public static <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> void undo(@NotNull final UndoModel<G, A, R> undoModel, @NotNull final MapModel<G, A, R> mapModel) {
        applyChange(undoModel, mapModel, undoModel.undo());
    }

    /**
     * Perform a "redo" action on a {@link MapModel}.
     * @param undoModel the state to redo
     * @param mapModel the map model to affect
     */
    public static <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> void redo(@NotNull final UndoModel<G, A, R> undoModel, @NotNull final MapModel<G, A, R> mapModel) {
        applyChange(undoModel, mapModel, undoModel.redo());
    }

    /**
     * Applies a change (for an undo or a redo operation).
     * @param undoModel the state to redo
     * @param mapModel the map model to affect
     * @param undoState the change to apply
     */
    private static <G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> void applyChange(final UndoModel<G, A, R> undoModel, final MapModel<G, A, R> mapModel, final UndoState<G, A, R> undoState) {
        try {
            mapModel.beginTransaction(undoState.getName());
            try {
                final A newMapArchObject = undoState.getMapArchObject();
                final MapArchObject<A> mapArchObject = mapModel.getMapArchObject();
                mapArchObject.setMapSize(newMapArchObject.getMapSize());
                mapArchObject.beginTransaction();
                try {
                    mapArchObject.setState(newMapArchObject);
                } finally {
                    mapArchObject.endTransaction();
                }
                undoState.getSavedSquares().applyChanges(mapModel);
            } finally {
                mapModel.endTransaction();
            }
        } finally {
            undoModel.finish();
        }
    }

}
