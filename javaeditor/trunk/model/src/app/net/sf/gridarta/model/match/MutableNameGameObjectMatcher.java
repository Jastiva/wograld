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

package net.sf.gridarta.model.match;

import net.sf.gridarta.model.gameobject.GameObject;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link GameObjectMatcher} that simply matches an archetype name. This is
 * probably the most basic and most simple implementation of {@link
 * GameObjectMatcher}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class MutableNameGameObjectMatcher implements GameObjectMatcher {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The archetype name to match.
     * @serial
     */
    @NotNull
    private String name;

    /**
     * Creates a <code>MutableNameGameObjectMatcher</code>.
     * @param name the archetype name to match against
     */
    public MutableNameGameObjectMatcher(@NotNull final String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMatching(@NotNull final GameObject<?, ?, ?> gameObject) {
        return name.matches(gameObject.getArchetype().getArchetypeName());
    }

    /**
     * Returns the archetype name matched by this matcher.
     * @return the archetype name matched
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Sets the archetype name to match.
     * @param name the archetype name to match against
     */
    public void setName(@NotNull final String name) {
        this.name = name;
    }

}
