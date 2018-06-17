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

package net.sf.gridarta.gui.panel.objectchooser;

import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Common base interface for ObjectChoosers.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public interface ObjectChooser<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Return if the Pickmap Chooser is active.
     * @return <code>true</code> if the Pickmap Chooser is active, or
     *         <code>false</code> if the Archetype Chooser is active.
     */
    boolean isPickmapActive();

    /**
     * Move the Archetype Chooser in front of the Pickmap Chooser.
     */
    void moveArchetypeChooserToFront();

    /**
     * Move the Pickmap Chooser in front of the Archetype Chooser.
     */
    void movePickmapChooserToFront();

    /**
     * Returns the selected archetype (if the archetype chooser is active) or
     * the game object selected by the map cursor (if the pickmap chooser) is
     * active.
     * @return the selection or <code>null</code>
     */
    @Nullable
    BaseObject<G, A, R, ?> getCursorSelection();

    /**
     * Returns the active arch in the left-side panel. This can either be a
     * default arch from the archetype set, or a custom game object from a
     * pickmap. IMPORTANT: The returned BaseObject is not a clone. A copy must
     * be generated before inserting such an object to the map.
     * @return the active arch in the left-side panel
     */
    @Nullable
    BaseObject<G, A, R, ?> getSelection();

    /**
     * Returns the selected arches in the left-side panel. This can either be
     * default arches, or custom arches from a pickmap. IMPORTANT: The returned
     * GameObject list contains no clone. A copy must be generated before
     * inserting such arches to the map.
     * @return the selected arches in the left-side panel
     */
    @NotNull
    List<? extends BaseObject<G, A, R, ?>> getSelections();

    /**
     * Returns whether the current selection matches a given game object.
     * @param gameObject the game object to match
     * @return whether the game object matches the selection
     */
    boolean isMatching(@NotNull G gameObject);

    /**
     * Adds a listener to be notified.
     * @param listener the listener to add
     */
    void addObjectChooserListener(@NotNull ObjectChooserListener<G, A, R> listener);

    /**
     * Removes a listener to be notified.
     * @param listener the listener to remove
     */
    void removeObjectChooserListener(@NotNull ObjectChooserListener<G, A, R> listener);

}
