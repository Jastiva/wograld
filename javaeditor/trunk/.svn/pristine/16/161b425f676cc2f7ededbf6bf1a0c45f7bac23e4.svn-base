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

import javax.swing.DefaultListModel;
import net.sf.gridarta.model.baseobject.GameObjectContainer;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.mapviewsettings.MapViewSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Updates a {@link DefaultListModel} instance to reflect the contents of a
 * {@link net.sf.gridarta.model.mapmodel.MapSquare} instance.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class ModelUpdater {

    /**
     * The {@link DefaultListModel} to update.
     */
    @NotNull
    private final DefaultListModel model;

    /**
     * The {@link MapViewSettings} to use.
     */
    @NotNull
    private final MapViewSettings mapViewSettings;

    /**
     * The next index to update. The index point into {@link #model}.
     */
    private int updateIndex;

    /**
     * Creates a new instance.
     * @param model the model to update
     * @param mapViewSettings the map view settings to use
     */
    public ModelUpdater(@NotNull final DefaultListModel model, @NotNull final MapViewSettings mapViewSettings) {
        this.model = model;
        this.mapViewSettings = mapViewSettings;
    }

    /**
     * Updates the model to reflect a {@link net.sf.gridarta.model.mapmodel.MapSquare}.
     * @param mapSquare the map square selection
     * @param gameObject the selected game object
     * @return the list index to select or <code>-1</code> to select nothing
     */
    public int update(@Nullable final GameObjectContainer<?, ?, ?> mapSquare, @Nullable final GameObject<?, ?, ?> gameObject) {
        if (mapSquare == null) {
            model.removeAllElements();
            return -1;
        }

        updateIndex = 0;
        int currentSelectionIndex = -1;
        int firstVisibleIndex = -1;

        // Now go through the list backwards and put all game object on the
        // panel in this order
        boolean foundVisibleIndex = false;
        for (final GameObject<?, ?, ?> node : mapSquare.reverse()) {
            // add the node
            if (node == gameObject) {
                currentSelectionIndex = updateIndex;
            }
            addElement(node);

            // if view-settings are applied, mark topmost "visible" square for selection
            if (mapViewSettings.isEditTypeSet() && !foundVisibleIndex && mapViewSettings.isEditType(node.getEditType())) {
                firstVisibleIndex = updateIndex - 1; // select this square
                foundVisibleIndex = true;      // this is it - don't select any other square
            }

            final int tmpSelect = addInvObjects(node, gameObject);
            if (currentSelectionIndex == -1 && tmpSelect != -1) {
                currentSelectionIndex = tmpSelect;
            }
        }
        if (updateIndex < model.size()) {
            model.removeRange(updateIndex, model.size() - 1);
        }

        final int selectedIndex;
        if (currentSelectionIndex != -1) {
            selectedIndex = currentSelectionIndex;
        } else if (firstVisibleIndex != -1) {
            selectedIndex = firstVisibleIndex;
        } else {
            selectedIndex = -1;
        }
        return selectedIndex;
    }

    /**
     * Add inventory objects to an arch in the SelectedSquareView recursively.
     * @param node the arch where the inventory gets added
     * @param gameObject selected GameObject
     * @return <code>listCounter</code> for <var>gameObject</var>, or
     *         <code>-1</code> if not found
     */
    private int addInvObjects(@NotNull final GameObject<?, ?, ?> node, @Nullable final GameObject<?, ?, ?> gameObject) {
        int selListCounter = -1;
        for (final GameObject<?, ?, ?> invObject : node.getHead().reverse()) {
            if (invObject == gameObject) {
                selListCounter = updateIndex;
            }
            addElement(invObject);

            final int tmpListCounter = addInvObjects(invObject, gameObject);
            if (tmpListCounter != -1) {
                selListCounter = tmpListCounter;
            }
        }
        return selListCounter;
    }

    /**
     * Adds one {@link GameObject} to {@link #model}.
     * @param gameObject the game object to add
     */
    private void addElement(final GameObject<?, ?, ?> gameObject) {
        if (updateIndex < model.size()) {
            if (model.get(updateIndex) != gameObject) {
                model.set(updateIndex, gameObject);
            }
        } else {
            model.addElement(gameObject);
        }
        updateIndex++;
    }

}
