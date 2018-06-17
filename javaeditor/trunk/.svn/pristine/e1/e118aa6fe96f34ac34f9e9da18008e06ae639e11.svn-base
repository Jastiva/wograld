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
 * A {@link GameObjectMatcher} that And-combines other <code>GameObjectMatcher</code>s.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class AndGameObjectMatcher implements GameObjectMatcher {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The list of {@link GameObjectMatcher GameObjectMatchers} to AND.
     * @serial
     */
    @NotNull
    private final Collection<GameObjectMatcher> matchers = new ArrayList<GameObjectMatcher>();

    /**
     * Creates an <code>AndGameObjectMatcher</code>.
     * @param matchers the matchers to And
     */
    public AndGameObjectMatcher(@NotNull final Collection<GameObjectMatcher> matchers) {
        this.matchers.addAll(matchers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMatching(@NotNull final GameObject<?, ?, ?> gameObject) {
        for (final GameObjectMatcher gameObjectMatcher : matchers) {
            if (!gameObjectMatcher.isMatching(gameObject)) {
                return false;
            }
        }
        return true;
    }

}
