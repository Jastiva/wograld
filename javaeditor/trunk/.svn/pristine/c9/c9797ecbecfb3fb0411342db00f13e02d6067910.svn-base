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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Maintains {@link GameObjectMatcher} instances.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public class GameObjectMatchers implements Iterable<NamedGameObjectMatcher>, Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Map with game object object matchers and their IDs.
     * @serial
     */
    @NotNull
    private final Map<String, NamedGameObjectMatcher> gameObjectMatchersByIds = new HashMap<String, NamedGameObjectMatcher>();

    /**
     * List with game object matchers.
     * @serial
     */
    @NotNull
    private final Collection<NamedGameObjectMatcher> gameObjectMatchers = new ArrayList<NamedGameObjectMatcher>();

    /**
     * Returns a matcher by id; returns <code>null</code> if the matcher does
     * not exist.
     * @param ids the ids to find
     * @return the matcher, or <code>null</code> if no such matcher exists
     */
    @Nullable
    public GameObjectMatcher getMatcher(@NotNull final String... ids) {
        for (final String id : ids) {
            final GameObjectMatcher matcher = gameObjectMatchersByIds.get(id);
            if (matcher != null) {
                return matcher;
            }
        }
        return null;
    }

    /**
     * Returns a matcher by id; prints a warning if the matcher does not exist.
     * @param errorViewCollector the error view collector to use
     * @param ids the ids to find
     * @return the matcher, or <code>null</code> if no such matcher exists
     */
    @Nullable
    public GameObjectMatcher getMatcherWarn(@NotNull final ErrorViewCollector errorViewCollector, @NotNull final String... ids) {
        final GameObjectMatcher matcher = getMatcher(ids);
        if (matcher == null) {
            errorViewCollector.addWarning(ErrorViewCategory.GAMEOBJECTMATCHERS_ENTRY_INVALID, "GameObjectMatcher '" + ids[0] + "' does not exist");
        }
        return matcher;
    }

    /**
     * Return all known game object matchers that should be used as filters.
     * @return the filter game object matches
     */
    @NotNull
    public Collection<NamedGameObjectMatcher> getFilters() {
        final Collection<NamedGameObjectMatcher> result = new HashSet<NamedGameObjectMatcher>(gameObjectMatchersByIds.size());
        for (final NamedGameObjectMatcher matcher : gameObjectMatchersByIds.values()) {
            if (!matcher.isSystemMatcher()) {
                result.add(matcher);
            }
        }
        return result;
    }

    /**
     * Adds a new {@link GameObjectMatcher}.
     * @param gameObjectMatcher the game object matcher to add
     */
    public void addGameObjectMatcher(@NotNull final NamedGameObjectMatcher gameObjectMatcher) {
        gameObjectMatchers.add(gameObjectMatcher);
        gameObjectMatchersByIds.put(gameObjectMatcher.getID(), gameObjectMatcher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<NamedGameObjectMatcher> iterator() {
        return Collections.unmodifiableCollection(gameObjectMatchers).iterator();
    }

}
