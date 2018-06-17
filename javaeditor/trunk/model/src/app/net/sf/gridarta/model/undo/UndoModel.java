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

package net.sf.gridarta.model.undo;

import java.util.ArrayList;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.SavedSquares;
import net.sf.gridarta.model.match.GameObjectMatchers;
import org.jetbrains.annotations.NotNull;

/**
 * The class <code>UndoModel</code> maintains the undo state for one map
 * control. The state consists of a linear list of {@link UndoState UndoStates}.
 * Possible operations are "undo" and "redo". If a map is changed after
 * performing one or more "undo" operations, the previously recorded "redo"
 * operations are discarded.
 * @author Andreas Kirschbaum
 */
public class UndoModel<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The {@link GameObjectFactory} to use.
     */
    @NotNull
    private final GameObjectFactory<G, A, R> gameObjectFactory;

    /**
     * The {@link GameObjectMatchers} to use.
     */
    @NotNull
    private final GameObjectMatchers gameObjectMatchers;

    /**
     * The undo stack. It consists of recorded {@link UndoState UndoStates} for
     * the associated {@link net.sf.gridarta.model.mapmodel.MapModel}.
     */
    @NotNull
    private final List<UndoState<G, A, R>> undoStack = new ArrayList<UndoState<G, A, R>>();

    /**
     * Current index into {@link #undoStack}. If <code>undoStackIndex ==
     * 0</code>, "undo" is not possible; if <code>undoStackIndex ==
     * undoStack.size()</code>, "redo" is not possible.
     */
    private int undoStackIndex;

    /**
     * The type for recording undo information.
     */
    private UndoType type = UndoType.NORMAL;

    /**
     * Creates a new instance.
     * @param gameObjectFactory the game object factory to use
     * @param gameObjectMatchers the game object matchers to use
     */
    public UndoModel(@NotNull final GameObjectFactory<G, A, R> gameObjectFactory, @NotNull final GameObjectMatchers gameObjectMatchers) {
        this.gameObjectFactory = gameObjectFactory;
        this.gameObjectMatchers = gameObjectMatchers;
    }

    /**
     * Add a new undo state to the undo stack. This discards the existing "redo"
     * information (if present).
     * @param undoState the undo state to add
     */
    private void addUndoState(@NotNull final UndoState<G, A, R> undoState) {
        discardAllRedo();

        assert undoStackIndex == undoStack.size();
        undoStack.add(undoState);
        undoStackIndex = undoStack.size();
    }

    /**
     * Discard old undo information.
     * @param maxUndoStates the maximum number of undo states to save
     */
    public void trimToSize(final int maxUndoStates) {
        while (undoStack.size() > maxUndoStates) {
            if (canRedo()) { // prefer discarding redo over undo information
                discardRedo();
            } else {
                discardUndo();
            }
        }
    }

    /**
     * Return whether an "undo" operation is possible.
     * @return <code>true</code> if an "undo" operation is possible,
     *         <code>false</code> otherwise
     */
    public boolean canUndo() {
        return undoStackIndex > 0;
    }

    /**
     * Return whether a "redo" operation is possible.
     * @return <code>true</code> if a "redo" operation is possible,
     *         <code>false</code> otherwise
     */
    public boolean canRedo() {
        return undoStackIndex < undoStack.size();
    }

    /**
     * Return the "undo" operation name.
     * @return the "undo" operation name
     */
    public String undoName() {
        if (undoStackIndex <= 0) {
            throw new IllegalStateException("undo stack is empty");
        }

        return undoStack.get(undoStackIndex - 1).getName();
    }

    /**
     * Return the "redo" operation name.
     * @return the "redo" operation name
     */
    public String redoName() {
        if (undoStackIndex >= undoStack.size()) {
            throw new IllegalStateException("redo stack is empty");
        }

        return undoStack.get(undoStackIndex).getName();
    }

    /**
     * Perform an "undo" operation.
     * @return the undo state to apply; the returned object must not be modified
     *         by the caller
     */
    @NotNull
    public UndoState<G, A, R> undo() {
        if (undoStackIndex <= 0) {
            throw new IllegalStateException("undo stack is empty");
        }

        undoStackIndex--;
        final UndoState<G, A, R> result = undoStack.get(undoStackIndex);
        type = UndoType.UNDO;
        return result;
    }

    /**
     * Perform a "redo" operation.
     * @return the undo state to apply; the returned object must not be modified
     *         by the caller
     */
    @NotNull
    public UndoState<G, A, R> redo() {
        if (undoStackIndex >= undoStack.size()) {
            throw new IllegalStateException("redo stack is empty");
        }

        final UndoState<G, A, R> result = undoStack.get(undoStackIndex);
        undoStackIndex++;
        type = UndoType.REDO;
        return result;
    }

    /**
     * Finishes an undo or redo operation. Does nothing if none is in progress.
     * @param undoState the changes of the operation
     */
    public void finish(@NotNull final UndoState<G, A, R> undoState) {
        switch (type) {
        case NORMAL:
            addUndoState(undoState);
            break;

        case UNDO:
            undoStack.set(undoStackIndex, undoState);
            type = UndoType.NORMAL;
            break;

        case REDO:
            undoStack.set(undoStackIndex - 1, undoState);
            type = UndoType.NORMAL;
            break;
        }
    }

    /**
     * Finishes an undo or redo operation. Does nothing if none is in progress.
     */
    public void finish() {
        final int index;
        switch (type) {
        default:
        case NORMAL:
            return;

        case UNDO:
            index = undoStackIndex;
            break;

        case REDO:
            index = undoStackIndex - 1;
            break;
        }

        final UndoState<G, A, R> prevUndoState = undoStack.get(index);
        final String name = prevUndoState.getName();
        final A mapArchObject = prevUndoState.getMapArchObject();
        final UndoState<G, A, R> undoState = new UndoState<G, A, R>(name, mapArchObject);
        undoState.setSavedSquares(new SavedSquares<G, A, R>(gameObjectFactory, gameObjectMatchers));
        undoStack.set(index, undoState);
        type = UndoType.NORMAL;
    }

    /**
     * Discard all "redo" information.
     */
    private void discardAllRedo() {
        while (undoStackIndex < undoStack.size()) {
            discardRedo();
        }
    }

    /**
     * Discard one "redo" information.
     */
    private void discardRedo() {
        assert !undoStack.isEmpty();
        assert undoStackIndex < undoStack.size();
        undoStack.remove(undoStack.size() - 1);
    }

    /**
     * Discard one "undo" information.
     */
    private void discardUndo() {
        assert !undoStack.isEmpty();
        assert undoStackIndex > 0;
        undoStack.remove(0);
        undoStackIndex--;
    }

}
