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

import javax.swing.JPanel;
import net.sf.gridarta.gui.utils.Severity;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import org.jetbrains.annotations.NotNull;

/**
 * A tab in the game object attributes panel.
 * @author Andreas Kirschbaum
 */
public interface GameObjectAttributesTab<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Returns the tab name.
     * @return the tab name
     */
    @NotNull
    String getName();

    /**
     * Creates the contents panel.
     * @return the contents panel
     */
    @NotNull
    JPanel getPanel();

    /**
     * Returns whether a selected game object exists and the current value
     * differs from the initially set value.
     * @return whether <code>apply()</code> should be called
     */
    boolean canApply();

    /**
     * Applies the current settings. It will be run within a map transaction for
     * the current game object. This function will not be called unless {@link
     * #canApply()} returns <code>true</code>.
     */
    void apply();

    /**
     * Adds a listener.
     * @param listener the listener
     */
    void addGameObjectAttributesTabListener(@NotNull GameObjectAttributesTabListener<G, A, R> listener);

    /**
     * Removes a listener.
     * @param listener the listener
     */
    void removeGameObjectAttributesTabListener(@NotNull GameObjectAttributesTabListener<G, A, R> listener);

    /**
     * Returns the tab severity.
     * @return the tab severity
     */
    @NotNull
    Severity getTabSeverity();

    /**
     * Sets the focus to this component.
     */
    void activate();

}
