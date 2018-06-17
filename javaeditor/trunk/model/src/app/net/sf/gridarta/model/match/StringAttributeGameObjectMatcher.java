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
 * A {@link AttributeGameObjectMatcher} that compares attributes values using
 * "string" type.
 * @author Andreas Kirschbaum
 */
public class StringAttributeGameObjectMatcher extends AttributeGameObjectMatcher {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The attribute name to match.
     * @serial
     */
    @NotNull
    private final String name;

    /**
     * The attribute value to match.
     * @serial
     */
    @NotNull
    private final String value;

    /**
     * Whether to query the archetype as well.
     * @serial
     */
    private final boolean useDefArch;

    /**
     * Creates an <code>AttributeGameObjectMatcher</code>.
     * @param name the attribute name to match
     * @param operation the <code>Operator</code> to use
     * @param value the attribute value to compare with
     * @param useDefArch whether to query the archetype as well
     */
    public StringAttributeGameObjectMatcher(@NotNull final String name, @NotNull final Operation operation, @NotNull final String value, final boolean useDefArch) {
        super(operation);
        this.name = name;
        this.value = value;
        this.useDefArch = useDefArch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int compareValue(@NotNull final GameObject<?, ?, ?> gameObject) {
        final Comparable<String> actual;
        if (name.equals("msg")) {
            final String tmp = gameObject.getMsgText();
            actual = tmp == null ? "" : tmp;
        } else {
            actual = gameObject.getAttributeString(name, useDefArch);
        }
        return actual.compareTo(value);
    }

}
