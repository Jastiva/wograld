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

package net.sf.gridarta.gui.panel.gameobjectattributes;

import java.util.ArrayList;
import java.util.Collection;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The model of the game object attributes dialog.
 * @author Andreas Kirschbaum
 */
public class GameObjectAttributesModel<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * The selected game object.
     */
    @Nullable
    private G selectedGameObject;

    /**
     * The registered listeners.
     */
    private final Collection<GameObjectAttributesModelListener<G, A, R>> listeners = new ArrayList<GameObjectAttributesModelListener<G, A, R>>();

    /**
     * If a game object is selected, the MapArchPanels (bottom right windows)
     * get updated.
     * @param selectedGameObject the selected game object
     */
    public void setSelectedGameObject(@Nullable final G selectedGameObject) {
        if (this.selectedGameObject == selectedGameObject) {
            return;
        }

        assert selectedGameObject == null || selectedGameObject.isHead();
        this.selectedGameObject = selectedGameObject;
        for (final GameObjectAttributesModelListener<G, A, R> listener : listeners) {
            listener.selectedGameObjectChanged(selectedGameObject);
        }
    }

    /**
     * Notifies all listeners that the selected game object has changed.
     */
    public void fireRefreshSelectedGameObject() {
        for (final GameObjectAttributesModelListener<G, A, R> listener : listeners) {
            listener.refreshSelectedGameObject();
        }
    }

    /**
     * Returns the selected game object.
     * @return the selected game object
     */
    @Nullable
    public G getSelectedGameObject() {
        return selectedGameObject;
    }

    /**
     * Adds a listener to be notified.
     * @param listener the listener
     */
    public void addGameObjectAttributesModelListener(@NotNull final GameObjectAttributesModelListener<G, A, R> listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener to be notified.
     * @param listener the listener
     */
    public void removeGameObjectAttributesModelListener(@NotNull final GameObjectAttributesModelListener<G, A, R> listener) {
        listeners.add(listener);
    }

}
