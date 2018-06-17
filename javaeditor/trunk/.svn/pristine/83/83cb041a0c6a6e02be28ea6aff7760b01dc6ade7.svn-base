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

package net.sf.gridarta.model.filter;

import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.match.GameObjectMatcher;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Filter} which filters according to a {@link
 * net.sf.gridarta.model.match.NamedGameObjectMatcher}.
 * @author Andreas Kirschbaum
 */
public class NamedGameObjectMatcherFilter implements Filter<NamedGameObjectMatcherFilter, NamedGameObjectMatcherFilterConfig> {

    /**
     * The {@link GameObjectMatcher} to use.
     */
    @NotNull
    private final GameObjectMatcher matcher;

    /**
     * Creates a new instance.
     * @param matcher the game object matcher to use
     */
    public NamedGameObjectMatcherFilter(@NotNull final GameObjectMatcher matcher) {
        this.matcher = matcher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(@NotNull final NamedGameObjectMatcherFilterConfig config, @NotNull final GameObject<?, ?, ?> gameObject) {
        return matcher.isMatching(gameObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean reset(@NotNull final NamedGameObjectMatcherFilterConfig config) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasGlobalMatch(@NotNull final NamedGameObjectMatcherFilterConfig config) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public NamedGameObjectMatcherFilterConfig createConfig() {
        return new NamedGameObjectMatcherFilterConfig(this);
    }

}
