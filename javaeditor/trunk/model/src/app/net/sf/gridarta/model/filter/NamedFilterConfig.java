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
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link FilterConfig} that has a name.
 * @author tchize
 * @author Andreas Kirschbaum
 */
public class NamedFilterConfig extends AbstractFilterConfig<NamedFilter, NamedFilterConfig> {

    /**
     * Whether the filter should match if all sub-filters match
     * (<code>true</code>) or if at least one sub-filter does not match
     * (<code>false></code>).
     */
    // TODO fix potential concurrency issues
    private boolean inverted;

    @NotNull
    private final Map<String, FilterConfig<?, ?>> map = new HashMap<String, FilterConfig<?, ?>>();

    /**
     * The Logger for printing log messages.
     */
    @NotNull
    private static final Category log = Logger.getLogger(NamedFilterConfig.class);

    @NotNull
    private final FilterConfigListener filterConfigListener = new FilterConfigListener() {

        @Override
        public void configChanged(@NotNull final FilterConfigChangeType filterConfigChangeType, @NotNull final FilterConfig<?, ?> filterConfig) {
            // XXX What's with newConfigEvent?
            // Not used, so why is it created?
            // This also has no side effect, so is this code wrong and fireEvent(newConfigEvent) should be invoked instead of fireEvent(event)?
            // If so, why is the argument ignored in the new event construction?
            //final ConfigEvent<?, ?> newConfigEvent = new ConfigEvent<NamedFilter, NamedFilterConfig>(FilterConfigChangeType.CHANGE, NamedFilterConfig.this);
            fireEvent(filterConfigChangeType, filterConfig);
        }

    };

    /**
     * Creates a new instance.
     * @param owner the filter this filter config belongs to
     */
    public NamedFilterConfig(@NotNull final NamedFilter owner) {
        super(owner);

        final NamedFilterListener namedFilterListener = new NamedFilterListener() {

            @Override
            public void nameFilterChanged(@NotNull final NamedFilterChangeType type, @NotNull final String filterName, @NotNull final Filter<?, ?> filter) {
                switch (type) {
                case REMOVE:
                    final FilterConfig<?, ?> oldConfig = map.get(filterName);
                    if (oldConfig != null) {
                        oldConfig.removeConfigChangeListener(filterConfigListener);
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("removing config for " + filterName);
                    }
                    map.remove(filterName);
                    break;

                case ADD:
                    final FilterConfig<?, ?> newConfig = filter.createConfig();
                    newConfig.addConfigChangeListener(filterConfigListener);
                    if (log.isDebugEnabled()) {
                        log.debug("adding config for " + filterName);
                    }
                    map.put(filterName, newConfig);
                    break;
                }
                fireEvent(FilterConfigChangeType.CHANGE, NamedFilterConfig.this);
            }

        };
        owner.addFilterListener(namedFilterListener);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    protected NamedFilterConfig getThis() {
        return this;
    }

    /**
     * Returns whether the filter should match if all sub-filters match or if at
     * least one sub-filter does not match.
     * @return whether the filter matches if all sub-filters match
     *         (<code>true</code>) or if any does not match
     *         (<code>false</code>)
     */
    public boolean isInverted() {
        return inverted;
    }

    /**
     * Sets whether the filter should match if all sub-filters match or if at
     * least one sub-filter does not match.
     * @param inverted whether the filter matches if all sub-filters match
     * (<code>true</code>) or if any does not match (<code>false</code>)
     */
    public void setInverted(final boolean inverted) {
        if (this.inverted == inverted) {
            return;
        }
        this.inverted = inverted;
        fireEvent(FilterConfigChangeType.CHANGE, this);
    }

    /**
     * Returns the {@link FilterConfig} for a sub-filter.
     * @param name the sub-filter's name
     * @return the sub-filter's filter config
     */
    @NotNull
    public FilterConfig<?, ?> getConfig(@NotNull final String name) {
        final FilterConfig<?, ?> filterConfig = map.get(name);
        if (filterConfig == null) {
            throw new IllegalArgumentException();
        }
        return filterConfig;
    }

    /**
     * Returns whether a sub-filter is enabled.
     * @param name the sub-filter's name
     * @return whether the sub-filter is enabled
     */
    public boolean isSubFilterEnabled(@NotNull final String name) {
        return getConfig(name).isEnabled();
    }

    /**
     * Sets whether a sub-filter is enabled.
     * @param name the sub-filter's name
     * @param enabled whether the sub-filter is enabled
     */
    public void setSubFilterEnabled(@NotNull final String name, final boolean enabled) {
        if (log.isDebugEnabled()) {
            log.debug("setSubFilterEnabled(" + name + ", " + enabled);
        }

        getConfig(name).setEnabled(enabled);
        fireEvent(FilterConfigChangeType.CHANGE, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(@NotNull final FilterConfigVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Returns a {@link Map} containing all configurations of sub-filters.
     * @return the map
     */
    @NotNull
    public Map<String, FilterConfig<?, ?>> getEntries() {
        return Collections.unmodifiableMap(map);
    }

}
