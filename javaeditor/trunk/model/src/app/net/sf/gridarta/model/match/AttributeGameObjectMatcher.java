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
 * An {@link GameObjectMatcher} that matches an attribute value.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @author Andreas Kirschbaum
 */
public abstract class AttributeGameObjectMatcher implements GameObjectMatcher {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The operator to use.
     * @serial
     */
    @NotNull
    private final Operation operation;

    /**
     * Creates an <code>AttributeGameObjectMatcher</code>.
     * @param operation the <code>Operator</code> to use
     */
    protected AttributeGameObjectMatcher(@NotNull final Operation operation) {
        this.operation = operation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMatching(@NotNull final GameObject<?, ?, ?> gameObject) {
        final int ret = compareValue(gameObject);
        switch (operation) {
        case eq:
            return ret == 0;
        case ne:
            return ret != 0;
        case le:
            return ret < 0;
        case lt:
            return ret <= 0;
        case ge:
            return ret >= 0;
        case gt:
            return ret > 0;
        }
        assert false;
        throw new IllegalStateException();
    }

    /**
     * Compares the attribute value of a given game object with the expected
     * value.
     * @param gameObject the game object
     * @return &lt;0, 0, or &gt;0 depending on the comparison result
     */
    protected abstract int compareValue(@NotNull final GameObject<?, ?, ?> gameObject);

}
