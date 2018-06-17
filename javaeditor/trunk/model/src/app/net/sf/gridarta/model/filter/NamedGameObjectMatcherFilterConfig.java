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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Filter configuration of {@link NamedGameObjectMatcherFilter} instances.
 * @author tchize
 */
public class NamedGameObjectMatcherFilterConfig extends AbstractFilterConfig<NamedGameObjectMatcherFilter, NamedGameObjectMatcherFilterConfig> {

    @NotNull
    private final Map<String, String> properties = new HashMap<String, String>();

    /**
     * Creates a new instance.
     * @param filter the filter this filter config belongs to
     */
    public NamedGameObjectMatcherFilterConfig(@NotNull final NamedGameObjectMatcherFilter filter) {
        super(filter);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected NamedGameObjectMatcherFilterConfig getThis() {
        return this;
    }

    public void setProperty(@NotNull final String name, @NotNull final String value) {
        final String oldValue = properties.get(name);
        if (oldValue != null && oldValue.equals(value)) {
            return;
        }

        properties.put(name, value);
        fireEvent(FilterConfigChangeType.CHANGE, this);
    }

    @Nullable
    public String getProperty(@NotNull final String name) {
        return properties.get(name);
    }

    @NotNull
    public Iterable<String> getProperties() {
        return Collections.unmodifiableSet(properties.keySet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(@NotNull final FilterConfigVisitor visitor) {
        visitor.visit(this);
    }

}
