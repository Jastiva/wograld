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

import java.util.ArrayList;
import java.util.Collection;
import net.sf.gridarta.model.gameobject.GameObject;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link GameObjectMatcher} that delegates to other <code>GameObjectMatcher</code>s.
 * If empty, {@link #isMatching(GameObject)} returns <code>true</code>.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class MutableOrGameObjectMatcher implements GameObjectMatcher {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The default matching state when {@link #gameObjectMatchers} is empty.
     * @serial
     */
    private final boolean defaultState;

    /**
     * The {@link GameObjectMatcher GameObjectMatchers} to OR.
     * @serial
     */
    @NotNull
    private final Collection<GameObjectMatcher> gameObjectMatchers = new ArrayList<GameObjectMatcher>();

    /**
     * Creates a <code>MutableOrGameObjectMatcher</code>.
     * @param defaultState the default matching state when {@link
     * #gameObjectMatchers} is empty
     */
    public MutableOrGameObjectMatcher(final boolean defaultState) {
        this.defaultState = defaultState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMatching(@NotNull final GameObject<?, ?, ?> gameObject) {
        if (gameObjectMatchers.isEmpty()) {
            return true;
        }
        for (final GameObjectMatcher gameObjectMatcher : gameObjectMatchers) {
            if (gameObjectMatcher.isMatching(gameObject)) {
                return true;
            }
        }
        return defaultState;
    }

    /**
     * Adds an {@link GameObjectMatcher}. The supplied <code>GameObjectMatcher</code>
     * is only really added if it isn't already included, e.g. {@link
     * #containsArchObjectMatcher(GameObjectMatcher)} returns
     * <code>false</code>.
     * @param gameObjectMatcher the matcher to add
     */
    public void addArchObjectMatcher(@NotNull final GameObjectMatcher gameObjectMatcher) {
        if (!containsArchObjectMatcher(gameObjectMatcher)) {
            gameObjectMatchers.add(gameObjectMatcher);
        }
    }

    /**
     * Removes an {@link GameObjectMatcher}.
     * @param gameObjectMatcher the matcher to remove
     */
    public void removeArchObjectMatcher(@NotNull final GameObjectMatcher gameObjectMatcher) {
        gameObjectMatchers.remove(gameObjectMatcher);
    }

    /**
     * Checks whether a certain {@link GameObjectMatcher} is contained in this
     * matcher.
     * @param gameObjectMatcher the matcher to check
     * @return <code>true</code> if the matcher is contained in this, otherwise
     *         <code>false</code>
     */
    public boolean containsArchObjectMatcher(@NotNull final GameObjectMatcher gameObjectMatcher) {
        return gameObjectMatchers.contains(gameObjectMatcher);
    }

    /**
     * Removes all {@link GameObjectMatcher GameObjectMatchers}.
     */
    public void removeAll() {
        gameObjectMatchers.clear();
    }

}
