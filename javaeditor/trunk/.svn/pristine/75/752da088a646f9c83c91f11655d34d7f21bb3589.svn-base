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

import java.awt.Component;
import java.util.List;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.baseobject.BaseObject;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for classes being part of the object chooser.
 * @author Andreas Kirschbaum
 */
public interface ObjectChooserTab<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Returns the component to show in the object chooser.
     * @return the component to show
     */
    @NotNull
    Component getComponent();

    /**
     * Called whenever this tab becomes active or inactive.
     * @param active whether this tab is active
     */
    void setActive(boolean active);

    /**
     * Returns whether the current selection matches the given game object.
     * @param gameObject the game object to compare to
     * @return whether the game object matches
     */
    boolean isMatching(G gameObject);

    /**
     * Returns the selected game object.
     * @return one of the selected game objects or <code>null</code> if no game
     *         objects are selected
     */
    @Nullable
    BaseObject<G, A, R, ?> getSelection();

    /**
     * Returns the selected game objects.
     * @return the selected game objects
     */
    @NotNull
    List<? extends BaseObject<G, A, R, ?>> getSelections();

    /**
     * Returns the title to display in the object chooser.
     * @return the title
     */
    @NotNull
    String getTitle();

}
