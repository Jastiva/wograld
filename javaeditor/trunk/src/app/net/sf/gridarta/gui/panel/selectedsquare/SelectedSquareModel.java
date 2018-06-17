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

package net.sf.gridarta.gui.panel.selectedsquare;

import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.utils.EventListenerList2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The model component of the selected square control.
 * @author Andreas Kirschbaum
 */
public class SelectedSquareModel<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The listeners to inform of changes.
     */
    @NotNull
    private final EventListenerList2<SelectedSquareModelListener<G, A, R>> listenerList = new EventListenerList2<SelectedSquareModelListener<G, A, R>>(SelectedSquareModelListener.class);

    /**
     * The currently selected {@link MapSquare}.
     */
    @Nullable
    private MapSquare<G, A, R> selectedMapSquare;

    /**
     * The currently selected {@link GameObject}.
     */
    @Nullable
    private G selectedGameObject;

    /**
     * Returns the currently selected map square.
     * @return the currently selected map square, or <code>null</code> if no
     *         square is selected
     */
    @Nullable
    public MapSquare<G, A, R> getSelectedMapSquare() {
        return selectedMapSquare;
    }

    /**
     * Returns the currently selected {@link GameObject} within this list
     * (currently selected MapSquare).
     * @return the currently selected game object or <code>null</code>
     */
    @Nullable
    public G getSelectedGameObject() {
        return selectedGameObject;
    }

    /**
     * Adds a {@link SelectedSquareModelListener} to be notified.
     * @param listener the listener to add
     */
    public void addSelectedSquareListener(@NotNull final SelectedSquareModelListener<G, A, R> listener) {
        listenerList.add(listener);
    }

    /**
     * Removes a {@link SelectedSquareModelListener} to be notified.
     * @param listener the listener to remove
     */
    public void removeSelectedSquareListener(@NotNull final SelectedSquareModelListener<G, A, R> listener) {
        listenerList.remove(listener);
    }

    /**
     * Notifies all listeners that the selected map square has changed.
     */
    private void fireSelectionChangedEvent() {
        final G gameObject = selectedGameObject;
        final G head = gameObject == null ? null : gameObject.getHead();
        for (final SelectedSquareModelListener<G, A, R> listener : listenerList.getListeners()) {
            listener.selectionChanged(selectedMapSquare, head);
        }
    }

    public boolean isSelectedMapSquares(@NotNull final Iterable<MapSquare<G, A, R>> mapSquares) {
        final MapSquare<G, A, R> selectedMapSquare = this.selectedMapSquare;
        if (selectedMapSquare == null) {
            return false;
        }

        for (final MapSquare<G, A, R> mapSquare : mapSquares) {
            if (selectedMapSquare == mapSquare) {
                return true;
            }
        }

        return false;
    }

    public boolean isSelectedGameObjects(@NotNull final Iterable<G> gameObjects) {
        final MapSquare<G, A, R> selectedMapSquare = this.selectedMapSquare;
        if (selectedMapSquare == null) {
            return false;
        }

        for (final GameObject<G, A, R> gameObject : gameObjects) {
            for (GameObject<G, A, R> gameObjectPart = gameObject; gameObjectPart != null; gameObjectPart = gameObjectPart.getMultiNext()) {
                if (selectedMapSquare == gameObjectPart.getMapSquare()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Sets the currently selected map square.
     * @param mapSquare the selected map square
     * @param gameObject the selected game object, or <code>null</code> to
     * deselect it
     * @return <code>true</code> if the state has changed
     */
    public boolean setSelectedMapSquare(@Nullable final MapSquare<G, A, R> mapSquare, @Nullable final G gameObject) {
        if (selectedMapSquare == mapSquare && selectedGameObject == gameObject) {
            return false;
        }

        selectedMapSquare = mapSquare;
        selectedGameObject = gameObject;
        fireSelectionChangedEvent();
        return true;
    }

    /**
     * Sets the currently selected {@link GameObject}.
     * @param gameObject the selected game object or <code>null</code>
     */
    public void setSelectedGameObject(@Nullable final G gameObject) {
        if (selectedMapSquare != null && selectedGameObject != gameObject) {
            selectedGameObject = gameObject;
            fireSelectionChangedEvent();
        }
    }

}
